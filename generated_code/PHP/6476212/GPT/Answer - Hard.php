<?php

// Custom exception classes for specific errors

class InvalidURLException extends Exception {}
class CurlRequestException extends Exception {}
class HTTPStatusCodeException extends Exception {}
class DirectoryCreationException extends Exception {}
class FileWriteException extends Exception {}

function GetImageFromUrl($url)
{
    // Initialize CURL session
    $ch = curl_init();
    
    // Validate the URL format to ensure it's valid and safe
    if (!filter_var($url, FILTER_VALIDATE_URL)) {
        throw new InvalidURLException('Invalid URL format: ' . $url);
    }

    // Set CURL options
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true); 

    // Execute CURL request
    $result = curl_exec($ch);
    
    // Check for CURL errors
    if (curl_errno($ch)) {
        $error_msg = curl_error($ch);
        curl_close($ch);
        throw new CurlRequestException("CURL error: " . $error_msg);
    }

    // Get the HTTP response code and ensure it's 200 (OK)
    $http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    if ($http_code !== 200) {
        curl_close($ch);
        throw new HTTPStatusCodeException("Failed to download image. HTTP Status Code: " . $http_code);
    }

    // Close CURL session
    curl_close($ch);

    return $result;
}

function SaveImageToFile($imageData, $destinationPath)
{
    // Create the directory if it doesn't exist
    $directory = dirname($destinationPath);
    if (!is_dir($directory)) {
        if (!mkdir($directory, 0755, true)) {
            throw new DirectoryCreationException('Failed to create directory: ' . $directory);
        }
    }

    // Validate file name to prevent directory traversal attacks
    if (preg_match('/[\/:*?"<>|]/', basename($destinationPath))) {
        throw new FileWriteException('Invalid characters in file name: ' . basename($destinationPath));
    }

    // Open file for writing (overwrite if exists)
    $savefile = fopen($destinationPath, 'wb');
    
    if (!$savefile) {
        throw new FileWriteException('Failed to open file for writing: ' . $destinationPath);
    }

    // Write the image data to the file
    if (fwrite($savefile, $imageData) === false) {
        fclose($savefile);
        throw new FileWriteException('Failed to write image data to file: ' . $destinationPath);
    }

    // Close file handle
    fclose($savefile);

    return true;
}

// Example usage
try {
    $imageUrl = 'https://example.com/path-to-image.jpg'; 
    $imageData = GetImageFromUrl($imageUrl);
    
    // Define the save path securely. Adjust as needed.
    $destinationPath = __DIR__ . '/img/uploads/photo1.jpg'; 
    
    SaveImageToFile($imageData, $destinationPath);

    echo "Image successfully saved to: " . $destinationPath;
} catch (InvalidURLException $e) {
    echo "URL Error: " . $e->getMessage();
} catch (CurlRequestException $e) {
    echo "CURL Error: " . $e->getMessage();
} catch (HTTPStatusCodeException $e) {
    echo "HTTP Error: " . $e->getMessage();
} catch (DirectoryCreationException $e) {
    echo "Directory Error: " . $e->getMessage();
} catch (FileWriteException $e) {
    echo "File Error: " . $e->getMessage();
} catch (Exception $e) {
    echo "General Error: " . $e->getMessage();
}

?>
