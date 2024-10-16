from bson import ObjectId
from pydantic import BaseModel
from pydantic_core import CoreSchema, PydanticUndefined

class PyObjectId(ObjectId):
    @classmethod
    def __get_pydantic_core_schema__(cls, _source_type: type[ObjectId], _handler: CoreSchema):
        return CoreSchema(
            schema_validator=cls.validate,
            serialization_info=cls.get_serialization_info(),
        )

    @classmethod
    def validate(cls, v: ObjectId | str | PydanticUndefined) -> ObjectId:
        if isinstance(v, PydanticUndefined):
            raise TypeError("ObjectId is required")
        if not isinstance(v, ObjectId):
            if not ObjectId.is_valid(v):
                raise ValueError("Invalid objectid")
            v = ObjectId(v)
        return v

    @classmethod
    def get_serialization_info(cls):
        return {"serialization": str}

class MyModel(BaseModel):
    id: PyObjectId