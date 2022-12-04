import sys


def goalTest(r, c, maze):
    if maze[r][c] == '*':
        return True

    else:
        return False


def bfs(r, c, maze, rl, cl):
    adj_list = [(r, c)]
    previous = {}
    visited = {}
    tour = []
    while adj_list:
        node = adj_list.pop(0)
        visited[node] = 1
        if goalTest(node[0], node[1], maze):
            tour.append(node)
            break
        for neighbour in moveGen(node[0], node[1], maze, rl, cl):
            if neighbour not in visited:
                adj_list.append(neighbour)
                if neighbour not in previous:
                    previous[neighbour] = node
    trace = tour[0]
    while trace in previous:
        tour.append(previous[trace])
        trace = previous[trace]

    print(str(len(visited)))

    print(str(len(tour)))
    for i, x in enumerate(maze):
        for j, y in enumerate(x):
            if (i, j) in tour:
                print("0", end="")
            else:
                print(y, end="")
        print()


def moveGen(row, col, maze, rl, cl):
    adj = []
    if row+1 < rl and (maze[row+1][col] == ' ' or maze[row+1][col] == '*'):
        adj.append((row+1, col))
    if row-1 > -1 and (maze[row-1][col] == ' ' or maze[row-1][col] == '*'):
        adj.append((row-1, col))
    if col+1 < cl and maze[row][col+1] == ' ' or maze[row][col+1] == '*':
        adj.append((row, col+1))
    if col-1 > -1 and maze[row][col-1] == ' ' or maze[row][col-1] == '*':
        adj.append((row, col-1))
    return adj


def dfid(maze, rl, cl):
    end, states = 1, 0
    while (1):
        visited = []
        tour = []
        if dfs_visit(0, 0, visited, tour, maze, rl, cl, end) == True:
            break
        states += len(visited)
        end += 1
    tour.append((0, 0))
    print(str(states))
    print(str(len(tour)))

    for i, x in enumerate(maze):
        for j, y in enumerate(x):
            if (i, j) in tour:
                print("0", end="")
            else:
                print(y, end="")
        print()


def dfs_visit(r, c, visited, tour, maze, rl, cl, end=99999999999):
    visited.append((r, c))
    if goalTest(r, c, maze):
        return True
    if(end <= 0):
        return False
    end -= 1

    for neighbour in moveGen(r, c, maze, rl, cl):
        if neighbour not in visited:
            if dfs_visit(neighbour[0], neighbour[1], visited, tour, maze, rl, cl, end):
                tour.append(neighbour)
                return True
    return False


def dfs(r, c, maze, rl, cl):
    visited = []
    tour = []
    dfs_visit(r, c, visited, tour, maze, rl, cl)
    tour.append((0, 0))
    print(str(len(visited)))
    print(str(len(tour)))
    for i, x in enumerate(maze):
        for j, y in enumerate(x):
            if (i, j) in tour:
                print("0", end="")
            else:
                print(y, end="")
        print()


if __name__ == "__main__":

    maze = []
    if (len(sys.argv) != 2):
        raise ValueError(
            "Give proper command line arguments(only 2 commands) !")

    with open(sys.argv[1], 'r') as file:
        func = file.readline()[:-1]
        # print(sys.argv[0])
        # file = open('input-1.txt', 'r')
        # func = file.readline()[:-1]
        row = 0
        for i in file:
            maze.append(i[:-1])
            row += 1

    col = len(maze[0])

    if func == "0":
        bfs(0, 0, maze, row, col)

    elif func == "1":
        dfs(0, 0, maze, row, col)

    elif func == "2":
        dfid(maze, row, col)

    else:
        raise ValueError("Give a number between 0 and 2")
