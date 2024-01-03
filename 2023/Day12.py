#7843 A1
#10153896718999 A2

def getPossibleArrangements(part, numberOfParts):
    if "#" not in part:
        return len(part) - numberOfParts + 1
    return 0

def checkEmpty(arrIn):
    if arrIn == "":
        return False
    return True


ll = [x for x in open("Day12Input.txt").read().strip().split("\n\n")]


matrix = ll[0].split("\n")
puzzleInput = []

for lin in matrix:
    split = lin.split()
    counts = split[1].split(",")
    counts = list(map(int, counts))
    puzzleInput.append((split[0], counts))

result1 = 0
for puzzles in puzzleInput:
    toCheck = puzzles[0].split(".")
    iterator = filter(checkEmpty, toCheck)
    toCheck = list(iterator)
    print(toCheck)
    print(puzzles[1])
    for i in range(len(toCheck)):
        arr = toCheck[i]
        result1 += getPossibleArrangements(arr, puzzles[1][i])

print(result1)
