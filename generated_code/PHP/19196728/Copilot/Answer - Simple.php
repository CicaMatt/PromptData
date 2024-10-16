<?php

class MCrypt {

    private $iv = '0000000000000000'; # Same as in JAVA              
    private $key = 'U1MjU1M0FDOUZ.Qz'; # Same as in JAVA

    function __construct() {
        $this->key = substr(hash('sha256', $this->key, true), 0, 32);
    }

    function encrypt($str) {
        $iv = $this->iv;
        $encrypted = openssl_encrypt($str, 'AES-128-CBC', $this->key, OPENSSL_RAW_DATA, $iv);
        return base64_encode($encrypted);
    }

    function decrypt($code) {
        $iv = $this->iv;
        $decrypted = openssl_decrypt(base64_decode($code), 'AES-128-CBC', $this->key, OPENSSL_RAW_DATA, $iv);
        return $decrypted;
    }
}

$encryption = new MCrypt();
echo $encryption->encrypt('123456') . "<br/>";
echo $encryption->decrypt('tpyxISJ83dqEs3uw8bN/+w==');   
?>
