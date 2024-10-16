from pyotp import TOTP
from googleauthenticator import GoogleAuthenticator
import hmac
import base64

totp = TOTP(secret='your-secret-key', interval=30)
print(totp.now()) # prints the current TOTP

ga = GoogleAuthenticator(secret='your-secret-key')
print(ga.now()) # prints the current TOTP

def get_hotp(secret, counter):
    key = base64.b32decode(secret)
    msg = struct.pack(">Q", counter)
    h = hmac.new(key, msg, hashlib.sha1).digest()
    o = ord(h[19]) & 15
    h = (struct.unpack(">I", h[o:o+4])[0] & 0x7fffffff) % 1000000
    return h