#include<stdio.h>
#include<stdlib.h>
void merge(int *arr,int start,int mid,int end,int num_int){
    int i,j,index=start;
    int* temp = (int*)malloc(num_int*sizeof(int)); // creating a temp array for storing arr and processing data in it
    if(temp==NULL){
        printf("Couldn't allocate memory in heap.\n");
        exit(0);
    }
    for(i=start,j=mid;(i<mid || j<end);){
        if(i<mid && j<end){                     // when both arrays aren't read
            if(arr[j]>arr[i]){
                temp[index]=arr[i];i++;
            }
            else{temp[index]=arr[j];j++;}
        }
        else{                                   // when one of the arrays is already read
            if(i<mid){temp[index]=arr[i];i++;}
            else{temp[index]=arr[j];j++;}
        }
    index++;
    }
    for(int i=start;i<end;i++){arr[i] = temp[i];} // copying temp into arr 
    free(temp);
    return;
}
void mergesort(int *arr,int start,int end,int num_int){
    if(end==start+1){return;}                       // termination for this recursion 
    int mid = start+(end-start)/2;                  // to avoid integer overflow because start+end/2 can exceed limit easliy comapred to this expression
    mergesort(arr, start, mid,num_int);
    mergesort(arr,mid,end,num_int);
    merge(arr,start,mid,end,num_int);
}
int main(int argc,char*argv[]){
    int num_int=0,temp;
    if(argc!=2){                                         // checking for proper inputs in command line 
        printf("Give proper inputs in the command line.");
        return -1;
    }
    FILE*fp = fopen(argv[1],"r");
    FILE* pos = fopen("mergesort.txt","w");
    if(fp==NULL||pos==NULL){
        printf("couldn't open the file.\n");
        return -1;
    }
    while(fscanf(fp,"%d",&temp)!=EOF){
        num_int++;                                        // reading number of intergers in the file
    }
    rewind(fp);                                           // need to rewind before using fscanf again
    int* data = (int*) malloc (num_int * sizeof(int));
    if(data==NULL){ 
        printf("Couldn't allocate memory in heap.\n");    
        return -1;
    }
    for(int i=0;;i++){
        if(fscanf(fp,"%d",&data[i])==EOF){
            break;
        }
    }
    mergesort(data,0,num_int,num_int);                    // calling mergersort => initiating sorting algo
    for(int i=0;i<num_int;i++){
        fprintf(pos,"%d",data[i]);
        if(i<num_int-1){fprintf(pos,"\n");}
    }
    free(data);                                           // deleting the allocated heap memory 
    return 0;
}