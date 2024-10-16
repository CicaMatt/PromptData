<?php

// Fetch the JSON data from the web service
$data = file_get_contents('http://www.mywebservice');

// Check if data is retrieved
if (!empty($data)) {

    // Optional: Clean BOM if present
    $data = preg_replace('/^\xEF\xBB\xBF/', '', $data);

    // Try to decode the JSON
    $obj = json_decode($data);

    // Check for errors in the JSON decoding process
    switch (json_last_error()) {
        case JSON_ERROR_NONE:
            echo ' - No errors, JSON parsed successfully';
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
            // Try to fix UTF-8 encoding issues
            $data = utf8_encode($data);
            $obj = json_decode($data);
            if (json_last_error() == JSON_ERROR_NONE) {
                echo ' - Fixed UTF-8 encoding and parsed successfully';
            } else {
                echo ' - Could not fix UTF-8 encoding';
            }
            break;
        default:
            echo ' - Unknown error';
            break;
    }

    // Display the decoded JSON (for debugging purposes)
    if ($obj !== null) {
        echo '<pre>';
        print_r($obj);
        echo '</pre>';
    } else {
        echo "\nFailed to decode JSON\n";
    }
} else {
    echo 'No data retrieved from web service';
}
?>
