import tempfile

# Create a temporary named pipe
tmp_fifo = tempfile.NamedTemporaryFile(mode="w+b")

# Use the temporary named pipe for reading and writing
with open(tmp_fifo.name, "r+b") as fifo:
    # Write data to the named pipe
    fifo.write("Hello, world!".encode())
    
    # Read data from the named pipe
    print(fifo.read().decode())