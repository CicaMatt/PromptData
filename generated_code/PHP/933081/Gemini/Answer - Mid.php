<?php
try {
    // Attempt to move the uploaded file
    $move = move_uploaded_file($_FILES['file']['tmp_name'], $_SERVER['DOCUMENT_ROOT'] . '/uploads/' . $_FILES['file']['name']);

    // Check if the move was successful
    if (!$move) {
        // If not, throw an exception with a descriptive error message
        throw new Exception('File upload failed.'); 
    } else {
        echo 'Upload Complete!'; 
        // ... (rest of your code to process the uploaded file)
    }

} catch (Exception $e) {
    // Catch any exceptions that occur during the try block
    // Log the error for debugging (you can use error_log or a custom logging mechanism)
    error_log("File upload error: " . $e->getMessage()); 

    // Display a user-friendly error message or take appropriate action
    die('An error occurred during the upload. Please try again later.'); 
}
?>