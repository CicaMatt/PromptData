<?php
$key = "123412341234123412341234";
$string = "username=test123";
$des = new Crypt_TripleDES();
$enc = $des->encrypt($string, $key);
$enc = base64_encode($enc);
$enc = str_replace(array("\r", "\n"), '', $enc);
echo $enc;
?>