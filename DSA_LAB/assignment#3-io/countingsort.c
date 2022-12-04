#include <stdio.h>
#include <stdlib.h>
void countingsort(int *arr, int num_count, int max)
{
    int *countarr = (int *)malloc(sizeof(int) * (max + 1));
    for (int i = 0; i < max + 1; i++)
    {
        countarr[i] = 0;
    }
    for (int i = 0; i < num_count; i++)
    {
        countarr[arr[i]] += 1;
    }
    for (int i = 0; i < max + 1; i++)
    {
        countarr[i] += countarr[i - 1];
    }
    int *outputarr = (int *)malloc(sizeof(int) * num_count);
    for (int i = 0; i < num_count; i++)
    {
        outputarr[countarr[arr[i]] - 1] = arr[i];
        countarr[arr[i]]--;
    }
    for (int i = 0; i < num_count; i++)
    {
        printf("%d\n", outputarr[i]);
    }
    free(countarr);
    free(outputarr);
}
int main(int argc, char *argv[])
{
    int num_count = 0, temp = 0, max = 0;
    FILE *fp;
    fp = fopen(argv[1], "r");
    if (fp == NULL)
    {
        printf("Couldn't open the file.\n");
        return -1;
    }
    while (fscanf(fp, "%d", &temp) != EOF)
    {
        num_count++;
    }
    rewind(fp);
    int *arr = (int *)malloc(sizeof(int) * num_count);
    if (arr == NULL)
    {
        printf("Couldn't allocate memory in heap.\n");
        return -1;
    }
    for (int i = 0;; i++)
    {
        if (fscanf(fp, "%d", &arr[i]) == EOF)
        {
            break;
        }
        if (arr[i] >= max)
        {
            max = arr[i];
        }
    }
    countingsort(arr, num_count, max);
    free(arr);
    return 0;
}