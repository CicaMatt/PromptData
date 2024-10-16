from bson import ObjectId
from pydantic import BaseModel, Field


class PydanticObjectId(ObjectId):
    """
    A Pydantic-compatible ObjectId class. It provides seamless integration 
    with Pydantic models while maintaining ObjectId functionality.
    """

    @classmethod
    def __get_pydantic_core_schema__(cls, _source_type):
        # Define the core schema for Pydantic v2 validation
        from pydantic_core import CoreSchema, ValidationError

        def validate_objectid(value) -> ObjectId:
            if not ObjectId.is_valid(value):
                raise ValidationError(
                    f"Invalid ObjectId: {value}", context={"type": "ObjectId"}
                )
            return ObjectId(value)

        return CoreSchema(
            validator=validate_objectid,
            serialization=lambda obj: str(obj),
        )


class MyModel(BaseModel):
    """
    Example model demonstrating the use of PydanticObjectId.
    """

    id: PydanticObjectId = Field(default_factory=PydanticObjectId, alias="_id")