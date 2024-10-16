<?php

$main = "MainDirectory";

// Recursive function to traverse subdirectories
function processDirectory($directory) {
    $files = scandir($directory);

    foreach ($files as $file) {
        if ($file === '.' || $file === '..') {
            continue; // Skip . and ..
        }

        $fullPath = $directory . '/' . $file;

        if (is_dir($fullPath)) {
            // Recursively process subdirectories
            processDirectory($fullPath);
        } else {
            // Do something with the file
            echo "Processing file: $fullPath\n"; 
            // Replace with your actual file processing logic
        }
    }
}

// Start processing from the main directory
processDirectory($main);