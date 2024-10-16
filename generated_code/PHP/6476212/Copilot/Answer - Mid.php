<?php
function GetImageFromUrl($link)
{
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $link);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1); 
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 1); 
    $result = curl_exec($ch);
    
    if (curl_errno($ch)) {
        echo 'Error:' . curl_error($ch);
        curl_close($ch);
        return false;
    }
    
    curl_close($ch);
    return $result;
}

// Change accordingly
$iticon = 'https://example.com/image.jpg'; 
$iconfilename = 'photo1.jpg'; 
$savePath = __DIR__ . '/img/uploads/'; 

// Create directory if it doesn't exist
if (!file_exists($savePath)) {
    mkdir($savePath, 0755, true);
}

$sourcecode = GetImageFromUrl($iticon);

if ($sourcecode !== false) {
    $savefile = fopen($savePath . $iconfilename, 'w');
    if ($savefile) {
        fwrite($savefile, $sourcecode);
        fclose($savefile);
        echo 'Image saved successfully!';
    } else {
        echo 'Failed to open file for writing.';
    }
} else {
    echo 'Failed to download image.';
}
