class Tree:
    def __init__(self, abs, ord, h, d, w, val, up_x=1000, up_y=1000, right=False, left=False, up=False, down=False):
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


def alloting_bool(ls):
    for obj in ls:
        temp = ls[:]
        temp.remove(obj)
        # O(k**2)     #need to check weight constraint
        for j in temp:
            if j.x == obj.x:
                if j.y < (obj.y + obj.height) and j.y > obj.y and j.weight < obj.weight:
                    obj.up = True
                    obj.x_up = min(obj.x_up,j.x)
                    obj.y_up = min(obj.y_up,j.y)
    # for obj in ls:
        # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.up,obj.x_up,obj.y_up,sep=' ')


def checking_time(ls, t, x1, y1):
    temp = []
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


def isolated_trees(t, search, x1, y1):
    search = checking_time(search, t, x1, y1)
    while len(search) != 0:
        t = t - search[0].time_taken - \
            abs(search[0].x - x1)-abs(search[0].y - y1)
        if t >= 0:
            # profit += search[0].value
            # print(profit)
            print_path(x1, y1, search[0].x, search[0].y)
            print("cut up")
            x1 = search[0].x
            y1 = search[0].y
            a = search[0]
            if a.up == True:
                for x in search:
                    if (x.x == a.x):
                        search.remove(x)
            else:
                search.remove(a)
        else:
            t = (t + search[0].time_taken +
                 abs(search[0].x - x1) + abs(search[0].y - y1))
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
        # print(obj.x,obj.y,obj.height,obj.time_taken,obj.weight,obj.value,obj.right,obj.left,obj.up,obj.down,sep=' ')
    search = ls
    # profit = 0
    x1 = 0
    y1 = 0
    alloting_bool(search)
    x1, y1, t = isolated_trees(t, search, x1, y1)
    print_lastpath(x1, y1, t)
    # print(profit)
