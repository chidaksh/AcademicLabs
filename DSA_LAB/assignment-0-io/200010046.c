#include<stdio.h>
#include<stdlib.h>
int main(int argc , char *argv[]){
    FILE *fp;
    fp = fopen(argv[argc-1],"r");
    if(fp == NULL){
        printf("couldn't open the file");
        return -1;
    }
    int i,count=0,sum=0,min,max=0;
    float avg;
    if(fscanf(fp,"%d",&min)!=EOF){
        rewind(fp);
    }
    else{
        printf("file has no text");
        return -1;
    }
    do{
        if(fscanf(fp,"%d",&i)==EOF){
            break;
        }
        else{
            sum+=i;
            count++;
            if(i>=max){max = i;}
            if(i<=min){min = i;}
        }
    }while(1);
    avg = (float)sum/count;
    fclose(fp);
    fp = fopen("output.txt","w");
    fprintf(fp,"%d\n%d\n%d\n%d\n%.2f\n",count,min,max,sum,avg);
    fclose(fp);
    return 0;
}