#34918
#33054


def getVerticalMirror(matrix):
    for i in range(len(matrix)-3):
        #print(matrix[i], matrix[i+1], "\n", matrix[i-1], matrix[i+2], "\n", matrix[i-2], matrix[i+3], "\n")
        #print(matrix[i-2] == matrix[i+3], matrix[i-2], matrix[i+3])
        if matrix[i] == matrix[i+1] and matrix[i-1] == matrix[i+2]:# and matrix[i-2] == matrix[i+3]:
            #print(i+1)
            return 100 * (i+1)
    return 0


def getHorizontalMirror(matrix):
    for i in range(2, len(matrix)):
        bisI = matrix[:i]
        abI = matrix[i:]
        revBis = getReversed(bisI)
        revAb = getReversed(abI)
        if revBis[:len(abI)] == abI[:len(bisI)]:# or revAb[:len(bisI)] == bisI[:len(abI)]:
            #print(i+1)
            return i+1
    return 0


def getReversed(inputArr):
    toReturn = []
    for atr in inputArr:
        toReturn.insert(0, atr)
    return toReturn



ll = [x for x in open("Day13Input.txt").read().strip().split("\n\n\n")][0]

pattern = []

cnt = 0
patternToAdd = []
for l in ll.split("\n"):
    #print(l)
    if l != "":
        patternToAdd.append(l)
    else:
        pattern.append(patternToAdd)
        patternToAdd = []
pattern.append(patternToAdd)
result = 0
for mat in pattern:
    result += getVerticalMirror(mat)# + getHorizontalMirror(mat)


#print(pattern[0][13] == pattern[0][8])
print(result)
