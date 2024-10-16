from datetime import datetime, timedelta
from typing import Optional, List

from fastapi import APIRouter, Depends, HTTPException, status, Request
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm, SecurityScopes
from jose import JWTError, jwt

# Importa o definisci queste classi e variabili secondo la tua implementazione
# from your_app.models import TokenData, User  # Assicurati che TokenData e User siano definiti
# from your_app.database import Users  # Assicurati che Users abbia il metodo search
# from your_app.utils import verify_user  # Funzione per verificare l'utente

SECRET_KEY = "your-secret-key"  # Dovresti usare una chiave segreta sicura
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

router = APIRouter()

class TokenData:
    def __init__(self, username: str = None, scopes: List[str] = None):
        self.username = username
        self.scopes = scopes or []

# Funzione per verificare l'utente (placeholder)
async def verify_user(username: str, password: str):
    # Implementa la logica per verificare l'utente
    # Restituisci l'oggetto utente se valido, altrimenti None
    pass

@router.post("/token")
async def token_generate(form_data: OAuth2PasswordRequestForm = Depends()):
    user = await verify_user(form_data.username, form_data.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Nome utente o password incorretti",
            headers={"WWW-Authenticate": "Bearer"},
        )
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": form_data.username, "scopes": ["user"]},
        expires_delta=access_token_expires
    )
    return {"access_token": access_token, "token_type": "bearer"}

def create_access_token(data: dict, expires_delta: Optional[timedelta] = None):
    """
    Crea un token JWT per l'autenticazione.
    """
    to_encode = data.copy()
    expire = datetime.utcnow() + (expires_delta or timedelta(minutes=15))
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt

async def get_current_user(
    security_scopes: SecurityScopes,
    token: str = Depends(oauth2_scheme)
):
    """
    Ottiene l'utente corrente in base al token JWT fornito.
    """
    if not security_scopes.scopes:
        security_scopes.scopes = []
    authenticate_value = f'Bearer scope="{security_scopes.scope_str}"'
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Impossibile validare le credenziali",
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

    # Implementa la logica per recuperare l'utente dal database
    user = Users.search(where('name') == token_data.username)
    if not user:
        raise credentials_exception

    # Controlla se l'utente ha i permessi necessari
    for scope in security_scopes.scopes:
        if scope not in token_data.scopes:
            raise HTTPException(
                status_code=status.HTTP_403_FORBIDDEN,
                detail="Permessi insufficienti",
                headers={"WWW-Authenticate": authenticate_value},
            )
    return user

@router.get('/user/me')
async def read_users_me(current_user: User = Depends(get_current_user)):
    """
    Endpoint per ottenere i dettagli dell'utente corrente.
    """
    return current_user

@router.get('/admin-only')
async def admin_only_route(current_user: User = Depends(get_current_user)):
    """
    Endpoint accessibile solo agli amministratori.
    """
    # Verifica che l'utente abbia il ruolo di amministratore
    if "admin" not in current_user.scopes:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="Permessi insufficienti",
        )
    return {"message": "Benvenuto, amministratore!"}
