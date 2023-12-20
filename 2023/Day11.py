ll = [x for x in open("Day11Input.txt").read().strip().split("\n\n")][0]

matrix = ll.split("\n")

for i in range(len(matrix)):
    matrix[i] = list(matrix[i])

emptyRows = []
emptyCols = []

print(matrix)

for i in range(len(matrix)):
    if "#" not in matrix[i]:
        emptyRows.append(i)

for i in range(len(matrix[0])):
    colIsEmpty = True
    for j in range(len(matrix)):
        if matrix[j][i] == "#":
            colIsEmpty = False
            break
    if colIsEmpty:
        emptyCols.append(i)


print(emptyCols)
print(emptyRows)
