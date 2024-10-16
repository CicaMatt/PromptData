import json
from datetime import datetime

def parse_post(json_str):
    try:
        # Parse the JSON data
        data = json.loads(json_str)
    except json.JSONDecodeError as e:
        print(f"JSON decoding error: {e}")
        return
    
    # Extract post ID and type, with basic error handling
    post_id = data.get('id', 'null')
    post_type = data.get('type', 'null')
    
    print(f"Post ID: {post_id}")
    print(f"Post Type: {post_type}")
    
    # Extract and validate created and updated times
    created_time = data.get('created_time', 'null')
    updated_time = data.get('updated_time', 'null')
    
    if created_time != 'null':
        try:
            created_time = datetime.strptime(created_time, '%Y-%m-%d').date()
        except ValueError:
            print("Invalid created_time format, expected YYYY-MM-DD")
            created_time = 'null'
    
    if updated_time != 'null':
        try:
            updated_time = datetime.strptime(updated_time, '%Y-%m-%d').date()
        except ValueError:
            print("Invalid updated_time format, expected YYYY-MM-DD")
            updated_time = 'null'
    
    print(f"Created Time: {created_time}")
    print(f"Updated Time: {updated_time}")
    
    # Extract 'application' details safely
    application = data.get('application', {})
    app_id = application.get('id', 'null')
    print(f"Application ID: {app_id}")
    
    # Safely handle the 'to' field which contains nested data
    to_data = data.get('to', {}).get('data', [])
    if to_data and isinstance(to_data, list) and len(to_data) > 0:
        to_id = to_data[0].get('id', 'null')
    else:
        to_id = 'null'
    
    print(f"To ID: {to_id}")

# Example usage:
json_str = '{"from": {"id": "8", "name": "Mary Pinter"}, "message": "How ARE you?", "comments": {"count": 0}, "updated_time": "2012-05-01", "created_time": "2012-05-01", "to": {"data": [{"id": "1543", "name": "Honey Pinter"}]}, "type": "status", "id": "id_7"}'
parse_post(json_str)
