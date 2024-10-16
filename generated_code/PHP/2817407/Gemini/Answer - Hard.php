<?php
/* Modified PHP Code */
$theKey = "123412341234123412341234";
$theString = "username=test123";

// Ensure the key is 24 bytes long
$theKey = substr(str_pad($theKey, 24, "\0"), 0, 24);

$strEncoded = base64_encode(openssl_encrypt($theString, 'DES-EDE3', $theKey, OPENSSL_RAW_DATA | OPENSSL_ZERO_PADDING));

echo $strEncoded;
