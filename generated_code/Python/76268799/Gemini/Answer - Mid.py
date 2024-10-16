import enum
from sqlalchemy.dialects.postgresql import ENUM as pgEnum
from sqlalchemy.orm import DeclarativeBase
from sqlalchemy import mapped_column, Mapped
from sqlalchemy.dialects.postgresql import UUID
import uuid
import datetime as dt


class Base(DeclarativeBase):
    pass

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

    id: Mapped[UUID] = mapped_column(primary_key=True, default=uuid4)
    created_at: Mapped[dt.datetime] = mapped_column(default=dt.datetime.now)
    status: Mapped[CampaignStatus] = mapped_column(CampaignStatusType, nullable=False)