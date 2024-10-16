from datetime import datetime, timedelta
from fastapi import Depends, HTTPException, status, APIRouter
from fastapi.security import OAuth2PasswordRequestForm, SecurityScopes, OAuth2PasswordBearer
from jose import JWTError, jwt
from pydantic import BaseModel
from typing import Optional

# Simuliamo SECRET_KEY e ALGORITHM per JWT
SECRET_KEY = "your_secret_key"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30

# Creazione di un oggetto Token e TokenData come in .models
class Token(BaseModel):
    access_token: str
    token_type: str

class TokenData(BaseModel):
    username: Optional[str] = None

# Simuliamo una funzione per creare il token JWT come in .auth
def create_access_token(data: dict, expires_delta: Optional[timedelta] = None, scopes: Optional[list] = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.utcnow() + expires_delta
    else:
        expire = datetime.utcnow() + timedelta(minutes=15)
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt

# Simuliamo il sistema di autenticazione con OAuth2
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

# Simuliamo una funzione per verificare l'utente
async def verify_user(username: str, password: str):
    # Logica di verifica utente simulata
    if username == "testuser" and password == "testpassword":
        return {"username": username}
    return None

# Creiamo un router per le rotte API
router = APIRouter()

# Decode the token
token = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZSIsIm5vbmNlIjoiMjAxNjMyMDkxMjIwMDEiLCJpYXQiOjE1ODkyNTUzNDh9.8ZG9Rr4LZs56XW7Cf0TaH7DJ7_qeBV80KbA4Sv2-Ncw'
decoded_token = jwt.decode(token, verify=False)

# Extract the expiration date from the token
expiration_date = decoded_token.get('exp')

if expiration_date:
    print('Token will expire on:', datetime.utcfromtimestamp(expiration_date).isoformat())
else:
    print("Token does not contain expiration information.")

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
    scopes = ["read", "write"]  # Replace with the desired scopes
    access_token = create_access_token(data={"sub": form_data.username}, expires_delta=access_token_expires, scopes=scopes)
    return {"access_token": access_token, "token_type": "bearer"}

@router.get("/user/me", dependencies=[Depends(SecurityScopes(["read"]))])
async def get_current_user(token: str = Depends(oauth2_scheme)):
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        username: str = payload.get("sub")
        if username is None:
            raise credentials_exception
        token_data = TokenData(username=username)
    except JWTError:
        raise credentials_exception
    # Simuliamo la ricerca utente
    if token_data.username != "testuser":
        raise credentials_exception
    return {"username": token_data.username}
