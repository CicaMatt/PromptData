<?php
$mainDirectory = "MainDirectory";

// Loop through subdirectories
foreach (glob("$mainDirectory/*", GLOB_ONLYDIR) as $subdirectory) {
    // Loop through files in each subdirectory
    foreach (glob("$subdirectory/*") as $file) {
        // Do something with each file
        echo "$file\n";
    }
}