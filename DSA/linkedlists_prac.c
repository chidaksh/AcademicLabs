#include <stdio.h>
#include<stdlib.h>
struct node
{
    int data;
    struct node *next;
};
struct node *head = NULL;
struct node *create()
{
    struct node *temp = (struct node *)malloc(sizeof(struct node));
    if (temp == NULL)
    {
        printf("couldn't allocate space in the heap.\n");
        exit(0);
    }
    printf("Enter the value of the data.\n");
    scanf("%d", &temp->data);
    temp->next = NULL;
    return temp;
}
void insert_begin()
{
    struct node *temp = create();
    if (head == NULL)
    {
        head = temp;
    }
    else
    {
        temp->next = head;
        head = temp;
    }
}
void display()
{
    struct node *temp = head;
    if (head == NULL)
    {
        printf("The list is empty\n");
        return;
    }
    else
    {
        while (temp != NULL)
        {
            printf("%d\n", temp->data);
            temp = temp->next;
        }
    }
}
void insert_end(){
    struct node *temp,*newnode;
    newnode = create();
    if(head==NULL){
        head=newnode;
    }
    else{
        while(temp->next!=NULL){
            temp = temp->next;
        }
        temp->next=newnode;
    }
}
void insert_pos(){
    struct node* position,*newnode;
    int pos;
    printf("enter the value of pos\n");
    scanf("%d",&pos);
    newnode = create();
    if(pos==0){
        newnode->next= head;
        head = newnode;
    }
    else{
        for(int i=0;i<pos-1;i++){
            position=head;
            position=position->next;
            if(position==NULL){
                printf("index out of bounds\n");
                return;
            }
        }
        newnode->next=position->next;
        position->next=newnode;        //??
    }
}
int main()
{

}