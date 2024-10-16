from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Util.Padding import pad, unpad

def encrypt(message, key):
    """Encrypts a message using AES in GCM mode.

    Args:
        message: The message to be encrypted (bytes).
        key: The encryption key (bytes). Must be 16, 24, or 32 bytes long.

    Returns:
        A tuple containing the ciphertext and the associated authentication tag.
    """

    cipher = AES.new(key, AES.MODE_GCM)
    ciphertext, tag = cipher.encrypt_and_digest(pad(message, AES.block_size))
    return (cipher.nonce, ciphertext, tag)

def decrypt(nonce, ciphertext, tag, key):
    """Decrypts a ciphertext using AES in GCM mode.

    Args:
        nonce: The nonce used during encryption (bytes).
        ciphertext: The encrypted message (bytes).
        tag: The authentication tag associated with the ciphertext (bytes).
        key: The decryption key (bytes). Must be the same as the encryption key.

    Returns:
        The decrypted message (bytes) or None if decryption fails.
    """

    cipher = AES.new(key, AES.MODE_GCM, nonce=nonce)
    try:
        plaintext = unpad(cipher.decrypt_and_verify(ciphertext, tag), AES.block_size)
        return plaintext
    except ValueError:
        # Decryption failed (likely due to an incorrect key or tampered data)
        return None