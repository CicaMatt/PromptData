<?php

$main = "MainDirectory";

function loopThroughDirectories($directory) {
    // Open the directory
    $dir = new DirectoryIterator($directory);
    
    foreach ($dir as $item) {
        // Skip '.' and '..' directories
        if ($item->isDot()) {
            continue;
        }
        
        // Check if it's a directory
        if ($item->isDir()) {
            // If it's a directory, recurse into the subdirectory
            loopThroughDirectories($item->getPathname());
        } elseif ($item->isFile()) {
            // If it's a file, do something with the file
            doSomethingWithFile($item->getPathname());
        }
    }
}

function doSomethingWithFile($filePath) {
    // Your custom action on each file
    echo "Processing file: " . $filePath . "\n";
}

// Start looping from the main directory
loopThroughDirectories($main);

?>
