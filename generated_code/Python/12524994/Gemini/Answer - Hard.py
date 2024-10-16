from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.Util.Padding import pad, unpad

def encrypt_message(message, key):
    """Encrypts a message using AES in GCM mode with a securely generated IV.

    Args:
        message (bytes): The message to be encrypted.
        key (bytes): The encryption key. It should be 16, 24, or 32 bytes long for AES-128, AES-192, or AES-256 respectively.

    Returns:
        tuple: A tuple containing the ciphertext, the authentication tag, and the nonce (IV).

    Raises:
        ValueError: If the key length is invalid.
    """

    key_length = len(key)
    if key_length not in (16, 24, 32):
        raise ValueError("Key must be 16, 24, or 32 bytes long")

    cipher = AES.new(key, AES.MODE_GCM)
    ciphertext, tag = cipher.encrypt_and_digest(pad(message, AES.block_size))
    return ciphertext, tag, cipher.nonce


def decrypt_message(ciphertext, tag, nonce, key):
    """Decrypts a message encrypted using AES in GCM mode.

    Args:
        ciphertext (bytes): The encrypted message.
        tag (bytes): The authentication tag generated during encryption.
        nonce (bytes): The nonce (IV) used during encryption.
        key (bytes): The decryption key. It should be the same key used for encryption.

    Returns:
        bytes: The decrypted message.

    Raises:
        ValueError: If the key length is invalid.
        ValueError: If the decryption fails due to an invalid tag or other issues.
    """

    key_length = len(key)
    if key_length not in (16, 24, 32):
        raise ValueError("Key must be 16, 24, or 32 bytes long")

    cipher = AES.new(key, AES.MODE_GCM, nonce=nonce)
    try:
        plaintext = unpad(cipher.decrypt_and_verify(ciphertext, tag), AES.block_size)
        return plaintext
    except ValueError:
        raise ValueError("Decryption failed. Ciphertext or tag may be corrupted.")