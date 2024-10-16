<?php
try {
    if (!move_uploaded_file($_FILES['file']['tmp_name'], $_SERVER['DOCUMENT_ROOT'].'/uploads/'.$_FILES['file']['name'])) {
        // Explicitly throw an exception if the move operation fails
        throw new Exception('File didn\'t upload');
    } 

    echo 'Upload Complete!';

} catch (Exception $e) {
    // Handle the exception gracefully
    die ($e->getMessage()); 
}