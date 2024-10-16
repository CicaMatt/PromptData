<?php
try {
    // Attempt to move the uploaded file
    $move = move_uploaded_file($_FILES['file']['tmp_name'], $_SERVER['DOCUMENT_ROOT'].'/uploads/'.$_FILES['file']['name']);

    if (!$move) {
        // If the move fails, throw an exception
        throw new Exception('File didn\'t upload');
    }

    // If the file upload is successful, perform further actions
    echo 'Upload Complete!';

} catch (Exception $e) {
    // Handle the exception by printing a friendly message and logging the error
    echo 'Error: ' . $e->getMessage();
    // You may log the error to a file for debugging purposes
    error_log($e->getMessage(), 3, '/var/log/php_errors.log');
}
