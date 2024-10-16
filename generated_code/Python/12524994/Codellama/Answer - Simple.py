import Crypto.Cipher.AES as AES
from Crypto.Random import get_random_bytes

def encrypt(message, key):
    iv = get_random_bytes(16)
    cipher = AES.new(key, AES.MODE_CBC, iv)
    encrypted_message = cipher.encrypt(message)
    return encrypted_message

def decrypt(encrypted_message, key):
    iv = get_random_bytes(16)
    cipher = AES.new(key, AES.MODE_CBC, iv)
    message = cipher.decrypt(encrypted_message)
    return message