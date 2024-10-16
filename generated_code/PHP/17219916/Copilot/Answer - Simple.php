<?php
$data = file_get_contents('http://www.mywebservice');

if (!empty($data)) {
    $data = trim($data); // Remove any extraneous whitespace
    $data = utf8_encode($data); // Ensure the data is UTF-8 encoded

    $obj = json_decode($data);

    if (json_last_error() !== JSON_ERROR_NONE) {
        echo 'Error: ' . json_last_error_msg(); // More detailed error message if using PHP 5.5+
    } else {
        echo ' - JSON_ERROR_NONE';
    }
}
