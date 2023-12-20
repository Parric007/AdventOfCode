from itertools import combinations

ll = [x for x in open("Day11Input.txt").read().strip().split("\n\n")][0]

matrix = ll.split("\n")
newMatrix = []
finalMatrix = []

for i in range(len(matrix)):
    matrix[i] = list(matrix[i])

emptyRows = []
emptyCols = []
positionsGalaxies = []
adjustedPos = set()

for i in range(len(matrix)):
    if "#" not in matrix[i]:
        newMatrix.append(["."*140])
        newMatrix.append(matrix[i])
    else:
        newMatrix.append(matrix[i])

print(newMatrix)

for i in range(len(matrix[0])):
    colIsEmpty = True
    for j in range(len(matrix)):
        if matrix[j][i] == "#":
            colIsEmpty = False
            break
    if colIsEmpty:
        emptyCols.append(i)

print(emptyCols)

for index in emptyCols:
    for line in newMatrix:
        line.insert(index, ".")


result1 = 0

for i in range(len(newMatrix)):
    for j in range(len(newMatrix[i])):
        if newMatrix[i][j] == "#":
            positionsGalaxies.append((i, j))

res = list(combinations(positionsGalaxies, 2))

for val in res:
    result1 += abs(val[0][0]-val[1][0]) + abs(val[0][1]-val[1][1])

print(result1)
#9 648 398