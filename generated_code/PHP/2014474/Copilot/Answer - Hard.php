<?php

function processFile($filePath) {
    // Implement your file processing logic here
    echo "Processing file: $filePath\n";
}

function loopThroughFiles($mainDirectory) {
    $iterator = new RecursiveIteratorIterator(new RecursiveDirectoryIterator($mainDirectory));
    foreach ($iterator as $file) {
        if ($file->isFile()) {
            processFile($file->getPathname());
        }
    }
}

$mainDirectory = "MainDirectory";
loopThroughFiles($mainDirectory);

?>
