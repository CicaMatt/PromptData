import pyotp
import time
import base64

# Replace with your actual secret (base32 encoded)
secret = "YOUR_BASE32_SECRET"

# For HOTP
hotp = pyotp.HOTP(base64.b32decode(secret))
hotp_token = hotp.at(0)  # Replace 0 with the correct counter value

# For TOTP
totp = pyotp.TOTP(base64.b32decode(secret))
totp_token = totp.now()

print("HOTP Token:", hotp_token)
print("TOTP Token:", totp_token)