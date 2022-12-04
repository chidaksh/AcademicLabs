import random
import sys
import time
import numpy as np
import math
import csv

"""
definig class A_C
"""

"""
"""
class A_C(object):

    """Defining Init function"""

    def __init__(self, dist, no_ants, no_iter, alpha, beta, rho, Q):
        
        """attributes """
        self.dist = dist
        """"
        """
        self.no_ants = no_ants
        """
        """
        self.no_iter = no_iter
        """
        no iterations
        """
        self.alpha = alpha
        self.beta = beta
        self.rho = rho
        self.Q = Q   
        self.p_top = int(0.1*num_cities+1)
        self.next = False
        """bool value"""
        self.N = len(dist)
        """len(dist)"""
        self.pheromones = [[0.1 for city in range(num_cities)] for selectedcity in range(num_cities)]
        """phermones"""
        self.b_cost = float('Inf')
        """num_cities"""
        self.b_tour = range(num_cities)

    """defining a method called optimise"""

    def optimise(self):
        while time.time()-start < 296:
            """run the loop based on condition of time"""
            ants = []
            change_pheromone = [[0 for x in range(num_cities)] for y in range(num_cities)]
            """change phermone is a double list"""
            for j in range(self.no_ants):
                ant = Ant(self.dist,self.pheromones,self.alpha, self.beta)
                """for loop"""
                ants.append(ant)
                """conditional statement"""
                if ant.cost_of_path(self.dist) < self.b_cost:
                    self.b_cost = ant.cost_of_path(self.dist)
                    """initializing b_cost"""
                    self.b_tour = ant.curr_city
                    """initializing the attributes"""
                    self.prev_change = time.time()
                    print("Length of the tour is =", self.b_cost, sep=' ')
                    """printing the tours"""
                    print(*self.b_tour, sep=" ")
                    print("------------------------------------------------------------------------------------------------------------------------------------------------------")

            """sorting ants"""
            ants.sort(key=lambda city: city.cost_of_path(self.dist))
            
            for var_1 in ants[:self.p_top]:
                for i, v in enumerate(var_1.curr_city):
                    nextOne = var_1.curr_city[(i+1)%num_cities]
                    """changing change_phermone"""
                    change_pheromone[v][nextOne] += self.Q/dist[v][nextOne]

            """loops"""
            for i in range(num_cities):
                """"""
                for j in range(num_cities):
                    """"""
                    self.pheromones[i][j] = (1-self.rho)*self.pheromones[i][j] + change_pheromone[i][j]

            """ break if time is greater than 300"""
            if time.time()-self.prev_change > 300:
                break

"""Defining class Ant"""


class Ant(object):

    """Defining Init function"""

    def __init__(self, dist, pheromones, alpha, beta):
        self.curr_city = []
        """"""
        self.getPath(dist, pheromones, alpha, beta)

    """Defining getpath method Ant"""

    def getPath(self, dist, pheromones, alpha, beta):
        """attributes"""
        initiate = random.randint(0, num_cities-1)
        possible_cities = list(range(0, num_cities))
        """possible_cities"""
        possible_cities.remove(initiate)
        """changing curr_city"""
        self.curr_city.append(initiate)
        while(len(self.curr_city) < num_cities):
            """loop condition is based on num_cities"""
            prev_city = self.curr_city[-1]
            """changign probability baed on phermones"""
            probability = [(pheromones[prev_city][nextPossibleCity]**alpha * (1/dist[prev_city][nextPossibleCity])**beta) for nextPossibleCity in possible_cities]
            """changing probSet"""
            probSet = [x/sum(probability) for x in probability]
            next_city = random.choices(possible_cities, weights=probSet)[0]
            """changing curr_city"""
            self.curr_city.append(next_city)
            possible_cities.remove(next_city)


    """Def method cost_of_path"""


    def cost_of_path(self, dist):
        value_1 = 0
        for i in range(len(self.curr_city)):
            """for loop"""
            value_1 += dist[self.curr_city[i % num_cities]][self.curr_city[(i+1) % num_cities]]
        return value_1
"""
Defining a function print_ls for printing all the entries of the list
"""


def print_ls(x):
    for i in x:
        print(x)
    return



if __name__ == '__main__':
    coor = []
    dist = []
    start = time.time()
    """Raising value error if number of commands are more than 2"""
    if (len(sys.argv) != 2):
        raise ValueError(
            "Give proper command line arguments(only 2 commands)!")

    """checking if it's euclidean or non-eculidean"""
    lines_info = open(sys.argv[1], "r").readlines()
    prob_model = False
    if(lines_info[0] == "euclidean"):
        prob_model = True

    num_cities = int(lines_info[1])

    for i in range(num_cities):
        c = [float(x) for x in lines_info[i+2].strip().split(' ')]
        """changing c """
        coor.append(c)
        d = [float(x) for x in lines_info[num_cities+2+i].strip().split(' ')]
        """changing d """
        dist.append(d)
    """if else by checking euclidean or non-eculidean"""

    if prob_model:
        ACO = A_C(dist, no_ants=int(num_cities),no_iter=200, alpha=3, beta=3, rho=0.1, Q=0.1)
        """euc"""
    else:
        ACO = A_C(dist, no_ants=int(num_cities),no_iter=300, alpha=5, beta=5, rho=0.05, Q=0.05)

    """Optimization"""
    ACO.optimise()
