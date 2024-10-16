from sqlalchemy import create_engine, Column, Enum
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, Mapped, mapped_column
from sqlalchemy.dialects.postgresql import ENUM as pgEnum
import enum
import datetime as dt
from uuid import uuid4, UUID

Base = declarative_base()

class CampaignStatus(str, enum.Enum):
    activated = "activated"
    deactivated = "deactivated"

class Campaign(Base):
    __tablename__ = "campaign"

    id: Mapped[UUID] = mapped_column(primary_key=True, default=uuid4)
    created_at: Mapped[dt.datetime] = mapped_column(default=dt.datetime.now)
    status: Mapped[CampaignStatus] = mapped_column(pgEnum(CampaignStatus, name="campaignstatus"), nullable=False)

# Example setup
engine = create_engine('postgresql://user:password@localhost/mydatabase')
Base.metadata.create_all(engine)
Session = sessionmaker(bind=engine)
session = Session()

# Example usage
new_campaign = Campaign(status=CampaignStatus.activated)
session.add(new_campaign)
session.commit()
