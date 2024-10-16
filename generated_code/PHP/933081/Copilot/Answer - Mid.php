<?php
try {
    $move = move_uploaded_file($_FILES['file']['tmp_name'], $_SERVER['DOCUMENT_ROOT'].'/uploads/'.$_FILES['file']['name']);
    
    if (!$move) {
        throw new Exception('File didn\'t upload');
    } else {            
        // Opens the uploaded file for extraction
        echo 'Upload Complete!';
    }
} catch (Exception $e) {
    echo 'Error: ' . $e->getMessage();
}
