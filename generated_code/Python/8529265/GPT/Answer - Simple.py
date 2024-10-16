import hmac
import base64
import struct
import hashlib
import time


def hotp(secret, counter, digits=6, digest=hashlib.sha1):
    """
    Generate an HOTP value as specified in RFC 4226.
    :param secret: The shared base32-encoded secret.
    :param counter: The counter value (increments with each call).
    :param digits: The number of digits for the OTP (default: 6).
    :param digest: The hashing algorithm (default: SHA1).
    :return: The HOTP value.
    """
    # Decode the base32 secret to bytes
    key = base64.b32decode(secret, casefold=True)
    
    # Pack the counter into a binary format, as a big-endian 64-bit integer
    counter_bytes = struct.pack(">Q", counter)
    
    # Create the HMAC hash using the secret key and counter
    hmac_hash = hmac.new(key, counter_bytes, digest).digest()
    
    # Get the dynamic offset from the last nibble of the hash
    offset = hmac_hash[-1] & 0x0F
    
    # Extract a 4-byte section starting at the offset
    truncated_hash = hmac_hash[offset:offset + 4]
    
    # Convert the truncated hash to an integer
    code = struct.unpack(">I", truncated_hash)[0] & 0x7FFFFFFF
    
    # Modulo by 10^digits to get the final OTP
    return code % (10 ** digits)


def totp(secret, time_step=30, digits=6, digest=hashlib.sha1):
    """
    Generate a TOTP value as specified in RFC 6238.
    :param secret: The shared base32-encoded secret.
    :param time_step: The time step in seconds (default: 30).
    :param digits: The number of digits for the OTP (default: 6).
    :param digest: The hashing algorithm (default: SHA1).
    :return: The TOTP value.
    """
    # Get the current Unix time and divide it by the time step
    current_time = int(time.time()) // time_step
    
    # HOTP can be reused with the current time as the counter
    return hotp(secret, current_time, digits, digest)


# Example usage
if __name__ == "__main__":
    # Use your base32-encoded secret shared with Google Authenticator
    secret = "JBSWY3DPEHPK3PXP"  # Example secret, replace with your own
    
    # Generate a TOTP (Time-based OTP)
    totp_value = totp(secret)
    print(f"TOTP: {totp_value}")
    
    # Generate an HOTP (Counter-based OTP)
    counter = 1  # Replace with actual counter value for HOTP
    hotp_value = hotp(secret, counter)
    print(f"HOTP: {hotp_value}")
