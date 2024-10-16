import threading
import sys
import time

class TheThread(threading.Thread):
    def __init__(self, sourceFolder, destFolder):
        threading.Thread.__init__(self)
        self.sourceFolder = sourceFolder
        self.destFolder = destFolder
    
    def run(self):
        try:
           shutil.copytree(self.sourceFolder, self.destFolder)
        except:
           print("Caught an exception")
           # re-throw the exception
           raise sys.exc_info()[0], sys.exc_info()[1], sys.exc_info()[2]

# create a thread and start it
thread = TheThread(sourceFolder, destFolder)
thread.start()

# output to indicate that the script is still running
while thread.is_alive():
    print("The script is still running")
    time.sleep(1) # sleep for 1 second before checking again