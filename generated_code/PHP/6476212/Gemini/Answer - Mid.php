<?php
function downloadImageFromUrl($url, $savePath, $filename = 'photo1.jpg') {
    // Validate the URL to prevent malicious input
    if (!filter_var($url, FILTER_VALIDATE_URL)) {
        return false; // Or throw an exception, log the error, etc.
    }

    // Sanitize the filename to prevent path traversal attacks
    $filename = preg_replace('/[^a-zA-Z0-9_\-\.]/', '', $filename);

    // Initialize cURL
    $ch = curl_init();

    // Set cURL options
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); 

    // Follow redirects to handle potential URL changes
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);

    // Check for SSL certificate validity if using HTTPS
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, true);

    // Execute the cURL request
    $imageData = curl_exec($ch);

    // Check for cURL errors
    if (curl_errno($ch)) {
        // Log the error or handle it appropriately
        return false;
    }

    // Close the cURL handle
    curl_close($ch);

    // Create the full save path and ensure the directory exists
    $fullPath = $savePath . '/' . $filename;
    if (!is_dir($savePath)) {
        mkdir($savePath, 0755, true); 
    }

    // Save the image data to the file
    if (file_put_contents($fullPath, $imageData) !== false) {
        return true;
    } else {
        // Log the error or handle it appropriately
        return false;
    }
}

// Example usage
$imageUrl = 'https://www.example.com/image.jpg';
$saveDirectory = '/img/uploads'; 

if (downloadImageFromUrl($imageUrl, $saveDirectory)) {
    echo "Image downloaded successfully!";
} else {
    echo "Image download failed.";
}