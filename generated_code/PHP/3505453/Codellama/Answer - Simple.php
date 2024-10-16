<?php

class Crypto {
    private $m_plainText;
    private $m_passPhrase;
    private $m_saltValue;
    private $m_hashAlgorithm;
    private $m_passwordIterations;
    private $m_initVector;
    private $m_keySize;

    public function __construct($plainText, $passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $initVector, $keySize) {
        $this->m_plainText = $plainText;
        $this->m_passPhrase = $passPhrase;
        $this->m_saltValue = $saltValue;
        $this->m_hashAlgorithm = $hashAlgorithm;
        $this->m_passwordIterations = $passwordIterations;
        $this->m_initVector = $initVector;
        $this->m_keySize = $keySize;
    }

    public function Encrypt() {
        $plainText = $this->m_plainText;
        $passPhrase = $this->m_passPhrase;
        $saltValue = $this->m_saltValue;
        $hashAlgorithm = $this->m_hashAlgorithm;
        $passwordIterations = $this->m_passwordIterations;
        $initVector = $this->m_initVector;
        $keySize = $this->m_keySize;

        // Generate a salt value.
        $saltValueBytes = hash($hashAlgorithm, $saltValue, TRUE);

        // Derive the encryption key.
        $password = new PasswordDeriveBytes($passPhrase, $saltValueBytes, $hashAlgorithm, $passwordIterations);
        $keyBytes = $password->GetBytes($keySize / 8);

        // Encrypt the plain text using AES.
        $cipherText = openssl_encrypt($plainText, "AES-256-CBC", $keyBytes, OPENSSL_RAW_DATA, $initVector);

        // Return the encrypted text as a base 64 encoded string.
        return base64_encode($cipherText);
    }
}

// Create a new instance of the Crypto class.
$crypto = new Crypto("text to encrypt", "passpharse", "saltvalue", "SHA1", 2, "1a2b3c4d5e6f7g8h", 256);

// Encrypt the plain text using the instance of the Crypto class.
$encryptedText = $crypto->Encrypt();

// Print the encrypted text as a base 64 encoded string.
echo $encryptedText;