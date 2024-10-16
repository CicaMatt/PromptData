<?php
function pkcs5_pad($text, $blocksize) 
{
    $pad = $blocksize - (strlen($text) % $blocksize);
    return $text . str_repeat(chr($pad), $pad);
}

$theKey = "123412341234123412341234";
$theString = "username=test123";
$paddedString = pkcs5_pad($theString, 8); 
$strEncodedEnc = base64_encode(mcrypt_ecb(MCRYPT_3DES, $theKey, $paddedString, MCRYPT_ENCRYPT));

echo $strEncodedEnc; 
?>