import os
import tempfile
import errno
import atexit

def create_temp_fifo():
    # Step 1: Create a secure temporary directory
    temp_dir = tempfile.mkdtemp()  # This avoids the deprecated mktemp and is secure
    fifo_path = os.path.join(temp_dir, 'temp_fifo')

    try:
        # Step 2: Create a FIFO (named pipe) at the fifo_path
        os.mkfifo(fifo_path)
    except OSError as e:
        if e.errno == errno.EEXIST:
            raise FileExistsError(f"FIFO {fifo_path} already exists.") from e
        else:
            raise

    # Step 3: Ensure the temporary directory and FIFO are cleaned up after use
    def cleanup():
        try:
            if os.path.exists(fifo_path):
                os.remove(fifo_path)
            if os.path.exists(temp_dir):
                os.rmdir(temp_dir)
        except Exception as e:
            print(f"Error cleaning up: {e}")

    atexit.register(cleanup)

    return fifo_path


# Example usage
if __name__ == "__main__":
    fifo_path = create_temp_fifo()
    print(f"FIFO created at: {fifo_path}")
    
    # Open the FIFO for writing (this process will block until there's a reader)
    try:
        with open(fifo_path, 'w') as fifo:
            fifo.write("This is a test message")
    except OSError as e:
        print(f"Failed to write to FIFO: {e}")
