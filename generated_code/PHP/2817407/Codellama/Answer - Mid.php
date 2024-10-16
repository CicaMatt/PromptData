<?php
$key = "123412341234123412341234";
$string = "username=test123";

// Use mcrypt to encrypt the string using the TripleDES algorithm
$encrypted = mcrypt_ecb(MCRYPT_3DES, $key, $string, MCRYPT_ENCRYPT);

// Base64 encode the encrypted string
$encoded = base64_encode($encrypted);

echo "Resulting string: $encoded\n";
?>