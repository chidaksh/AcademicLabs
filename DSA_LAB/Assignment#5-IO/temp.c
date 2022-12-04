#include<stdio.h>
#include<stdlib.h>
#include<string.h>

FILE *fw;
struct node{
    char data[99];
    struct node *next;
};
struct node* create_node(char data[]);
int hash_function(char s[],int m);
void insert(char s[],struct node* hash_table[],int m);
void cmp(char s[],int len);
void display(char s[],struct node *hash_table[],int key);

int main(int argc,char*argv[])
{
    FILE *f1,*f2;
    f1=fopen(argv[1],"r");
    f2=fopen(argv[3],"r");
    fw=fopen("anagrams.txt","w+");
    int m=atoi(argv[2]);
    struct node* hash_table[m];
    for(int k=0;k<m;k++)
    {
        hash_table[k]=NULL;
    }
    char s[25];
    char word[25];
    while(fscanf(f1,"%s",s)!=EOF)
    {
        insert(s,hash_table,m);
    }
    int line=0,r=0;
    while(fscanf(f2,"%s",word)!=EOF)
    {
    line++;
    }
    rewind(f2);
    while(fscanf(f2,"%s",word)!=EOF)
    {
        r++;
        int key=hash_function(word,m);
        display(word,hash_table,key);
        // fseek(fw,-1,SEEK_CUR);
        if(r!=line)
        fprintf(fw,"\n");
    }
    fclose(f1);
    fclose(f2);
    fclose(fw);
    return 0;
}

struct node* create_node(char data[])
{
    struct node *newnode=malloc(sizeof(struct node));
    int j=0;
    for(int i=0;data[i]!='\0';i++)
    {
        (newnode->data)[i]=data[i];
        j++;
    }
    (newnode->data)[j]='\0';
    newnode->next=NULL;
    return newnode;
}
int hash_function(char s[],int m)
{
    int key=0;
    for(int i=0;s[i]!='\0';i++){
        key+=(int)s[i];//casting
    }
    key=key%m;
    return key;
}
void insert(char s[],struct node* hash_table[],int m)
{
    struct node *head=create_node(s);
    int key=0;
    key=hash_function(s,m);
    if(hash_table[key]==NULL)
    {
        hash_table[key]=head;
    }
    else
    {
        head->next=hash_table[key];
        hash_table[key]=head;
    }
}
void cmp(char s[],int len)
{
    for(int i=0;i<len;i++)
    {
        for(int j=0;j<len;j++)
        {
             char c;
            if(s[i]>s[j])
            {
                c=s[i];
                s[i]=s[j];
                s[j]=c;
            }
        }
    }
}
void display(char s[],struct node *hash_table[],int key)
{
    if(hash_table[key]==NULL)
    {
       return;
    }
    else{
        int len=strlen(s);
        struct node *temp=hash_table[key];
        int i=0;
        while(temp!=NULL)
        {
            int len1=strlen(temp->data);
            if(len==len1){
                char s1[25],s2[25];
                strcpy(s1,s);
                strcpy(s2,temp->data);
                cmp(s1,len);
                cmp(s2,len1);
                if(strcmp(s1,s2)==0)
                {
                    if(i==0)
                    {
                    fprintf(fw,"%s",temp->data);
                    i++;
                    }
                    else
                    fprintf(fw," %s",temp->data);
                }  
            }
            temp=temp->next;
        }
        // fprintf(fw,"\n");
    }
    return;
}