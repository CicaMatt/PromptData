from jose import jwt, JWTError
from typing import Optional
from datetime import datetime, timedelta
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm, SecurityScopes
from fastapi import APIRouter, Depends, HTTPException, status
from tinydb import TinyDB, where
from tinydb import Query
from passlib.hash import bcrypt
from pydantic import BaseModel
from passlib.context import CryptContext

class TokenData(BaseModel):
    username: Optional[str] = None

class Token(BaseModel):
    access_token: str
    token_type: str

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
    user = Users.search(where('name') == username)
    if user:
        return user[0]

@router.post('/verif')
async def verify_user(name, password):
    user = Users.search(where('name') == name)
    print(user)
    if not user:
        return False
    passw = user[0]['password']
    if not bcrypt.verify(password, passw):
        return False
    return user

def create_access_token(data: dict, expires_delta: Optional[timedelta] = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.utcnow() + expires_delta
    else:
        expire = datetime.utcnow() + timedelta(minutes=1)
    to_encode.update({"exp": expire})
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

    # Assegna scopes in base al ruolo dell'utente
    scopes = ["read"]  
    if user.get("is_admin"):  
        scopes.append("write")

    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": form_data.username, "scopes": scopes},
        expires_delta=access_token_expires
    )
    return {"access_token": access_token, "token_type": "bearer"}

@router.get('/user/me', dependencies=[Depends(oauth2_scheme), Depends(SecurityScopes(scopes=["read"]))])
async def get_current_user(security_scopes: SecurityScopes, token: str = Depends(oauth2_scheme)):
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM], options={"verify_exp": True})
        username: str = payload.get("sub")
        if username is None:
            raise credentials_exception
        token_data = TokenData(username=username)
    except JWTError as e:
        raise credentials_exception from e

    user = Users.search(where('name') == token_data.username)
    if user is None:
        raise credentials_exception
    return user

@router.post('/user')
async def create_user(name, password):
    Users.insert({'name': name, 'password': bcrypt.hash(password)})
    return True
