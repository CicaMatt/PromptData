from sqlalchemy.orm import Mapped, mapped_column
import enum
from sqlalchemy.types import Enum as SQLAlchemyEnumType
from uuid import UUID, uuid4
import datetime as dt
import Base

class CampaignStatus(str, enum.Enum):
    activated = "activated"
    deactivated = "deactivated"

CampaignStatusType: SQLAlchemyEnumType = SQLAlchemyEnumType(
    CampaignStatus,
    name="campaignstatus",
    create_constraint=True,
    # Assuming you have a 'Base' class for your models
    metadata=Base.metadata,  
    validate_strings=True,
)

class Campaign(Base):
    __tablename__ = "campaign"

    id: Mapped[UUID] = mapped_column(primary_key=True, default=uuid4)
    created_at: Mapped[dt.datetime] = mapped_column(default=dt.datetime.now)
    status: Mapped[CampaignStatus] = mapped_column(CampaignStatusType, nullable=False)