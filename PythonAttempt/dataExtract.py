import sys 
import os
import re
import math

## A basic 3D point class to make everything else easier
class DataPoint():
    def __init__(self,x,y,z):
        self.data = (float(x),float(y),float(z))

    def getDiff(self,other):
        return sum(math.fabs(self.data[i]-other.data[i]) for i in range(len(self.data)))

    def __add__(self,other):
        return (self.data[0]+other.data[0],self.data[1]+other.data[1],self.data[2]+other.data[2]) 

    def __sub__(self,other):
        return (self.data[0]-other.data[0],self.data[1]-other.data[1],self.data[2]-other.data[2]) 

    def __str__ (self):
        return str(self.data)
    
    def __repr__(self):
        return str(self.data)

## A Class to extract the data from Ming's app
class DataExtractor():
    in_directory = ""
    users = {}

    def __init__(self,in_directory):
        self.in_directory = in_directory
        self.extractAllData()
    
    ## TODO: fix the fact that ACC and GYR are getting mixed into one pot here
    def extractAllData(self):
        for in_file in os.listdir(self.in_directory):
            data = self.extractFileData(in_file)[0]
            ## This line is to pull out the user number
            user = int(re.search(r'^User.',in_file).group()[4])
            if user in self.users:
                self.users[user].append(data)
            else:
                self.users[user] = [data]

    ## A Function to ectract all the data from a file
    def extractFileData(self,in_file):
        infile = open(self.in_directory+in_file,'r')
        ## [14:] is there to remove "SensorActivity" from the first line
        first_line = infile.readline()[14:]
        acc = [self.lineAcc(first_line)]
        gyr = [self.lineGyr(first_line)]
        for line in infile:
            gyr.append(self.lineGyr(line))
            acc.append(self.lineAcc(line))
        infile.close()
        return list(filter(None,acc)),list(filter(None,gyr))
    
    ## Function to extract accelerometer data from a single line
    def lineAcc(self,line):
        if not re.search("^ACC:",line):
            return None
        x_pattern = r'x=.+? '
        y_pattern = r'y=.+? '
        z_pattern = r'z=.+'
        x = re.search(x_pattern,line).group()[2:]
        y = re.search(y_pattern,line).group()[2:]
        z = re.search(z_pattern,line).group()[2:]
        return DataPoint(x.strip(),y.strip(),z.strip())

    ## Function to extract gyroscope data from a single line
    def lineGyr(self,line):
        if not re.search("^GY:",line):
            return None
        x_pattern = r'x axis=.+? '
        y_pattern = r'y axis=.+? '
        z_pattern = r'z axis=.+'
        x = re.search(x_pattern,line).group()[7:]
        y = re.search(y_pattern,line).group()[7:]
        z = re.search(z_pattern,line).group()[7:]
        return DataPoint(x.strip(),y.strip(),z.strip())

