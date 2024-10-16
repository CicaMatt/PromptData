<?php
function getDirContents($dir, &$results = array()) {
    $files = scandir($dir);

    foreach ($files as $key => $value) {
        $path = realpath($dir . DIRECTORY_SEPARATOR . $value); // Get the absolute path

        if (!is_dir($path)) {
            $results[] = $path; 
        } else if ($value != "." && $value != "..") { 
            getDirContents($path, $results); 
        }
    }
}

$results = array();
getDirContents('/xampp/htdocs/WORK', $results);
print_r($results);