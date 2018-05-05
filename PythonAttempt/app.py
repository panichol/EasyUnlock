from kivy.uix.button import Button
from kivy.app import App
from kivy.utils import platform
import sys
import time
import threading

"""
By: Patrick Nichols
An android phone app meant to implement a DTW comparison to incoming pickup signals
It doesn't work. The accelerometer api is broken, and out of my hands to fix.
For a working app, see the android studio project.
"""

from plyer import accelerometer#, gyroscope

## Allows us to collect the data we need
## Don't forget to add these permissions to the buildozer file!
button_text = "Start Data Collection"
try:
    accelerometer.enable()
    #gyroscope.enable()
except BaseException as e:
    button_text = repr(e)

__version__ = 0.1
TIMESTEP = 0.05
exit_request = False

## A custom thread class to aid with collecting data
class threadCollector(threading.Thread):
    def __init__(self,thread_id,user,count = 0):
        threading.Thread.__init__(self)
        self.thread_id = thread_id
        self.user = user
        self.count = count
    def run(self):
        print("Collection Started")
        _collect(self,self.user,self.count)
        print("Done")

## A helper function that actually logs the ACC and GYR data
def _collect(thread,user,count,wait_time = 5):
    infile = open("%s_pickup.txt"%(user),'w')
    global exit_request
    try:
        count =int(wait_time/TIMESTEP)
        while not exit_request:
            ## These line need to be commented out if you're not running on android
            #infile.write("ACC:"+accelerometer.get_acceleration())
            #infile.write("GYR:"+gyroscope.get_orientation())
            print("hah")
            time.sleep(TIMESTEP)
    ## This should probably be changed to a general error type
    except Exception as err:
        print("Unexpected error:", err)
    finally:
        ## Things that allways need to happen when the program finishes
        infile.close()
        exit_request = False


class dataApp(App):
    pressed = False
    ## A counter to spawn new threads
    current_thread = 0

    def build(self):
        global button_text
        b = Button(text = button_text,
                    background_color = (1,0,0,1),
                    font_size = 100)
        b.bind(on_press = self.Collect)
        return b

    def Collect(self,instance):
        if not self.pressed:
            instance.text = "End Data Collection"
            ## Create our new thread to log incoming signals
            collector = threadCollector(self.current_thread,0,1)
            self.current_thread += 1
            collector.start()
        else:
            instance.text = "Start Data Collection"
            ## The exit_request is how we kill the collection when the button is pressed again
            ## their is a slight chance of breaking everything if you press the button 2 times
            ## in less than TIMESTEP seconds
            global exit_request
            exit_request = True
        self.pressed = not self.pressed

