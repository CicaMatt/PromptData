<?php
class MCrypt {
    // Same as in JAVA
    private $iv = '0000000000000000'; 
    private $key = 'U1MjU1M0FDOUZ.Qz'; 

    function __construct() {
        $this->key = hash('sha256', $this->key, true);
    }

    function encrypt($str) {
        $iv = str_repeat(chr(0), 16);
        $encrypted = openssl_encrypt($str, 'aes-256-cbc', $this->key, OPENSSL_RAW_DATA, $iv);
        return base64_encode($encrypted);
    }

    function decrypt($code) {
        $iv = str_repeat(chr(0), 16);
        $decrypted = openssl_decrypt(base64_decode($code), 'aes-256-cbc', $this->key, OPENSSL_RAW_DATA, $iv);
        return $decrypted;
    }
}

$encryption = new MCrypt();
echo $encryption->encrypt('123456') . "<br/>";
echo $encryption->decrypt('tpyxISJ83dqEs3uw8bN/+w==');
