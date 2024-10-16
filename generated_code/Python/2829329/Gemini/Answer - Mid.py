import threading
import shutil
import time
import sys

class TheThread(threading.Thread):
    def __init__(self, source_folder, dest_folder):
        threading.Thread.__init__(self)
        self.source_folder = source_folder
        self.dest_folder = dest_folder
        self.exception = None  # To store any exception

    def run(self):
        try:
            shutil.copytree(self.source_folder, self.dest_folder)
        except Exception as e:
            self.exception = e  # Capture the exception

def main():
    source_folder = "path/to/source"
    dest_folder = "path/to/destination"

    thread_class = TheThread(source_folder, dest_folder)
    thread_class.start()

    while thread_class.is_alive():
        print("....")  # Indicate the script is running
        time.sleep(1)  # Adjust the sleep interval as needed

    if thread_class.exception:
        print("Caught an exception:", thread_class.exception)
        # Optionally, you can use sys.exc_info() here for more details:
        # exc_type, exc_value, exc_traceback = sys.exc_info()
        # print("Exception details:", exc_type, exc_value, exc_traceback)
    else:
        print("File copying completed successfully!")

if __name__ == "__main__":
    main()