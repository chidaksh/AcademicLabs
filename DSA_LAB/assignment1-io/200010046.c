#include<stdio.h>
#include<stdlib.h>
struct stackrec{ // using struct to maintain proper record for each stack
    int sizeofstack;
    char symbol;
    int* data;
};
void push(int num,struct stackrec *stack,FILE*fp,int stacksize);
int pop(struct stackrec *stack,FILE*fp);
void toh(int num_disks,int stacksize,struct stackrec*from_stack,struct stackrec*dest_stack,struct stackrec*aux_stack,FILE*fp);
int main(int argc,char* argv[]){
    FILE*fp = fopen("toh.txt","w");
    // checking if file pointer is NULL or not 
    if(fp==NULL){
        printf("Couldn't open the file\n");
        return -1;
    }
    // checking whether two command line arguments are given or not 
    if(argc!=2){
        printf("please provide proper inputs in the command line.Give only 2 agruments\n");
        return -1;
    }
    int stacksize = atoi(argv[argc-1]);//stackmaxsize
    struct stackrec *stacks = (struct stackrec*)malloc(3 * sizeof(struct stackrec));
    //checking if the pointer is successfully allocated heap memory or not
    if(stacks==NULL){
        printf("Couldn't allocate memory in heap\n");
        return -1;
    }
    for(int i=0;i<3;i++){
        stacks[i].data = (int*) malloc (sizeof(int)*(stacksize));
        if(stacks[i].data==NULL){
            printf("Couldn't allocate memory in heap\n");
            return -1;
        }
    }
    //initializing top and symbol for every stack
    for(int i=0;i<3;i++){
        stacks[i].sizeofstack=-1;
    }
    stacks[0].symbol='A';stacks[1].symbol='B';stacks[2].symbol='C';
    for(int i=1;i<stacksize+1;i++){ //#loop from 1 to atoi(argv[1])+1
        push(stacksize+1-i,&stacks[0],fp,stacksize);
    }
    // calling the toh function with proper parameters
    toh(stacksize,stacksize,&stacks[0],&stacks[2],&stacks[1],fp);// #num_disks =6;
    fclose(fp);
    return 0;
}
void push(int num,struct stackrec *stack,FILE*fp,int stacksize){
    //push function pushes an element into the given stack and prints the statement in the given file
    if(stack->sizeofstack==stacksize-1){  // #numdisks-1;
        printf("Stack Overflow\n");
        return;
    }
    stack->sizeofstack++;
    stack->data[stack->sizeofstack]=num;
    fprintf(fp,"Push disk %d to Stack %c\n",num,stack->symbol);
    return;
}
int pop(struct stackrec *stack,FILE*fp){
    //pop function pops out and element and returns it and prints the statement in the given file
    if(stack->sizeofstack==-1){
        printf("Stack Underflow\n");
        return -1;
    }
    int temp=stack->data[stack->sizeofstack];
    fprintf(fp,"Pop disk %d from Stack %c\n",stack->data[stack->sizeofstack],stack->symbol);
    stack->data[stack->sizeofstack]=-1;
    stack->sizeofstack--;
    return temp;
}
void toh(int num_disks,int stacksize,struct stackrec*from_stack,struct stackrec*dest_stack,struct stackrec*aux_stack,FILE*fp){
    //main recursive function to solve the tower of hanoi problem statement 

    if(num_disks==1){ // termination case of the recursive function 
        //need to move disk from_Stack to dest_stack
        pop(from_stack,fp);
        push(1,dest_stack,fp,stacksize);
        return;
    }
    //taking the help of aux_stack to carry on the process
    toh(num_disks-1,stacksize,from_stack,aux_stack,dest_stack,fp);
    pop(from_stack,fp);
    push(num_disks,dest_stack,fp,stacksize);
    toh(num_disks-1,stacksize,aux_stack,dest_stack,from_stack,fp);
}