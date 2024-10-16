<?php
function GetImageFromUrl($link, $savePath, $desiredFilename = 'photo1')
{
    // Sanitize the filename to prevent potential directory traversal attacks.
    $filename = pathinfo($link, PATHINFO_BASENAME);
    $filename = preg_replace('/[^a-zA-Z0-9_.-]/', '', $filename);
    $filename = $desiredFilename . '.' . pathinfo($filename, PATHINFO_EXTENSION);
    $fullPath = $savePath . '/' . $filename;

    // Ensure the save directory exists, creating it if necessary.
    if (!is_dir($savePath)) {
        mkdir($savePath, 0755, true); 
    }

    $fp = fopen($fullPath, 'w');

    if (!$fp) {
        return false; 
    }

    $ch = curl_init($link);

    // Set cURL options for security and efficiency.
    curl_setopt($ch, CURLOPT_FILE, $fp);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true); 
    curl_setopt($ch, CURLOPT_TIMEOUT, 30); 
    curl_setopt($ch, CURLOPT_USERAGENT, 'YourAppName/1.0'); 
    // Add more cURL options as needed for specific scenarios.

    curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);

    curl_close($ch);
    fclose($fp);

    // Check for successful download.
    if ($httpCode == 200) {
        return $fullPath; 
    } else {
        unlink($fullPath); 
        return false;
    }
}

// Example usage
$imageUrl = 'https://example.com/image.jpg';
$saveDirectory = '/img/uploads'; 
$savedFilePath = GetImageFromUrl($imageUrl, $saveDirectory);

if ($savedFilePath) {
    echo "Image saved successfully at: $savedFilePath";
} else {
    echo "Failed to download the image.";
}