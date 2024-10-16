<?php
$theKey = "123412341234123412341234";
$theString = "username=test123";
$iv = "12345678"; 
$encrypted = openssl_encrypt($theString, 'DES-EDE3', $theKey, OPENSSL_RAW_DATA, $iv);
$strEncodedEnc = base64_encode($encrypted);
echo $strEncodedEnc;
