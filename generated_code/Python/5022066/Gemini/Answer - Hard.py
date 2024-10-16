from sqlalchemy.orm import class_mapper
from sqlalchemy.ext.declarative import DeclarativeMeta
import json

def serialize_query_result(query_result):
    """Serializes SQLAlchemy query result to JSON format."""

    def serialize_object(obj):
        """Serializes a single SQLAlchemy object to a dictionary."""
        if isinstance(obj.__class__, DeclarativeMeta):
            fields = {}
            for field in class_mapper(obj.__class__).columns:
                data = obj.__getattribute__(field.name)
                try:
                    json.dumps(data)  # Check if data is already JSON serializable
                    fields[field.name] = data
                except TypeError:
                    fields[field.name] = str(data)
            return fields
        return str(obj)  # Fallback for non-SQLAlchemy objects

    if isinstance(query_result, list):
        return json.dumps([serialize_object(item) for item in query_result])
    else:
        return json.dumps(serialize_object(query_result))

# Example usage:
# items = session.query(Product).all()
# json_data = serialize_query_result(items)