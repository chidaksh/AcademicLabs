#include <iostream>
#include <fstream>
#include <cstring>
#include <queue>
#include <algorithm>

// Assuming min time of burst is 1 unit
using namespace std;

typedef struct process
{
    int pid;
    int ArrivalTime;
    int *BurstTime;
    int cpu_burst_index;
    int io_burst_index;
    int TurnAroundTime;
    int WaitingTime;
    int ResponseTime;
    int LastRunTime;
    bool occupied;
    int leftiotime;
    int leftcputime;
    int pseudoArrivalTime;
    bool exited;
    bool ionotdone;
} P;

bool compare(P const &p1, P const &p2)
{
    if (p1.pseudoArrivalTime == 0 && p2.pseudoArrivalTime == 0)
    {
        // cout << "checking here " << endl;
        return ((p1.ArrivalTime < p2.ArrivalTime) && p1.occupied == false && p1.exited == false && p1.ionotdone == false);
    }
    else
    {
        return ((p1.leftcputime < p2.leftcputime) && p1.occupied == false && p1.pseudoArrivalTime != 0 && p1.exited == false && p1.ionotdone == false);
    }
}

vector<P> sortqueue(vector<P> ShortestJobQueue)
{
    std::sort(ShortestJobQueue.begin(), ShortestJobQueue.end(), compare);
    return ShortestJobQueue;
}

void PrintAverageTimes(P *process, int num_proc, int TotalTime)
{
    float AvgWaitingTime = 0.0;
    float AvgResponseTime = 0.0;
    float AvgTurnAroundTime = 0.0;
    float PenaltyRatio = 0.0;
    float Throughput = 0.0;
    float AvgPenaltyRatio = 0.0;
    float AvgThroughput = 0.0;
    for (int i = 0; i < num_proc; i++)
    {
        AvgWaitingTime += process[i].WaitingTime;
        AvgResponseTime += process[i].ResponseTime;
        AvgTurnAroundTime += process[i].TurnAroundTime;
        PenaltyRatio = (1.0 * process[i].TurnAroundTime) / (process[i].TurnAroundTime - process[i].WaitingTime);
        AvgPenaltyRatio += PenaltyRatio;
        cout << "Process " << process[i].pid << " TurnAroundTime is " << process[i].TurnAroundTime << " WaitingTime is "
             << process[i].WaitingTime << " ResponseTime is " << process[i].ResponseTime << " PenaltyRatio is " << PenaltyRatio << endl;
    }
    AvgResponseTime /= num_proc;
    AvgTurnAroundTime /= num_proc;
    AvgWaitingTime /= num_proc;
    AvgPenaltyRatio /= num_proc;
    Throughput = (1.0 * num_proc) / TotalTime;
    cout << "Average Waiting Time is: " << AvgWaitingTime << endl;
    cout << "Average Response Time is: " << AvgResponseTime << endl;
    cout << "Average TurnAround Time is: " << AvgTurnAroundTime << endl;
    cout << "Average PenaltyRati is: " << AvgPenaltyRatio << endl;
    cout << "Throughput is: " << Throughput << endl;
    cout << "Total Time is: " << TotalTime << endl;
}

int SRTFscheduler(P *process, int *burstTimeArr, int num_proc)
{
    int time = 0;
    // int contextswitch = 0.5;
    vector<P> ShortestJobQueue;
    for (int i = 0; i < num_proc; i++)
    {
        process[i].leftcputime = process[i].BurstTime[process[i].cpu_burst_index];
        // cout << "Process " << process[i].pid << " leftcputime is " << process[i].leftcputime << endl;
        ShortestJobQueue.push_back(process[i]);
    }

    while (!ShortestJobQueue.empty())
    {
        ShortestJobQueue = sortqueue(ShortestJobQueue);
        P p1 = ShortestJobQueue.front();
        ShortestJobQueue.clear();
        // cout << "Popped process " << p1.pid << endl;
        int iorunning = -1;
        bool iooccupied = false;
        int ioexecution = time;
        int currentjob = p1.pid;
        for (int j = 0; j < num_proc; j++)
        {
            if (process[j].pid == currentjob)
            {
                currentjob = j;
                break;
            }
        }
        if (process[currentjob].cpu_burst_index == 0 && process[currentjob].LastRunTime == 0 && process[currentjob].exited == false)
        {
            process[currentjob].ResponseTime = time - process[currentjob].ArrivalTime;
        }
        // if (process[currentjob].LastRunTime != time){
        //     time += contextswitch;
        // }
        time += 1;
        process[currentjob].leftcputime = process[currentjob].leftcputime - 1;

        for (int i = 0; i < num_proc; i++)
        {
            if (time >= process[i].ArrivalTime)
            {
                // cout << "Process " << process[i].pid << " Entered at time " << time << endl;
                process[i].pseudoArrivalTime = time;
            }
        }
        // cout << "After executing " << time << endl;
        // cout << " Burst Index of " << process[currentjob].pid << " is " << burstTimeArr[process[currentjob].pid] - 1 << endl;
        if (process[currentjob].cpu_burst_index == burstTimeArr[process[currentjob].pid] - 1 && process[currentjob].leftcputime == 0)
        {
            // cout << " Burst Index of " << process[currentjob].pid << " is " << burstTimeArr[process[currentjob].pid] - 1 << endl;
            process[currentjob].TurnAroundTime = time - process[currentjob].ArrivalTime;
            process[currentjob].exited = true;
            // ShortestJobQueue.pop();
            // cout << "Process with pid: " << p1.pid << " "
            //      << "exited" << endl;
        }
        for (int j = 0; j < num_proc; j++)
        {
            if (j != p1.pid)
            {
                if (time > process[j].ArrivalTime && process[j].exited == false)
                {
                    // cout << " Process with " << process[j].pid << " has Last Run time " << process[j].LastRunTime << endl;
                    process[j].WaitingTime += 1;
                    // cout << " Waiting time for Process " << process[j].pid << " is " << process[j].WaitingTime << endl;
                }
                if (process[j].LastRunTime != 0 && process[j].cpu_burst_index != 0 && process[j].exited == false && process[j].cpu_burst_index > process[j].io_burst_index)
                {
                    // cout << "Process with " << process[j].pid << " Entered" << endl;
                    for (int k = 0; k < num_proc; k++)
                    {
                        if (k != p1.pid)
                        {
                            if (process[k].occupied == true)
                            {
                                iorunning = k;
                                iooccupied = true;
                                // cout << "Process with " << process[k].pid << " is Occupied" << endl;
                                break;
                            }
                        }
                    }
                    if (iooccupied == false)
                    {
                        if (process[j].LastRunTime == ioexecution || process[j].ionotdone == true)
                        {
                            // cout << "Process with " << process[j].pid << " Entered the inner if condition" << endl;
                            int temp = process[j].BurstTime[process[j].io_burst_index];
                            if (process[j].leftiotime == 0)
                            {
                                process[j].leftiotime = temp;
                            }
                            if (process[j].leftiotime - 1 == 0)
                            {
                                process[j].occupied = false;
                                process[j].io_burst_index += 2;
                                process[j].pseudoArrivalTime = time;
                                process[j].ionotdone = false;
                                ShortestJobQueue.push_back(process[j]);
                                // cout << "Pushed process " << process[j].pid << endl;
                                // cout << "Updated Pseudo ArrivalTime " << endl;
                            }
                            else
                            {
                                process[j].occupied = true;
                                process[j].leftiotime -= 1;
                            }
                        }
                    }
                    else if (iooccupied = true)
                    {
                        int temp = process[iorunning].leftiotime;
                        if (temp - 1 == 0)
                        {
                            process[iorunning].occupied = false;
                            process[iorunning].io_burst_index += 2;
                            process[iorunning].pseudoArrivalTime = time;
                            process[iorunning].ionotdone = false;
                            // cout << "Updated Pseudo ArrivalTime " << endl;
                            ShortestJobQueue.push_back(process[iorunning]);
                            // cout << "Pushed process " << process[iorunning].pid << endl;
                        }
                        else
                        {
                            process[iorunning].occupied = true;
                            process[iorunning].leftiotime -= 1;
                        }
                    }
                }
            }
        }
        process[currentjob].LastRunTime = time;
        if (process[currentjob].cpu_burst_index != burstTimeArr[process[currentjob].pid] - 1 && process[currentjob].leftcputime == 0)
        {
            process[currentjob].ionotdone = true;
        }
        if (process[currentjob].leftcputime == 0 and process[currentjob].exited == false)
        {
            process[currentjob].cpu_burst_index += 2;
            process[currentjob].leftcputime = process[currentjob].BurstTime[process[currentjob].cpu_burst_index];
        }
        if (process[currentjob].leftcputime != 0 and process[currentjob].exited == false && process[currentjob].ionotdone == false)
        {
            ShortestJobQueue.push_back(process[currentjob]);
            // cout << " King is Pushing Process " << process[currentjob].pid << endl;
        }
        int leftoverjobs = 0;
        for (int i = 0; i < num_proc; i++)
        {
            if (i != currentjob && i != iorunning)
            {
                if (process[i].exited == false and time >= process[i].ArrivalTime && process[i].ionotdone == false)
                {
                    leftoverjobs += 1;
                    ShortestJobQueue.push_back(process[i]);
                    // cout << "Process " << process[i].pid << " is pushed into queue" << endl;
                }
            }
        }
        if (leftoverjobs == 0 && process[currentjob].exited == false && process[currentjob].ionotdone == false)
        {
            ShortestJobQueue.push_back(process[currentjob]);
        }
        else if (leftoverjobs == 0 and process[currentjob].ionotdone == true)
        {
            time += process[currentjob].BurstTime[process[currentjob].io_burst_index];
            process[currentjob].io_burst_index += 2;
            ShortestJobQueue.push_back(process[currentjob]);
            process[currentjob].ionotdone = false;
        }
        else if (leftoverjobs == 0)
        {
            // cout << "I am here" << endl;
            break;
        }
        // cout << " Process " << process[currentjob].pid << " Values changed " << process[currentjob].LastRunTime << " " << process[currentjob].cpu_burst_index << endl;
    }
    return time;
}

int main(int argc, char **argv)
{
    if (argc != 2)
    {
        cout << "Give Proper inputs! Please mention the input file name\n"
             << endl;
        return -1;
    }
    fstream fp;
    fp.open(argv[argc - 1], ios::in);
    if (!fp.is_open())
    {
        cout << "Couldn't open the file\n"
             << endl;
        return -1;
    }
    string line;
    int num_proc = 0;
    while (getline(fp, line))
    {
        if (line[0] != '<')
        {
            num_proc += 1;
        }
    }
    fp.close();
    // cout << "Num Proc: " << num_proc << endl;

    fp.open(argv[argc - 1], ios::in);
    if (!fp.is_open())
    {
        cout << "Couldn't open the file\n"
             << endl;
        return -1;
    }
    P *proc = new P[num_proc];
    int *burstTimeArr = new int[num_proc];
    string token;
    string delimiter = " ";
    int burstind = 0;
    while (getline(fp, line))
    {
        int temp = 0;
        if (line[0] != '<')
        {
            size_t pos = 0;
            while ((pos = line.find(delimiter)) != std::string::npos)
            {
                token = line.substr(0, pos);
                temp += 1;
                line.erase(0, pos + delimiter.length());
            }
            burstTimeArr[burstind] = temp - 1;
            burstind += 1;
        }
    }
    fp.close();
    for (int i = 0; i < num_proc; i++)
    {
        proc[i].pid = i;
        proc[i].ArrivalTime = 0;
        proc[i].BurstTime = new int[burstTimeArr[i]];
        proc[i].cpu_burst_index = 0;
        proc[i].io_burst_index = 1;
        proc[i].TurnAroundTime = 0;
        proc[i].ResponseTime = 0;
        proc[i].WaitingTime = 0;
        proc[i].LastRunTime = 0;
        proc[i].occupied = false;
        proc[i].leftiotime = 0;
        proc[i].leftcputime = 0;
        proc[i].pseudoArrivalTime = 0;
        proc[i].ionotdone = 0;
    }
    int index = 0;
    int burstindex = 0;

    fp.open(argv[argc - 1], ios::in);
    if (!fp.is_open())
    {
        cout << "Couldn't open the file\n"
             << endl;
        return -1;
    }
    while (getline(fp, line))
    {
        if (line[0] != '<')
        {
            int ind = 0;
            int tk = 0;
            size_t pos = 0;
            while ((pos = line.find(delimiter)) != std::string::npos)
            {
                token = line.substr(0, pos);
                // string s = "";
                // if (int(token[0]) == 0)
                // {
                //     cout << "I entered" << endl;
                //     token = line.substr(0, pos);
                // }
                // cout << "token " << int(token[0]) << endl;
                tk = stoi(token);
                if (ind == 0 && tk != -1)
                {
                    proc[index].ArrivalTime = tk;
                }
                else if (ind % 2 == 1 && tk != -1)
                {
                    proc[index].BurstTime[burstindex] = tk;
                    burstindex += 1;
                }
                else if (ind % 2 == 0 && tk != -1)
                {
                    proc[index].BurstTime[burstindex] = tk;
                    burstindex += 1;
                }
                ind += 1;
                line.erase(0, pos + delimiter.length());
            }
            index += 1;
            burstindex = 0;
        }
    }
    fp.close();

    // for (int i = 0; i < num_proc; i++)
    // {
    //     cout << "Process ID: " << proc[i].pid << endl;
    //     cout << "Process ArrivalTime: " << proc[i].ArrivalTime << endl;
    //     cout << "Process BurstTime ";
    //     for (int j = 0; j < burstTimeArr[i]; j++)
    //     {
    //         cout << proc[i].BurstTime[j] << " ";
    //     }
    //     cout << endl;
    // }
    int TotalTime = 0;
    TotalTime = SRTFscheduler(proc, burstTimeArr, num_proc);
    PrintAverageTimes(proc, num_proc, TotalTime);
    for (int i = 0; i < num_proc; i++)
    {
        delete[] proc[i].BurstTime;
    }
    delete[] burstTimeArr;
    return 0;
}
