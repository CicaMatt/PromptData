<?php

class AESCrypt {

    private $iv = "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"; // 16 null bytes for the IV
    private $key = 'U1MjU1M0FDOUZ.Qz';

    function __construct() {
        $this->key = hash('sha256', $this->key, true);
    }

    function encrypt($str) {
        $ciphertext = openssl_encrypt($str, 'AES-128-CBC', $this->key, OPENSSL_RAW_DATA, $this->iv);
        return base64_encode($ciphertext);
    }

    function decrypt($code) {
        $ciphertext = base64_decode($code);
        return openssl_decrypt($ciphertext, 'AES-128-CBC', $this->key, OPENSSL_RAW_DATA, $this->iv);
    }

}

$encryption = new AESCrypt();
echo $encryption->encrypt('123456') . "<br/>";
$encryptedText = 'tpyxISJ83dqEs3uw8bN/+w=='; 
echo $encryption->decrypt($encryptedText);