import os
import tempfile
import stat

# Create a temporary directory using tempfile.mkdtemp()
temp_dir = tempfile.mkdtemp()

# Construct the FIFO (named pipe) path within the temp directory
fifo_path = os.path.join(temp_dir, "my_fifo")

# Create the FIFO (named pipe)
os.mkfifo(fifo_path)

# Open the FIFO for writing (os.O_WRONLY)
fifo_fd = os.open(fifo_path, os.O_WRONLY)

# ... some process somewhere will read from this FIFO ...

# Don't forget to clean up (close and remove FIFO, and delete the temp directory)
os.close(fifo_fd)
os.remove(fifo_path)
os.rmdir(temp_dir)
