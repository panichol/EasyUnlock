import numpy as np
from dataExtract import *
import re
import math
import sys

## A constant to mess with certain DTW properties
## It add some cost for every step the DTW algorithm does
step_cost = 0.0

def DTWDist(sig1,sig2):
    if len(sig1) == 0 and len(sig2) == 0:
        return 0
    elif len(sig1) == 0:
        sig1 = [DataPoint(0,0,0) for i in range(len(sig2))]
    elif len(sig2) == 0:
        sig2 = [DataPoint(0,0,0) for i in range(len(sig1))]
    DTW_matrix = np.zeros((len(sig1),len(sig2)))
    ## Ideally, the 999999 should just be some arbitrarily high cost
    for i in range(len(sig1)):
            DTW_matrix[i,0] = 999999
    for i in range(len(sig2)):
        DTW_matrix[0,i] = 999999
    DTW_matrix[0,0] = 0

    for i in range(len(sig1)):
        for k in range(len(sig2)):
            cost = sig1[i].getDiff(sig2[k])+step_cost
            DTW_matrix[i,k] = cost + min(DTW_matrix[i-1,k],DTW_matrix[i,k-1],DTW_matrix[i-1,k-1])
    return DTW_matrix[len(sig1)-1,len(sig2)-1]

## Returns the average DTW distance between one users signals
def selfCompare(data,user):
    user_len = len(data.users[user])
    diff = 0
    for i in range(user_len):
        for k in range(i+1,user_len):
            assert k != i
            sig1 = data.users[user][i]
            sig2 = data.users[user][k]
            diff += DTWDist(sig1,sig2)
    return diff/(user_len*(user_len+1)/2)

## Returns the average DTW distance between the signals of two users
def compareUsers(data,user1,user2):
    diff = 0
    if (user1 == user2):
        return selfCompare(data,user1)
    for sig1 in data.users[user1]:
        for sig2 in data.users[user2]:
            diff += DTWDist(sig1,sig2)
    return diff/(len(data.users[user1])*len(data.users[user2]))


def main():
    data = DataExtractor("../CollectedData/sampleData/")
    ## A quick and dirty loop to get some interesting data 
    for i in range(10):
        print('User ',i,': ',end = '',sep = '')
        for k in range(0,10):
            ## Prints a 10 by 10 table showing comparisan scores
            print('%.2f \t'%(compareUsers(data,i,k)/10),end = '')
        print()



main()
