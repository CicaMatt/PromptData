import threading
import shutil
import queue
import logging

# Configure logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

class TheThread(threading.Thread):
    def __init__(self, sourceFolder, destFolder, exception_queue):
        threading.Thread.__init__(self)
        self.sourceFolder = sourceFolder
        self.destFolder = destFolder
        self.exception_queue = exception_queue
    
    def run(self):
        try:
            shutil.copytree(self.sourceFolder, self.destFolder)
        except Exception as e:
            self.exception_queue.put(e)
            logging.error(f"Error copying files: {e}")

def main():
    sourceFolder = 'path/to/source'
    destFolder = 'path/to/destination'
    exception_queue = queue.Queue()

    thread = TheThread(sourceFolder, destFolder, exception_queue)
    thread.start()

    while thread.is_alive():
        print("Copying files...")  # Indicate that the script is still running
        thread.join(1)  # Wait for the thread to finish

    # Check if any exceptions were raised in the thread
    if not exception_queue.empty():
        exception = exception_queue.get()
        print(f"Caught an exception: {exception}")
    else:
        print("Files copied successfully.")

if __name__ == "__main__":
    main()
