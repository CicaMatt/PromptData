from pyotp import HOTP, TOTP

# Generate a HOTP token using the secret key "ABCD" and the current time as the counter value
hotp = HOTP("ABCD") # Example
token = hotp.generate()
print(token)

# Generate a TOTP token using the secret key "ABCD" and the current time as the counter value
totp = TOTP("ABCD") # Example
token = totp.generate()
print(token)