<?php
try {
    $move = move_uploaded_file($_FILES['file']['tmp_name'], $_SERVER['DOCUMENT_ROOT'].'/uploads/'.$_FILES['file']['name']);
} catch (\Exception $e) {
    echo 'An error occurred during the upload process. Please try again later.';
} finally {
    if (!$move) {
        die ('File didn\'t upload');
    } else {            
        //opens the uploaded file for extraction
        echo 'Upload Complete!';
    }
}