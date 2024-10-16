<?php
$plainText = "Text To Encrypt";
$passPhrase = "passpharse";
$saltValue = "saltvalue";
$hashAlgorithm = "SHA1";
$passwordIterations = 2;
$initVector = "1a2b3c4d5e6f7g8h";
$keySize = 256;

function encrypt($plainText, $passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $initVector, $keySize) {
    $saltValueBytes = $saltValue;
    $initVectorBytes = $initVector;

    // Derive the key
    $key = hash_pbkdf2($hashAlgorithm, $passPhrase, $saltValueBytes, $passwordIterations, $keySize / 8, true);

    // Encrypt the data
    $cipherText = openssl_encrypt($plainText, 'aes-256-cbc', $key, OPENSSL_RAW_DATA, $initVectorBytes);

    // Encode the cipher text to base64
    return base64_encode($cipherText);
}

$encryptedText = encrypt($plainText, $passPhrase, $saltValue, $hashAlgorithm, $passwordIterations, $initVector, $keySize);
echo "Encrypted Text: " . $encryptedText;
?>
