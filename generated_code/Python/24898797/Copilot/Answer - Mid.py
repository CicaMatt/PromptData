import json

# Sample JSON data as a string
str_data = '{"from": {"id": "8", "name": "Mary Pinter"}, "message": "How ARE you?", "comments": {"count": 0}, "updated_time": "2012-05-01", "created_time": "2012-05-01", "to": {"data": [{"id": "1543", "name": "Honey Pinter"}]}, "type": "status", "id": "id_7"}'

# Load the JSON string into a Python dictionary
data = json.loads(str_data)

# Extract post_id and post_type
post_id = data.get('id', 'null')
post_type = data.get('type', 'null')

# Print post_id and post_type
print(f"Post ID: {post_id}")
print(f"Post Type: {post_type}")

# Extract and print created_time and updated_time
created_time = data.get('created_time', 'null')
updated_time = data.get('updated_time', 'null')

print(f"Created Time: {created_time}")
print(f"Updated Time: {updated_time}")

# Extract application ID if it exists, otherwise print 'null'
app_id = data.get('application', {}).get('id', 'null')
print(f"Application ID: {app_id}")

# Safely extract 'to' ID if it exists, otherwise print 'null'
to_data = data.get('to', {}).get('data', [])
if to_data and isinstance(to_data, list) and len(to_data) > 0:
    to_id = to_data[0].get('id', 'null')
else:
    to_id = 'null'

print(f"To ID: {to_id}")
