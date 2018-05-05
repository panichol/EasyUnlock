import plotly as py
import plotly.graph_objs as go
import numpy as np
import sys
"""
By:Patrick Nichols
A dirty python scrpit to visualize some of the pickup signals
usage is ./seeAcc.sh path/to/file | python3 seeData.py
so if you wanted to see the signal held in data/signal.txt you would run:
./extractACC.sh data/signal1.txt | python3 seeData.txt
and eventually an html document will open with fancy graphs!
"""

## Used to fix the broken readings my phone gives out
## Values are such that when the phone is stationary, all accelerations are adjusted to 0
CORRECT = True
XCORRECT = 0.45
YCORRECT = 0.45
ZCORRECT = 8.9
##  Used to filter out stationary data
FILTER = False 
THRESHOLD = 10
NEEDED_COUNT = 2

## Converts from acceleration to position (poorly)
def convertAcc(acc):
    velocity = [0]
    position = [0]
    for i,val in enumerate(acc):
        velocity.append(velocity[-1]+val*0.02)
        position.append(position[-1] + velocity[i]*0.02)
    return position, velocity

def main():
    ## Extracting the data from standard in
    xAcc = []
    yAcc = []
    zAcc = []
    data = sys.stdin.read().split()
    tick = 0
    for x in data:
        if tick == 0:
            try:
                xAcc.append(float(x))
            except ValueError:
                xAcc.append(0.0)
            tick += 1
        elif tick == 1:
            yAcc.append(float(x))
            tick += 1
        elif tick ==2:
            zAcc.append(float(x))
            tick = 0

    ## If we want to filter out "Stationary" points
    if FILTER:
        count = 0
        for i in range(len(xAcc)):
            if xAcc[0] > THRESHOLD:
                count += 1
            if yAcc[0] > THRESHOLD:
                count += 1
            if zAcc[0] > THRESHOLD:
                count += 1
            if count >= NEEDED_COUNT:
                break
            xAcc.pop(0)
            yAcc.pop(0)
            zAcc.pop(0)

    if CORRECT:
        xAcc = list(map(lambda x:x-XCORRECT,xAcc))
        yAcc = list(map(lambda y:y-YCORRECT,yAcc))
        zAcc = list(map(lambda z:z-ZCORRECT,zAcc))

    xDist = convertAcc(xAcc)[0]
    yDist = convertAcc(yAcc)[0]
    zDist = convertAcc(zAcc)[0]
    time = [i for i in range(len(xDist))]
    ## Generating the graph using plotly, may need to install plotly for this to work
    phonePlot = go.Scatter3d(
        x=xDist,
        y=yDist,
        z=zDist,
        mode='markers',
        marker=dict(
            size=12,
            color = time,
            colorscale = "Reds",
            line=dict(
                color='rgba(217, 217, 217, 0.14)',
                width=1),
            ))            

    phoneData = [phonePlot]
    layout = go.Layout(
        margin=dict(
            l=0,
            r=0,
            b=0,
            t=0
        )
    )
    fig = go.Figure(data=phoneData, layout=layout)
    py.offline.plot(fig, filename='plots/sample-3d-scatter.html')

main()
