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
    if (p1.num_accessed == p2.num_accessed)
    {
        return (p1.ArrivalTime < p2.ArrivalTime);
    }
    else
    {
        // return (p1.ArrivalTime < p2.ArrivalTime);
        return (p1.num_accessed < p2.num_accessed);
    }
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

int LFU(vector<Page> PageSeq, int pages, int frames, int time)
{
    vector<Page> q;
    vector<Page> temp;
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
        // cout << q.size() << endl;
        q = sortqueue(q);
        // for (int j = 0; j < q.size(); j++)
        // {
        //     cout << q[j].page_num << endl;
        // }
        time += 1;
        if (queue_size < frames && arr[PageSeq[i].page_num] == 0)
        {
            // cout << PageSeq[i].page_num << endl;
            q.push_back(PageSeq[i]);
            // cout << "queue size " << q.size() << endl;
            q[q.size() - 1].num_accessed += 1;
            q[q.size() - 1].ArrivalTime = time;
            pageFaults += 1;
            queue_size += 1;
            arr[PageSeq[i].page_num] = 1;   
            // cout << PageSeq[i].page_num << endl;
        }
        else if (arr[PageSeq[i].page_num] == 1)
        {
            // cout << "Page Hit" << endl;
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
            // cout << q[0].page_num << " replaced by " << PageSeq[i].page_num << endl;
            Page p = q[0];
            q.erase(q.begin());
            arr[p.page_num] = 0;
            q.push_back(PageSeq[i]);
            q[q.size() - 1].num_accessed += 1;
            q[q.size() - 1].ArrivalTime = time;
            arr[PageSeq[i].page_num] = 1;
            pageFaults += 1;
        }
        // break;
    }
    temp = sortqueue(q);
    // printQueue(temp);
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
    // blocks < pages error
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
        // cout << p.page_num << " " << p.ArrivalTime << endl;
        PageSeq.push_back(p);
    }
    // for (int i=0;i<PageSeq.size();i++){
    //     cout << PageSeq[i].page_num << " ";
    // }
    // cout << endl;
    int num_faults = LFU(PageSeq, pages, frames, time);
    cout << "Number of Page Faults are " << num_faults << endl;
    return 0;
}