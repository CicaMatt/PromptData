import json
from jose import jwt, JWTError
from typing import Optional, List
from datetime import datetime, timedelta
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm, SecurityScopes
from fastapi import APIRouter, Depends, HTTPException, status
from tinydb import TinyDB, where
from passlib.context import CryptContext
from pydantic import BaseModel

# FastAPI router
router = APIRouter()

# Security configurations
SECRET_KEY = "e79b2a1eaa2b801bc81c49127ca4607749cc2629f73518194f528fc5c8491713"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30  # Increased expiration time for usability

# OAuth2 Password Bearer
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/dev-service/api/v1/openvpn/token", scopes={"me": "Read info about the user", "admin": "Admin access"})

# Initialize TinyDB
db = TinyDB('app/Users.json')
Users = db.table('User')
UserQuery = where

# Password hashing context
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

# Pydantic models
class TokenData(BaseModel):
    username: Optional[str] = None
    scopes: List[str] = []

class Token(BaseModel):
    access_token: str
    token_type: str

class User(BaseModel):
    username: str
    password: str

# Helper function to get user by username
def get_user(username: str):
    user = Users.search(UserQuery.name == username)
    if user:
        return user[0]
    return None

# Helper function to verify password
def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)

# Token creation function
def create_access_token(data: dict, expires_delta: Optional[timedelta] = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.utcnow() + expires_delta
    else:
        expire = datetime.utcnow() + timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt

# Route to generate token
@router.post("/token", response_model=Token)
async def token_generate(form_data: OAuth2PasswordRequestForm = Depends()):
    user = get_user(form_data.username)
    if not user or not verify_password(form_data.password, user['password']):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )

    # Assign default scopes or based on user role
    scopes = ["me"]
    if user.get("role") == "admin":
        scopes.append("admin")

    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": form_data.username, "scopes": scopes}, expires_delta=access_token_expires
    )
    return {"access_token": access_token, "token_type": "bearer"}

# Function to get the current user based on token and validate scopes
async def get_current_user(security_scopes: SecurityScopes, token: str = Depends(oauth2_scheme)):
    if security_scopes.scopes:
        authenticate_value = f'Bearer scope="{security_scopes.scope_str}"'
    else:
        authenticate_value = "Bearer"

    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": authenticate_value},
    )

    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        username: str = payload.get("sub")
        token_scopes: List[str] = payload.get("scopes", [])

        if username is None:
            raise credentials_exception

        token_data = TokenData(username=username, scopes=token_scopes)
    except JWTError:
        raise credentials_exception

    user = get_user(token_data.username)
    if user is None:
        raise credentials_exception

    # Validate the scopes
    for scope in security_scopes.scopes:
        if scope not in token_data.scopes:
            raise HTTPException(
                status_code=status.HTTP_403_FORBIDDEN,
                detail="Not enough permissions",
            )
    
    return user

# Route to get current user info, requires 'me' scope
@router.get('/user/me', response_model=User)
async def get_current_user_info(current_user: User = Depends(get_current_user)):
    return current_user

# Route to create a new user
@router.post('/user')
async def create_user(username: str, password: str):
    hashed_password = pwd_context.hash(password)
    Users.insert({'name': username, 'password': hashed_password, 'role': 'user'})
    return {"message": "User created successfully"}

# Admin-only route to get user list
@router.get('/users', dependencies=[Depends(SecurityScopes(scopes=["admin"]))])
async def get_all_users(current_user: User = Depends(get_current_user)):
    # Only accessible to users with 'admin' scope
    return Users.all()
