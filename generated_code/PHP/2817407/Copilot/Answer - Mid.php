<?php
$theKey = "123412341234123412341234";
$theString = "username=test123";

// Ensure the key is 24 bytes for TripleDES
$theKey = substr(hash('sha256', $theKey, true), 0, 24);

// Encrypt using TripleDES in ECB mode with PKCS7 padding
$encrypted = openssl_encrypt($theString, 'DES-EDE3', $theKey, OPENSSL_RAW_DATA | OPENSSL_ZERO_PADDING);

// Base64 encode the result
$strEncodedEnc = base64_encode($encrypted);

echo $strEncodedEnc;
