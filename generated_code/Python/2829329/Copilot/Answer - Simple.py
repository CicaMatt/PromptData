import threading
import shutil

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
    sourceFolder = "path/to/source"
    destFolder = "path/to/destination"
    
    threadClass = TheThread(sourceFolder, destFolder)
    threadClass.start()
    threadClass.join()  # Wait for the thread to finish
    
    if threadClass.exception:
        print(f"Caught an exception: {threadClass.exception}")
    else:
        print("Files copied successfully")

if __name__ == "__main__":
    main()
