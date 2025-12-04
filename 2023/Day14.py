def slideAllNorth(inputLL):
    for i in range(len(inputLL)-1, 1, -1):
        for j in range(len(inputLL[i])-1, 1, -1):
            for k in range(len(list(inputLL[0]))-1):
                #print(j,k)
                if inputLL[j][k] == "O" and inputLL[j - 1][k] == ".":
                    #print("swap")
                    inputLL[j][k] = "."
                    inputLL[j - 1][k] = "O"


def slideAllSouth(inputLL):
    for i in range(len(inputLL)-2, -1, -1):
        for j in range(len(inputLL)-2, -1, -1):
            for k in range(len(list(inputLL[0]))-1):
                if inputLL[j][k] == "O" and inputLL[j + 1][k] == ".":
                    #print("swap")
                    inputLL[j][k] = "."
                    inputLL[j + 1][k] = "O"


def slideAllEast(inputLL):
    for index, line in enumerate(inputLL):
        toReplace = []
        str = "".join(line)
        str = str.split("#")
        for group in str:
            lists = list(group)
            lists.sort()
            if group != str[-1]:
                lists.append("#")
            toReplace.extend(lists)
        inputLL[index] = toReplace


def slideAllWest(inputLL):
    for index, line in enumerate(inputLL):
        toReplace = []
        str = "".join(line)
        str = list(str.split("#"))
        for indexInner, group in enumerate(str):
            lists = list(group)
            lists.sort(reverse=True)
            if indexInner != len(str)-1:
                lists.append("#")
            toReplace.extend(lists)
        inputLL[index] = toReplace


def performCycle(inputLL):
    slideAllNorth(inputLL)
    slideAllWest(inputLL)
    slideAllSouth(inputLL)
    slideAllEast(inputLL)


if __name__ == "__main__":
    ll = [x for x in open("Day14Test.txt").read().strip().split("\n\n")][0].split("\n")
    inputMat = []
    cycles = 1000000000
    for l in ll:
        inputMat.append(list(l))
    result = 0
    for i in range(cycles):
        if i%100000 == 0:
            print(i)
        performCycle(inputMat)
    for l in inputMat:
        print(l)
        for c in l:
            if c == "O":
                result += len(inputMat)-inputMat.index(l)
    print(result)
    #110407
    #87273
