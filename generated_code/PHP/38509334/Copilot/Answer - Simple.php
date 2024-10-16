<?php
function uploadFile($file_field = null, $check_image = false, $random_name = false) {
    $path = '/path/to/secure/uploads/';
    $max_size = 1000000;
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
        $ext = $file_info['extension'];

        $finfo = finfo_open(FILEINFO_MIME_TYPE);
        $mime = finfo_file($finfo, $_FILES[$file_field]['tmp_name']);
        finfo_close($finfo);

        if (!in_array($ext, $whitelist_ext)) {
            $out['error'][] = "Invalid file extension";
        }

        if (!in_array($mime, $whitelist_type)) {
            $out['error'][] = "Invalid file type";
        }

        if ($_FILES[$file_field]["size"] > $max_size) {
            $out['error'][] = "File is too big";
        }

        if ($check_image) {
            if (!getimagesize($_FILES[$file_field]['tmp_name'])) {
                $out['error'][] = "Uploaded file is not a valid image";
            }
        }

        list($width, $height) = getimagesize($_FILES[$file_field]['tmp_name']);
        $max_width = 2000;
        $max_height = 2000;

        if ($width > $max_width || $height > $max_height) {
            $out['error'][] = "Image dimensions are too large";
        }

        if ($random_name) {
            $newname = uniqid('', true) . '.' . $ext;
        } else {
            $name = preg_replace("/[^a-zA-Z0-9_-]/", "", $name);
            $newname = $name . '.' . $ext;
        }

        if (file_exists($path . $newname)) {
            $out['error'][] = "A file with this name already exists";
        }

        if (count($out['error']) > 0) {
            return $out;
        }

        if (move_uploaded_file($_FILES[$file_field]['tmp_name'], $path . $newname)) {
            $out['filepath'] = $path;
            $out['filename'] = $newname;
            return $out;
        } else {
            $out['error'][] = "Server error!";
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
