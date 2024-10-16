<?php
class Cipher 
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

    private function getKey() 
    {
        return openssl_pbkdf2($this->passPhrase, $this->saltValue, $this->keySize / 8, $this->passwordIterations, $this->hashAlgorithm);
    }

    public function encrypt($plainText) {
        $key = $this->getKey();
        $cipherText = openssl_encrypt($plainText, 'aes-256-cbc', $key, OPENSSL_RAW_DATA, $this->initVector);
        return base64_encode($cipherText);
    }

    public function decrypt($cipherText) {
        $key = $this->getKey();
        $cipherText = base64_decode($cipherText);
        return openssl_decrypt($cipherText, 'aes-256-cbc', $key, OPENSSL_RAW_DATA, $this->initVector);
    }
}

$passPhrase = "passpharse";
$saltValue = "saltvalue";
$hashAlgorithm = "sha1";
$passwordIterations = 2;
$initVector = "1a2b3c4d5e6f7g8h";
$keySize = 256;

$cipher = new Cipher($passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $initVector, $keySize);

$plainText = "Text To Encrypt";
$encryptedText = $cipher->encrypt($plainText);
echo "Encrypted: $encryptedText\n";

$decryptedText = $cipher->decrypt($encryptedText);
echo "Decrypted: $decryptedText\n";
?>
