<?php

// Fetch the JSON data from the web service
$data = file_get_contents('http://www.mywebservice');

if ($data === false) {
    die('Error: Unable to fetch data from the web service.');
}

// Ensure that the data is not empty
if (!empty($data)) {
    // Convert encoding to UTF-8 if it's not already
    $data = mb_convert_encoding($data, 'UTF-8', 'UTF-8');

    // Decode the JSON data
    $obj = json_decode($data);

    // Check for JSON decoding errors
    switch (json_last_error()) {
        case JSON_ERROR_NONE:
            echo ' - No errors';
            break;
        case JSON_ERROR_DEPTH:
            echo ' - Maximum stack depth exceeded';
            break;
        case JSON_ERROR_STATE_MISMATCH:
            echo ' - Underflow or the modes mismatch';
            break;
        case JSON_ERROR_CTRL_CHAR:
            echo ' - Unexpected control character found';
            break;
        case JSON_ERROR_SYNTAX:
            echo ' - Syntax error, malformed JSON';
            break;
        case JSON_ERROR_UTF8:
            echo ' - Malformed UTF-8 characters, possibly incorrectly encoded';
            // Try to re-encode the data to UTF-8 and attempt decoding again
            $data = utf8_encode($data);
            $obj = json_decode($data);
            if (json_last_error() === JSON_ERROR_NONE) {
                echo ' - Successfully fixed malformed UTF-8 characters and decoded the JSON';
            } else {
                echo ' - Still encountering an error after attempting to fix UTF-8 encoding';
            }
            break;
        default:
            echo ' - Unknown error';
            break;
    }

    // If you want a more detailed error message (PHP 5.5+)
    if (function_exists('json_last_error_msg')) {
        echo ' - Error message: ' . json_last_error_msg();
    }

} else {
    die('Error: Received empty response from the web service.');
}

?>
