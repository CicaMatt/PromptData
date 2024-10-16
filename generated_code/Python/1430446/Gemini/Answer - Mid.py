import tempfile
import os

with tempfile.TemporaryDirectory() as temp_dir:
    fifo_path = os.path.join(temp_dir, 'my_fifo')
    os.mkfifo(fifo_path)

    # Open the FIFO for writing
    with open(fifo_path, 'w') as fifo:
        fifo.write('Hello from the FIFO!')

    # ... some other process can now read from the FIFO ...