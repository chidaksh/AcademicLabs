class Tree:
    def __init__(self, abs, ord, h, d, w, val, up_x=-1, up_y=-1, down_x=-1, down_y=-1, right_x=-1, right_y=-1, left_x=-1, left_y=-1, value_up=-1, value_down=-1, value_right=-1, value_left=-1, right=False, left=False, up=False, down=False):
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


class var:
    def __init__(self,val=-1,w=100000):
        self.value = val
        self.weight = w


def alloting_bool(ls, Grid,n):
    for obj in ls:
        Grid[obj.x][obj.y] = obj

    for obj in ls:
        up_list = [t for t in ls if t.x == obj.x and t.y > obj.y]
        up_dist = n+1

        if up_list:

            for t in up_list:
                if( (t.y-obj.y) < up_dist):
                    up_dist = t.y - obj.y
                
            if(up_dist==n+1):
                obj.up = False
            
            else:
                up_near = Grid[obj.x][obj.y + up_dist]
                if up_near.weight < obj.weight and up_dist < obj.height:
                    obj.up = True
                    obj.x_up = up_near.x
                    obj.y_up = up_near.y
                
                else:
                    obj.up = False
            
        else:
            obj.up = False

def check_time(tree, t, x1, y1):
    if (abs(tree.x - x1) + abs(tree.y - y1) + tree.time_taken) <=t:
        return True
    else:
        return False


def print_path(x, y, x1, y1):
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
        x.value = max(x.value, x.up_value, x.right_value,
                      x.down_value, x.left_value)
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



def Remove_trees(t, search, x1, y1):

    while len(search) != 0 and t>0:
        
        for obj in search:
            print(obj.x,obj.y,obj.time_taken)


        for obj in search:
            print(obj.x,obj.y,obj.time_taken,obj.value)
            if (check_time(obj,t,x1,y1)):
                print("DONE")
            else:
                print(obj.x)
                search.remove(obj)
        
        for obj in search:
            print(obj.x,obj.y,obj.time_taken)

        search.sort(key=lambda x: (x.value)/((x.time_taken)+abs(x.x - x1)+abs(x.y - y1)), reverse=True)

        current_tree = search[0]
        a = search[0]

        print_path(x1, y1, search[0].x, search[0].y)
        print("cut up")

        search.remove(current_tree)

        if current_tree.up == True:
            
            current_tree = Grid[current_tree.x_up][current_tree.y_up]

            while current_tree.up == True:

                search.remove(current_tree)
                current_tree = Grid[current_tree.x_up][current_tree.y_up]

        t = t - abs(a.x- x1) - abs(a.y- y1) - a.time_taken
        x1 = a.x
        y1 = a.y
        
    return x1, y1, t

if __name__ == '__main__':
    ls = []
    t, n, k = map(int, input().split())
    grid_size = n
    Grid = [[var() for i in range(grid_size)] for j in range(grid_size)]
    for i in range(k):
        x, y, h, d, c, p = map(int, input().split())
        ls.append(Tree(x, y, h, d, c*d*h, p*d*h))
    # for obj in ls:
        # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
    search = ls
    # profit = 0
    x1 = 0
    y1 = 0
    alloting_bool(search, Grid, n)
    # print("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
    # for obj in search:
    # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
    # print("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
    value_update(search, Grid)
    final_value_update(search)
    x1, y1, t = Remove_trees(t, search, x1, y1)
    print_lastpath(x1, y1, t)
    # print(profit)