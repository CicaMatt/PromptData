from sqlalchemy import create_engine, Column, Enum, func
from sqlalchemy.orm import declarative_base, Mapped, mapped_column, sessionmaker
import enum
import uuid
import datetime as dt
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.ext.declarative import declared_attr

# SQLAlchemy Base
Base = declarative_base()

# Enum for campaign status
class CampaignStatus(str, enum.Enum):
    activated = "activated"
    deactivated = "deactivated"

# Define Campaign class with mapped_column
class Campaign(Base):
    __tablename__ = "campaign"

    id: Mapped[uuid.UUID] = mapped_column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4, nullable=False)
    created_at: Mapped[dt.datetime] = mapped_column(default=dt.datetime.utcnow, server_default=func.now(), nullable=False)
    status: Mapped[CampaignStatus] = mapped_column(Enum(CampaignStatus, name="campaignstatus", create_constraint=True, native_enum=True), nullable=False)

# Database engine creation (example using PostgreSQL)
engine = create_engine("postgresql://user:password@localhost/mydatabase", echo=True)

# Create tables
Base.metadata.create_all(engine)
