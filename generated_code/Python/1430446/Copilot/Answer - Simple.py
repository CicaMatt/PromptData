import os
import tempfile

# Create a temporary directory
with tempfile.TemporaryDirectory() as temp_dir:
    # Define the path for the FIFO within the temporary directory
    fifo_path = os.path.join(temp_dir, 'temp_fifo')
    
    # Create the FIFO
    os.mkfifo(fifo_path)
    
    # Open the FIFO for writing
    with open(fifo_path, 'w') as fifo:
        fifo.write('Hello, FIFO!')

    # ... some process, somewhere, will read it ...
