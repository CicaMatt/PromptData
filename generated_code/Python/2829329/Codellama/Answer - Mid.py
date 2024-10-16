import sys
from threading import Thread
import shutil

class TheThread(Thread):
    def __init__(self, sourceFolder, destFolder):
        super().__init__()
        self.sourceFolder = sourceFolder
        self.destFolder = destFolder
    
    def run(self):
        try:
            shutil.copytree(self.sourceFolder, self.destFolder)
        except:
            print("Caught an exception")
            if sys.exc_info()[0] is not None:
                raise