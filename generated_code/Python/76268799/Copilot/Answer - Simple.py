from sqlalchemy import Enum
import enum
from sqlalchemy.orm import Mapped, mapped_column
from sqlalchemy.ext.declarative import declarative_base
from uuid import uuid4
import datetime as dt

Base = declarative_base()

class CampaignStatus(str, enum.Enum):
    activated = "activated"
    deactivated = "deactivated"

class Campaign(Base):
    __tablename__ = "campaign"

    id: Mapped[UUID] = mapped_column(primary_key=True, default=uuid4)
    created_at: Mapped[dt.datetime] = mapped_column(default=dt.datetime.now)
    status: Mapped[CampaignStatus] = mapped_column(Enum(CampaignStatus), nullable=False)
