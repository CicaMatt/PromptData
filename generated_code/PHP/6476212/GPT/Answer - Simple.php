<?php
function GetImageFromUrl($link)
{
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $link);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1); // Ensures redirects are followed, which is often necessary for image URLs
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false); // Disable SSL verification if needed
    $result = curl_exec($ch);
    curl_close($ch);
    return $result;
}

$iticon = "https://example.com/path-to-image.jpg"; // URL of the image
$iconfilename = "photo1.jpg"; // The name you want to give to the image file

// Get image data
$sourcecode = GetImageFromUrl($iticon);

// Ensure the directory exists and is writable
$saveDir = __DIR__ . '/img/uploads/'; // Use __DIR__ to avoid leading spaces in the directory path
if (!file_exists($saveDir)) {
    mkdir($saveDir, 0777, true); // Create the directory if it doesn't exist
}

// Save the image file
$savefile = fopen($saveDir . $iconfilename, 'w');
if ($savefile) {
    fwrite($savefile, $sourcecode);
    fclose($savefile);
    echo "File saved successfully!";
} else {
    echo "Failed to open the file for writing.";
}
