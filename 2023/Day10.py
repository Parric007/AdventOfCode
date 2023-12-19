import pandas as pd
import numpy as np
import re
from typing import Generator, Tuple, List
from IPython.display import clear_output
import time
import math

ll = [x for x in open("Day10Input.txt").read().strip().split("\n\n")]
def getNextPos(startPos, targetPos):
    if matrix[targetPos[0]][targetPos[1]] == "-":
        return targetPos[0],(targetPos[1]-startPos[1])+targetPos[1]

    if matrix[targetPos[0]][targetPos[1]] == "|":
        return (targetPos[0] - startPos[0]) + targetPos[0], targetPos[1]

    if matrix[targetPos[0]][targetPos[1]] == "L":
        if startPos[0] == targetPos[0]:
            return targetPos[0]-1, targetPos[1]
        else:
            return targetPos[0], targetPos[1]+1

    if matrix[targetPos[0]][targetPos[1]] == "J":
        if startPos[0] == targetPos[0]:
            return targetPos[0]-1, targetPos[1]
        else:
            return targetPos[0], targetPos[1]-1

    if matrix[targetPos[0]][targetPos[1]] == "7":
        if startPos[0] == targetPos[0]:
            return targetPos[0]+1, targetPos[1]
        else:
            return targetPos[0], targetPos[1]-1

    if matrix[targetPos[0]][targetPos[1]] == "F":
        if startPos[0] == targetPos[0]:
            return targetPos[0]+1, targetPos[1]
        else:
            return targetPos[0], targetPos[1]+1

startTime = time.perf_counter()

startPos = (0,0)

matrix = ll[0].split("\n")

for l in matrix:
    if "S" in l:
        startPos = ll[0].split("\n").index(l), l.index("S")

loopOnePos = startPos[0], startPos[1]+1
loopTwoPos = startPos[0]+1, startPos[1]

startPos1 = startPos
startPos2 = startPos
targetPos1 = (0,0)
targetPos2 = (0,1)
counter = 1
while loopOnePos != loopTwoPos and targetPos1 != targetPos2:
    targetPos1 = getNextPos(startPos1, loopOnePos)
    startPos1 = loopOnePos
    loopOnePos = targetPos1
    targetPos2 = getNextPos(startPos2, loopTwoPos)
    startPos2 = loopTwoPos
    loopTwoPos = targetPos2
    counter += 1

print(counter)

print("Zeit in ms: " , time.perf_counter()-startTime)

#7096 in 6,7 us

#A2 355

