from bson import ObjectId
from pydantic import BaseModel
from pydantic_core import core_schema
from typing import Any

class PyObjectId(ObjectId):
    @classmethod
    def __get_pydantic_core_schema__(cls, source: Any, handler: core_schema.Handler) -> core_schema.CoreSchema:
        return core_schema.str_schema(
            validator=cls.validate,
            json_schema={"type": "string"}
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
    my_model = MyModel(id="60d5f9b5f9b5f9b5f9b5f9b5")
    print(my_model)
except ValueError as e:
    print(e)
