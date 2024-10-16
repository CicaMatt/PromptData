<?php
function uploadFile($file_field = null, $check_image = false, $random_name = false) {
    $path = 'uploads/';
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
        $ext = strtolower($file_info['extension']);

        // Validate file extension
        if (!in_array($ext, $whitelist_ext)) {
            $out['error'][] = "Invalid file extension";
        }

        // Validate MIME type
        if (!in_array($_FILES[$file_field]["type"], $whitelist_type)) {
            $out['error'][] = "Invalid file type";
        }

        // Validate file size
        if ($_FILES[$file_field]["size"] > $max_size) {
            $out['error'][] = "File is too big";
        }

        // Validate image
        if ($check_image) {
            if (!getimagesize($_FILES[$file_field]['tmp_name'])) {
                $out['error'][] = "Uploaded file is not a valid image";
            }
        }

        // Generate random file name
        if ($random_name) {
            $tmp = bin2hex(random_bytes(16));
            $newname = $tmp . '.' . $ext;
        } else {
            $newname = $name . '.' . $ext;
        }

        // Prevent directory traversal
        $newname = basename($newname);

        // Check if file already exists
        if (file_exists($path . $newname)) {
            $out['error'][] = "A file with this name already exists";
        }

        if (count($out['error']) > 0) {
            return $out;
        }

        // Move uploaded file
        if (move_uploaded_file($_FILES[$file_field]['tmp_name'], $path . $newname)) {
            $out['filepath'] = $path;
            $out['filename'] = $newname;
            return $out;
        } else {
            $out['error'][] = "Server error!";
        }
    } else {
        $out['error'][] = "No file uploaded";
    }

    return $out;
}
