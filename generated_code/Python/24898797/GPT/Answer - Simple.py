import json

# Your JSON string
str = '{"from": {"id": "8", "name": "Mary Pinter"}, "message": "How ARE you?", "comments": {"count": 0}, "updated_time": "2012-05-01", "created_time": "2012-05-01", "to": {"data": [{"id": "1543", "name": "Honey Pinter"}]}, "type": "status", "id": "id_7"}'
data = json.loads(str)

# Extract post_id and post_type
post_id = data['id']
post_type = data['type']
print(post_id)
print(post_type)

# Extract created_time and updated_time
created_time = data['created_time']
updated_time = data['updated_time']
print(created_time)
print(updated_time)

# Check if "application" field exists, print app_id if available, else print 'null'
if data.get('application'):
    app_id = data['application'].get('id', 0)
    print(app_id)
else:
    print('null')

# Extract "to_id" from "to" field if it exists
if data.get('to') and data['to'].get('data') and len(data['to']['data']) > 0:
    to_id = data['to']['data'][0].get('id', 'null')
    print(to_id)
else:
    print('null')
