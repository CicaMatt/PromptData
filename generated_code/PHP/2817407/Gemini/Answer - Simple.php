<?php
$key = "123412341234123412341234";
$data = "username=test123";

// Ensure key is 24 bytes
if (strlen($key) == 16) {
    $key .= substr($key, 0, 8); 
}

$encrypted = openssl_encrypt($data, 'des-ede3-ecb', $key, OPENSSL_RAW_DATA | OPENSSL_ZERO_PADDING);
$encoded = base64_encode($encrypted);

echo $encoded;