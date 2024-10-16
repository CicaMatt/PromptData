<?php

function encryptInPhp($plainText) {
    $passPhrase = "passpharse";
    $saltValue = "saltvalue";
    $hashAlgorithm = "sha1";
    $passwordIterations = 2;
    $initVector = "1a2b3c4d5e6f7g8h";
    $keySize = 256;

    // Key derivation using PBKDF2 (mimicking C#'s PasswordDeriveBytes)
    $keyBytes = hash_pbkdf2($hashAlgorithm, $passPhrase, $saltValue, $passwordIterations, $keySize / 8, true);

    // Initialization Vector
    $ivBytes = $initVector;

    // Encryption using OpenSSL
    $cipherText = openssl_encrypt($plainText, 'AES-' . $keySize . '-CBC', $keyBytes, OPENSSL_RAW_DATA, $ivBytes);

    // Base64 encoding for safe transmission
    return base64_encode($cipherText);
}

// Example usage
$plainText = "Text To Encrypt";
$encryptedText = encryptInPhp($plainText);

echo "Encrypted Text: " . $encryptedText;