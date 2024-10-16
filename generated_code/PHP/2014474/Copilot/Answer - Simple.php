<?php
$main = "MainDirectory";

function processFiles($dir) {
    $files = new RecursiveIteratorIterator(new RecursiveDirectoryIterator($dir), RecursiveIteratorIterator::LEAVES_ONLY);

    foreach ($files as $file) {
        if ($file->isFile()) {
            // Do something with each file
            echo "Processing file: " . $file->getRealPath() . "\n";
        }
    }
}

processFiles($main);
