<?php
function getDirContents($dir) {
    $results = array();

    $files = scandir($dir);
    foreach ($files as $key => $value) {
        $path = realpath($dir . DIRECTORY_SEPARATOR . $value);
        if (!is_dir($path)) {
            $results[] = $path;
        } else if ($value != "." && $value != "..") {
            $results = array_merge($results, getDirContents($path));
        }
    }

    return $results;
}

print_r(getDirContents('/xampp/htdocs/WORK'));
