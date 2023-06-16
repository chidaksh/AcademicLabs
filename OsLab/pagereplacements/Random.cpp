#include <iostream>
#include <cstdlib>
#include <ctime>
#include <string>
#include <fstream>
#include <sstream>
#include <vector>
using namespace std;

int RANDOM(vector<int> PageSeq, int pages, int frames)
{
    srand(time(NULL));
    int *arr;
    int *entries;
    arr = new int[pages];
    entries = new int[frames];
    int num_entries = 0, pageFaults = 0;
    for (int i = 0; i < pages; i++)
    {
        arr[i] = 0;
    }
    for (int i = 0; i < PageSeq.size(); i++)
    {
        if (arr[PageSeq[i]] == 0 && num_entries < frames)
        {
            entries[num_entries] = PageSeq[i];
            pageFaults += 1;
            num_entries += 1;
            arr[PageSeq[i]] = 1;
            // cout << PageSeq[i] << endl;
        }
        else if (arr[PageSeq[i]] == 1)
        {
            // cout << "Page Hit" << endl;
        }
        else
        {
            // cout << q.front() << " replaced by " << PageSeq[i] << endl;
            int random_number = rand() % frames;
            arr[entries[random_number]] = 0;
            entries[random_number] = PageSeq[i];
            arr[PageSeq[i]] = 1;
            pageFaults += 1;
        }
    }
    delete[] arr;
    delete[] entries;
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
    vector<int> PageSeq;
    while (ss >> page)
    {
        PageSeq.push_back(page);
    }
    // for (int i=0;i<PageSeq.size();i++){
    //     cout << PageSeq[i] << " ";
    // }
    // cout << endl;
    int num_faults = RANDOM(PageSeq, pages, frames);
    cout << "Number of Page Faults are " << num_faults << endl;
    return 0;
}