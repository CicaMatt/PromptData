<?php
function getDirContents($dir, &$results = []) {
    $files = scandir($dir);

    foreach($files as $key => $value){
        if ($value == '.' || $value == '..') continue; // Skip . and ..

        $path = $dir . DIRECTORY_SEPARATOR . $value;

        if (is_dir($path)) {
            getDirContents($path, $results); // Recurse into subdirectories
        } else {
            $results[] = $path; 
        }
    }
}

$allFiles = [];
getDirContents('/xampp/htdocs/WORK', $allFiles);
print_r($allFiles);