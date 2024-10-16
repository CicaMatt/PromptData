<?php
// Load the necessary libraries
use Crypt\TripleDES;
use MIME\Base64;

// Set the key and string variables
$key = "123412341234123412341234";
$string = "username=test123";

// Create a new instance of TripleDES and set the encryption mode to ECB
$des = new TripleDES(TripleDES::MODE_ECB);

// Encrypt the string using the key
$enc = $des->encrypt3($string, $key);

// Encode the encrypted data as Base64
$enc = base64_encode($enc);

// Remove any line breaks from the encoded data
$enc = str_replace("\n", "", $enc);

echo "Resulting string: $enc\n";