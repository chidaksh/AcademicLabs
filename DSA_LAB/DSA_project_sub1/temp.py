class Tree:
    def __init__(self, abs, ord, h, d, w, val, up_x=10000, up_y=10000, down_x=-1, down_y=-1, right_x=10000, right_y=10000, left_x=-1, left_y=-1, value_up=-1, value_down=-1, value_right=-1, value_left=-1, right=False, left=False, up=False, down=False):
        self.x = abs
        self.y = ord
        self.height = h
        self.time_taken = d
        self.weight = w
        self.value = val
        self.right = right
        self.left = left
        self.up = up
        self.down = down
        self.x_up = up_x
        self.y_up = up_y
        self.x_down = down_x
        self.y_down = down_y
        self.x_right = right_x
        self.y_right = right_y
        self.x_left = left_x
        self.y_left = left_y
        self.up_value = value_up
        self.down_value = value_down
        self.right_value = value_right
        self.left_value = value_left
        self.finaldir = "up"


def alloting_bool(ls):
    for obj in ls:
        temp = ls[:]
        temp.remove(obj)
        for j in temp:  # O(k**2)     #need to check weight constraint
            if j.x == obj.x:
                if j.y < (obj.y + obj.height) and j.y > obj.y and j.weight < obj.weight:
                    obj.up = True
                    obj.x_up = obj.x
                    obj.y_up = min(obj.y_up, j.y)
                if j.y > (obj.y-obj.height) and j.y < obj.y and j.weight < obj.weight:
                    obj.down = True
                    obj.x_down = obj.x
                    obj.y_down = max(obj.y_down, j.y)
            if j.y == obj.y:
                if j.x < (obj.x + obj.height) and j.x > obj.x and j.weight < obj.weight:
                    obj.right = True
                    obj.x_right = min(obj.x_right, j.x)
                    obj.y_right = obj.y
                if j.x > (obj.x - obj.height) and j.x < obj.x and j.weight < obj.weight:
                    obj.left = True
                    obj.x_left = max(obj.x_left, j.x)
                    obj.y_left = obj.y
    # for obj in ls:
             # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.down,obj.up,obj.x_right,obj.y_right,obj.x_left,obj.y_left,obj.x_down,obj.y_down,obj.x_up,obj.y_up,sep=' ')
    # print("---------------------------------------------------------------------")


def checking_time(ls, t, x1, y1):
    temp = []
    if len(ls) == 0:
        return temp
    for obj in ls:
        if(abs(obj.x - x1) + abs(obj.y - y1) + obj.time_taken <= t):
            temp.append(obj)
        # print(obj.value)
    temp.sort(key=lambda x: (x.value)/((x.time_taken) +
              abs(x.x - x1)+abs(x.y - y1)), reverse=True)
    # for obj in temp:
    # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,sep=' ')
    # print("-------------------------------------------")
    return temp


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


def value_update(ls):
    for i in range(len(ls)):
        if ls[i].up == True:
            temp = []
            a = ls[i]
            while (a.up == True):
                fresh = ls[:]
                b = next((t for t in fresh if (
                    t.x == a.x_up and t.y == a.y_up)), 0)
                if b == 0:
                    break
                temp.append(b.value)
                a = b
            ls[i].up_value = ls[i].value + cumulative_sum(temp)
            # print(i,ls[i].up_value)
            # print("########################################################")
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
            # print(i,ls[i].left_value)
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
    """ 
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


def isolated_trees(t, search, x1, y1):
    search = checking_time(search, t, x1, y1)
    # print("+++++++++++++++++++++++++++++++++++++++")
    # for obj in search:
    # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
    # print("+++++++++++++++++++++++++++++++++++++++")
    while len(search) != 0 and t > 0:
        t = t - search[0].time_taken - \
            abs(search[0].x - x1)-abs(search[0].y - y1)
        if t >= 0:
            # profit += search[0].value
            # print(profit)
            print_path(x1, y1, search[0].x, search[0].y)
            print("cut " + search[0].finaldir)
            x1 = search[0].x
            y1 = search[0].y
            # or search[0].down == True or search[0].right == True or search[0].left == True:
            if search[0].up == True or search[0].right == True or search[0].left == True or search[0].down==True:
                a = search[0]
                # k = search[0]
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
            else:
                search.remove(search[0])
            """    
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
                        search.remove(b)"""
        else:
            t = t + search[0].time_taken + \
                abs(search[0].x - x1) + abs(search[0].y - y1)
            search.remove(search[0])
        # print("+++++++++++++++++++++++++++++++++++++++")
        # for obj in search:
        # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
        # print("+++++++++++++++++++++++++++++++++++++++")
        search = checking_time(search, t, x1, y1)
        # print("+++++++++++++++++++++++++++++++++++++++")
        # for obj in search:
        # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
        # print("+++++++++++++++++++++++++++++++++++++++")
    return x1, y1, t


if __name__ == '__main__':
    ls = []
    t, n, k = map(int, input().split())
    for i in range(k):
        x, y, h, d, c, p = map(int, input().split())
        ls.append(Tree(x, y, h, d, c*d*h, p*d*h))
    # for obj in ls:
        # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
    search = ls
    # profit = 0
    x1 = 0
    y1 = 0
    alloting_bool(search)
    # print("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
    # for obj in search:
    # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
    # print("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
    value_update(search)
    final_value_update(search)
    x1, y1, t = isolated_trees(t, search, x1, y1)
    print_lastpath(x1, y1, t)
    # print(profit)
