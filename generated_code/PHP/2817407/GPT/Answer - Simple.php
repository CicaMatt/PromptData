<?php
$theKey = "123412341234123412341234";
$theString = "username=test123";

// Ensure PKCS7 padding
$blockSize = 8; 
$padding = $blockSize - (strlen($theString) % $blockSize);
$theString .= str_repeat(chr($padding), $padding);

// Encrypt using TripleDES in ECB mode
$encryptedString = openssl_encrypt($theString, 'des-ede3', $theKey, OPENSSL_RAW_DATA | OPENSSL_NO_PADDING);

// Base64 encode the result
$strEncodedEnc = base64_encode($encryptedString);

echo $strEncodedEnc;
