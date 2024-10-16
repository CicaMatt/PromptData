<?php
function uploadFile($file_field = null, $check_image = false, $random_name = false) {
  // Define the allowed file types and extensions
  $allowed_types = array('jpeg', 'jpg', 'png', 'gif');
  $allowed_exts = array('image/jpeg', 'image/jpg', 'image/png', 'image/gif');

  // Get the uploaded file information
  if (!empty($_FILES[$file_field])) {
    $file = $_FILES[$file_field];
  } else {
    return array(
      'error' => array('Please select a valid file to upload.')
    );
  }

  // Check if the uploaded file is a valid image
  if ($check_image && !getimagesize($file['tmp_name'])) {
    return array(
      'error' => array('The selected file is not a valid image.')
    );
  }

  // Sanitize the file name to prevent directory traversal attacks
  $sanitized_filename = basename($file['name']);

  // Get the extension of the uploaded file
  $extension = pathinfo($sanitized_filename, PATHINFO_EXTENSION);

  // Check if the uploaded file has a valid extension
  if (!in_array($extension, $allowed_exts)) {
    return array(
      'error' => array('The selected file is not an allowed type.')
    );
  }

  // Get the size of the uploaded file in bytes
  $size = $file['size'];

  // Check if the uploaded file exceeds the maximum size
  if ($size > 1000000) {
    return array(
      'error' => array('The selected file is too large.')
    );
  }

  // Get a random name for the uploaded file
  $random_name = uniqid() . '_' . time();

  // Define the path to store the uploaded file
  $upload_path = 'uploads/';

  // Check if the upload directory exists and is writable
  if (!is_dir($upload_path)) {
    return array(
      'error' => array('The upload destination does not exist.')
    );
  } elseif (!is_writable($upload_path)) {
    return array(
      'error' => array('The upload destination is not writable.')
    );
  }

  // Move the uploaded file to the specified path
  if (move_uploaded_file($file['tmp_name'], $upload_path . $random_name)) {
    return array(
      'filepath' => $upload_path,
      'filename' => $random_name
    );
  } else {
    return array(
      'error' => array('Server error while moving the uploaded file.')
    );
  }
}