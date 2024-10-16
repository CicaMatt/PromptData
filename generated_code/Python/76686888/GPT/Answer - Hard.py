from bson import ObjectId
from pydantic import GetCoreSchemaHandler
from pydantic.json_schema import JsonSchemaValue
from pydantic.core_schema import ValidationError, CoreSchema, FunctionPlainValidator


class PyObjectId(ObjectId):
    @classmethod
    def __get_pydantic_core_schema__(cls, source_type: type, handler: GetCoreSchemaHandler) -> CoreSchema:
        """
        This method defines the core schema for Pydantic v2 to handle ObjectId.
        """
        # Use a function-based plain validator
        return FunctionPlainValidator(cls.validate)

    @classmethod
    def validate(cls, v) -> ObjectId:
        """
        Validation method for ObjectId.
        Ensures that the value provided is a valid ObjectId.
        """
        if isinstance(v, ObjectId):
            return v
        if not ObjectId.is_valid(v):
            raise ValidationError(f"Invalid ObjectId: {v}")
        return ObjectId(v)

    @classmethod
    def __get_pydantic_json_schema__(cls, core_schema: CoreSchema, handler: GetCoreSchemaHandler) -> JsonSchemaValue:
        """
        Modifies the JSON schema representation to show 'type' as 'string'.
        """
        json_schema = handler(core_schema)
        json_schema.update(type="string")
        return json_schema

