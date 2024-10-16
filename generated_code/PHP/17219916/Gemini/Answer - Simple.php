<?php

$data = file_get_contents('http://www.mywebservice');

if (!empty($data)) {
    // Decode JSON with error reporting enabled
    json_decode($data);

    if (json_last_error() !== JSON_ERROR_NONE) {
        // If there's an error, fetch the detailed error message
        $errorMessage = json_last_error_msg();

        // Find the approximate location of the error
        $errorPosition = strpos($data, substr($data, json_last_error_pos()));

        // Display the error message and position
        echo "JSON Error: $errorMessage\n";
        echo "Approximate Error Position: $errorPosition\n";

        // Optionally, print a portion of the JSON around the error for further inspection
        $contextStart = max(0, $errorPosition - 50);
        $contextEnd = min(strlen($data), $errorPosition + 50);
        echo "Context:\n" . substr($data, $contextStart, $contextEnd - $contextStart) . "\n";
    } else {
        // If no errors, proceed with your usual processing
        $obj = json_decode($data);
        // ... your code here
    }
}