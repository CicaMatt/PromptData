import pyotp

# Generate a base32 secret
secret = pyotp.random_base32()

# Create a TOTP object
totp = pyotp.TOTP(secret)

# Generate a TOTP token
print("Current TOTP token:", totp.now())

# Create a HOTP object
hotp = pyotp.HOTP(secret)

# Generate a HOTP token
counter = 1
print("HOTP token at counter {}: {}".format(counter, hotp.at(counter)))

# Verify a TOTP token
token = totp.now()
print("Is the TOTP token valid?", totp.verify(token))

# Verify a HOTP token
print("Is the HOTP token valid?", hotp.verify(hotp.at(counter), counter))
