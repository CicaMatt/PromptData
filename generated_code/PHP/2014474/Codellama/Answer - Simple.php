<?php
$main = "MainDirectory";

// Loop through subdirectories
foreach (glob("$main/*", GLOB_ONLYDIR) as $subdirectory) {
    // Loop through files in each subdirectory
    foreach (glob("$subdirectory/*") as $file) {
        // Do something with each file
        echo "$file\n";
    }
}