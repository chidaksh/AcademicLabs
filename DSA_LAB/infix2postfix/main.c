#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

bool is_operator(char c)
{
    return (c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '%');
}

int num_operators(char *expression)
{
    int stack_sz = 0;
    for (int i = 0; i < strlen(expression); i++)
    {
        if (is_operator(expression[i]))
        {
            stack_sz++;
        }
    }
    return stack_sz;
}

int precedence(char operator)
{
    switch (operator)
    {
    case '+':
    case '-':
        return 1;
    case '*':
    case '/':
    case '%':
        return 2;
    case '^':
        return 3;
    default:
        return -1;
    }
}

bool isInfixValid(char *expression, FILE *out)
{
    int length = strlen(expression);
    int opening_parentheses = 0, closing_parentheses = 0;
    bool last_char_was_operator = true; // As starting charecter of the expression can't be an operator
    bool last_char_was_operand = false;

    // Check if the expression has an equal number of opening and closing parentheses
    // Check if the expression contains two operators or two operands in a row
    for (int i = 0; i < length; i++)
    {
        char c = expression[i];
        if (is_operator(c))
        {
            if (last_char_was_operator || i == length - 1) // can't end the expression with a operator
            {
                fprintf(out, "Invalid expression\n");
                return false;
            }
            last_char_was_operator = true;
            last_char_was_operand = false;
        }
        else if (c == '(')
        {
            opening_parentheses++;
        }
        else if (c == ')')
        {
            closing_parentheses++;
        }
        else if (expression[i] == ' ')
        {
            // printf("%c\n",expression[i]);
            printf("Spaces not allowed in the expression\n");
            return false;
        }
        else
        {
            if (last_char_was_operand)
            {
                fprintf(out, "Invalid expression\n");
                return false;
            }
            last_char_was_operator = false;
            last_char_was_operand = true;
        }
    }
    if (opening_parentheses != closing_parentheses)
    {
        fprintf(out, "Unequal Parentheses\n");
        return false;
    }
    return true;
}

void *Postfix(char *expression, int stacksize, FILE *out)
{
    char *postfix = NULL;
    int length = strlen(expression);
    postfix = (char *)malloc(sizeof(char) * (length + 2));
    if (postfix == NULL)
    {
        printf("Memory allocation error due to malloc\n");
    }
    char stack[stacksize];
    for (int i = 0; i < stacksize; i++)
    {
        stack[i] = '0'; // initializing all entries by default to 0.
    }
    int top = 0, index = 0;

    for (int i = 0; i < length; i++)
    {
        // printf("entering loop\n");
        char c = expression[i];
        if (is_operator(c))
        {
            while (top > 0 && precedence(stack[top - 1]) >= precedence(c))
            {
                postfix[index++] = stack[top - 1];
                // printf("%c is popped from stack\n", stack[top - 1]);
                fprintf(out, "%c is popped from stack\n", stack[top - 1]);
                stack[--top] = '0';
            }
            stack[top++] = c;
            // printf("%c is pushed into stack\n", c);
            fprintf(out, "%c is pushed into stack\n", c);
        }
        else
        {
            if (c != '(' && c != ')')
            {
                postfix[index++] = c;
            }
            else if (c == '(')
            {
                stack[top++] = c;
                // printf("%c is pushed into stack\n", c);
                fprintf(out, "%c is pushed into stack\n", c);
            }
            else if (c == ')')
            {
                while (top > 0 && stack[top - 1] != '(')
                {
                    postfix[index++] = stack[top - 1];
                    // printf("%c is popped from stack\n", stack[top - 1]);
                    fprintf(out, "%c is popped from stack\n", stack[top - 1]);
                    stack[--top] = '0';
                }
                // printf("%c is popped from stack\n", stack[top - 1]);
                fprintf(out, "%c is popped from stack\n", stack[top - 1]);
                stack[--top] = '0';
            }
        }
    }
    while (top > 0)
    {
        postfix[index++] = stack[top - 1];
        // printf("%c is popped from stack\n", stack[top - 1]);
        fprintf(out, "%c is popped from stack\n", stack[top - 1]);
        stack[--top] = '0';
    }
    postfix[index] = '\0';
    fprintf(out, "Postfix expression is:%s\n", postfix);
}

int main(int argc, char *argv[])
{
    if (argc != 2)
    {
        printf("Wrong Input Format! Correct way to run the program is ./a.out <filename>\n");
        return -1;
    }
    FILE *fp = fopen(argv[argc - 1], "r");
    if (fp == NULL)
    {
        printf("Error in opening %s\n", argv[argc - 1]);
        return -1;
    }
    FILE *out = fopen("output.txt", "w");
    if (out == NULL)
    {
        printf("Error in opening output.txt\n");
        return -1;
    }
    char *infix = NULL;
    size_t infix_size = 0;
    size_t read;
    read = getline(&infix, &infix_size, fp);
    if (read == -1)
    {
        printf("Error reading infix expression from input file\n");
        return -1;
    }
    // printf("%s",infix);
    fclose(fp);
    int stack_size;
    stack_size = num_operators(infix);
    // printf("%d\n", stack_size);
    if (!isInfixValid(infix, out))
    {
        return -1;
    }
    // printf("starting postfix\n");
    Postfix(infix, stack_size, out);
    fclose(out);
    free(infix);
    return 0;
}