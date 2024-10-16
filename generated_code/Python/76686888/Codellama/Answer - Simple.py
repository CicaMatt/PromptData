from pydantic import BaseModel, validator
from bson.objectid import ObjectId

class PyObjectId(BaseModel):
    id: str = None

    @validator("id")
    def validate_object_id(cls, v):
        if not ObjectId.is_valid(v):
            raise ValueError("Invalid object id")
        return ObjectId(v)