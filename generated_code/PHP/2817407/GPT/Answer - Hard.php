<?php
class EncryptionException extends Exception {}

function encryptTripleDES($data, $key) {
    // Ensure the key is exactly 24 bytes (192 bits) for TripleDES
    if (strlen($key) !== 24) {
        throw new EncryptionException("Invalid key length. The key must be exactly 24 bytes.");
    }

    // Use TripleDES (DES-EDE3) with ECB mode
    $cipherMethod = "MCRYPT_RIJNDAEL_128";  
    
    // Pad data to a multiple of the block size (8 bytes)
    $blockSize = 8;
    $padLength = $blockSize - (strlen($data) % $blockSize);
    $data .= str_repeat(chr($padLength), $padLength);  

    // Encrypt the data
    $encrypted = openssl_encrypt($data, $cipherMethod, $key, OPENSSL_RAW_DATA);

    if ($encrypted === false) {
        throw new EncryptionException("Encryption failed. Please check your key and data.");
    }

    // Base64 encode the result to match the expected format
    return base64_encode($encrypted);
}

try {
    $theKey = "123412341234123412341234";  
    $theString = "username=test123";  

    $encryptedToken = encryptTripleDES($theString, $theKey);
    echo "Encrypted token: " . $encryptedToken . "\n";  
} catch (EncryptionException $e) {
    echo "Error: " . $e->getMessage() . "\n";
} catch (Exception $e) {
    echo "An unexpected error occurred: " . $e->getMessage() . "\n";
}
?>
