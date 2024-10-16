from bson import ObjectId
from pydantic import BaseModel
from pydantic_core import core_schema
from pydantic.json_schema import JsonSchemaValue

class PyObjectId(ObjectId):
    @classmethod
    def __get_pydantic_core_schema__(cls, _source_type: type, _handler: core_schema.Handler) -> core_schema.CoreSchema:
        return core_schema.str_schema()

    @classmethod
    def __get_pydantic_json_schema__(cls, _core_schema: core_schema.CoreSchema, _handler: core_schema.Handler) -> JsonSchemaValue:
        return {"type": "string"}

    @classmethod
    def validate(cls, v):
        if not ObjectId.is_valid(v):
            raise ValueError("Invalid ObjectId")
        return ObjectId(v)

class MyModel(BaseModel):
    id: PyObjectId

    @classmethod
    def model_validate(cls, data):
        data['id'] = PyObjectId.validate(data['id'])
        return super().model_validate(data)

# Example usage
try:
    my_model = MyModel(id="507f1f77bcf86cd799439011")
    print(my_model)
except ValueError as e:
    print(e)
