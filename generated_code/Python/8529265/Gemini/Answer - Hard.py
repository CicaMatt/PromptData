import pyotp
import base64

# Sample secret key (replace with your actual key from Google Authenticator)
secret_key = "JBSWY3DPEHPK3PXP"  # Base32 encoded secret

# Decode the secret key
decoded_key = base64.b32decode(secret_key)

# Generate HOTP
hotp = pyotp.HOTP(decoded_key)
hotp_password = hotp.at(0)  # Generate the first HOTP password (counter = 0)

# Generate TOTP
totp = pyotp.TOTP(decoded_key)
totp_password = totp.now()  # Generate the current TOTP password

print("HOTP Password:", hotp_password)
print("TOTP Password:", totp_password)