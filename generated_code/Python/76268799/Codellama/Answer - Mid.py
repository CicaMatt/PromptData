from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column
from uuid import UUID, uuid4
import enum
import datetime as dt

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
    id: Mapped[UUID] = Column(UUID, primary_key=True, default=uuid4)
    created_at: Mapped[dt.datetime] = Column(dt.datetime, default=dt.datetime.now)
    status: Mapped[CampaignStatusType] = Column(CampaignStatusType, nullable=False)