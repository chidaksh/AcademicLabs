#include<stdio.h>
#define LIMIT 100
int stack[LIMIT],top ;
void push(){
    int element;
    if(top==LIMIT-1){
        printf("Can't insert the element , stack is already full");
        return ;
    }
    else{
        printf("Enter the element to be printed");
        scanf("%d",&element);
        stack[top]=element;
        top++;
    }
}
int pop(){
    int temp;
    if(top==-1){
        printf("Stack underflow");
        return ;
    }
    else{
        temp = stack[top];
        top--;
    }
    return temp;
}
void display(){
    if(top == -1 ){
        printf("nothing to display! The stack is empty");
        return;
    }
    else{
        for(int i=top;i>=0;i--){
            printf("%d",stack[i]);
        }
    }
}
int main(){
    top = -1;
}