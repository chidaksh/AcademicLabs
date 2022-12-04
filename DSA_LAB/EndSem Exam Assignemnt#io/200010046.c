#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <limits.h>

int V;

int min_distance(int dist[], bool sptSet[])
{
    int min = INT_MAX, min_ind;
    for (int i = 0; i < V; i++)
    {
        if (sptSet[i] == false && dist[i] <= min)
        {
            min = dist[i], min_ind = i;
        }
    }
    return min_ind;
}

void print(int dist[]) // printing in output file
{
    FILE *output;
    output = fopen("dijkstra.txt", "w");
    if (output == NULL)
    {
        printf("Couldn't open ts.txt,sorry try again!\n");
        return;
    }
    for (int i = 0; i < V; i++)
    {
        if (dist[i] == INT_MAX)
        {
            fprintf(output, "%d -1\n", i);
        }
        else
        {
            fprintf(output, "%d %d\n", i, dist[i]);
        }
    }
    fclose(output);
}

void dijkstra(int **Graph, int src)
{
    int dist[V];
    bool sptSet[V];
    for (int i = 0; i < V; i++)
    {
        dist[i] = INT_MAX, sptSet[i] = false;
    }
    dist[src] = 0;
    for (int count = 0; count < V - 1; count++)
    {
        int u = min_distance(dist, sptSet);
        sptSet[u] = true;
        for (int v = 0; v < V; v++)
        {
            if (!sptSet[v] && Graph[u][v] && dist[u] != INT_MAX && dist[u] + Graph[u][v] < dist[v])
            {
                dist[v] = dist[u] + Graph[u][v];
            }
        }
    }
    print(dist);
}
typedef struct node
{
    int vertex;
    struct node *next;
    int parent;
} node;

node *topo_sort = NULL;

node *createnode(int vertex) //creating a node
{
    node *newnode = (node *)malloc(sizeof(node));
    newnode->vertex = vertex;
    newnode->next = NULL;
    newnode->parent = -2;
    return newnode;
}
void merge(int *arr, int start, int mid, int end, int num_int)
{
    int i, j, index = start;
    int *temp = (int *)malloc(num_int * sizeof(int)); // creating a temp array for storing arr and processing data in it
    if (temp == NULL)
    {
        printf("Couldn't allocate memory in heap.\n");
        exit(0);
    }
    for (i = start, j = mid; (i < mid || j < end);)
    {
        if (i < mid && j < end)
        { // when both arrays aren't read
            if (arr[j] > arr[i])
            {
                temp[index] = arr[i];
                i++;
            }
            else
            {
                temp[index] = arr[j];
                j++;
            }
        }
        else
        { // when one of the arrays is already read
            if (i < mid)
            {
                temp[index] = arr[i];
                i++;
            }
            else
            {
                temp[index] = arr[j];
                j++;
            }
        }
        index++;
    }
    for (int i = start; i < end; i++)
    {
        arr[i] = temp[i];
    } // copying temp into arr
    free(temp);
    return;
}
// typedef struct Graph // format of struct graph
// {
//     struct node **lsvertices;
//     struct node **adjlists;
// } Graph;

// Graph *creategraph(int vertices) // creates the memory for the entire graph and does initialization work and all
// {
//     Graph *graph = (Graph *)malloc(sizeof(Graph));
//     graph->adjlists = (node **)malloc(vertices * sizeof(node *));
//     graph->lsvertices = (node **)malloc(vertices * sizeof(node *));
//     // graph->num_vertices = vertices;
//     for (int i = 0; i < vertices; i++)
//     {
//         graph->adjlists[i] = NULL;
//         graph->lsvertices[i] = createnode(i);
//     }
//     return graph;
// }

// void addedge(Graph *graph, int src, int dest) // reads edges and then adds them in their respective adjency lists
// {
//     node *newnode = createnode(dest); // Inserting dest at the beginning of src
//     newnode->next = graph->adjlists[src];
//     graph->adjlists[src] = newnode;
// }

// void insertbegin(int vertex)  // insert at the beginning of the list topo_sort
// {
//     node *tail = createnode(vertex);
//     tail->next = topo_sort;
//     topo_sort = tail;
//     // free(tail);
// }

// void dfs_visit(Graph *graph, int i)  // visit adjacency list of every vertex and if it's unvisited check it's adjacency list recursively
// {
//     node *temp = graph->adjlists[i];
//     if (temp == NULL)
//     {
//         return;
//     }
//     else
//     {
//         if (temp->next == NULL)
//         {
//             if (temp->parent == -2 && graph->lsvertices[temp->vertex]->parent == -2)
//             {
//                 temp->parent = graph->lsvertices[i]->vertex;
//                 graph->lsvertices[temp->vertex]->parent = -1;
//                 dfs_visit(graph, temp->vertex);
//                 insertbegin(temp->vertex);
//             }
//             else
//             {
//                 return;
//             }
//         }
//         else
//         {
//             while (temp->next)
//             {
//                 if (temp->parent == -2 && graph->lsvertices[temp->vertex]->parent == -2)
//                 {
//                     temp->parent = graph->lsvertices[i]->vertex;
//                     graph->lsvertices[temp->vertex]->parent = -1;
//                     dfs_visit(graph, temp->vertex);
//                     insertbegin(temp->vertex);
//                     temp = temp->next;
//                 }
//                 else
//                 {
//                     temp = temp->next;
//                 }
//             }
//             if (temp->parent == -2 && graph->lsvertices[temp->vertex]->parent == -2)
//             {
//                 temp->parent = graph->lsvertices[i]->vertex;
//                 graph->lsvertices[temp->vertex]->parent = -1;
//                 dfs_visit(graph, temp->vertex);
//                 insertbegin(temp->vertex);
//                 return;
//             }
//             else
//             {
//                 return;
//             }
//         }
//     }
// }

// void dfs(Graph *graph, int vertices)  // checks dfs for every vertex
// {
//     for (int i = 0; i < vertices; i++)
//     {
//         if (graph->lsvertices[i]->parent == -2)
//         {
//             graph->lsvertices[i]->parent = -1;
//             dfs_visit(graph, i);
//             insertbegin(graph->lsvertices[i]->vertex);
//         }
//     }
// }
void mergesort(int *arr, int start, int end, int num_int)
{
    if (end == start + 1)
    {
        return;
    }                                    // termination for this recursion
    int mid = start + (end - start) / 2; // to avoid integer overflow because start+end/2 can exceed limit easliy comapred to this expression
    mergesort(arr, start, mid, num_int);
    mergesort(arr, mid, end, num_int);
    merge(arr, start, mid, end, num_int);
}



int main(int argc, char *argv[])
{
    if (argc != 3)
    {
        printf("Please give correct number(3) of command line inputs!\n");
        return -1;
    }
    FILE *fp;
    int source_vertex = atoi(argv[argc - 1]);
    fp = fopen(argv[argc - 2], "r");
    if (fp == NULL)
    {
        printf("Couldn't open input file, sorry try again!\n");
        return -1;
    }
    int origin, destination, weight, line = 0;
    int max = 0;
    rewind(fp);
    while (fscanf(fp, "%d%d%d", &origin, &destination, &weight) != EOF)
    {
        if (origin > max)
        {
            max = origin;
        }
        if (destination > max)
        {
            max = destination;
        }
    }
    max = max + 1;
    int **Graph = (int **)malloc(max * sizeof(int *));
    for (int i = 0; i < max; i++)
    {
        Graph[i] = (int *)malloc(max * sizeof(int));
    }
    for (int i = 0; i < max; i++)
    {
        for (int j = 0; j < max; j++)
        {
            Graph[i][j] = 0;
        }
    }
    rewind(fp);
    while (fscanf(fp, "%d %d %d", &origin, &destination, &weight) != EOF)
    {
        Graph[origin][destination] = weight;
    }
    fclose(fp);
    V = max;
    dijkstra(Graph, source_vertex);
    return 0;
}