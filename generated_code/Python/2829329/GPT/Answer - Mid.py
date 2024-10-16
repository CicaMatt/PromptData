import threading
import shutil
import os
import queue
import time

class TheThread(threading.Thread):
    def __init__(self, sourceFolder, destFolder, exception_queue):
        threading.Thread.__init__(self)
        self.sourceFolder = sourceFolder
        self.destFolder = destFolder
        # Use a queue to pass exceptions
        self.exception_queue = exception_queue  

    def run(self):
        try:
            if not os.path.exists(self.sourceFolder):
                raise FileNotFoundError(f"Source folder '{self.sourceFolder}' does not exist.")
            shutil.copytree(self.sourceFolder, self.destFolder)
        except Exception as e:
            # Put the exception in the queue so the main thread can handle it
            self.exception_queue.put(e)

def copy_files_with_progress(sourceFolder, destFolder):
    exception_queue = queue.Queue()

    # Start the copying in a separate thread
    thread = TheThread(sourceFolder, destFolder, exception_queue)
    thread.start()

    # Show progress indicator while thread is running
    while thread.is_alive():
        print("Copying files... Press Ctrl+C to cancel.", end="\r", flush=True)
        # Sleep for a while to reduce CPU usage
        time.sleep(0.5)  

    # Once the thread finishes, check if any exception occurred
    thread.join()  
    if not exception_queue.empty():
        exception = exception_queue.get()
        # Re-raise the exception in the main thread
        raise exception  

    print("\nFiles copied successfully.")

if __name__ == "__main__":
    try:
        # Replace with your actual source folder
        source = "/path/to/source"  
        # Replace with your actual destination folder
        destination = "/path/to/destination"  
        copy_files_with_progress(source, destination)
    except Exception as e:
        print(f"Error: {e}")
