<?php

// Define dedicated exceptions for different error scenarios
class ImageDownloadException extends Exception {}
class InvalidDirectoryException extends Exception {}
class FileWriteException extends Exception {}

function GetImageFromUrl($link)
{
    // Initialize cURL
    $ch = curl_init();

    // Set cURL options
    curl_setopt($ch, CURLOPT_URL, $link);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true); // Follow redirects

    // Execute cURL request
    $result = curl_exec($ch);

    // Error handling for cURL
    if (curl_errno($ch)) {
        $error_msg = curl_error($ch);
        curl_close($ch);
        throw new ImageDownloadException("Error downloading image: " . $error_msg);
    }

    // Check if HTTP response code is OK (200)
    $http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    if ($http_code !== 200) {
        curl_close($ch);
        throw new ImageDownloadException("Image download failed with HTTP status code: " . $http_code);
    }

    curl_close($ch);
    return $result;
}

function saveImage($imageData, $savePath)
{
    // Ensure the folder exists and is writable
    $directory = dirname($savePath);
    if (!is_dir($directory)) {
        throw new InvalidDirectoryException("The directory does not exist: " . $directory);
    }
    if (!is_writable($directory)) {
        throw new InvalidDirectoryException("The directory is not writable: " . $directory);
    }

    // Open the file for writing
    $savefile = fopen($savePath, 'w');
    if (!$savefile) {
        throw new FileWriteException("Failed to open file for writing: " . $savePath);
    }

    // Write the image data to the file
    if (fwrite($savefile, $imageData) === false) {
        fclose($savefile);
        throw new FileWriteException("Failed to write image data to file: " . $savePath);
    }

    // Close the file
    fclose($savefile);
}

try {
    // URL of the image to download (to replace)
    $imageUrl = 'https://example.com/image.jpg';  

    // Image filename and save path (to replace)
    $iconFilename = 'photo1.jpg';  
    $savePath = __DIR__ . '/img/uploads/' . basename($iconFilename);  

    // Get image from URL
    $imageData = GetImageFromUrl($imageUrl);

    // Save the image to the server
    saveImage($imageData, $savePath);

    echo "Image downloaded and saved successfully.";
} catch (ImageDownloadException $e) {
    // Handle image download errors
    echo "Image Download Error: " . $e->getMessage();
} catch (InvalidDirectoryException $e) {
    // Handle invalid directory errors
    echo "Directory Error: " . $e->getMessage();
} catch (FileWriteException $e) {
    // Handle file writing errors
    echo "File Write Error: " . $e->getMessage();
}
