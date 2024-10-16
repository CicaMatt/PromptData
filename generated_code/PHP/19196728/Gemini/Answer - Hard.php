<?php
class MCrypt {
    private $iv = "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"; // 16 null bytes for IV
    private $key = 'U1MjU1M0FDOUZ.Qz';

    function __construct() {
        $this->key = hash('sha256', $this->key, true);
    }

    function encrypt($str) {
        $ciphertext = openssl_encrypt(
            $str, 
            'AES-128-CBC', 
            $this->key, 
            OPENSSL_RAW_DATA, 
            $this->iv
        );
        return base64_encode($ciphertext);
    }

    function decrypt($code) {
        $ciphertext = base64_decode($code);
        $plaintext = openssl_decrypt(
            $ciphertext, 
            'AES-128-CBC', 
            $this->key, 
            OPENSSL_RAW_DATA, 
            $this->iv
        );
        return $plaintext;
    }
}

$encryption = new MCrypt();
echo $encryption->encrypt('123456') . "<br/>"; 
echo $encryption->decrypt('tpyxISJ83dqEs3uw8bN/+w==');