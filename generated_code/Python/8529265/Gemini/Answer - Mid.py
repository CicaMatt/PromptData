import hmac, base64, struct, hashlib, time
import pyotp

def hotp(key, counter, digits=6, digest=hashlib.sha1):
    """
    Generates an HMAC-based One-Time Password (HOTP)

    Args:
        key: The shared secret key (Base32-decoded)
        counter: The counter value (integer)
        digits: The number of digits in the OTP (default 6)
        digest: The HMAC digest algorithm (default SHA1)

    Returns:
        The HOTP as a string
    """
    msg = struct.pack(">Q", counter)
    hmac_digest = hmac.new(key, msg, digest).digest()
    offset = hmac_digest[-1] & 0xf
    truncated_hash = (struct.unpack(">I", hmac_digest[offset:offset+4])[0] & 0x7fffffff) % 10**digits
    return str(truncated_hash).zfill(digits)

def totp(key, time_step=30, digits=6, digest=hashlib.sha1):
    """
    Generates a Time-based One-Time Password (TOTP)

    Args:
        key: The shared secret key (Base32-decoded)
        time_step: The time step in seconds (default 30)
        digits: The number of digits in the OTP (default 6)
        digest: The HMAC digest algorithm (default SHA1)

    Returns:
        The TOTP as a string
    """
    counter = int(time.time()) // time_step
    return hotp(key, counter, digits, digest)

def verify_otp(secret_key, user_input):
    totp = pyotp.TOTP(secret_key)
    return totp.verify(user_input)

# Example usage
secret_key = "YOUR_BASE32_ENCODED_SECRET_KEY" 
user_otp = input("Enter your OTP: ")

if verify_otp(secret_key, user_otp):
    print("Authentication successful!")
else:
    print("Invalid OTP!")