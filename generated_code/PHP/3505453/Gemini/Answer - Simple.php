<?php

$HashPassPhrase = "passpharse";
$HashSalt = "saltvalue";
$HashAlgorithm = "SHA1";
$HashIterations = 2;
$InitVector = "1a2b3c4d5e6f7g8h";
$keySize = 256;

class Cipher 
{
    private $securekey, $iv;

    function __construct($textkey) 
    {
        // Replicate C#'s PasswordDeriveBytes 
        $saltValueBytes = $HashSalt;
        $derivedKey = '';
        $block = hash_hmac($HashAlgorithm, $saltValueBytes . $textkey, $textkey, true);
        for ($i = 1; $i < $HashIterations; $i++) {
            $block = hash_hmac($HashAlgorithm, $block, $textkey, true);
            $derivedKey .= $block;
        }
        $this->securekey = substr($derivedKey, 0, $keySize / 8);

        $this->iv = $InitVector;
    }

    function encrypt($input) {
        return base64_encode(openssl_encrypt($input, 'AES-256-CBC', $this->securekey, OPENSSL_RAW_DATA, $this->iv));
    }

    function decrypt($input) {
        return openssl_decrypt(base64_decode($input), 'AES-256-CBC', $this->securekey, OPENSSL_RAW_DATA, $this->iv);
    }
}

$cipher = new Cipher($HashPassPhrase);

$encryptedtext = $cipher->encrypt("Text To Encrypt");
echo "->encrypt = $encryptedtext<br />";

$decryptedtext = $cipher->decrypt($encryptedtext);
echo "->decrypt = $decryptedtext<br />";