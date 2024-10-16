<?php

class MCrypt {

    private $iv = "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"; // 16 null bytes
    private $key = 'U1MjU1M0FDOUZ.Qz';

    function __construct() {
        $this->key = hash('sha256', $this->key, true);
    }

    function encrypt($str) {
        return base64_encode(openssl_encrypt(
            $str, 
            'AES-128-CBC', 
            $this->key, 
            OPENSSL_RAW_DATA, 
            $this->iv
        ));
    }

    function decrypt($code) {
        return openssl_decrypt(
            base64_decode($code), 
            'AES-128-CBC', 
            $this->key, 
            OPENSSL_RAW_DATA, 
            $this->iv
        );
    }
}

$encryption = new MCrypt();
echo $encryption->encrypt('123456') . "<br/>";
echo $encryption->decrypt('tpyxISJ83dqEs3uw8bN/+w==');