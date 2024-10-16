import datetime
from typing import Optional
from jose import jwt, JWTError
from fastapi import APIRouter, Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from pydantic import BaseModel
from tinydb import TinyDB, Query, where
from passlib.context import CryptContext
from datetime import timedelta, datetime

# Constants
SECRET_KEY = "e79b2a1eaa2b801bc81c49127ca4607749cc2629f73518194f528fc5c8491713"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 1

# Router
router = APIRouter()

# OAuth2 Scheme
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/dev-service/api/v1/openvpn/token")

# TinyDB instance
db = TinyDB('app/Users.json')
Users = db.table('User')
UserQuery = Query()

# Password hashing context
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

# Pydantic models
class UserModel(BaseModel):
    username: str
    password: str

class Token(BaseModel):
    access_token: str
    token_type: str

class TokenData(BaseModel):
    username: Optional[str] = None

# Helper function to get hashed password
def get_password_hash(password):
    return pwd_context.hash(password)

# Helper function to verify password
def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)

# Function to get user from the database
def get_user(username: str):
    user = Users.search(where('name') == username)
    if not user:
        return None
    return user[0]

# Function to create access token
def create_access_token(data: dict, expires_delta: Optional[timedelta] = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.utcnow() + expires_delta
    else:
        expire = datetime.utcnow() + timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt

# Authentication verification
async def verify_user(username: str, password: str):
    user = get_user(username)
    if not user:
        return None
    if not verify_password(password, user['password']):
        return None
    return user

# Route to generate token
@router.post("/token", response_model=Token)
async def token_generate(form_data: OAuth2PasswordRequestForm = Depends()):
    user = await verify_user(form_data.username, form_data.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(data={"sub": form_data.username}, expires_delta=access_token_expires)
    return {"access_token": access_token, "token_type": "bearer"}

# Route to get the current user
@router.get("/user/me")
async def get_current_user(token: str = Depends(oauth2_scheme)):
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        username: str = payload.get("sub")
        if not username:
            raise credentials_exception
        token_data = TokenData(username=username)
    except JWTError:
        raise credentials_exception

    user = get_user(token_data.username)
    if not user:
        raise credentials_exception
    return user

# Route to create a new user
@router.post("/user")
async def create_user(name: str, password: str):
    hashed_password = get_password_hash(password)
    Users.insert({'name': name, 'password': hashed_password})
    return {"msg": "User created successfully!"}
