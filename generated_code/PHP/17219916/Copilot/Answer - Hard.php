<?php
$data = file_get_contents('http://www.mywebservice');

if ($data === false) {
    die('Error fetching the JSON data.');
}

$obj = json_decode($data);

if (json_last_error() !== JSON_ERROR_NONE) {
    echo 'JSON Error: ' . json_last_error_msg();
    exit;
}

// Process your JSON object here
print_r($obj);
?>
