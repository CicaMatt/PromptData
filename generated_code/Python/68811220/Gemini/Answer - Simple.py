import json
from jose import jwt, JWTError
from typing import Optional
from datetime import datetime, timedelta
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from fastapi import APIRouter, UploadFile, File, Depends, HTTPException, status
from tinydb import TinyDB, where
from tinydb import Query
from passlib.hash import bcrypt
from pydantic import BaseModel
from passlib.context import CryptContext

# ... (Your imports and configurations)

SCOPES = {
    "read": "Read access",
    "write": "Write access",
    "admin": "Administrative access",
}

router = APIRouter()
SECRET_KEY = "e79b2a1eaa2b801bc81c49127ca4607749cc2629f73518194f528fc5c8491713"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 1
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/dev-service/api/v1/openvpn/token")
db = TinyDB('app/Users.json')
Users = db.table('User')
User = Query

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

class User(BaseModel):
    username: str
    password: str

def get_user(username: str):
    user = Users.search((where('name') == username))
    if user:
        return user[0]

@router.post('/verif')
async def verify_user(name, password):
    user = Users.search((where('name') == name))
    if not user:
        return False
    passw = user[0]['password']
    if not bcrypt.verify(password, passw):
        return False
    return user

def create_access_token(data: dict, expires_delta: Optional[timedelta] = None, scopes: list = []):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.utcnow() + expires_delta
    else:
        expire = datetime.utcnow() + timedelta(minutes=1)
    to_encode.update({"exp": expire, "scopes": scopes})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt

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
    access_token = create_access_token(data={"sub": form_data.username}, expires_delta=access_token_expires, scopes=["read"])  # Example: Grant "read" scope by default
    return {"access_token": access_token, "token_type": "bearer"}

@router.get('/user/me')
async def get_current_user(token: str = Depends(oauth2_scheme), required_scopes: list = []):
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        if payload["exp"] < datetime.utcnow():
            raise credentials_exception
        username: str = payload.get("sub")
        if username is None:
            raise credentials_exception
        token_data = TokenData(username=username)
        scopes = payload.get("scopes")
        if not all(scope in scopes for scope in required_scopes):
            raise HTTPException(
                status_code=status.HTTP_403_FORBIDDEN,
                detail="Insufficient permissions",
                headers={"WWW-Authenticate": "Bearer"},
            )
    except JWTError:
        raise credentials_exception
    user = Users.search(where('name') == token_data.username)
    if user is None:
        raise credentials_exception
    return user

@router.post('/user')
async def create_user(name, password, user: User = Depends(get_current_user, required_scopes=["admin"])):
    Users.insert({'name': name, 'password': bcrypt.hash(password)})
    return True