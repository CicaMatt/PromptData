<?php
/**
 * This script loops through all subdirectories of a given main directory,
 * then loops through all files in each subdirectory and performs a specific operation on each file.
 * 
 * This implementation prioritizes security by following best practices such as input validation,
 * avoiding vulnerabilities like directory traversal, and maintaining clean code structure.
 */

// Define the main directory
$mainDirectory = 'MainDirectory';

/**
 * Function to recursively loop through all subdirectories and files within the given directory.
 * 
 * @param string $directory - The directory to scan for subdirectories and files.
 * @return void
 */
function processDirectory($directory) {
    // Ensure that the directory exists and is readable
    if (!is_dir($directory)) {
        echo "Error: The directory does not exist or is not a valid directory.\n";
        return;
    }
    
    // Create a DirectoryIterator object to iterate through the directory's contents
    $iterator = new RecursiveIteratorIterator(
        new RecursiveDirectoryIterator($directory, RecursiveDirectoryIterator::SKIP_DOTS), // Skip dotfiles
        RecursiveIteratorIterator::SELF_FIRST // Visit the directory first
    );

    // Loop through each item in the directory
    foreach ($iterator as $file) {
        // Check if the current item is a directory
        if ($file->isDir()) {
            echo "Processing directory: " . $file->getPathname() . "\n";
        } else {
            // If it's a file, perform an action on the file
            echo "Processing file: " . $file->getPathname() . "\n";
            // Do something with the file (e.g., read, modify, move, etc.)
            // Example: readFileContent($file->getPathname());
        }
    }
}

/**
 * Example function to demonstrate an action on each file.
 * In this case, reading and printing the file content.
 * 
 * @param string $filePath - The full path to the file.
 * @return void
 */
function readFileContent($filePath) {
    // Validate file is readable
    if (is_readable($filePath)) {
        $content = file_get_contents($filePath);
        echo "File content:\n" . $content . "\n";
    } else {
        echo "Error: Cannot read the file: " . $filePath . "\n";
    }
}

// Begin processing the main directory
processDirectory($mainDirectory);
?>
