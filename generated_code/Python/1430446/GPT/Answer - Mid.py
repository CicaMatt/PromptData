import os
import tempfile

# Create a unique temporary directory using tempfile
with tempfile.TemporaryDirectory() as temp_dir:
    # Construct the path to the FIFO inside the temporary directory
    fifo_path = os.path.join(temp_dir, "my_fifo")

    # Create the FIFO (named pipe)
    os.mkfifo(fifo_path)

    # Open the FIFO for writing
    with open(fifo_path, 'w') as fifo:
        # Write some data to the FIFO (example)
        fifo.write("Hello from FIFO\n")

    # At this point, the process can read from the FIFO from another process or thread
    print(f"Temporary FIFO created at {fifo_path}")
    print("Write operation completed.")

# The temporary directory and FIFO are automatically cleaned up when the block exits.
