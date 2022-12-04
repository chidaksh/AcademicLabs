#include <stdio.h>
#include <stdlib.h>

typedef struct node //struct node
{
    int vertex;
    struct node *next;
    int parent;
} node;
node *head = NULL; // head pointer
node *tail = NULL; // tail pointer

typedef struct Graph // format of struct graph
{
    struct node *lsvertices;
    struct node **adjlists;
    int *visited;
} Graph;

node *createnode(int num_vertex) //creating a node
{
    node *newnode = (node *)malloc(sizeof(node));
    newnode->vertex = num_vertex;
    newnode->next = NULL;
    newnode->parent = -1;
    return newnode;
}

void enqueue(int value) // enqueue function (adding elements to queue)
{
    if (head == NULL)
    {
        node *temp = createnode(value);
        head = temp;
        tail = temp;
    }
    else
    {
        node *temp;
        temp = createnode(value);
        tail->next = temp;
        tail = tail->next;
    }
}

int dequeue() // Removing elements from queue
{
    if (head == NULL)
    {
        printf("The Queue is empty! \n");
        return -1;
    }
    else
    {
        struct node *temp;
        int del;
        temp = head;
        del = temp->vertex;
        head = head->next;
        return del;
        free(temp);
    }
}

Graph *creategraph(int vertices) // creates the memory for the entire graph and does initialization work and all
{
    Graph *graph = (Graph *)malloc(sizeof(Graph));
    graph->lsvertices = (node *)malloc(sizeof(node));
    graph->adjlists = (node **)malloc(vertices * sizeof(node *));
    graph->visited = (int *)malloc(vertices * sizeof(int));
    for (int i = 0; i < vertices; i++)
    {
        graph->adjlists[i] = NULL;
        graph->visited[i] = 0;
    }
    return graph;
}

void addedge(Graph *graph, int src, int dest) // reads edges and then adds them in their respective adjency lists
{
    node *newnode = createnode(dest); // Inserting dest at the beginning of src
    newnode->next = graph->adjlists[src];
    graph->adjlists[src] = newnode;

    newnode = createnode(src); // Inserting src at the beginning of dest
    newnode->next = graph->adjlists[dest];
    graph->adjlists[dest] = newnode;
}

void *bfs(Graph *graph, int start) // does bfs traversal through the graph and initializes parent element to each node
{
    graph->visited[start] = 1;
    enqueue(start);
    graph->lsvertices = createnode(start);
    node *tail2 = graph->lsvertices;
    //tail2->parent=0;
    while (head != NULL)
    {
        int currvertex = dequeue();
        // should allocate parent here
        node *temp = graph->adjlists[currvertex];
        while (temp)
        {
            int adjvertex = temp->vertex;

            if (graph->visited[adjvertex] == 0)
            {
                graph->visited[adjvertex] = 1;
                enqueue(adjvertex);
                tail2->next = createnode(adjvertex);
                tail2 = tail2->next;
                if (tail2->parent == -1)
                {
                    tail2->parent = currvertex;
                }
            }
            temp = temp->next;
        }
    }
}

int shrt_dist(Graph *graph, int vertex) // calculates and returns shortest distance of each node
{
    if (vertex == 0)
    {
        return 0;
    }
    node *temp = graph->lsvertices;
    while (temp != NULL && temp->vertex != vertex)
    {
        temp = temp->next;
    }
    if (temp == NULL)
    {
        return -1;
    }
    else
    {
        if (temp->parent == -1)
        {
            return -1;
        }
        else
        {
            int prev = shrt_dist(graph, temp->parent);
            if (prev == -1)
            {
                return -1;
            }
            else
            {
                return (1 + prev);
            }
        }
    }
}

int main(int argc, char *argv[])
{
    FILE *fp, *output;
    if (argc != 2) // checking for correct number of command line arguments
    {
        printf("Please give correct command line arguments.\n");
        return -1;
    }
    fp = fopen(argv[argc - 1], "r");
    if (fp == NULL)
    {
        printf("Couldn't open the file.\n");
        return -1;
    }
    output = fopen("sd.txt", "w");
    if (output == NULL)
    {
        printf("Couldn't open the file.\n");
    }
    int num_vertices, num_edges, v1, v2;
    fscanf(fp, "%d %d", &num_vertices, &num_edges);
    Graph *graph = creategraph(num_vertices);
    while (fscanf(fp, "%d %d", &v1, &v2) != EOF)
    {
        addedge(graph, v1, v2);
    }
    bfs(graph, 0);
    fprintf(output, "0\n");
    for (int i = 1; i < num_vertices; i++)
    {
        fprintf(output, "%d\n", shrt_dist(graph, i));
    }
    return 0;
}