<?php
class MCrypt {

    private $iv = '0000000000000000'; // Same as in JAVA (16 bytes IV)
    private $key = 'U1MjU1M0FDOUZ.Qz'; // Same as in JAVA

    function __construct() 
    {
        // Generate a 256-bit (32-byte) key, same as in Java using SHA-256
        $this->key = hash('sha256', $this->key, true);
    }

    function encrypt($str) 
    {
        // Use openssl_encrypt with AES-128-CBC and PKCS7 padding (which is the default)
        $encrypted = openssl_encrypt($str, 'AES-128-CBC', $this->key, OPENSSL_RAW_DATA, $this->iv);
        return base64_encode($encrypted); // Return base64 encoded encrypted string
    }

    function decrypt($code) 
    {
        // Decode the base64 string, then decrypt using AES-128-CBC
        $encrypted = base64_decode($code);
        $decrypted = openssl_decrypt($encrypted, 'AES-128-CBC', $this->key, OPENSSL_RAW_DATA, $this->iv);
        return $decrypted; // Return decrypted string
    }
}

$encryption = new MCrypt();

// Testing encryption and decryption
$encrypted = $encryption->encrypt('123456');
echo "Encrypted: " . $encrypted . "<br/>";

$decrypted = $encryption->decrypt($encrypted);
echo "Decrypted: " . $decrypted . "<br/>";

// Testing decryption of a known encrypted value (must match Java output)
echo "Decryption of provided ciphertext: " . $encryption->decrypt('tpyxISJ83dqEs3uw8bN/+w==') . "<br/>";
?>
