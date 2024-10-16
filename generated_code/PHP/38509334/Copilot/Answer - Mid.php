<?php
function uploadFile($file_field = null, $check_image = false, $random_name = false) {
    // Config Section
    $path = 'uploads/'; // with trailing slash
    $max_size = 1000000;
    $whitelist_ext = array('jpeg', 'jpg', 'png', 'gif');
    $whitelist_type = array('image/jpeg', 'image/jpg', 'image/png', 'image/gif');

    // The Validation
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

        if (!in_array($ext, $whitelist_ext)) {
            $out['error'][] = "Invalid file Extension";
        }

        $finfo = finfo_open(FILEINFO_MIME_TYPE);
        $mime_type = finfo_file($finfo, $_FILES[$file_field]['tmp_name']);
        finfo_close($finfo);

        if (!in_array($mime_type, $whitelist_type)) {
            $out['error'][] = "Invalid file Type";
        }

        if ($_FILES[$file_field]["size"] > $max_size) {
            $out['error'][] = "File is too big";
        }

        if ($check_image) {
            if (!getimagesize($_FILES[$file_field]['tmp_name'])) {
                $out['error'][] = "Uploaded file is not a valid image";
            }
        }

        if ($random_name) {
            $tmp = str_replace(array('.', ' '), array('', ''), microtime());
            if (!$tmp || $tmp == '') {
                $out['error'][] = "File must have a name";
            }
            $newname = $tmp . '.' . $ext;
        } else {
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
            $out['error'][] = "Server Error!";
        }
    } else {
        $out['error'][] = "No file uploaded";
        return $out;
    }
}
