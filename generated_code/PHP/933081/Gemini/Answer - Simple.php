<?php
try {
    // Attempt to move the uploaded file
    if (!move_uploaded_file($_FILES['file']['tmp_name'], $_SERVER['DOCUMENT_ROOT'].'/uploads/'.$_FILES['file']['name'])) {
        // If the move operation fails, throw an exception
        throw new Exception('File upload failed'); 
    }

    // If the file upload is successful, execute this code
    echo 'Upload Complete!';

} catch (Exception $e) {
    // If an exception is thrown, handle the error here
    die ('Error: ' . $e->getMessage()); 
}