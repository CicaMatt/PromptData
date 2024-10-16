import pyotp

def get_token(secret):
    hotp = pyotp.HOTP(secret, digits=6)
    totp = pyotp.TOTP(secret, interval=30)
    return (hotp.at(int(time.time()) // 30), totp.now())

def get_token(secret):
    totp = pyotp.TOTP(secret, interval=30)
    return totp.now()
