#include<stdio.h>
#include<stdlib.h>
struct stackrec{
    //#need to allocate this 3 during run time
    int top;
    char symbol;
    int data[];
};
/*void x(int var){
    printf("%d\n",var);
}*/
void push(int num,struct stackrec *stack,FILE*fp,int stacksize);
void pop(int num,struct stackrec *stack,FILE*fp);
void toh(int num_disks,int stacksize,struct stackrec*from_stack,struct stackrec*dest_stack,struct stackrec*aux_stack,FILE*fp);
int main(int argc,char* argv[]){
    FILE*fp = fopen("output.txt","w");
    if(fp==NULL){
        printf("Couldn't open the file");
        return -1;
    }
    int stacksize = atoi(argv[1]);
    //struct stackrec stacks[3];
    struct stackrec **stacks = (struct stackrec**)malloc(3 * sizeof(struct stackrec*));
    //struct stackrec stacks[3];
    for(int i=0;i<3;i++){
        stacks[i] = (struct stackrec*) malloc (sizeof(struct stackrec)+sizeof(int)*(stacksize));
    }
    for(int i=0;i<3;i++){
        stacks[i]->top=-1;
    }
    stacks[0]->symbol='A';stacks[1]->symbol='B';stacks[2]->symbol='C';
    for(int i=1;i<stacksize+1;i++){ //#loop from 1 to atoi(argv[1])+1
        push(stacksize+1-i,stacks[0],fp,stacksize);
    }
    toh(stacksize,stacksize,stacks[0],stacks[2],stacks[1],fp);// #num_disks =6;
    fclose(fp);
    return 0;
}
void push(int num,struct stackrec *stack,FILE*fp,int stacksize){
    if(stack->top==stacksize-1){  // #numdisks-1;
        printf("Stack Overflow");
        return;
    }
    stack->top++;
    stack->data[stack->top]=num;
    fprintf(fp,"Push disk %d to Stack %c\n",num,stack->symbol);
    return;
}
void pop(int num,struct stackrec *stack,FILE*fp){
    if(stack->top==-1){
        printf("Stack Underflow");
        return;
    }
    stack->data[stack->top]=-1;
    stack->top--;
    fprintf(fp,"Pop disk %d from Stack %c\n",num,stack->symbol);
}
void toh(int num_disks,int stacksize,struct stackrec*from_stack,struct stackrec*dest_stack,struct stackrec*aux_stack,FILE*fp){
    if(num_disks==1){
        //need to move disk from Stack A to Stack C
        pop(1,from_stack,fp);
        push(1,dest_stack,fp,stacksize);
        return;
    }
    toh(num_disks-1,stacksize,from_stack,aux_stack,dest_stack,fp);
    pop(num_disks,from_stack,fp);
    push(num_disks,dest_stack,fp,stacksize);
    toh(num_disks-1,stacksize,aux_stack,dest_stack,from_stack,fp);
}