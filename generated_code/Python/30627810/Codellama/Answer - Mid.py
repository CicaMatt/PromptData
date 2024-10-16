import json
import re

# Open the log file
with open("log_file.txt") as f:
    # Read each line from the file and parse it as JSON
    for line in f:
        try:
            json_data = json.loads(line)
            print(json_data)
        except ValueError:
            pass

with open("log_file.txt") as f:
    # Read each line from the file and parse it as JSON
    for line in f:
        match = re.match(r"^([\d-]+) - (__main__) - (\w+) - (.+)$", line)
        if match:
            date, module_name, log_level, message = match.groups()
            try:
                json_data = json.loads(message)
                print("{}: {}".format(date, json_data))
            except ValueError:
                pass