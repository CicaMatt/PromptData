<?php
try {
    // Attempt to move the uploaded file
    $move = move_uploaded_file($_FILES['file']['tmp_name'], $_SERVER['DOCUMENT_ROOT'].'/uploads/'.$_FILES['file']['name']);
    
    // If the move operation fails, throw an exception
    if (!$move) {
        throw new Exception('File didn\'t upload');
    }
    
    // If the file is successfully uploaded
    echo 'Upload Complete!';
    
} catch (Exception $e) {
    // Catch the exception and display the error message
    echo 'Error: ' . $e->getMessage();
}
