import threading
import shutil
import time
import os
import queue

class TheThread(threading.Thread):
    def __init__(self, sourceFolder, destFolder, exception_queue):
        threading.Thread.__init__(self)
        self.sourceFolder = sourceFolder
        self.destFolder = destFolder
        self.exception_queue = exception_queue
    
    def run(self):
        try:
            # Ensure that the source folder exists
            if not os.path.exists(self.sourceFolder):
                raise FileNotFoundError(f"Source folder '{self.sourceFolder}' does not exist.")

            # Copying files from sourceFolder to destFolder
            shutil.copytree(self.sourceFolder, self.destFolder)
        except Exception as e:
            # Put any exception into the queue so the main thread can catch it
            self.exception_queue.put(e)

def monitor_thread(thread, exception_queue):
    """
    Function to monitor the thread's progress and provide user feedback.
    """
    while thread.is_alive():
        print("Copying files... Please wait.", end="\r")
        time.sleep(1)

    # Check if an exception occurred in the thread
    if not exception_queue.empty():
        exception = exception_queue.get()
        raise exception

def main():
    # Replace with actual source folder path
    source_folder = "/path/to/source/folder"  
    # Replace with actual destination folder path
    dest_folder = "/path/to/destination/folder"  

    # Queue for thread exception handling
    exception_queue = queue.Queue()

    # Create and start the thread
    copy_thread = TheThread(source_folder, dest_folder, exception_queue)
    copy_thread.start()

    try:
        # Monitor the thread and show progress
        monitor_thread(copy_thread, exception_queue)
        # Wait for the thread to finish
        copy_thread.join()  
        print("\nFile copy completed successfully!")
    except Exception as e:
        print(f"\nCaught an exception during file copy: {e}")

if __name__ == "__main__":
    main()
