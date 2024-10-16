from bson import ObjectId
from pydantic import BaseModel
from pydantic_core import CoreSchema

class PyObjectId(ObjectId):
    @classmethod
    def __get_pydantic_core_schema__(cls, _source_type: type[ObjectId], _handler) -> CoreSchema:
        return CoreSchema(
            schema_ref="string",
            serialization=ObjectId.__str__,
            deserialize=cls.validate,
        )

    @classmethod
    def validate(cls, v: str) -> ObjectId:
        if not ObjectId.is_valid(v):
            raise ValueError("Invalid objectid")
        return ObjectId(v)

class MyModel(BaseModel):
    id: PyObjectId