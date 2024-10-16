import json

def extract_facebook_post_data(json_str):
    """
    Extracts and prints relevant data from a Facebook post JSON string.

    Args:
        json_str: A string containing the JSON data of a Facebook post.

    Returns:
        None
    """

    try:
        data = json.loads(json_str)

        post_id = data.get('id', 'null')  
        post_type = data.get('type', 'null')
        created_time = data.get('created_time', 'null')
        updated_time = data.get('updated_time', 'null')

        print(f"Post ID: {post_id}")
        print(f"Post Type: {post_type}")
        print(f"Created Time: {created_time}")
        print(f"Updated Time: {updated_time}")

        app_id = data.get('application', {}).get('id', 'null')  
        print(f"App ID: {app_id}")

        # Handle the 'to' field with nested data
        to_data = data.get('to', {}).get('data', [])
        if to_data:
            to_id = to_data[0].get('id', 'null') 
        else:
            to_id = 'null'

        print(f"To ID: {to_id}")

    except json.JSONDecodeError as e:
        print(f"Error decoding JSON: {e}")


# Example usage
str_data = '{"from": {"id": "8", "name": "Mary Pinter"}, "message": "How ARE you?", "comments": {"count": 0}, "updated_time": "2012-05-01", "created_time": "2012-05-01", "to": {"data": [{"id": "1543", "name": "Honey Pinter"}]}, "type": "status", "id": "id_7"}'
extract_facebook_post_data(str_data)