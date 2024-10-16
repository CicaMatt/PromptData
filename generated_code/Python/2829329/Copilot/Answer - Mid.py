import threading
import shutil
import sys

class TheThread(threading.Thread):
    def __init__(self, sourceFolder, destFolder):
        threading.Thread.__init__(self)
        self.sourceFolder = sourceFolder
        self.destFolder = destFolder
        self.exception = None
    
    def run(self):
        try:
            shutil.copytree(self.sourceFolder, self.destFolder)
        except Exception as e:
            self.exception = e

def main():
    sourceFolder = 'path/to/source'
    destFolder = 'path/to/destination'
    
    thread = TheThread(sourceFolder, destFolder)
    thread.start()
    thread.join()  # Wait for the thread to finish
    
    if thread.exception:
        print(f"Caught an exception: {thread.exception}")
    else:
        print("Files copied successfully")

if __name__ == "__main__":
    main()
