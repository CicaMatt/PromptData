import json

# Sample JSON string
str = '{"from": {"id": "8", "name": "Mary Pinter"}, "message": "How ARE you?", "comments": {"count": 0}, "updated_time": "2012-05-01", "created_time": "2012-05-01", "to": {"data": [{"id": "1543", "name": "Honey Pinter"}]}, "type": "status", "id": "id_7"}'
data = json.loads(str)

# Extract post id and type
post_id = data.get('id')
post_type = data.get('type')
print(post_id)
print(post_type)

# Extract creation and update times
created_time = data.get('created_time')
updated_time = data.get('updated_time')
print(created_time)
print(updated_time)

# Extract application id if it exists, else print null
app_id = data.get('application', {}).get('id', 'null')
print(app_id)

# Extract the 'to_id' from the "to" field or print 'null'
to_data = data.get('to', {}).get('data', [])
if to_data and isinstance(to_data, list) and len(to_data) > 0:
    to_id = to_data[0].get('id', 'null')  # Get the first 'id' in the 'data' list
    print(to_id)
else:
    print('null')
