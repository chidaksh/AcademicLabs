class Tree:
    def __init__(self, abs, ord, h, d, w, val, 
                up_x=-1, up_y=-1, down_x=-1, down_y=-1, 
                right_x=-1, right_y=-1, left_x=-1, left_y=-1, 
                value_up=-1, value_down=-1, value_right=-1, value_left=-1, 
                right=False, left=False, up=False, down=False):
        self.x = abs  # x-coordinate
        self.y = ord  # y-cordinate
        self.height = h  # height of the tree
        self.time_taken = d  # time taken to cut the tree
        self.weight = w  # weight of the tree
        self.value = val  # value of the tree
        self.right = right  # boolean value indicating domino in right direction
        self.left = left  # boolean value indicating domino in left direction
        self.up = up  # boolean value indicating domino in up direction
        self.down = down  # boolean value indicating domino in down direction
        # x coordinate of tree which is the very first tree after this tree in the domino (in up direction)
        self.x_up = up_x
        # y coordinate of tree which is the very first tree after this tree in the domino (in up direction)
        self.y_up = up_y
        # x coordinate of tree which is the very first tree after this tree in the domino (in down direction)
        self.x_down = down_x
        # y coordinate of tree which is the very first tree after this tree in the domino (in down direction)
        self.y_down = down_y
        # x coordinate of tree which is the very first tree after this tree in the domino (in right direction)
        self.x_right = right_x
        # y coordinate of tree which is the very first tree after this tree in the domino (in right direction)
        self.y_right = right_y
        # x coordinate of tree which is the very first tree after this tree in the domino (in left direction)
        self.x_left = left_x
        # y coordinate of tree which is the very first tree after this tree in the domino (in left direction)
        self.y_left = left_y
        self.up_value = value_up  # net cumulative value of domino in up direction
        self.down_value = value_down  # net cumulative value of domino in down direction
        self.right_value = value_right  # net cumulative value of domino in right direction
        self.left_value = value_left  # net cumulative value of domino in left direction
        self.finaldir = "up"  # final direction along which we are going to cut the tree

    def __eq__(self,other):
        if (other != None and self.x == other.x and self.y == other.y and self.value==other.value):
            return True
        else:
            return False

class var:
    def __init__(self, val = -1):
        self.value = val


def alloting_bool(ls, Grid, n):
    # s=0
    # for i in range(n):
    #     for j in range(n):
    #         if(Grid[i][j].value!=-1):
    #             s=s+1
    # for obj in ls:
    #     if (Grid[obj.x][obj.y].value==-1):
    #         print("+++++++++++++++++++++++++++++++++++++++++")
    # for obj in ls:
    #     print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,
    #         obj.down,obj.up,obj.x_right,obj.y_right,obj.x_left,obj.y_left,obj.x_down,
    #         obj.y_down,obj.x_up,obj.y_up,sep=' ')
    
    for p in ls:
        i = 1
        assert(p.x>=0 and p.y>=0)
        while((p.y+i) < n and Grid[p.x][p.y + i].value == -1 and  i <= p.height):
            i = i+1
        if(Grid[p.x][p.y + i].value != -1):
            if (Grid[p.x][p.y+i].weight < p.weight and i <= p.height):
                p.up = True
                p.x_up = p.x
                p.y_up = p.y + i
                assert(p.x_up>=0 and p.y_up>0)
                #print(p.x_up,p.y_up)
        # if (j!=len(ls)):
        #     print("++++++++++++++++++++++++++++++++++++++++")
    # else:
    #     print("++++++++++++++++++++++++++++++++++++++")
    #for obj in ls:
    #    print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.down,obj.up,obj.x_right,obj.y_right,obj.x_left,obj.y_left,obj.x_down,obj.y_down,obj.x_up,obj.y_up,sep=' ')
    # print("---------------------------------------------------------------------")



def checking_time(given_list, t, x1, y1):
    temp = []
    if len(given_list) == 0:
        return temp
    for obj in given_list:
        if((abs(obj.x - x1) + abs(obj.y - y1) + obj.time_taken <= t)):
            temp.append(obj)
        # print(obj.value)
    temp.sort(key=lambda x: (x.value)/((x.time_taken)+abs(x.x - x1)+abs(x.y - y1)), reverse=True)
    # for obj in temp:
        # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,sep=' ')
    # print("-------------------------------------------")
    return temp


def print_path(x, y, x1, y1):
    assert(x1>=0 and y1>=0 and x>=0 and y>=0)
    if x < x1:
        for i in range(x1-x):
            print("move right")
    else:
        for i in range(x-x1):
            print("move left")
    if y < y1:
        for i in range(y1-y):
            print("move up")
    else:
        for i in range(y-y1):
            print("move down")


def print_lastpath(x1, y1, t):
    if t > 0:
        for i in range(t):
            if (x1 > 0):
                print("move left")
                x1 = x1-1
            else:
                print("move right")
                x1 = x1+1


def cumulative_sum(x):
    temp = 0
    for i in x:
        temp = temp + i
    return temp


def value_update(ls, Grid):
    for i in range(len(ls)):
        if ls[i].up == True:
            temp = []
            a = ls[i]
            while (a.up == True):
                temp.append(Grid[a.x_up][a.y_up].value)
                a = Grid[a.x_up][a.y_up]
            ls[i].up_value = ls[i].value + cumulative_sum(temp)

    """ if ls[i].down == True:
            temp = []
            a = ls[i]
            while (a.down == True):
                fresh = ls[:]
                b = next((t for t in fresh if (
                    t.x == a.x_down and t.y == a.y_down)), 0)
                if b == 0:
                    break
                temp.append(b.value)
                a = b
            ls[i].down_value = ls[i].value + cumulative_sum(temp)
            # print(i,ls[i].down_value)

        if ls[i].down== True:
            temp = []
            a = ls[i]
            while (a.down == True):
                temp.append(Grid[a.x_down-1][a.y_down-1].value)
                a = Grid[a.x_down-1][a.y_down-1]
            ls[i].down_value = ls[i].value + cumulative_sum(temp)
        
        if ls[i].right == True:
            temp = []
            a = ls[i]
            while (a.right == True):
                temp.append(Grid[a.x_right-1][a.y_right-1].value)
                a = Grid[a.x_right-1][a.y_right-1]
            ls[i].right_value = ls[i].value + cumulative_sum(temp)
        
        
        
        if ls[i].left== True:
            temp = []
            a = ls[i]
            while (a.left == True):
                temp.append(Grid[a.x_left-1][a.y_left-1].value)
                a = Grid[a.x_left-1][a.y_left-1]
            ls[i].left_value = ls[i].value + cumulative_sum(temp)
        
        if ls[i].down == True:
            temp = []
            a = ls[i]
            while (a.down == True):
                fresh = ls[:]
                b = next((t for t in fresh if (
                    t.x == a.x_down and t.y == a.y_down)), 0)
                if b == 0:
                    break
                temp.append(b.value)
                a = b
            ls[i].down_value = ls[i].value + cumulative_sum(temp)
            # print(i,ls[i].down_value)

        if ls[i].right == True:
            temp = []
            a = ls[i]
            while (a.right == True):
                fresh = ls[:]
                b = next((t for t in fresh if (
                    t.x == a.x_right and t.y == a.y_right)), 0)
                if b == 0:
                    break
                temp.append(b.value)
                a = b
            ls[i].right_value = ls[i].value + cumulative_sum(temp)
            # print(i,ls[i].right_value)

        if ls[i].left == True:
            temp = []
            a = ls[i]
            while (a.left == True):
                fresh = ls[:]
                b = next((t for t in fresh if (
                    t.x == a.x_left and t.y == a.y_left)), 0)
                if b == 0:
                    break
                temp.append(b.value)
                a = b
            ls[i].left_value = ls[i].value + cumulative_sum(temp)
            # print(i,ls[i].left_value)"""


def final_value_update(ls):
    for x in ls:
        x.value = max(x.value, x.up_value, x.right_value,x.down_value, x.left_value)
        if (x.value == x.right_value):
            x.finaldir = "right"
        elif (x.value == x.down_value):
            x.finaldir = "down"
        elif (x.value == x.left_value):
            x.finaldir = "left"
        else:
            x.finaldir = "up"
        # print("--------------------------------------")
        # print(x.x,x.y,x.value,x.finaldir)
    """ if (x.value == x.left_value):
            x.finaldir = "left"
            continue
        elif (x.value == x.down_value):
            x.finaldir = "down"
            continue
        elif (x.value == x.right_value):
            x.finaldir = "right"
            continue
        else:"""

def contains(list,filter):
    for x in list:
        if filter(x):
            return True
    return False

def isolated_trees(t, search, x1, y1, n, Grid):
    search = checking_time(search, t, x1, y1)
    print("+++++++++++++++++++++++++++++++++++++++")
    for obj in search:
        print("261",obj.x,obj.y,obj.height,obj.time_taken,obj.weight,
        obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
    print("+++++++++++++++++++++++++++++++++++++++")
    #profit = 0
    while len(search) != 0:
        t = t - search[0].time_taken - \
            abs(search[0].x - x1)-abs(search[0].y - y1)
        assert(t>=0)
        #profit += search[0].value
        #print(profit)
        print_path(x1, y1, search[0].x, search[0].y)
        print("cut up")
        x1 = search[0].x
        y1 = search[0].y
        a = search[0]
        # or search[0].down == True or search[0].right == True or search[0].left == True:
        if a.up == True:
            search.remove(a)
            # ls.remove(a)
            # print(a.x,a.y)
            # k = search[0]
            # if a.finaldir == "up":
            while(a.up == True):
                assert(a.x_up>=0 and a.y_up>=0)
                assert(Grid[a.x_up][a.y_up].value != -1)
                a = Grid[a.x_up][a.y_up]
                print(a.x,a.y)
                if a in search:
                    search.remove(a)
                # if a in ls:
                    # ls.remove(a)
                    # print(a.x,a.y)
        else:
            search.remove(a)
            # ls.remove(search[0])
        print("+++++++++++++++++++++++++++++++++++++++")
        for obj in search:
            print("298",obj.x,obj.y,obj.height,obj.time_taken,obj.weight,
            obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
        print("+++++++++++++++++++++++++++++++++++++++")
        search = checking_time(search, t, x1, y1)
        print("+++++++++++++++++++++++++++++++++++++++")
        for obj in search:
            print("304",obj.x,obj.y,obj.height,obj.time_taken,obj.weight,
            obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
        print("+++++++++++++++++++++++++++++++++++++++")
    return x1, y1, t
    """  
                if a.finaldir == "down":
                    new = search[:]
                    b = [t for t in new if (t.x == a.x_down)]
                    for x in b:
                        search.remove(x)

                if a.finaldir == "left":
                    new = search[:]
                    b = [t for t in new if (t.y == a.y_left)]
                    for x in b:
                        search.remove(x)

                if a.finaldir == "down":
                    new = search[:]
                    b = [t for t in new if (t.x == a.x_down)]
                    for x in b:
                        search.remove(x)

                if a.finaldir == "up":
                    new = search[:]
                    b = [t for t in new if (t.x == a.x_up)]
                    for x in b:
                        search.remove(x)

                if a.finaldir == "right":
                    new = search[:]
                    b = [t for t in new if (t.y == a.y_right)]
                    for x in b:
                        search.remove(x)
                
               if a.finaldir == "down":
                    while (a.x == k.x):
                        new = search[:]
                        # new.remove(a)
                        b = next(
                            (t for t in new if t.x == a.x), 0)
                        # print("------------------------------------------------")
                        if b == 0:
                            break
                        a = b
                        # value_check_domino(b, search, b.finaldir)
                        search.remove(b)
                if a.finaldir == "right":
                    while a.y==k.y:
                        new = search[:]
                        # new.remove(a)
                        b = next(
                            (t for t in new if t.y == a.y), 0)
                        # print("------------------------------------------------")
                        if b == 0:
                            break
                        a = b
                        # value_check_domino(b, search, b.finaldir)
                        search.remove(b)

                if a.finaldir == "left":
                    while a.y==k.y:
                        new = search[:]
                        # new.remove(a)
                        b = next(
                            (t for t in new if t.y == a.y), 0)
                        # print("------------------------------------------------")
                        if b == 0:
                            break
                        a = b
                        # value_check_domino(b, search, b.finaldir)
                        search.remove(b)
        else:
            t = t + search[0].time_taken + \
                abs(search[0].x - x1) + abs(search[0].y - y1)
            search.remove(search[0])"""
    # ls.remove(search[0])
    # print("+++++++++++++++++++++++++++++++++++++++")
    # for obj in search:
    # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
    # print("+++++++++++++++++++++++++++++++++++++++")
    # print("+++++++++++++++++++++++++++++++++++++++")
    # for obj in search:
    # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
    # print("+++++++++++++++++++++++++++++++++++++++")


if __name__ == '__main__':
    ls = []
    t, n, k = map(int, input().split())
    grid_size = n
    Grid = [[var() for i in range(grid_size+1)] for j in range(grid_size+1)]
    for i in range(k):
        x, y, h, d, c, p = map(int, input().split())
        assert(x>=0 and y>=0 and p*d*h > 0)
        ls.append(Tree(x, y, h, d, c*d*h, p*d*h))
    # for obj in ls:
        # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
    j=0
    for obj in ls:
        if(obj.value>0):
            j=j+1
    if (j!=len(ls)):
        print("+++++++++++++++++++++++++++++++++++++++++++++++++++")
    search = ls
    # profit = 0
    x1 = 0
    y1 = 0
    for obj in ls:
        Grid[obj.x][obj.y] = obj
    alloting_bool(search, Grid, n)
    #print("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
    for obj in search:
        print("417",obj.x,obj.y,obj.height,obj.time_taken,obj.weight,
            obj.value,obj.right,obj.left,obj.down,obj.up,obj.x_up,obj.y_up,sep=' ')
    print("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
    for i in range(n+1):
        for j in range(n+1):
            if (Grid[i][j].value!=-1):
                obj = Grid[i][j]
                print("424",obj.x,obj.y,obj.height,obj.time_taken,obj.weight,
                      obj.value,obj.right,obj.left,obj.down,obj.up,obj.x_up,obj.y_up,sep=' ')
    # print("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
    #value_update(search, Grid)
    #final_value_update(search)
    x1, y1, t = isolated_trees(t, search, x1, y1, n, Grid)
    print_lastpath(x1, y1, t)
    # for obj in ls:
        # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
    # print(profit) 