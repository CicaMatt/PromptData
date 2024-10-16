<?php

function uploadFile($file_field = null, $check_image = false, $random_name = false)
{
    $path = 'uploads/'; // Consider using a path outside the web root for better security
    $max_size = 1000000; // 1MB
    $whitelist_ext = array('jpeg', 'jpg', 'png', 'gif');
    $whitelist_type = array('image/jpeg', 'image/jpg', 'image/png', 'image/gif');

    $out = array('error' => null);

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
        $file_info = pathinfo($_FILES[$file_field]['name']);
        $name = $file_info['filename'];
        $ext = strtolower($file_info['extension']); // Convert extension to lowercase for consistency

        // Additional security checks
        if (!in_array($ext, $whitelist_ext)) {
            $out['error'][] = "Invalid file Extension";
        }

        // Use finfo for more reliable MIME type validation
        $finfo = finfo_open(FILEINFO_MIME_TYPE);
        $mime_type = finfo_file($finfo, $_FILES[$file_field]['tmp_name']);
        finfo_close($finfo);

        if (!in_array($mime_type, $whitelist_type)) {
            $out['error'][] = "Invalid file Type. Detected: " . $mime_type; 
        }

        if ($_FILES[$file_field]["size"] > $max_size) {
            $out['error'][] = "File is too big";
        }

        if ($check_image) {
            if (!getimagesize($_FILES[$file_field]['tmp_name'])) {
                $out['error'][] = "Uploaded file is not a valid image";
            } else {
                // Additional image validation using GD library if available
                if (function_exists('imagecreatefromstring')) {
                    $image = @imagecreatefromstring(file_get_contents($_FILES[$file_field]['tmp_name']));
                    if (!$image) {
                        $out['error'][] = "Uploaded file is not a valid image (GD check failed)";
                    }
                    imagedestroy($image); 
                }
            }
        }

        if ($random_name) {
            // Use a cryptographically secure random function for filename generation
            $newname = bin2hex(random_bytes(16)) . '.' . $ext;
        } else {
            // Sanitize the original filename to remove potentially harmful characters
            $newname = preg_replace('/[^A-Za-z0-9_\-\.]/', '_', $name) . '.' . $ext;
        }

        $full_path = $path . $newname;

        if (file_exists($full_path)) {
            $out['error'][] = "A file with this name already exists";
        }

        if (count($out['error']) > 0) {
            return $out;
        }

        if (move_uploaded_file($_FILES[$file_field]['tmp_name'], $full_path)) {
            // Set appropriate file permissions after upload
            chmod($full_path, 0644); // Example: Read/write for owner, read for group and others

            $out['filepath'] = $path;
            $out['filename'] = $newname;
            return $out;
        } else {
            $out['error'][] = "Server Error!";
        }
    } else {
        $out['error'][] = "No file uploaded";
        return $out;
    }
}

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