<?php
function handleImageUpload($input_field = null, $validate_image = false, $generate_random_name = false) 
{
    // Allowed file extensions and MIME types
    $valid_extensions = array('jpeg', 'jpg', 'png', 'gif');
    $valid_mime_types = array('image/jpeg', 'image/jpg', 'image/png', 'image/gif');

    // Ensure a file has been uploaded
    if (!isset($_FILES[$input_field]) || $_FILES[$input_field]['error'] !== 0) {
        return array('error' => array('No file was uploaded or an error occurred.'));
    }

    $file = $_FILES[$input_field];

    // Check if file size exceeds limit (1 MB)
    $max_size = 1000000; 
    if ($file['size'] > $max_size) {
        return array('error' => array('File is too large.'));
    }

    // Extract file extension and check if valid
    $file_info = pathinfo($file['name']);
    $file_extension = strtolower($file_info['extension']);
    if (!in_array($file_extension, $valid_extensions)) {
        return array('error' => array('Invalid file extension.'));
    }

    // Check if MIME type is valid
    if (!in_array($file['type'], $valid_mime_types)) {
        return array('error' => array('Invalid file type.'));
    }

    // Optional: Verify if file is a valid image
    if ($validate_image && !getimagesize($file['tmp_name'])) {
        return array('error' => array('The file is not a valid image.'));
    }

    // Sanitize the original filename to avoid security risks
    $sanitized_name = preg_replace("/[^a-zA-Z0-9\._-]/", "", basename($file['name']));

    // Generate a random name if requested
    if ($generate_random_name) {
        $new_filename = uniqid('img_', true) . '.' . $file_extension;
    } else {
        $new_filename = $sanitized_name;
    }

    // Define the upload directory and check if writable
    $upload_directory = 'uploads/';
    if (!is_dir($upload_directory) || !is_writable($upload_directory)) {
        return array('error' => array('Upload directory does not exist or is not writable.'));
    }

    // Check if file with the same name already exists
    if (file_exists($upload_directory . $new_filename)) {
        return array('error' => array('A file with this name already exists.'));
    }

    // Attempt to move the uploaded file to the target directory
    if (move_uploaded_file($file['tmp_name'], $upload_directory . $new_filename)) {
        return array('filepath' => $upload_directory, 'filename' => $new_filename);
    } else {
        return array('error' => array('Error occurred while moving the uploaded file.'));
    }
}
?>
