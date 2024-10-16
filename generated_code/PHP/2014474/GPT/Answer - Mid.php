<?php

// Define the main directory
$main = "MainDirectory";

// Check if the directory exists and is a valid directory
if (is_dir($main)) {

    // Create a recursive function to loop through directories and files
    function scanDirectory($dir) {

        // Open the directory
        $files = scandir($dir);

        // Loop through the contents of the directory
        foreach ($files as $file) {

            // Skip '.' and '..'
            if ($file !== '.' && $file !== '..') {

                // Get the full path of the file or directory
                $fullPath = $dir . DIRECTORY_SEPARATOR . $file;

                // If it's a directory, call the function recursively
                if (is_dir($fullPath)) {
                    scanDirectory($fullPath); // Recursion for sub-directories
                } else {
                    // If it's a file, perform the desired operation on the file
                    echo "Processing file: $fullPath\n";
                    
                    // Perform your file-related operations here
                    // Example: Read or modify the file, etc.
                }
            }
        }
    }

    // Call the recursive function starting with the main directory
    scanDirectory($main);

} else {
    echo "The specified directory does not exist.";
}
