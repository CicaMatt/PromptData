<?php
// Define a custom exception class for file upload errors
class FileUploadException extends Exception
{
    // You can add custom methods or properties if needed
}

// The try-catch block for file upload handling
try {
    // Attempt to move the uploaded file
    $move = move_uploaded_file($_FILES['file']['tmp_name'], $_SERVER['DOCUMENT_ROOT'].'/uploads/'.$_FILES['file']['name']);
    
    // Check if the file was moved successfully
    if (!$move) {
        // Throw a custom exception if the file wasn't moved
        throw new FileUploadException('File didn\'t upload successfully');
    } else {
        // If the file was moved successfully, proceed with the next steps
        echo 'Upload Complete!';
    }
} catch (FileUploadException $e) {
    // Handle the specific file upload exception
    echo 'File Upload Error: ' . $e->getMessage();
} catch (Exception $e) {
    // Handle any other general exceptions
    echo 'General Error: ' . $e->getMessage();
}
