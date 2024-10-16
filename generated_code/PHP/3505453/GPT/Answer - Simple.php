<?php
// Parameters - must match the C# configuration
$passPhrase = "passpharse";
$saltValue = "saltvalue";
$hashAlgorithm = "sha1"; 
$passwordIterations = 2;
$initVector = "1a2b3c4d5e6f7g8h";
$keySize = 256; 

// Function to derive key using password and salt (similar to PasswordDeriveBytes in C#)
function deriveKey($passPhrase, $saltValue, $hashAlgorithm, $iterations, $keySize) {
    $saltBytes = $saltValue;
    $key = $passPhrase . $saltBytes;
    
    // Hash the passphrase with the salt for the specified number of iterations
    for ($i = 0; $i < $iterations; $i++) {
        $key = hash($hashAlgorithm, $key, true);
    }
    
    // Return the derived key truncated or padded to the correct key size
    return substr($key, 0, $keySize / 8);
}

// Encryption function
function encrypt($plainText, $passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $initVector, $keySize) {
    // Derive the key from the passphrase, salt, and iterations
    $key = deriveKey($passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $keySize);
    
    // Encrypt the data
    $cipherText = openssl_encrypt($plainText, 'aes-256-cbc', $key, OPENSSL_RAW_DATA, $initVector);
    
    // Encode the cipherText in base64 so it can be transmitted
    return base64_encode($cipherText);
}

// Decryption function
function decrypt($cTextBase64, $passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $initVector, $keySize) {
    // Derive the key
    $key = deriveKey($passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $keySize);
    
    // Decode the base64-encoded cipherText
    $cipherText = base64_decode($cTextBase64);
    
    // Decrypt the data
    return openssl_decrypt($cipherText, 'aes-256-cbc', $key, OPENSSL_RAW_DATA, $initVector);
}

// Test the encryption and decryption process
$plainText = "Text To Encrypt";
$encrText = encrypt($plainText, $passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $initVector, $keySize);
echo "Encrypted Text: $encrText\n";

$decrText = decrypt($encryptedText, $passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $initVector, $keySize);
echo "Decrypted Text: $decrText\n";
?>
