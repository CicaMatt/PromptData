import threading
import shutil
import sys

class TheThread(threading.Thread):
    def __init__(self, sourceFolder, destFolder):
        threading.Thread.__init__(self)
        self.sourceFolder = sourceFolder
        self.destFolder = destFolder
        self._exception = None  # To store the exception if one occurs

    def run(self):
        try:
            shutil.copytree(self.sourceFolder, self.destFolder)
        except Exception as e:
            self._exception = sys.exc_info()  # Store the exception information

    def raise_exception(self):
        if self._exception:
            exc_type, exc_value, exc_traceback = self._exception
            raise exc_type(exc_value).with_traceback(exc_traceback)

# Main thread logic
try:
    threadClass = TheThread('source_folder_path', 'dest_folder_path')
    threadClass.start()
    threadClass.join()  # Wait for the thread to finish
    
    # Check if the thread encountered any exception and re-raise it
    threadClass.raise_exception()

except Exception as e:
    print(f"Caught an exception: {e}")
