<?php
$theKey = "123412341234123412341234";
$theString = "username=test123";

// Ensure key is properly formatted as 24 bytes
$theKey = substr(hash('sha256', $theKey, true), 0, 24);

// Encrypt with OpenSSL
$strEncodedEnc = openssl_encrypt($theString, 'des-ede3', $theKey, OPENSSL_RAW_DATA | OPENSSL_NO_PADDING, "");

// Base64 encode the result
$strEncodedEnc = base64_encode($strEncodedEnc);
echo $strEncodedEnc;  
// Expected output: AYOF+kRtg239Mnyc8QIarw==
