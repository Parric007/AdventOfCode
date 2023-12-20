from itertools import combinations

ll = [x for x in open("Day11Input.txt").read().strip().split("\n\n")][0]

matrix = ll.split("\n")

for i in range(len(matrix)):
    matrix[i] = list(matrix[i])

emptyRows = []
emptyCols = []
positionsGalaxies = []
adjustedPos = []

for i in range(len(matrix)):
    if "#" not in matrix[i]:
        emptyRows.append(i)

for i in range(len(matrix[0])):
    colIsEmpty = True
    for j in range(len(matrix)):
        if matrix[j][i] == "#":
            positionsGalaxies.append((j,i))
            colIsEmpty = False
            break
    if colIsEmpty:
        emptyCols.append(i)

for cols in emptyCols:
    for pos in positionsGalaxies:
        if pos[1] > cols:
            adjustedPos.append((pos[0], pos[1]+1))
        else:
            adjustedPos.append(pos)

for rows in emptyRows:
    for pos in positionsGalaxies:
        if pos[0] > rows:
            adjustedPos.append((pos[0]+1, pos[1]))
        else:
            adjustedPos.append(pos)

result1 = 0

#res = [(a, b) for idx, a in enumerate(adjustedPos) for b in adjustedPos[idx + 1:]]
res = list(combinations(adjustedPos, 2))

for val in res:
    result1 += abs(val[0][0]-val[1][0]) + abs(val[0][1]-val[1][1])

print(result1)
#9 648 398