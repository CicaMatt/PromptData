<?php
function getDirContents($dir, &$results = array()) {
    // Ensure we have a valid directory
    if (!is_dir($dir)) {
        return $results; 
    }

    $files = scandir($dir);

    foreach ($files as $key => $value) {
        $path = realpath($dir . DIRECTORY_SEPARATOR . $value); // Resolve symbolic links

        if ($value == '.' || $value == '..') {
            continue; 
        }

        if (is_dir($path)) {
            getDirContents($path, $results); // Recursive call, capturing results
        } else {
            $results[] = $path; 
        }
    }

    return $results;
}

$allFiles = getDirContents('/xampp/htdocs/WORK');
print_r($allFiles);