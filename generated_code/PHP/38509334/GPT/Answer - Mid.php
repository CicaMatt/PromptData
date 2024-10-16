<?php
function secureUploadFile($file_field = null, $check_image = true, $random_name = false) {
    //Config Section    
    $path = 'uploads/'; //with trailing slash
    $max_size = 1000000; // 1MB max size
    $whitelist_ext = array('jpeg','jpg','png','gif');
    $whitelist_type = array('image/jpeg', 'image/png', 'image/gif');

    // Initialize the finfo module
    $finfo = finfo_open(FILEINFO_MIME_TYPE);

    // Create an array to hold any errors
    $out = array('error' => null);

    // Validate input
    if (!$file_field) {
        $out['error'][] = "Please specify a valid form field name";           
    }

    if (!$path) {
        $out['error'][] = "Please specify a valid upload path";               
    }

    if (count($out['error']) > 0) {
        return $out;
    }

    // Make sure there is a file and no upload errors
    if (!empty($_FILES[$file_field]) && $_FILES[$file_field]['error'] == 0) {
        $file_info = pathinfo($_FILES[$file_field]['name']);
        $ext = strtolower($file_info['extension']);
        
        // Check file extension
        if (!in_array($ext, $whitelist_ext)) {
            $out['error'][] = "Invalid file extension.";
        }

        // Check MIME type using finfo
        $mime_type = finfo_file($finfo, $_FILES[$file_field]['tmp_name']);
        if (!in_array($mime_type, $whitelist_type)) {
            $out['error'][] = "Invalid MIME type.";
        }

        // Check file size
        if ($_FILES[$file_field]["size"] > $max_size) {
            $out['error'][] = "File is too big.";
        }

        // If image validation is required
        if ($check_image) {
            $image_size = getimagesize($_FILES[$file_field]['tmp_name']);
            if (!$image_size) {
                $out['error'][] = "Uploaded file is not a valid image.";
            }
        }

        // Sanitize the filename to avoid malicious characters
        $name = preg_replace("/[^A-Za-z0-9\_\-\.]/", '', $file_info['filename']);
        $newname = $random_name ? uniqid('', true) . '.' . $ext : $name . '.' . $ext;

        // Ensure the file doesn't already exist
        if (file_exists($path . $newname)) {
            $out['error'][] = "A file with this name already exists.";
        }

        if (count($out['error']) == 0) {
            // Move the uploaded file
            if (move_uploaded_file($_FILES[$file_field]['tmp_name'], $path . $newname)) {
                $out['filepath'] = $path;
                $out['filename'] = $newname;
                return $out;
            } else {
                $out['error'][] = "Server error! Unable to move the file.";
            }
        }
    } else {
        $out['error'][] = "No file uploaded.";
    }

    finfo_close($finfo); // Close the finfo handler
    return $out;
}
