<?php
class AESCrypt {
    private $key;
    private $iv = '0000000000000000'; // Same IV as in Java, 16 bytes

    public function __construct($keySeed) 
    {
        // Generate a SHA-256 hash of the key, same as Java
        $this->key = substr(hash('sha256', $keySeed, true), 0, 32); // 256-bit key
    }

    // Encrypt function
    public function encrypt($plaintext) 
    {
        // PKCS7 padding
        $plaintext = $this->pkcs7Pad($plaintext, 16);

        // Encrypt using AES-128-CBC and return the base64 encoded result
        $ciphertext = openssl_encrypt($plaintext, 'aes-128-cbc', $this->key, OPENSSL_RAW_DATA, $this->iv);
        return base64_encode($ciphertext);
    }

    // Decrypt function
    public function decrypt($ciphertextBase64) 
    {
        // Decode the base64 encoded string
        $ciphertext = base64_decode($ciphertextBase64);

        // Decrypt using AES-128-CBC
        $decrypted = openssl_decrypt($ciphertext, 'aes-128-cbc', $this->key, OPENSSL_RAW_DATA, $this->iv);

        // Remove padding
        return $this->pkcs7Unpad($decrypted);
    }

    // PKCS7 padding
    private function pkcs7Pad($data, $blockSize) 
    {
        $pad = $blockSize - (strlen($data) % $blockSize);
        return $data . str_repeat(chr($pad), $pad);
    }

    // PKCS7 unpadding
    private function pkcs7Unpad($data) 
    {
        $pad = ord($data[strlen($data) - 1]);
        if ($pad > strlen($data)) {
            return false; 
        }
        return substr($data, 0, -$pad);
    }
}

// Example usage
$encryption = new AESCrypt("U1MjU1M0FDOUZ.Qz"); 

// Encrypt
$encrypted = $encryption->encrypt('123456');
echo "Encrypted: " . $encrypted . "<br/>";

// Decrypt
$decrypted = $encryption->decrypt($encrypted);
echo "Decrypted: " . $decrypted . "<br/>";

