import threading
import shutil
import time
import sys

class FileCopyThread(threading.Thread):
    def __init__(self, source_folder, dest_folder):
        super().__init__()  # Use super() for better inheritance in modern Python
        self.source_folder = source_folder
        self.dest_folder = dest_folder
        self.exception = None  # Store any exception that occurs during copying

    def run(self):
        try:
            shutil.copytree(self.source_folder, self.dest_folder)
        except Exception as e:
            self.exception = e  # Capture the exception for later handling

def main():
    source_folder = "path/to/source"
    dest_folder = "path/to/destination"

    copy_thread = FileCopyThread(source_folder, dest_folder)
    copy_thread.start()

    while copy_thread.is_alive():
        print("....", end="\r")  # Output progress indicator, overwrite previous line
        time.sleep(1)  # Adjust sleep duration as needed

    if copy_thread.exception:
        print("\nAn error occurred during copying:")
        print(copy_thread.exception)
    else:
        print("\nCopying completed successfully!")

if __name__ == "__main__":
    main()