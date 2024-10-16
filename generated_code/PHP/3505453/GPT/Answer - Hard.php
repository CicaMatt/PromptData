<?php

class AESCryptor
{
    private $passPhrase;
    private $saltValue;
    private $hashAlgorithm;
    private $passwordIterations;
    private $initVector;
    private $keySize;

    public function __construct($passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $initVector, $keySize)
    {
        $this->passPhrase = $passPhrase;
        $this->saltValue = $saltValue;
        $this->hashAlgorithm = $hashAlgorithm;
        $this->passwordIterations = $passwordIterations;
        $this->initVector = $initVector;
        $this->keySize = $keySize;
    }

    // Padding for block size
    private function pkcs7_pad($data, $blocksize)
    {
        $pad = $blocksize - (strlen($data) % $blocksize);
        return $data . str_repeat(chr($pad), $pad);
    }

    public function encrypt($plainText)
    {
        // Convert input data to bytes
        $initVectorBytes = $this->initVector;
        $saltValueBytes = $this->saltValue;

        // Derive key using PBKDF2 (Password-based key derivation)
        $key = hash_pbkdf2(
            $this->hashAlgorithm, 
            $this->passPhrase, 
            $saltValueBytes, 
            $this->passwordIterations, 
            $this->keySize / 8, 
            true
        );

        // Apply padding to the plaintext to make it a multiple of block size (16 bytes for AES)
        $paddedText = $this->pkcs7_pad($plainText, 16);

        // Encrypt using OpenSSL AES-256-CBC
        $encryptedText = openssl_encrypt(
            $paddedText, 
            'aes-256-cbc', 
            $key, 
            OPENSSL_RAW_DATA, 
            $initVectorBytes
        );

        // Return base64-encoded ciphertext
        return base64_encode($encryptedText);
    }

    public function decrypt($cipherText)
    {
        // Convert input data to bytes
        $initVectorBytes = $this->initVector;
        $saltValueBytes = $this->saltValue;

        // Derive key using PBKDF2 (Password-based key derivation)
        $key = hash_pbkdf2(
            $this->hashAlgorithm, 
            $this->passPhrase, 
            $saltValueBytes, 
            $this->passwordIterations, 
            $this->keySize / 8, 
            true
        );

        // Decrypt the ciphertext (it's base64-encoded)
        $decodedCipherText = base64_decode($cipherText);

        $decryptedText = openssl_decrypt(
            $decodedCipherText, 
            'aes-256-cbc', 
            $key, 
            OPENSSL_RAW_DATA, 
            $initVectorBytes
        );

        // Remove padding
        $pad = ord($decryptedText[strlen($decryptedText) - 1]);
        $decryptedText = substr($decryptedText, 0, -$pad);

        return $decryptedText;
    }
}

// Initialize parameters (must match with C# code)
$passPhrase = "passpharse";
$saltValue = "saltvalue";
$hashAlgorithm = "sha1";
$passwordIterations = 2;
$initVector = "1a2b3c4d5e6f7g8h";  
$keySize = 256;  // AES-256

// Create an instance of the AESCryptor class
$cryptor = new AESCryptor($passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $initVector, $keySize);

// Test the encryption and decryption
$plainText = "Text To Encrypt";
$encryptedText = $cryptor->encrypt($plainText);
echo "Encrypted Text: " . $encryptedText . PHP_EOL;

$decryptedText = $cryptor->decrypt($encryptedText);
echo "Decrypted Text: " . $decryptedText . PHP_EOL;

?>
