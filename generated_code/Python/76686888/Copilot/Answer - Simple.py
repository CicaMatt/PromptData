from bson import ObjectId
from pydantic import BaseModel
from pydantic_core import core_schema, CoreSchema
from typing import Any

class PyObjectId(ObjectId):
    @classmethod
    def __get_pydantic_core_schema__(cls, _source_type: Any, _handler: Any) -> CoreSchema:
        return core_schema.general_after_validator_function(
            cls.validate,
            core_schema.str_schema(),
            serialization=core_schema.to_string_ser_schema()
        )

    @classmethod
    def validate(cls, v: Any) -> ObjectId:
        if not ObjectId.is_valid(v):
            raise ValueError("Invalid ObjectId")
        return ObjectId(v)

class MyModel(BaseModel):
    id: PyObjectId

# Example usage
try:
    my_model = MyModel(id="507f1f77bcf86cd799439011")
    print(my_model)
except ValueError as e:
    print(e)
