import sys
import threading

class TheThread(threading.Thread):
    def __init__(self, sourceFolder, destFolder):
        threading.Thread.__init__(self)
        self.sourceFolder = sourceFolder
        self.destFolder = destFolder
    
    def run(self):
        try:
           shutil.copytree(self.sourceFolder, self.destFolder)
        except Exception as e:
           sys.excepthook(type(e), e, e.__traceback__)
           print("Caught an exception")

try:
    threadClass = TheThread("source_folder", "dest_folder")
    threadClass.start()
except:
    print("Caught an exception")