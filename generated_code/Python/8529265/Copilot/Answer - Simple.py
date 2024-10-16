import pyotp

# Secret key (base32 encoded)
secret = 'JBSWY3DPEHPK3PXP'

# Generate HOTP
hotp = pyotp.HOTP(secret)
hotp_token = hotp.at(1)  # Counter value is 1
print(f"HOTP Token: {hotp_token}")

# Generate TOTP
totp = pyotp.TOTP(secret)
totp_token = totp.now()  # Current time-based token
print(f"TOTP Token: {totp_token}")
