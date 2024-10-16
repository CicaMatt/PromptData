<?php
function getDirContents($dir) {
    $results = array();
    $files = scandir($dir);

    foreach ($files as $value) {
        $path = $dir . DIRECTORY_SEPARATOR . $value;

        // Skip the current (.) and parent (..) directories
        if ($value === '.' || $value === '..') {
            continue;
        }

        // If it's a directory, recursively get its contents
        if (is_dir($path)) {
            // Get the contents of the directory and merge them into the results array
            $results = array_merge($results, getDirContents($path));
        } else {
            // If it's a file, add it to the results array
            $results[] = $path;
        }
    }

    return $results;
}

// Example usage:
print_r(getDirContents('/xampp/htdocs/WORK'));
