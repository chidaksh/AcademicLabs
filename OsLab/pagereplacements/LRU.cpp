#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include <queue>
#include <vector>
#include <algorithm>
using namespace std;

class Page
{
public:
    int page_num, ArrivalTime, num_accessed;
    Page(int pagen, int time)
    {
        page_num = pagen;
        ArrivalTime = time;
        num_accessed = 0;
        // cout << page_num << " " << ArrivalTime << endl;
        return;
    }
};

bool compare(Page const &p1, Page const &p2)
{
    return (p1.ArrivalTime < p2.ArrivalTime);
}

vector<Page> sortqueue(vector<Page> leastrecentused)
{
    // cout << "started" << endl;
    std::sort(leastrecentused.begin(), leastrecentused.end(), compare);
    // cout << "ended" << endl;
    return leastrecentused;
}

void printQueue(vector<Page> q)
{
    // printing content of queue
    while (!q.empty())
    {
        cout << " " << q.front().page_num;
        q.erase(q.begin());
    }
    cout << endl;
}

int LRU(vector<Page> PageSeq, int pages, int frames, int time)
{
    vector<Page> q;
    int *arr;
    arr = new int[pages];
    for (int i = 0; i < pages; i++)
    {
        arr[i] = 0;
    }
    int queue_size = 0;
    int pageFaults = 0;
    for (int i = 0; i < PageSeq.size(); i++)
    {
        q = sortqueue(q);
        time += 1;
        if (queue_size < frames && arr[PageSeq[i].page_num] == 0)
        {
            q.push_back(PageSeq[i]);
            q[q.size() - 1].num_accessed += 1;
            q[q.size() - 1].ArrivalTime = time;
            pageFaults += 1;
            queue_size += 1;
            arr[PageSeq[i].page_num] = 1;
        }
        else if (arr[PageSeq[i].page_num] == 1)
        {
            int temp = PageSeq[i].page_num;
            for (int j = 0; j < q.size(); j++)
            {
                if (q[j].page_num == temp)
                {
                    q[j].num_accessed += 1;
                    q[j].ArrivalTime = time;
                }
            }
        }
        else
        {
            Page p = q[0];
            q.erase(q.begin());
            arr[p.page_num] = 0;
            q.push_back(PageSeq[i]);
            q[q.size() - 1].num_accessed += 1;
            q[q.size() - 1].ArrivalTime = time;
            arr[PageSeq[i].page_num] = 1;
            pageFaults += 1;
        }
    }
    delete[] arr;
    return pageFaults;
}

int main(int argc, char *argv[])
{
    if (argc != 5)
    {
        cerr << "Error: Give proper command line arguments" << endl;
        return -1;
    }
    int pages = stoi(argv[1]);
    int frames = stoi(argv[2]);
    int blocks = stoi(argv[3]);
    if (blocks > pages)
    {
        cerr << "Error: Swap Space is not enough to hold all the pages" << endl;
        return -1;
    }
    ifstream inputFile(argv[4]);
    if (!inputFile)
    {
        cerr << "Error: File could not be opened" << endl;
        return -1;
    }
    string inputLine;
    getline(inputFile, inputLine);
    inputFile.close();
    stringstream ss(inputLine);
    int page;
    int time = 0;
    vector<Page> PageSeq;
    while (ss >> page)
    {
        time += 1;
        Page p(page, time);
        PageSeq.push_back(p);
    }
    int num_faults = LRU(PageSeq, pages, frames, time);
    cout << "Number of Page Faults are " << num_faults << endl;
    return 0;
}