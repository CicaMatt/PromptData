<?php
$data = file_get_contents('http://www.mywebservice');
if(!empty($data)) {
    $obj = json_decode($data);
    switch (json_last_error()) {
        case JSON_ERROR_NONE:
            echo ' - JSON_ERROR_NONE';
            break;
        case JSON_ERROR_DEPTH:
            echo ' - JSON_ERROR_DEPTH';
            break;
        case JSON_ERROR_STATE_MISMATCH:
            echo ' - JSON_ERROR_STATE_MISMATCH';
            break;
        case JSON_ERROR_CTRL_CHAR:
            echo ' -  JSON_ERROR_CTRL_CHAR';
            break;
        case JSON_ERROR_SYNTAX:
            echo "

 - SYNTAX ERROR

";
            break;
        case JSON_ERROR_UTF8:
            echo ' - JSON_ERROR_UTF8';
            break;
        default:
            echo ' - Unknown error';
            break;
    }
} else {
    // Handle the empty data case
}