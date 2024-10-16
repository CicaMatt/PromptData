import hmac
import base64
import struct
import hashlib
import time

# Helper function to calculate HOTP
def hotp(secret, counter, digits=6, digest_mode=hashlib.sha1):
    # Decode the base32 encoded secret
    key = base64.b32decode(secret, casefold=True)
    
    # Pack the counter into a byte array (64-bit unsigned integer in network byte order)
    msg = struct.pack(">Q", counter)
    
    # Create HMAC object with the secret key and counter message
    hmac_hash = hmac.new(key, msg, digest_mode).digest()
    
    # Use dynamic truncation to extract a 4-byte binary code from the HMAC result
    o = hmac_hash[-1] & 15  # Use the last nibble (4 bits) of the HMAC result as offset
    binary_code = struct.unpack(">I", hmac_hash[o:o+4])[0] & 0x7fffffff  # Mask most significant bit
    
    # Generate the OTP by reducing the binary code to the desired number of digits
    otp = binary_code % (10 ** digits)
    
    # Pad the result if necessary to ensure it's of the correct length
    return str(otp).zfill(digits)

# Helper function to calculate TOTP
def totp(secret, time_step=30, digits=6, digest_mode=hashlib.sha1):
    # Get the current Unix time in seconds and calculate the current counter value
    current_time = int(time.time())
    counter = current_time // time_step
    
    # Call HOTP with the current counter to generate a time-based OTP
    return hotp(secret, counter, digits, digest_mode)

# Example usage:
# The secret should be a base32 encoded string (case insensitive).
# Example: 'JBSWY3DPEHPK3PXP' (it's the key from Google Authenticator setup)
secret = 'JBSWY3DPEHPK3PXP'

# Generate HOTP (HMAC-based OTP) for a given counter
hotp_otp = hotp(secret, counter=1)
print(f"HOTP: {hotp_otp}")

# Generate TOTP (Time-based OTP), which changes every 30 seconds
totp_otp = totp(secret)
print(f"TOTP: {totp_otp}")
