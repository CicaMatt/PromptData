from sqlalchemy import Column, Integer, String, create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

Base = declarative_base()

class Product(Base):
    __tablename__ = 'products'
    id = Column(Integer, primary_key=True)
    name = Column(String)
    description = Column(String)

# Database setup
engine = create_engine('sqlite:///:memory:')  # In-memory database for demonstration
Base.metadata.create_all(engine)
Session = sessionmaker(bind=engine)
session = Session()

# Create sample data
product1 = Product(name="Product 1", description="Description 1")
product2 = Product(name="Product 2", description="Description 2")
session.add_all([product1, product2])
session.commit()

# Serialize a single product
product = session.query(Product).first()
product_json = json.dumps(product, cls=AlchemyEncoder)
print(product_json)

# Serialize all products
products = session.query(Product).all()
products_json = json.dumps(products, cls=AlchemyEncoder)
print(products_json)
