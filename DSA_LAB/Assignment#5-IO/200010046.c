#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct node  // format of node
{
    char string[100];
    struct node *next;
}node;

node *create(char *data);

int hash_function(char *str, int size);

void insert_ht(char *str, node **hash_table, int size);

void sort(char *str);

void display_ht(char *str, node **hash_table, int key,FILE *output);

int main(int argc, char *argv[])
{
    FILE *fp, *query, *output;
    if (argc != 4) // checking for correct number of command line arguments
    {
        printf("Please give correct command line arguments.\n");
        return -1;
    }
    fp = fopen(argv[1], "r");
    if (fp == NULL)
    {
        printf("Couldn't open the file.\n"); // error handling
        return -1;
    }
    query = fopen(argv[argc - 1], "r");
    if (query == NULL)
    {
        printf("Couldn't open the file.\n");
        return -1;
    }
    output = fopen("anagrams.txt", "w");
    if (output == NULL)
    {
        printf("Couldn't open the file.\n");
    }
    char str[20];
    int size = atoi(argv[2]);
    int num_words= 0, num_lines= 0;
    node **hash_table = (node **)malloc(size * sizeof(node *));
    for (int i = 0; i < size; i++)
    {
        hash_table[i] = NULL;
    }
    while (fscanf(fp, "%s", str) != EOF) //reading from words.txt
    {
        insert_ht(str, hash_table, size);
    }
    while (fscanf(query, "%s", str) != EOF) //reading from query.txt 
    {
        num_words++;
    }
    rewind(query);
    while (fscanf(query, "%s", str) != EOF)
    {
        num_lines++;
        int key = hash_function(str, size);
        display_ht(str, hash_table, key, output);
        if (num_lines!= num_words)
            fprintf(output,"\n");
    }
    fclose(fp);
    fclose(query);
    fclose(output); 
    return 0;
}

node *create(char *data)  // creating nodes
{
    struct node *newnode = malloc(sizeof(struct node));
    int j = 0;
    strcpy(newnode->string, data);
    newnode->next = NULL;
    return newnode;
}
int hash_function(char *str, int size)  // hash function that we are following while inserting
{
    int val = 0;
    for (int i = 0; i < strlen(str); i++)
    {
        val += str[i];
    }
    return val % size;
}
void insert_ht(char *str, node **hash_table, int size) // inserting into hash table
{
    node *head = create(str);
    int key = 0;
    key = hash_function(str, size);
    if (hash_table[key] == NULL)
    {
        hash_table[key] = head;
    }
    else
    {
        head->next = hash_table[key];
        hash_table[key] = head;
    }
}
void sort(char *str)  // sorting the given string in decreasing order
{
    for (int i = 0; i < strlen(str); i++)
    {
        for (int j = 0; j < strlen(str); j++)
        {
            char temp;
            if (str[i] > str[j])
            {
                temp = str[i];
                str[i] = str[j];
                str[j] = temp;
            }
        }
    }
}
void display_ht(char *str, node **hash_table, int key, FILE *output) // displaying the hash table
{
    if (hash_table[key] == NULL)
    {
        return;
    }
    else
    {
        node *temp = hash_table[key];
        int i = 0;
        while (temp != NULL)
        {
            if (strlen(str) == strlen(temp->string))
            {
                char str1[20], str2[20];
                strcpy(str1, str);
                strcpy(str2, temp->string);
                sort(str1);
                sort(str2);
                if (strcmp(str1, str2) == 0)
                {
                    if (i == 0)
                    {
                        fprintf(output, "%s", temp->string);
                        i++;
                    }
                    else
                        fprintf(output, " %s", temp->string);
                }
            }
            temp = temp->next;
        }
    }
    return;
}