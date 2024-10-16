from sqlalchemy.dialects.postgresql import ENUM as pgEnum
from sqlalchemy.orm import Mapped, mapped_column
from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
import enum
import uuid
import datetime as dt
from uuid import uuid4

# Declare the Base
Base = declarative_base()

# Define your Enum class
class CampaignStatus(enum.Enum):
    activated = "activated"
    deactivated = "deactivated"

# Define the PostgreSQL ENUM type for SQLAlchemy
CampaignStatusType: pgEnum = pgEnum(
    CampaignStatus,
    name="campaignstatus",
    create_type=True,
    validate_strings=True
)

# Define the Campaign model using the updated syntax for SQLAlchemy 2.0
class Campaign(Base):
    __tablename__ = "campaign"

    id: Mapped[uuid.UUID] = mapped_column(primary_key=True, default=uuid4)
    created_at: Mapped[dt.datetime] = mapped_column(default=dt.datetime.now)
    status: Mapped[CampaignStatus] = mapped_column(CampaignStatusType, nullable=False)


# Example of setting up the engine (connection to the DB)
# This would be your actual database URI
engine = create_engine("postgresql://user:password@localhost/test")

# Create the tables
Base.metadata.create_all(engine)
