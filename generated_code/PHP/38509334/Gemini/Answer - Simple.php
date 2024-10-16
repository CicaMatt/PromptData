<?php
function uploadFile ($file_field = null, $check_image = false, $random_name = false) {
    $path = 'uploads/';

    // Security Enhancements:
    // 1. Sanitize the upload path
    $path = realpath($path) . DIRECTORY_SEPARATOR; // Ensure it's a real path and add directory separator

    // 2. More restrictive file size limit (adjust as needed)
    $max_size = 5 * 1024 * 1024; // 5MB 

    $whitelist_ext = array('jpeg','jpg','png','gif');
    $whitelist_type = array('image/jpeg', 'image/jpg', 'image/png','image/gif');

    $out = array('error'=>null);

    // ... (rest of the validation logic remains the same) ...

    if ($random_name) {
        // 3. Use a cryptographically secure random name generator
        $newname = bin2hex(random_bytes(16)) . '.' . $ext; 
    } else {
        // 4. Sanitize the original filename to prevent malicious filenames
        $newname = preg_replace("/[^A-Za-z0-9_\-\.]/", '', $name) . '.' . $ext;
    }

    // 5. Check if the file already exists with the sanitized name
    if (file_exists($path . $newname)) {
        $out['error'][] = "A file with this name already exists";
    }

    // ... (rest of the upload and error handling logic) ...
}

// ... (rest of your code) ...