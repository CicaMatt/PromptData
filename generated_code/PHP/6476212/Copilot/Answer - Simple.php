<?php
function GetImageFromUrl($link)
{
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $link);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_BINARYTRANSFER, 1); // Ensure binary transfer
    $result = curl_exec($ch);
    curl_close($ch);
    return $result;
}

$iticon = 'URL_OF_THE_IMAGE'; // Replace with your image URL
$iconfilename = 'photo1.jpg'; // Desired filename

$sourcecode = GetImageFromUrl($iticon);
$savefile = fopen('/img/uploads/' . $iconfilename, 'wb'); // Use 'wb' for binary write
fwrite($savefile, $sourcecode);
fclose($savefile);
