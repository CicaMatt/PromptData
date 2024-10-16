import os
import tempfile

# Create a temporary directory
with tempfile.TemporaryDirectory() as temp_dir:
    # Define the path for the FIFO within the temporary directory
    fifo_path = os.path.join(temp_dir, 'my_fifo')
    
    # Create the FIFO
    os.mkfifo(fifo_path)
    
    # Open the FIFO for writing
    with open(fifo_path, 'w') as fifo:
        # Write data to the FIFO
        fifo.write('Hello, FIFO!')
    
    # At this point, some other process can read from the FIFO
    print(f"FIFO created at: {fifo_path}")

# The temporary directory and FIFO are automatically cleaned up
