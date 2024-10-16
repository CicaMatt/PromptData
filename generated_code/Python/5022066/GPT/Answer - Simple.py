from sqlalchemy.ext.declarative import declarative_base
import json

Base = declarative_base()

class Product(Base):
    __tablename__ = 'products'
    
    id = Column(Integer, primary_key=True)
    name = Column(String)
    description = Column(String)
    
    def to_dict(self):
        return {
            'id': self.id,
            'name': self.name,
            'description': self.description,
        }

# Usage
items = session.query(Product).all()
json_result = json.dumps([item.to_dict() for item in items])
print(json_result)
