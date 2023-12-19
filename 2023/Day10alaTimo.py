import time


if __name__ == '__main__':
    # read file
    file = open('Day10Input.txt', 'r')
    matrix = file.readlines()

    startTime = time.perf_counter()

    # default value for start
    start = (0, 0)

    # find start
    for row in matrix:
        for char in row:
            if char == 'S':
                start = (matrix[matrix.index(row)].index(char), matrix.index(row), ' ')

    # find possible start direction
    steps = [start]
    def nextStep():
        current = steps[len(steps)-1]
        if current[2] != 's' and (matrix[current[1] - 1][current[0]] == 'F' or matrix[current[1] - 1][current[0]] == '7' or matrix[current[1] - 1][current[0]] == '|'):
            nxt = (current[0], current[1] - 1, 'w')
        elif current[2] != 'a' and (matrix[current[1]][current[0] + 1] == 'J' or matrix[current[1]][current[0] + 1] == '7' or matrix[current[1]][current[0] + 1] == '-'):
            nxt = (current[0] + 1, current[1], 'd')
        elif current[2] != 'w' and (matrix[current[1] + 1][current[0]] == 'J' or matrix[current[1] + 1][current[0]] == 'L' or matrix[current[1] + 1][current[0]] == '|'):
            nxt = (current[0], current[1] + 1, 's')
        elif current[2] != 'd' and (matrix[current[1]][current[0] - 1] == 'F' or matrix[current[1]][current[0] - 1] == 'L' or matrix[current[1]][current[0] - 1] == '-'):
            nxt = (current[0] - 1, current[1], 'a')
        steps.append(nxt)
        print(nxt, matrix[current[1]][current[0]])

    while len(steps) == 1 or steps[len(steps)-1] != start:
        try:
            nextStep(
        except UnboundLocalError:
            break

    print(steps)

    print("Zeit in ms: ", time.perf_counter()-startTime)