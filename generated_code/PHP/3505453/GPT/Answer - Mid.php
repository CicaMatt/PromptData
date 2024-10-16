<?php
// Configuration values
$plainText = "Text To Encrypt"; 
$passPhrase = "passpharse";      
$saltValue = "saltvalue";        
$hashAlgorithm = "SHA1";         
$passwordIterations = 2;         
$initVector = "1a2b3c4d5e6f7g8h";
$keySize = 256;                  

// Function to perform encryption similar to the C# code
function encrypt($plainText, $passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $initVector, $keySize) {
    // Derive the key from the passphrase and salt using the same algorithm and iterations
    $saltBytes = utf8_encode($saltValue);
    $key = openssl_pbkdf2($passPhrase, $saltBytes, $keySize / 8, $passwordIterations, $hashAlgorithm);

    // Encrypt using AES-256-CBC (equivalent to RijndaelManaged with a 256-bit key in CBC mode)
    $encrypted = openssl_encrypt($plainText, "aes-256-cbc", $key, OPENSSL_RAW_DATA, $initVector);

    // Return the base64 encoded encrypted text
    return base64_encode($encrypted);
}

// Function to perform decryption
function decrypt($cipherText, $passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $initVector, $keySize) {
    // Derive the key from the passphrase and salt using the same algorithm and iterations
    $saltBytes = utf8_encode($saltValue);
    $key = openssl_pbkdf2($passPhrase, $saltBytes, $keySize / 8, $passwordIterations, $hashAlgorithm);

    // Decrypt using AES-256-CBC
    $decrypted = openssl_decrypt(base64_decode($cipherText), "aes-256-cbc", $key, OPENSSL_RAW_DATA, $initVector);

    return $decrypted;
}

// Encrypt the text
$encrText = encrypt($plainText, $passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $initVector, $keySize);
echo "Encrypted: " . $encrText . "\n";

// Decrypt the text to verify
$decrText = decrypt($encryptedText, $passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $initVector, $keySize);
echo "Decrypted: " . $decrText . "\n";
?>
