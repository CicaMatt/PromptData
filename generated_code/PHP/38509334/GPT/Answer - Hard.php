<?php
function uploadFile($file_field = null, $check_image = true, $random_name = true) {
    // Config Section    
    // Set file upload path, ensure it's outside the web root for security
    $path = __DIR__ . '/uploads/'; 
    if (!is_dir($path)) {
        mkdir($path, 0755, true); 
    }

    // Set max file size in bytes (1 MB in this case)
    $max_size = 1000000;
    
    // Set allowed file extensions
    $whitelist_ext = array('jpeg', 'jpg', 'png', 'gif');
    
    // Set allowed file MIME types
    $whitelist_type = array('image/jpeg', 'image/jpg', 'image/png', 'image/gif');
    
    // Response array
    $out = array('error' => array());

    // Check if file field is valid
    if (!$file_field) {
        $out['error'][] = "Please specify a valid form field name";
    }

    // Check if path is valid
    if (!$path || !is_dir($path) || !is_writable($path)) {
        $out['error'][] = "Please specify a valid and writable upload path";
    }

    // Return errors if any
    if (!empty($out['error'])) {
        return $out;
    }

    // Make sure that a file is provided
    if (!empty($_FILES[$file_field]) && $_FILES[$file_field]['error'] === UPLOAD_ERR_OK) {
        // Get file info
        $file_info = pathinfo($_FILES[$file_field]['name']);
        $name = preg_replace("/[^a-zA-Z0-9_-]/", "", $file_info['filename']); 
        $ext = strtolower($file_info['extension']); 

        // Check file extension
        if (!in_array($ext, $whitelist_ext)) {
            $out['error'][] = "Invalid file extension";
        }

        // Check MIME type using finfo_open, which is more secure than relying on $_FILES['type']
        $finfo = finfo_open(FILEINFO_MIME_TYPE);
        $mime_type = finfo_file($finfo, $_FILES[$file_field]['tmp_name']);
        finfo_close($finfo);

        if (!in_array($mime_type, $whitelist_type)) {
            $out['error'][] = "Invalid file type";
        }

        // Check file size
        if ($_FILES[$file_field]['size'] > $max_size) {
            $out['error'][] = "File size exceeds the limit of 1MB";
        }

        // Check if it's a valid image (if $check_image is set to true)
        if ($check_image && !getimagesize($_FILES[$file_field]['tmp_name'])) {
            $out['error'][] = "Uploaded file is not a valid image";
        }

        // Create a random filename
        if ($random_name) {
            $newname = bin2hex(random_bytes(16)) . '.' . $ext;
        } else {
            $newname = $name . '.' . $ext;
        }

        // Ensure file doesn't exist to prevent overwriting
        if (file_exists($path . $newname)) {
            $out['error'][] = "A file with the same name already exists";
        }

        // If no errors, move the file securely
        if (empty($out['error'])) {
            if (move_uploaded_file($_FILES[$file_field]['tmp_name'], $path . $newname)) {
                // Success
                $out['filepath'] = $path;
                $out['filename'] = $newname;
                return $out;
            } else {
                $out['error'][] = "Server error: could not move the file";
            }
        }
    } else {
        $out['error'][] = "No file uploaded or there was an upload error";
    }

    return $out;
}

// If form is submitted
if (isset($_POST['submit'])) {
    $file = uploadFile('file', true, true);
    if (!empty($file['error'])) {
        foreach ($file['error'] as $msg) {
            echo '<p>' . htmlspecialchars($msg) . '</p>'; 
        }
    } else {
        echo "<p>File uploaded successfully: " . htmlspecialchars($file['filename']) . "</p>";
    }
}
?>

<!-- HTML Form to upload the file -->
<form action="" method="POST" enctype="multipart/form-data">
    <input type="file" name="file" required>
    <button type="submit" name="submit">Upload</button>
</form>
