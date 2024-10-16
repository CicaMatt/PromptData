<?php
function uploadFile($file_field = null, $check_image = false, $random_name = false) {

    $path = 'uploads/'; // Consider using a path outside the web root for added security

    // Stricter file size limit (1 MB), adjust as needed 
    $max_size = 1024 * 1024; 

    // Explicitly allow only specific image extensions and types
    $whitelist_ext = array('jpeg', 'jpg', 'png', 'gif');
    $whitelist_type = array('image/jpeg', 'image/jpg', 'image/png', 'image/gif');

    $out = array('error' => null);

    // Input Validation and Sanitization
    if (!$file_field || !preg_match('/^[a-zA-Z0-9_-]+$/', $file_field)) {
        $out['error'][] = "Invalid form field name";
    }

    if (!$path || !preg_match('/^[a-zA-Z0-9_\-\/]+$/', $path)) {
        $out['error'][] = "Invalid upload path";
    }

    if (count($out['error']) > 0) {
        return $out;
    }

    // Ensure file was uploaded successfully
    if (!empty($_FILES[$file_field]) && $_FILES[$file_field]['error'] == 0) {

        // Retrieve file information securely
        $file_info = pathinfo(basename($_FILES[$file_field]['name']));
        $name = $file_info['filename'];
        $ext = strtolower($file_info['extension']); 

        // Validate file extension and type against whitelists
        if (!in_array($ext, $whitelist_ext)) {
            $out['error'][] = "Invalid file extension";
        }

        if (!in_array($_FILES[$file_field]["type"], $whitelist_type)) {
            $out['error'][] = "Invalid file type";
        }

        // Enforce file size limit
        if ($_FILES[$file_field]["size"] > $max_size) {
            $out['error'][] = "File is too large";
        }

        // Content-Based Validation (Image Verification)
        if ($check_image) {
            $image_info = getimagesize($_FILES[$file_field]['tmp_name']);
            if (!$image_info || !in_array($image_info[2], array(IMAGETYPE_JPEG, IMAGETYPE_PNG, IMAGETYPE_GIF))) {
                $out['error'][] = "Uploaded file is not a valid image";
            }
        }

        // Generate a cryptographically secure random filename if requested
        if ($random_name) {
            $newname = bin2hex(random_bytes(16)) . '.' . $ext; 
        } else {
            // Sanitize the original filename to prevent potential attacks
            $newname = preg_replace('/[^a-zA-Z0-9_\-\.]/', '', $name) . '.' . $ext;
        }

        // Prevent overwriting existing files
        if (file_exists($path . $newname)) {
            $newname = uniqid() . '_' . $newname; 
        }

        if (count($out['error']) > 0) {
            return $out;
        }

        // Move uploaded file to the designated path securely
        $upload_result = move_uploaded_file($_FILES[$file_field]['tmp_name'], $path . $newname);
        if ($upload_result) {
            // Set appropriate file permissions (e.g., 0644 for read/write by owner, read by others)
            chmod($path . $newname, 0644); 

            $out['filepath'] = $path;
            $out['filename'] = $newname;
            return $out;
        } else {
            $out['error'][] = "File upload failed"; 
        }
    } else {
        $out['error'][] = "No file uploaded or an error occurred during upload";
        return $out;
    }
}

// Usage (similar to your original code, but with the new function)
if (isset($_POST['submit'])) {
    $file = uploadFile('file', true, true);
    if (is_array($file['error'])) {
        $message = '';
        foreach ($file['error'] as $msg) {
            $message .= '<p>' . $msg . '</p>';
        }
    } else {
        $message = "File uploaded successfully: " . $file['filename'];
    }
    echo $message;
}
?>