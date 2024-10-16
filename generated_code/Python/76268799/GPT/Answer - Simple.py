from sqlalchemy import Enum as SQLAlchemyEnum
from sqlalchemy.dialects.postgresql import ENUM as pgEnum
from sqlalchemy.orm import Mapped, mapped_column
from sqlalchemy.ext.declarative import declarative_base
import enum
import uuid
import datetime as dt
from uuid import UUID

Base = declarative_base()

# Define the Python enum
class CampaignStatus(str, enum.Enum):
    activated = "activated"
    deactivated = "deactivated"

# No need to declare a `pgEnum` here explicitly for the mapped_column
class Campaign(Base):
    __tablename__ = "campaign"

    id: Mapped[UUID] = mapped_column(primary_key=True, default=uuid.uuid4)
    created_at: Mapped[dt.datetime] = mapped_column(default=dt.datetime.now)
    
    # Use SQLAlchemy's Enum type with the Python enum directly
    status: Mapped[CampaignStatus] = mapped_column(SQLAlchemyEnum(CampaignStatus, name="campaignstatus"), nullable=False)
