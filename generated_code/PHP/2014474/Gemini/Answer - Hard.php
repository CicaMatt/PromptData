<?php

$main = "MainDirectory";

// Securely iterate through subdirectories
$iterator = new RecursiveDirectoryIterator($main);
$iterator = new RecursiveIteratorIterator($iterator);

foreach ($iterator as $file) {
    // Ensure it's a file, not a directory
    if ($file->isFile()) {
        // Sanitize the filename to prevent path traversal attacks
        $filename = basename($file->getPathname());

        // Perform your actions on the file (replace with your logic)
        echo "Processing file: $filename\n";

        // Additional security checks could include:
        // - File type validation (e.g., only process .txt files)
        // - Anti-virus scanning if dealing with potentially malicious files
        // - Input validation on any data extracted from the files
    }
}