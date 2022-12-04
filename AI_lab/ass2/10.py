
import sys

from operator import itemgetter


start = []
goal = []


if (len(sys.argv) != 2):
    raise ValueError(
        "Give proper command line arguments(only 2 commands) !")


"opening input file"
fp = open(sys.argv[1], "r")

"opening output file"
pos = open("output.txt", "w")

string = fp.readline()
row1 = string.split()
algo_name = row1[0]
huristic_num = int(row1[1])

open_list = []
closed_list = []

for i in range(3):
    line = fp.readline()
    list = line.split()
    start.append(list)
fp.readline()

heap_befs = []
heap_hill_climbing = []
var_check = 0

for i in range(3):
    line = fp.readline()
    list = line.split()
    goal.append(list)

goal_dict = dict((j, (x, y)) for x, i in enumerate(goal)
                 for y, j in enumerate(i))

import copy

def goal_test(state):
    global goal
    if goal == state:
        return True
    else:
        return False


"if a random block is in it's correct position then the value is +1, otherwise it is -1"


def huristic_1(current_dict, current):
    "1st huristic"
    global goal_dict
    heu_val = 0
    for i in range(3):
        for j in range(len(current[i])):
            goal_x, goal_y = goal_dict[current[i][j]]
            current_x, current_y = current_dict[current[i][j]]
            if(goal_x == current_x and goal_y == current_y):
                heu_val += 1
            else:
                heu_val -= 1
    return heu_val


"manhattan distance"


def huristic_2(current_dict, current):
    "2nd huristic"
    global goal_dict
    heu_val = 0
    "based on manhattan distance"
    for i in range(3):
        for j in range(len(current[i])):
            goal_x, goal_y = goal_dict[current[i][j]]
            current_x, current_y = current_dict[current[i][j]]
            heu_val += (abs(current_x - goal_x) + abs(current_y - goal_y))
    return heu_val

import math
"euclidean distance"


def huristic_3(current_dict, current):
    "3rd huristic"
    global goal_dict
    heu_val = 0
    "based on eucldian distance"
    for i in range(3):
        for j in range(len(current[i])):
            goal_x, goal_y = goal_dict[current[i][j]]
            current_x, current_y = current_dict[current[i][j]]
            "use of math library"
            heu_val -= math.sqrt((current_x - goal_x) **
                                 2 + (current_y - goal_y)**2)
    return heu_val


def heuristic(current_state, huristic_num):
    "main heuristic"
    global goal, goal_dict
    "need copy library here "
    current = copy.deepcopy(current_state)
    current_dict = dict((j, (x, y)) for x, i in enumerate(current)
                        for y, j in enumerate(i))
    if(huristic_num == 1):
        "1st"
        heu = huristic_1(current_dict, current)
    elif(huristic_num == 2):
        "2nd"
        heu = huristic_2(current_dict, current)
    elif(huristic_num == 3):
        "3rd"
        heu = huristic_3(current_dict, current)
    return heu



def move_gen(current_state):
    "deepcopy does complete copy of the list not pointer copy "
    present_state = copy.deepcopy(current_state)
    sides = []
    global closed_list, open_list
    "range(x) goes from 0 to x-1"
    for i in range(len(present_state)):
        check = copy.deepcopy(present_state)
        if len(check[i]) > 0:
            value = check[i].pop()
            for k in range(len(check)):
                check_t = copy.deepcopy(check)
                if k != i:
                    check_t[k] = check_t[k] + [value]
                    if (check_t not in closed_list and check_t not in open_list):
                        sides.append(check_t)
    "generates all the unvisited neighbours"
    return sides

def print_state(state):
    for i in state:
        print(i)

def hill_climbing(huristic_num):
    " this function is used for hill_climbing"
    global closed_list, open_list, goal, heap_hill_climbing, start, var_check
    "using copy method"
    open_list.append(copy.deepcopy(start))
    c_state = copy.deepcopy(start)
    while(1):
        "deepcopy copies completly unlike shallow copy"
        "the parent list doesnt change"
        closed_list.append(copy.deepcopy(c_state))
        "checking goaltest"
        if(goal_test(c_state)):
            pos.write("The goal state is reached successfully\n\n")
            return c_state
        heu_prev = heuristic(c_state, huristic_num)
        sides = move_gen(c_state)
        "looping through sides"
        for i in sides:
            heuri = heuristic(i, huristic_num)
            heap_hill_climbing.append([i, heuri])
        if(huristic_num == 2):
            "using itemgetter"
            c_heap = copy.deepcopy(min(heap_hill_climbing, key=itemgetter(1)))
        else:
            "using itemgetter"
            c_heap = copy.deepcopy(max(heap_hill_climbing, key=itemgetter(1)))
        if(c_heap[1] <= heu_prev):
            "writing into output file"
            pos.write("goal state cannot be reached\n\n")
            var_check = 1
            return c_state

        c_state = c_heap[0]
        heap_hill_climbing = []


def befs(huristic_num):
    "best first search"
    global closed_list, open_list, goal, heap_befs, start, var_check
    "using deepcopy"
    open_list.append(copy.deepcopy(start))
    c_state = copy.deepcopy(start)
    "while loop with True condition"
    while(1):
        closed_list.append(copy.deepcopy(c_state))
        if(goal_test(c_state)):
            "writing into output file"
            pos.write("The goal state is reached successfully\n\n")
            return c_state
        open_list.remove(c_state)
        heu_prev = heuristic(c_state, huristic_num)
        sides = move_gen(c_state)
        for i in sides:
            open_list.append(i)
            heuri = heuristic(i, huristic_num)
            heap_befs.append([i, heuri])
        list = [c_state, heu_prev]
        if(list in heap_befs):
            heap_befs.remove(list)
        if(len(open_list) == 0):
            "writing into output file"
            pos.write("goal state cannot be reached\n")
            var_check = 1
            return c_state
        if(huristic_num == 2):
            "2nd"
            "using itemgetter"
            c_heap = copy.deepcopy(min(heap_befs, key=itemgetter(1)))
        else:
            "itemgetter"
            c_heap = copy.deepcopy(max(heap_befs, key=itemgetter(1)))
        c_state = c_heap[0]


if(algo_name == 'befs'):
    "initiating befs"
    opt = copy.deepcopy(befs(huristic_num))

elif(algo_name == 'hill_climbing'):
    "initiating hill climbing"
    opt = copy.deepcopy(hill_climbing(huristic_num))
else:
    "wrong inputs"
    print("Enter valid inputs\n")

if(var_check == 0):
    "writing into output file"
    pos.write(
        "\nThe number of intermediate states (including start and final state) are : ")
    pos.write(str(len(closed_list)))
    "adding \n"
    pos.write("\n\n\n")

for i in closed_list:
    for j in i:
        for k in j:
            "writing into output file"
            pos.write(k)
        pos.write(" ")
    pos.write("\n")

pos.close()
