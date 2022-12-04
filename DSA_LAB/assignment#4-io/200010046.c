#include <stdio.h>
#include <stdlib.h>
#include <string.h>
typedef struct BSTree   // format of struct BSTree
{
    struct BSTree *left;
    int data;
    struct BSTree *right;
} Tree;

Tree *root = NULL;

void insert(int element, FILE *output);
void inorder(Tree *inordertraversal, FILE *output);
void preorder(Tree *preordertraversal, FILE *output);
void postorder(Tree *postordertraversal, FILE *output);
Tree *search(Tree *searching, int element);
Tree *minimum(Tree *min);
Tree *maximum(Tree *max);
void predecessor(Tree *temp, int element, FILE *output);
void successor(Tree *temp, int element, FILE *output);

int i = 0, j = 0, k = 0;    // global vairables

int main(int argc, char *argv[])
{
    FILE *fp, *output;
    if (argc != 2)     // checking for correct number of command line arguments
    {
        printf("Please give correct command line arguments.\n");
        return -1;
    }
    fp = fopen(argv[argc - 1], "r");
    if (fp == NULL)
    {
        printf("Couldn't open the file.\n");
        return -1;
    }
    output = fopen("bst.txt", "w");
    if (output == NULL)
    {
        printf("Couldn't open the file.\n");
    }
    char command[20];
    int num = -1;
    while (fscanf(fp, "%s%d", command, &num) != EOF) // reading file inputs using fscanf
    {
        if (strcmp(command, "insert") == 0)
        {
            insert(num, output);
        }
        else if (strcmp(command, "inorder") == 0)
        {
            inorder(root, output);
            i = 0;
            fprintf(output, "\n");
        }
        else if (strcmp(command, "preorder") == 0)
        {
            preorder(root, output);
            j = 0;
            fprintf(output, "\n");
        }
        else if (strcmp(command, "postorder") == 0)
        {
            postorder(root, output);
            k = 0;
            fprintf(output, "\n");
        }
        else if (strcmp(command, "search") == 0)
        {
            Tree *temp = search(root, num);
            if (temp == NULL)
            {
                fprintf(output, "%d not found\n", num);
            }
            else
            {
                fprintf(output, "%d found\n", temp->data);
            }
        }
        else if (strcmp(command, "minimum") == 0)
        {
            Tree *temp = minimum(root);
            if (temp != NULL)
            {
                fprintf(output, "%d\n", temp->data);
            }
            else
            {
                fprintf(output, "\n");
            }
        }
        else if (strcmp(command, "maximum") == 0)
        {
            Tree *temp = maximum(root);
            if (temp != NULL)
            {
                fprintf(output, "%d\n", temp->data);
            }
            else
            {
                fprintf(output, "\n");
            }
        }
        else if (strcmp(command, "predecessor") == 0)
        {
            Tree *temp = search(root, num);
            if (temp == NULL)
            {
                fprintf(output, "%d does not exist\n", num);
            }
            else
            {
                predecessor(root, num, output);
            }
        }
        else if (strcmp(command, "successor") == 0)
        {
            Tree *temp = search(root, num);
            if (temp == NULL)
            {
                fprintf(output, "%d does not exist\n", num);
            }
            else
            {
                successor(root, num, output);
            }
        }
    }
    fclose(fp);
    fclose(output);
    return 0;
}

void insert(int element, FILE *output)  // inserting a node
{
    Tree *newnode = (Tree *)malloc(sizeof(Tree));
    if (newnode == NULL)
    {
        printf("Couldn't allocate memory in heap.\n");
        return;
    }
    newnode->data = element;
    newnode->left = NULL;
    newnode->right = NULL;
    if (root == NULL)
    {
        root = newnode;
    }
    else
    {
        Tree *current, *temp;
        current = root;
        while (current != NULL)
        {
            temp = current;
            if (current->data < newnode->data)
            {
                current = current->right;
            }
            else
            {
                current = current->left;
            }
        }
        if (temp->data > newnode->data)
        {
            temp->left = newnode;
        }
        else
        {
            temp->right = newnode;
        }
    }
    fprintf(output, "%d inserted\n", element);
    //free(newnode);
}
void inorder(Tree *inordertraversal, FILE *output)  // inorder transversal
{
    if (inordertraversal == NULL)
    {
        return;
    }
    inorder(inordertraversal->left, output);
    if (i == 0)
    {
        fprintf(output, "%d", inordertraversal->data);
        i++;
    }
    else
    {
        fprintf(output, " %d", inordertraversal->data);
    }
    inorder(inordertraversal->right, output);
}
void preorder(Tree *preordertraversal, FILE *output)  // preorder transversal
{
    if (preordertraversal == NULL)
    {
        return;
    }
    if (j == 0)
    {
        fprintf(output, "%d", preordertraversal->data);
        j++;
    }
    else
    {
        fprintf(output, " %d", preordertraversal->data);
    }
    preorder(preordertraversal->left, output);
    preorder(preordertraversal->right, output);
}
void postorder(Tree *postordertraversal, FILE *output)  // postorder transversal
{
    if (postordertraversal == NULL)
    {
        return;
    }
    postorder(postordertraversal->left, output);
    postorder(postordertraversal->right, output);
    if (k == 0)
    {
        fprintf(output, "%d", postordertraversal->data);
        k++;
    }
    else
    {
        fprintf(output, " %d", postordertraversal->data);
    }
}
Tree *search(Tree *searching, int element)  // binary search
{
    while (searching != NULL)
    {
        if (searching->data == element)
        {
            return searching;
        }
        else if (searching->data > element)
        {
            searching = searching->left;
        }
        else
        {
            searching = searching->right;
        }
    }
    return NULL;
}
Tree *minimum(Tree *min)  // minimum element in a tree
{
    if (min == NULL)
    {
        return min;
    }
    while (min->left != NULL)
    {
        min = min->left;
    }
    return min;
}
Tree *maximum(Tree *max) // maximum element in a tree
{
    if (max == NULL)
    {
        return max;
    }
    while (max->right != NULL)
    {
        max = max->right;
    }
    return max;
}
void predecessor(Tree *temp, int element, FILE *output) // predecessor in the given tree
{
    Tree *pre = NULL;
    if (temp == NULL)
    {
        fprintf(output, "%d does not exist\n", element);
        return;
    }
    Tree *min = minimum(temp);
    if (min)
    {
        if (element == min->data)
        {
            fprintf(output, "predecessor of %d does not exist\n", element);
        }
    }
    while (temp != NULL)
    {
        if (temp->data == element)
        {
            if (temp->left)
            {
                pre = temp->left;
                while (pre->right)
                {
                    pre = pre->right;
                }
            }
            break;
        }
        else if (temp->data < element)
        {
            pre = temp;
            temp = temp->right;
        }
        else
        {
            temp = temp->left;
        }
    }
    fprintf(output, "%d\n", pre->data);
}

void successor(Tree *temp, int element, FILE *output) // successor in the given tree
{
    Tree *suc = NULL;

    if (temp == NULL)
    {
        fprintf(output, "%d does not exist\n", element);
        return;
    }
    Tree *max = maximum(temp);

    if (max)
    {
        if (element == max->data)
        {
            fprintf(output, "successor of %d does not exist\n", element);
            return;
        }
    }

    while (temp != NULL)
    {
        if (temp->data == element)
        {
            if (temp->right)
            {
                suc = temp->right;
                while (suc->left)
                {
                    suc = suc->left;
                }
                break;
            }
            else
            {
                temp = temp->right;
            }
        }
        else if (temp->data < element)
        {
            temp = temp->right;
        }
        else
        {
            suc = temp;
            temp = temp->left;
        }
    }
    fprintf(output, "%d\n", suc->data);
}