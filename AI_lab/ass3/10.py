import random as rnd
import copy
from operator import itemgetter
import itertools
var_1 = ['~x1','x1']
var_2 = ['~x2','x2']
var_3 = ['~x3','x3']
var_4 = ['~x4','x4']

shuru_karte_hai = []
ops = []

#Initialising the 4-SAT 
def Initialize(arr):
    #ops.clear()
    while len(arr)!=5 : 
        cnt_1=rnd.randint(0,1);cnt_2=rnd.randint(0,1);cnt_3=rnd.randint(0,1);cnt_4=rnd.randint(0,1)
        # cnt_1 = 0; cnt_2=0; cnt_3 = 0;cnt_4=0         
        clause = [var_1[cnt_1],var_2[cnt_2],var_3[cnt_3],var_4[cnt_4]]
        arr.append(clause)
        oper = [cnt_1,cnt_2,cnt_3,cnt_4]
        ops.append(oper)
    
    return arr

# Opening output file "
def Write():
    out = open("output.txt","w")

    begin = Initialize(shuru_karte_hai)
  
    for i in range(5):
            string = "( " + begin[i][0] + " V " + begin[i][1] + " V " + begin[i][2] + " V " + begin[i][3] + " )"
            out.write(string + "\n")
    out.close()


#Calling The Write() function to overwrite the output.txt file
Write()

import copy
start = [1,0,1,1]
stored_start = start
open_ls = []

print("Start state: ",stored_start)
def goaltest(leaf):
    if  heuristic(leaf) == 5 :
        return True
    else :
        return False

def toggle(bit):
    if bit == 0:
        bit =1
    else:
        bit = 0
    return bit

def movegen(leaf):
    open_ls.append(leaf)
    neighbour = []
    for i in range(4):
        temp = copy.deepcopy(leaf)
        temp[i] = toggle(temp[i])
        if open_ls.count(temp) == 0:
            neighbour.append(temp)
    return neighbour

def movegen_struck(leaf):
    neighbour = []
    for i in range(4):
        for j in range(i+1,4):
            temp = copy.deepcopy(leaf)
            temp[i] = toggle(temp[i])
            temp[j] = toggle(temp[j])
            if open_ls.count(temp) == 0:
                neighbour.append(temp)
    return neighbour

def heuristic(list,matrix = ops):
    # print(ops)
    num=0
    for i in range(5):
      #for j in len(list)
          if (matrix[i][0]==list[0] or matrix[i][1]==list[1] or matrix[i][2]==list[2] or matrix[i][3]==list[3]):
            num +=1
    return num

def max_hur(ls,curr):
    score = []
    for x in ls :
        score.append([x,heuristic(x)])
    score.sort(reverse = True , key = lambda x:x[1])
    if heuristic(curr) > heuristic(score[0][0]):
        return curr
    else:
        return score[0][0]

def struck(start,explored):


    while(goaltest(start)!=True ):
        
        explored+=1 
        nbr = movegen_struck(start)
        temp = copy.deepcopy(start)
        start = max_hur(nbr,temp)
        if (temp == start):
            print("Hill climbing is struck even with the variation!")
            return explored
    
    if (goaltest(start)==True):
        print("Goal State: ",start)
    return explored

def vnd(start):
    #print("1",start,heuristic(start))
    explored = 0
    # print("hi")
    while (goaltest(start)!=True):
        # print(start)
        explored += 1
        nbr = movegen(start)
        #print(nbr)
        temp = copy.deepcopy(start)
        start = max_hur(nbr,temp)
        #print(heuristic(start))
        if (temp == start):
            explored = struck(start,explored)
            break
        #print("hi")

    if (goaltest(start)==True):
        print("Goal State: ",start)    
    explored += 1
    return explored

num_nodes = vnd(start)

print("Number of nodes explored in VND Search: ",num_nodes)

def max_beta_hur(ls, curr, beta = 2):
    score = []
    beta_list = []
    for x in ls:
        score.append([x, heuristic(x)])
    score.sort(reverse=True, key=lambda x: x[1])
    for i in range(beta):
        beta_list.append(score[i][0])
    # print(beta_list)
    # for i in beta_list:
    #     print(i,heuristic(i))
    return beta_list

def beam_search(start):

    # print(start,heuristic(start))

    num = int(input("Please Enter Beam width: "))
    found = False

    open_list = []
    list2 = []

    explored = 0
    temp1 = copy.deepcopy(start)

    if goaltest(start) == True:
        explored += 1
        found = True
        print("Goal State: ",start)
        return explored

    open_list.append(temp1)

    neighb = movegen(start)

    for i in neighb:
        open_list.append(i)
    explored += 1

    while(found == False and len(open_list) != 0):
        
        list2 = max_beta_hur(open_list,temp1,num)

        open_list.clear()

        for start in list2:
            explored += 1

            if (goaltest(start) == True):
                found = True
                print("Goal State: ",start)
                return explored
            
            else:
                open_list = movegen(start)
    return explored


nod = beam_search(start)
print("Number of nodes explored in Beam Search : ", nod)


def Tabu_search(start, tabu_tenure):
    len_tabulist = 3
    tabu_list = []
    steps = 0
    m = []
    m.append(1)
    m.append(0)
    m.append(1)
    m.append(1)
    temp = copy.deepcopy(m)

    while 1:
        
        if goaltest(start) == True:
            steps += 1
            print("Goal State ", start)
            print("Number of states explored ", steps)
            break

        else:
            adjacents = movegen(start)
            # print(adjacents)
            max = 0
            k = -1
            for i in range(4):
                if max < heuristic(adjacents[i]) and adjacents[i] not in tabu_list:
                    if m[i] == temp[i]:
                        max = heuristic(adjacents[i])
                        k = i
            if k != -1:
                for i in range(4):
                    if m[i] != temp[i]:
                        m[i] = m[i]-1
                m[k] = tabu_tenure
                start = adjacents[k]
                if len_tabulist > len(tabu_list):
                    tabu_list.append(start)
                else:
                    tabu_list.pop(0)
                    tabu_list.append(start)

                steps += 1


tenure = int(input("Please enter tabu tennure: "))
Tabu_search(start, tenure)


