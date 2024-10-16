<?php
$mainDirectory = "MainDirectory";

function processFiles($dir) {
    $files = scandir($dir);
    foreach ($files as $file) {
        if ($file != '.' && $file != '..') {
            $filePath = $dir . DIRECTORY_SEPARATOR . $file;
            if (is_dir($filePath)) {
                // Recursively call the function for subdirectories
                processFiles($filePath);
            } else {
                // Do something with each file
                echo "Processing file: $filePath\n";
            }
        }
    }
}

processFiles($mainDirectory);
?>
