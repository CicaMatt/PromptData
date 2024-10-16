from sqlalchemy.dialects.postgresql import ENUM
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.orm import declarative_base, mapped_column, Mapped
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.types import Enum as SQLAlchemyEnum
import enum
import uuid
import datetime as dt


Base = declarative_base()

class CampaignStatus(str, enum.Enum):
    activated = 'activated'
    deactivated = 'deactivated'


class Campaign(Base):
    __tablename__ = 'campaign'

    id: Mapped[UUID] = mapped_column(UUID(as_uuid=True), primary_key=True, default=uuid4)
    created_at: Mapped[dt.datetime] = mapped_column(default=dt.datetime.now)
    status: Mapped[CampaignStatus] = mapped_column(SQLAlchemyEnum(CampaignStatus), nullable=False)

engine = create_engine('postgresql://your_user:your_password@localhost/your_database')

Base.metadata.create_all(engine)