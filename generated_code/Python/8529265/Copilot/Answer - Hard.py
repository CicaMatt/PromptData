import pyotp

# Secret key (base32 encoded)
secret = 'JBSWY3DPEHPK3PXP'

# Generate HOTP
hotp = pyotp.HOTP(secret)
counter = 1
print("HOTP:", hotp.at(counter))

# Generate TOTP
totp = pyotp.TOTP(secret)
print("TOTP:", totp.now())
