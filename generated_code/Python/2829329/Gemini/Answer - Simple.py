import threading
import shutil
import sys

class TheThread(threading.Thread):
    def __init__(self, sourceFolder, destFolder):
        threading.Thread.__init__(self)
        self.sourceFolder = sourceFolder
        self.destFolder = destFolder
        self.exc_info = None  # Store exception information

    def run(self):
        try:
            shutil.copytree(self.sourceFolder, self.destFolder)
        except Exception:
            self.exc_info = sys.exc_info()  # Capture exception details

def main():
    try:
        threadClass = TheThread(param1, param2)
        threadClass.start()

        # ... (Your code to output .... while the thread runs)

        threadClass.join()  # Wait for the thread to finish

        if threadClass.exc_info:  # Check if an exception occurred
            raise threadClass.exc_info[1].with_traceback(threadClass.exc_info[2])

    except Exception as e:
        print(f"Caught an exception: {e}")

if __name__ == "__main__":
    main()