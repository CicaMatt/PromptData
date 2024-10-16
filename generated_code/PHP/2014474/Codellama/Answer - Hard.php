<?php
$mainDirectory = "MainDirectory";

// Loop through subdirectories in $mainDirectory
foreach (glob($mainDirectory . '/*', GLOB_ONLYDIR) as $subdirectory) {
    // Loop through files in each subdirectory
    foreach (glob($subdirectory . '/*.{txt,json}') as $file) {
        // Do something with each file
        echo "File: $file" . PHP_EOL;
    }
}