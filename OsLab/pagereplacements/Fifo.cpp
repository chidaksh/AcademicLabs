#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include <queue>
#include <vector>
using namespace std;

void printQueue(queue<int> q)
{
	//printing content of queue 
	while (!q.empty()){
		cout<<" "<<q.front();
		q.pop();
	}
	cout<<endl;
}

int FIFO(vector<int> PageSeq,int pages,int frames){
    queue<int> q;
    queue<int> temp;
    int *arr;
    int queue_size = 0;
    int pageFaults = 0;
    arr = new int[pages];
    for (int i=0;i<pages;i++){
        arr[i] = 0;
    }
    for (int i=0;i<PageSeq.size();i++){
        if (arr[PageSeq[i]] == 0 && queue_size < frames){
            q.push(PageSeq[i]);
            pageFaults += 1;
            queue_size += 1;
            arr[PageSeq[i]] = 1;
            // cout << PageSeq[i] << endl;
        }
        else if(arr[PageSeq[i]] == 1){
            // cout << "Page Hit" << endl;
        }
        else{
            // cout << q.front() << " replaced by " << PageSeq[i] << endl;
            arr[q.front()] = 0;
            q.pop();
            q.push(PageSeq[i]);
            arr[PageSeq[i]] = 1;
            pageFaults += 1;
        }
    }
    temp = q;
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
    vector<int> PageSeq;
    while (ss >> page) {
        PageSeq.push_back(page);
    }
    // for (int i=0;i<PageSeq.size();i++){
    //     cout << PageSeq[i] << " ";
    // }
    // cout << endl;
    int num_faults = FIFO(PageSeq,pages,frames);
    cout << "Number of Page Faults are "<< num_faults << endl;
    return 0;
}