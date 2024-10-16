<?php
$data = file_get_contents('http://www.mywebservice');
if(!empty($data)) {
    $obj = json_decode($data);
    if (json_last_error() !== JSON_ERROR_NONE) {
        echo 'Error: ', json_last_error_msg(), "\n";
    } else {
        // Process the data...
    }
}