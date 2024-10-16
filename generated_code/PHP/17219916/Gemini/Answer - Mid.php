<?php

$data = file_get_contents('http://www.mywebservice'); 

if (!empty($data)) {
    // Attempt to decode JSON
    $obj = json_decode($data);

    // Check for errors
    if (json_last_error() !== JSON_ERROR_NONE) {
        // If an older PHP version, try to provide more context
        if (function_exists('json_last_error_msg')) {
            $errorMsg = json_last_error_msg();
        } else {
            $errorMsg = "Unknown error. Consider upgrading PHP for more detailed error messages.";
        }

        // Output the error along with a portion of the problematic JSON
        $errorPos = json_last_error_pos(); 
        $snippetStart = max(0, $errorPos - 20); 
        $snippetEnd = min(strlen($data) - 1, $errorPos + 20); 

        echo "JSON Decoding Error: $errorMsg\n";
        echo "Near: " . substr($data, $snippetStart, $snippetEnd - $snippetStart) . "\n";

    } else {
        // JSON decoded successfully, proceed with further processing
        // ... your code to work with $obj ...
    }
} else {
    echo "Error fetching data from the webservice.";
}