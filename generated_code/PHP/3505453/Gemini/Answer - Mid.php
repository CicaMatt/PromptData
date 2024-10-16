<?php

$plainText = "Text To Encrypt"; 
$passPhrase = "passpharse";
$saltValue = "saltvalue";
$hashAlgorithm = "sha1";
$passwordIterations = 2;
$initVector = "1a2b3c4d5e6f7g8h";
$keySize = 256;

// Derive the key using PBKDF2 (similar to C#'s PasswordDeriveBytes)
$keyBytes = hash_pbkdf2($hashAlgorithm, $passPhrase, $saltValue, $passwordIterations, $keySize / 8, true);

// Initialize the cipher
$cipher = "aes-256-cbc"; 
$options = OPENSSL_RAW_DATA; 

// Perform the encryption
$encryptedData = openssl_encrypt($plainText, $cipher, $keyBytes, $options, $initVector);

// Encode the encrypted data to Base64 for transmission
$cipherText = base64_encode($encryptedData);

echo "Encrypted Text: " . $cipherText;