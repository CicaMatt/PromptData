from marshmallow import Schema, fields
from sqlalchemy import Column, Integer, String
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

Base = declarative_base()

class Product(Base):
    __tablename__ = 'products'
    id = Column(Integer, primary_key=True)
    name = Column(String)
    description = Column(String)

class ProductSchema(Schema):
    id = fields.Int()
    name = fields.Str()
    description = fields.Str()

engine = ... # Your SQLAlchemy engine setup
Session = sessionmaker(bind=engine)
session = Session()

products = session.query(Product).all()
schema = ProductSchema(many=True)
result = schema.dump(products)

print(result)