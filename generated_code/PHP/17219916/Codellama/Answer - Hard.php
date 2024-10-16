<?php

// Load the JSON data from the webservice
$data = file_get_contents('http://www.mywebservice');

// Check if there were any errors during JSON parsing
if (json_last_error() !== JSON_ERROR_NONE) {
    // Get the error message and display it to the user
    $errorMessage = json_last_error_msg();
    echo "Error parsing JSON: {$errorMessage}";
} else {
    // Parse the JSON data into an object
    $obj = json_decode($data);

    // Check if there were any syntax errors in the JSON data
    if (json_last_error() !== JSON_ERROR_NONE) {
        // Get the error message and display it to the user
        $errorMessage = json_last_error_msg();
        echo "Error parsing JSON: {$errorMessage}";
    } else {
        // Do something with the parsed data, such as displaying it on the page
        echo "<h1>{$obj->title}</h1>";
    }
}