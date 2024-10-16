import tempfile

# Create a temporary file that will persist until it is explicitly deleted or the program exits
with tempfile.NamedTemporaryFile(delete=False) as fifo_file:
    # Do something with the file, such as read and write to it
    print("Created temporary FIFO:", fifo_file.name)