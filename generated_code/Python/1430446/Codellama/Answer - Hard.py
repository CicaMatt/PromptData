import tempfile
import os

# Create a unique temporary file and get its name
tmp_file = tempfile.NamedTemporaryFile()
tmp_file_name = tmp_file.name

# Create the named pipe by opening the temporary file with mkfifo
os.mkfifo(tmp_file_name)

# Open the named pipe for reading and writing
with open(tmp_file_name, 'w+') as f:
    # Some process can read from the pipe here
    pass