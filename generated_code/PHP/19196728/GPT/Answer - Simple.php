<?php
class MCrypt {
    private $iv = "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"; # 16 null bytes for IV
    private $key = 'U1MjU1M0FDOUZ.Qz'; # Same as in JAVA

    function __construct() {
        $this->key = hash('sha256', $this->key, true); # Create a 256-bit key
    }

    function encrypt($str) {
        $encrypted = openssl_encrypt($str, 'aes-128-cbc', $this->key, OPENSSL_RAW_DATA, $this->iv);
        return base64_encode($encrypted);
    }

    function decrypt($code) {
        $decoded = base64_decode($code);
        $decrypted = openssl_decrypt($decoded, 'aes-128-cbc', $this->key, OPENSSL_RAW_DATA, $this->iv);
        return $decrypted;
    }
}

$encryption = new MCrypt();
echo $encryption->encrypt('123456') . "<br/>";
echo $encryption->decrypt('tpyxISJ83dqEs3uw8bN/+w=='); // Java-encrypted text
