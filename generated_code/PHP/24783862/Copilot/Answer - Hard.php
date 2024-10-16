<?php
function getDirContents($dir) {
    $results = array();

    // Ensure the directory exists and is readable
    if (!is_dir($dir) || !is_readable($dir)) {
        return $results;
    }

    $files = scandir($dir);

    foreach ($files as $key => $value) {
        $path = $dir . DIRECTORY_SEPARATOR . $value;

        // Skip the special directories "." and ".."
        if ($value === '.' || $value === '..') {
            continue;
        }

        if (is_dir($path)) {
            // Recursively get contents of the directory
            $results = array_merge($results, getDirContents($path));
        } else {
            // Add the file to the results array
            $results[] = $path;
        }
    }

    return $results;
}

// Example usage
print_r(getDirContents('/xampp/htdocs/WORK'));
