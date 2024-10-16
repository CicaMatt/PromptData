<?php
class MCrypt {
    private $iv;
    // Same as in JAVA
    private $key = 'U1MjU1M0FDOUZ.Qz';

    function __construct() {
        // Initialize IV as a string of 16 zero bytes instead of an array for better compatibility
        $this->iv = str_repeat(chr(0), 16);
        // Generate a SHA-256 key
        $this->key = hash('sha256', $this->key, true);
    }

    function encrypt($str) {
        // Open the cipher module for AES-128 CBC mode
        $td = mcrypt_module_open('rijndael-128', '', 'cbc', '');
        mcrypt_generic_init($td, $this->key, $this->iv);

        // Apply PKCS7-like padding
        $block = mcrypt_get_block_size(MCRYPT_RIJNDAEL_128, MCRYPT_MODE_CBC);
        $str = $this->addPadding($str, $block);

        // Encrypt the string and close the cipher
        $encrypted = mcrypt_generic($td, $str);
        mcrypt_generic_deinit($td);
        mcrypt_module_close($td);

        // Return base64-encoded encrypted string
        return base64_encode($encrypted);
    }

    function decrypt($code) {
        // Open the cipher module for AES-128 CBC mode
        $td = mcrypt_module_open('rijndael-128', '', 'cbc', '');
        mcrypt_generic_init($td, $this->key, $this->iv);

        // Decode the base64 encoded string and decrypt it
        $decrypted = mdecrypt_generic($td, base64_decode($code));
        mcrypt_generic_deinit($td);
        mcrypt_module_close($td);

        // Remove padding from decrypted string
        return $this->stripPadding($decrypted);
    }

    // Improved padding function
    private function addPadding($string, $blocksize = 16) {
        $pad = $blocksize - (strlen($string) % $blocksize);
        return $string . str_repeat(chr($pad), $pad);
    }

    // Improved padding stripping function
    private function stripPadding($string) {
        $pad = ord(substr($string, -1));
        if ($pad > 0 && $pad <= 16) {
            return substr($string, 0, -$pad);
        }
        return $string; // Return the original string if padding is invalid
    }
}

// Test encryption and decryption
$encryption = new MCrypt();
echo "Encrypted: " . $encryption->encrypt('123456') . "<br/>";
echo "Decrypted: " . $encryption->decrypt('tpyxISJ83dqEs3uw8bN/+w==') . "<br/>";
?>
