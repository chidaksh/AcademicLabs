#include <stdio.h>
#include <stdlib.h>
void countingsort(int *arr, int num_count, int digit)
{
    int max = 0;
    for (int i = 0; i < num_count; i++)
    {
        if (((arr[i] / digit) % 10) > max)
        {
            max = arr[i];
        }
    }
    int *countarr = (int *)malloc(sizeof(int) * (max + 1));
    int *outputarr = (int *)malloc(sizeof(int) * num_count);
    if (countarr == NULL || outputarr == NULL)
    {
        printf("Couldn't allocate memory for ths arrays in heap.\n");
        return;
    }
    for (int i = 0; i < max + 1; i++)
    {
        countarr[i] = 0;
    }
    for (int i = 0; i < num_count; i++)
    {
        countarr[(arr[i] / digit) % 10] += 1;         // implementing array "countarr" like a associative array
    }
    for (int i = 0; i < max + 1; i++)
    {
        countarr[i] += countarr[i - 1];             // finding cumulative sum of the array "countarr"
    }
    for (int i = num_count - 1; i >= 0; i--)
    {
        outputarr[countarr[(arr[i] / digit) % 10] - 1] = arr[i];  // storing the sorted (digit wise sorted) elements in 
        countarr[(arr[i] / digit) % 10]--;                        // an array called "outputarr"
    }
    for (int i = 0; i < num_count; i++)
    {
        arr[i] = outputarr[i];      // copying outputarr to arr 
    }
    free(countarr);
    free(outputarr);
}
void radixsort(int *arr, int num_count, int places, FILE *output)
{
    for (int digit = 1; places > 0; digit *= 10, places--)
    {
        countingsort(arr, num_count, digit);     //calling countingsort 
    }
    for (int i = 0; i < num_count; i++)
    {
        fprintf(output, "%d", arr[i]);          // printing final sorted array
        if (i < num_count - 1)
        {
            fprintf(output, "\n");
        }
    }
}
int main(int argc, char *argv[])
{
    int num_count = 0, temp = 0;          
    FILE *fp, *output;
    if (argc != 3)
    {
        printf("Please give correct command line arguments.\n");
        return -1;
    }
    fp = fopen(argv[argc - 2], "r");
    if (fp == NULL)
    {
        printf("Couldn't open the file.\n");
        return -1;
    }
    output = fopen("radix.txt", "w");
    if (output == NULL)
    {
        printf("Couldn't open the file.\n");
    }
    while (fscanf(fp, "%d", &temp) != EOF)
    {
        num_count++;                         //counting total number of elements in the input file
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
        if (fscanf(fp, "%d", &arr[i]) == EOF) //copying elements from input file to our array "arr"
        { 
            break;
        }
    }
    radixsort(arr, num_count, atoi(argv[argc - 1]), output); //calling function radixsort
    fclose(fp);
    fclose(output);
    free(arr);
    return 0;
}