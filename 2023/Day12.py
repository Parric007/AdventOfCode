#7843 A1
#10153896718999 A2

ll = [x for x in open("Day12Input.txt").read().strip().split("\n\n")]

matrix = ll[0].split("\n")
puzzleInput = []

for lin in matrix:
    split = lin.split()
    counts = split[1].split(",")
    counts = list(map(int, counts))
    puzzleInput.append((split[0], counts))

print(puzzleInput)

