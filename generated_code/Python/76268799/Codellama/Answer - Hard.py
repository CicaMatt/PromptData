from sqlalchemy.dialects.postgresql import ENUM as pgEnum
import enum
from sqlalchemy import Column, DateTime
from sqlalchemy.orm import mapped_column, Mapped
from sqlalchemy.ext.declarative import declarative_base
from datetime import datetime as dt
from uuid import uuid4, UUID  

Base = declarative_base()

class CampaignStatus(str, enum.Enum):
    activated = "activated"
    deactivated = "deactivated"

CampaignStatusType: pgEnum = pgEnum(
    CampaignStatus,
    name="campaignstatus",
    create_constraint=True,
    metadata=Base.metadata,
    validate_strings=True,
)

class Campaign(Base):
    __tablename__ = "campaign"

    id: Column[UUID] = Column(UUID, primary_key=True, default=uuid4)  
    created_at: Column[dt] = Column(DateTime, default=dt.now)  
    status: Mapped[CampaignStatusType] = mapped_column(nullable=False)
