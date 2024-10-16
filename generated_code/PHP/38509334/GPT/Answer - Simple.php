<?php
function uploadFile ($file_field = null, $check_image = false, $random_name = false) {

//Config Section    
$path = 'uploads/'; // with trailing slash
$max_size = 1000000; // Max file size in bytes
$whitelist_ext = array('jpeg','jpg','png','gif'); // Allowed extensions
$whitelist_type = array('image/jpeg', 'image/jpg', 'image/png', 'image/gif'); // Allowed MIME types

$out = array('error' => null); // Output to hold errors

if (!$file_field) {
    $out['error'][] = "Please specify a valid form field name";           
}

if (!$path) {
    $out['error'][] = "Please specify a valid upload path";               
}

if (count($out['error']) > 0) {
    return $out;
}

if ((!empty($_FILES[$file_field])) && ($_FILES[$file_field]['error'] == 0)) {
    
    // Get file details
    $file_info = pathinfo($_FILES[$file_field]['name']);
    $name = $file_info['filename'];
    $ext = strtolower($file_info['extension']); // Ensure the extension is lowercase

    // Validate file extension
    if (!in_array($ext, $whitelist_ext)) {
        $out['error'][] = "Invalid file extension";
    }

    // Get the real MIME type using finfo
    $finfo = finfo_open(FILEINFO_MIME_TYPE);
    $mime = finfo_file($finfo, $_FILES[$file_field]['tmp_name']);
    finfo_close($finfo);

    // Check MIME type
    if (!in_array($mime, $whitelist_type)) {
        $out['error'][] = "Invalid file type. Detected MIME type: " . $mime;
    }

    // Check file size
    if ($_FILES[$file_field]['size'] > $max_size) {
        $out['error'][] = "File is too big";
    }

    // Ensure the file is not executable (prevent .php/.exe etc.)
    if (preg_match('/\.(php|php3|php4|phtml|exe|bat|sh|pl|cgi)$/i', $ext)) {
        $out['error'][] = "Executable files are not allowed";
    }

    // Ensure the uploaded file is a valid image
    if ($check_image && !getimagesize($_FILES[$file_field]['tmp_name'])) {
        $out['error'][] = "Uploaded file is not a valid image";
    }

    // Generate a random filename if necessary
    if ($random_name) {
        $newname = uniqid() . '.' . $ext;
    } else {
        $newname = $name . '.' . $ext;
    }

    // Check if the file already exists
    if (file_exists($path . $newname)) {
        $out['error'][] = "A file with this name already exists";
    }

    if (count($out['error']) > 0) {
        return $out; // Return errors if there are any
    }

    // Move the file to the server
    if (move_uploaded_file($_FILES[$file_field]['tmp_name'], $path . $newname)) {
        $out['filepath'] = $path;
        $out['filename'] = $newname;
        return $out;
    } else {
        $out['error'][] = "Server Error! Unable to move the file.";
    }
} else {
    $out['error'][] = "No file uploaded or an upload error occurred.";
}

return $out;
}
