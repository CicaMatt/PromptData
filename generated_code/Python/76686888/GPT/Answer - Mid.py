from pydantic import BaseModel
from pydantic import GetCoreSchemaHandler
from pydantic.json_schema import JsonSchemaValue
from bson import ObjectId
from typing import Any

class PyObjectId(ObjectId):
    @classmethod
    def __get_pydantic_core_schema__(cls, source_type: Any, handler: GetCoreSchemaHandler):
        # This is where you define the core schema for validation
        return handler(source_type).copy(update={
            "type": "str",
            "validate": cls.validate
        })

    @classmethod
    def validate(cls, v: Any) -> 'ObjectId':
        if not ObjectId.is_valid(v):
            raise ValueError("Invalid ObjectId")
        return ObjectId(v)

    @classmethod
    def __get_pydantic_json_schema__(cls, core_schema: Any, handler: GetCoreSchemaHandler) -> JsonSchemaValue:
        # Modifying the schema for OpenAPI/JSON Schema
        json_schema = handler(core_schema)
        json_schema.update({"type": "string"})
        return json_schema

# Example Pydantic BaseModel using PyObjectId
class MyModel(BaseModel):
    id: PyObjectId

    class Config:
        json_encoders = {ObjectId: str}
