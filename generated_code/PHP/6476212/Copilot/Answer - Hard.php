<?php

function getImageFromUrl($url, $savePath) {
    // Initialize cURL session
    $ch = curl_init($url);
    
    // Set cURL options
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true); 
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, true); 
    
    // Execute cURL session
    $data = curl_exec($ch);
    
    // Check for cURL errors
    if (curl_errno($ch)) {
        echo 'cURL error: ' . curl_error($ch);
        curl_close($ch);
        return false;
    }
    
    // Close cURL session
    curl_close($ch);
    
    // Ensure the save directory exists
    $directory = dirname($savePath);
    if (!is_dir($directory)) {
        mkdir($directory, 0755, true);
    }
    
    // Save the image to the specified path
    if (file_put_contents($savePath, $data)) {
        echo "Image successfully saved to $savePath";
        return true;
    } else {
        echo "Failed to save the image.";
        return false;
    }
}

// Example usage
$url = "https://example.com/image.jpg";
$savePath = "/img/uploads/photo1.jpg";
getImageFromUrl($url, $savePath);

?>
