class Tree:
    def __init__(self, abs, ord, h, d, w, val, up_x=-1, up_y=-1, down_x=-1, down_y=-1, right_x=-1, right_y=-1, left_x=-1, left_y=-1, value_up=-1, value_down=-1, value_right=-1, value_left=-1, right=False, left=False, up=False, down=False):
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
                if j.y < (obj.y + obj.height) and j.y > obj.y and j.weight < obj.weight and obj.up == False:
                    obj.up = True
                    obj.x_up = j.x
                    obj.y_up = j.y
                if j.y > (obj.y-obj.height) and j.y < obj.y and j.weight < obj.weight and obj.down == False:
                    obj.down = True
                    obj.x_down = j.x
                    obj.y_down = j.y
            if j.y == obj.y:
                if j.x < (obj.x + obj.height) and j.x > obj.x and j.weight < obj.weight and obj.right == False:
                    obj.right = True
                    obj.x_right = j.x
                    obj.y_right = j.y
                if j.x > (obj.x - obj.height) and j.x < obj.x and j.weight < obj.weight and obj.left == False:
                    obj.left = True
                    obj.x_left = j.x
                    obj.y_left = j.y
    # for obj in ls:
             # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.down,obj.up,obj.x_right,obj.y_right,obj.x_left,obj.y_left,obj.x_down,obj.y_down,obj.x_up,obj.y_up,sep=' ')
    # print("---------------------------------------------------------------------")


def checking_time(ls, t, x1, y1):
    temp = []
    if len(ls)==0:
        return temp
    for obj in ls:
        if(abs(obj.x - x1) + abs(obj.y - y1) + obj.time_taken <= t):
            temp.append(obj)
        # print(obj.value)
    temp.sort(key=lambda x: (x.value)/((x.time_taken) +
              abs(x.x - x1)+abs(x.y - y1)), reverse=True)
    # for obj in temp:
    #print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,sep=' ')
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
            # print(i,ls[i].left_value)


def final_value_update(ls):
    for x in ls:
        x.value = max(x.value, x.up_value, x.down_value,
                      x.right_value, x.left_value)
        if (x.value == x.left_value):
            x.finaldir = "left"
            continue
        elif (x.value == x.down_value):
            x.finaldir = "down"
            continue
        elif (x.value == x.right_value):
            x.finaldir = "right"
            continue
        else:
            x.finaldir = "up"
            continue
        # print("--------------------------------------")
        # print(x.x,x.y,x.value,x.finaldir)


def value_check_domino(a, ls, s):
    if (s == "up"):
        while (a.right == True):
            new = ls[:]
            # new.remove(a)
            b = next((t for t in new if (t.x_right == a.x and t.y_right == a.y)), 0)
            # print("------------------------------------------------")
            if b == 0:
                break
            b.right_value = b.right_value - a.right_value
            a = b  # while condition, bool value of tree b for one time
        while (a.left == True):
            new = ls[:]
            # new.remove(a)
            b = next((t for t in new if (t.x_left == a.x and t.y_left == a.y)), 0)
            # print("------------------------------------------------")
            if b == 0:
                break
            b.left_value = b.left_value - a.left_value
            a = b
        while (a.down == True):
            new = ls[:]
            # new.remove(a)
            b = next((t for t in new if (t.x_down == a.x and t.y_down == a.y)), 0)
            # print("------------------------------------------------")
            if b == 0:
                break
            b.down_value = b.down_value - a.down_value
            a = b
    elif (s == "right"):
        while (a.left == True):
            new = ls[:]
            # new.remove(a)
            b = next((t for t in new if (t.x_left == a.x and t.y_left == a.y)), 0)
            # print("------------------------------------------------")
            if b == 0:
                break
            b.left_value = b.left_value - a.left_value
            a = b
        while (a.down == True):
            new = ls[:]
            # new.remove(a)
            b = next((t for t in new if (t.x_down == a.x and t.y_down == a.y)), 0)
            # print("------------------------------------------------")
            if b == 0:
                break
            b.down_value = b.down_value - a.down_value
            a = b
        while (a.up == True):
            new = ls[:]
            # new.remove(a)
            b = next((t for t in new if (t.x_up == a.x and t.y_up == a.y)), 0)
            # print("------------------------------------------------")
            if b == 0:
                break
            b.up_value = b.up_value - a.up_value
            a = b
    elif (s == "left"):
        while (a.down == True):
            new = ls[:]
            # new.remove(a)
            b = next((t for t in new if (t.x_down == a.x and t.y_down == a.y)), 0)
            # print("------------------------------------------------")
            if b == 0:
                break
            b.down_value = b.down_value - a.down_value
            a = b
        while (a.up == True):
            new = ls[:]
            # new.remove(a)
            b = next((t for t in new if (t.x_up == a.x and t.y_up == a.y)), 0)
            # print("------------------------------------------------")
            if b == 0:
                break
            b.up_value = b.up_value - a.up_value
            a = b
        while (a.right == True):
            new = ls[:]
            # new.remove(a)
            b = next((t for t in new if (t.x_right == a.x and t.y_right == a.y)), 0)
            # print("------------------------------------------------")
            if b == 0:
                break
            b.right_value = b.right_value - a.right_value
            a = b
    else:
        while (a.up == True):
            new = ls[:]
            # new.remove(a)
            b = next((t for t in new if (t.x_up == a.x and t.y_up == a.y)), 0)
            # print("------------------------------------------------")
            if b == 0:
                break
            b.up_value = b.up_value - a.up_value
            a = b
        while (a.right == True):
            new = ls[:]
            # new.remove(a)
            b = next((t for t in new if (t.x_right == a.x and t.y_right == a.y)), 0)
            # print("------------------------------------------------")
            if b == 0:
                break
            b.right_value = b.right_value - a.right_value
            a = b
        while (a.left == True):
            new = ls[:]
            # new.remove(a)
            b = next((t for t in new if (t.x_left == a.x and t.y_left == a.y)), 0)
            # print("------------------------------------------------")
            if b == 0:
                break
            b.left_value = b.left_value - a.left_value
            a = b


def isolated_trees(t, search, x1, y1):
    search = checking_time(search, t, x1, y1)
    while len(search) != 0 and t>0:
        t = t - search[0].time_taken -abs(search[0].x - x1)-abs(search[0].y - y1)
        if t >= 0:
            #profit += search[0].value
            # print(profit)
            print_path(x1, y1, search[0].x, search[0].y)
            print("cut " + search[0].finaldir)
            x1 = search[0].x
            y1 = search[0].y
            if search[0].up == True or search[0].down == True or search[0].right == True or search[0].left == True:
                a = search[0]
                if a.finaldir == "up":
                    while (a.up == True):
                        new = search[:]
                        # new.remove(a)
                        b = next(
                            (t for t in new if (t.x == a.x_up and t.y == a.y_up)), 0)
                        # print("------------------------------------------------")
                        if b == 0:
                            break  #remove trees in the entire column and check
                        a = b
                        #value_check_domino(b, search, b.finaldir)
                        search.remove(b)
                if a.finaldir == "down":
                    while (a.down == True):
                        new = search[:]
                        # new.remove(a)
                        b = next(
                            (t for t in new if (t.x == a.x_down and t.y == a.y_down)), 0)
                        # print("------------------------------------------------")
                        if b == 0:
                            break
                        a = b
                        #value_check_domino(b, search, b.finaldir)
                        search.remove(b)
                if a.finaldir == "right":
                    while a.right == True:
                        new = search[:]
                        # new.remove(a)
                        b = next(
                            (t for t in new if (t.x == a.x_right and t.y == a.y_right)), 0)
                        # print("------------------------------------------------")
                        if b == 0:
                            break
                        a = b
                        #value_check_domino(b, search, b.finaldir)
                        search.remove(b)

                if a.finaldir == "left":
                    while a.left == True:
                        new = search[:]
                        # new.remove(a)
                        b = next(
                            (t for t in new if (t.x == a.x_left and t.y == a.y_left)), 0)
                        # print("------------------------------------------------")
                        if b == 0:
                            break
                        a = b
                        #value_check_domino(b, search, b.finaldir)
                        search.remove(b)

        else:
            t = t + search[0].time_taken + \
                abs(search[0].x - x1) + abs(search[0].y - y1)
        search.remove(search[0])
        search = checking_time(search, t, x1, y1)
    return x1, y1, t


if __name__ == '__main__':
    ls = []
    t, n, k = map(int, input().split())
    for i in range(k):
        x, y, h, d, c, p = map(int, input().split())
        ls.append(Tree(x, y, h, d, c*d*h, p*d*h))
    # for obj in ls:
        #print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
    search = ls
    #profit = 0
    x1 = 0
    y1 = 0
    alloting_bool(search)
    value_update(search)
    final_value_update(search)
    x1, y1, t = isolated_trees(t, search, x1, y1)
    print_lastpath(x1, y1, t)

    # print(profit)
