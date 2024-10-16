<?php
function GetImageFromUrl($link)
{
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $link);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_BINARYTRANSFER, 1); // Crucial for binary data (images)
    $result = curl_exec($ch);
    curl_close($ch);
    return $result;
}

$sourcecode = GetImageFromUrl($iticon); 

// Ensure the directory exists
$saveDirectory = '/img/uploads/';
if (!is_dir($saveDirectory)) {
    mkdir($saveDirectory, 0755, true); // Create directory recursively if needed
}

$savefile = fopen($saveDirectory . 'photo1.jpg', 'wb'); // 'wb' for binary write
fwrite($savefile, $sourcecode);
fclose($savefile);