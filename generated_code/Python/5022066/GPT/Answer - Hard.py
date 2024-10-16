import json
from sqlalchemy.ext.declarative import DeclarativeMeta
from sqlalchemy.orm import sessionmaker
from sqlalchemy import create_engine

# Assuming Product is your SQLAlchemy model
# Example Product model:
# class Product(Base):
#     __tablename__ = 'products'
#     id = Column(Integer, primary_key=True)
#     name = Column(String)
#     description = Column(String)

# Create your database engine
engine = create_engine('sqlite:///yourdatabase.db')
Session = sessionmaker(bind=engine)
session = Session()

def serialize_model(obj):
    """
    Function to serialize SQLAlchemy ORM object to dictionary.
    Handles single objects as well as lists of objects.
    """
    if isinstance(obj.__class__, DeclarativeMeta):
        # SQLAlchemy class instance
        fields = {}
        for field in [x for x in dir(obj) if not x.startswith('_') and x != 'metadata']:
            data = obj.__getattribute__(field)
            try:
                json.dumps(data)  # Test if field is JSON serializable
                fields[field] = data
            except TypeError:
                fields[field] = str(data)  # Fallback to string if not serializable
        return fields
    elif isinstance(obj, list):
        # Handle lists of SQLAlchemy objects
        return [serialize_model(item) for item in obj]
    else:
        raise TypeError(f"Object of type {obj.__class__.__name__} is not serializable.")

# Example usage
# Assuming you want to serialize all products from the database
products = session.query(Product).all()  # Retrieve all Product instances

# Serialize the list of products
products_serialized = serialize_model(products)

# Convert the serialized data to JSON
json_data = json.dumps(products_serialized, indent=4)

# Print or return the JSON data
print(json_data)
