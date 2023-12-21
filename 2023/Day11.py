from itertools import combinations

ll = [x for x in open("Day11Input.txt").read().strip().split("\n\n")]

matrix = ll[0].split("\n")
newMatrix = []

for i in range(len(matrix)):
    matrix[i] = list(matrix[i])

emptyRows = []
emptyCols = []
positionsGalaxies = []
expansionNumber = 10

for i in range(len(matrix[0])):
    colIsEmpty = True
    for j in range(len(matrix)):
        if matrix[j][i] == "#":
            colIsEmpty = False
            break
    if colIsEmpty:
        emptyCols.append(i)


for i in range(len(matrix)):
    if "#" not in matrix[i]:
        dotArr = []
        for j in range(len(newMatrix[0])):
            dotArr.append(".")
        newMatrix.extend([dotArr])
        newMatrix.append(matrix[i])
    else:
        newMatrix.append(matrix[i])


cnt = 0
for index in emptyCols:
    for line in newMatrix:
        line.insert(index + cnt, ".")
    cnt += 1

result1 = 0

for i in range(len(newMatrix)):
    for j in range(len(newMatrix[i])):
        if newMatrix[i][j] == "#":
            positionsGalaxies.append((i, j))

#positionsGalaxies = [(x, y) for x in range(len(newMatrix[0])) for y in range(len(newMatrix)) if newMatrix[y][x] == '#']

for i, galaxy1 in enumerate(positionsGalaxies):
    for galaxy2 in positionsGalaxies[i + 1:]:
        result1 += abs(galaxy1[0] - galaxy2[0]) + abs(galaxy1[1] - galaxy2[1])

print(result1)
#9 648 398
