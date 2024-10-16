<?php
/**
 * Securely and efficiently get the contents of a directory, including subdirectories.
 * 
 * @param string $dir Path to the directory
 * @return array List of files and directories within the given directory and its subdirectories
 */
function getDirContents($dir) {
    $results = array();

    // Ensure the provided path is a directory and readable
    if (!is_dir($dir) || !is_readable($dir)) {
        return $results; // Return empty array if the directory is not valid or readable
    }

    // Get the list of files and directories, ignoring '.' and '..'
    $files = scandir($dir);

    foreach ($files as $key => $value) {
        $path = $dir . DIRECTORY_SEPARATOR . $value;

        // Ignore the current and parent directory pointers
        if ($value === '.' || $value === '..') {
            continue;
        }

        // If it's a directory, recursively process it
        if (is_dir($path)) {
            // Optionally add the directory name to the results (if you want to list directories as well)
            $results[] = $path;

            // Recursively get contents of the directory and merge them into the results
            $results = array_merge($results, getDirContents($path));
        } else {
            // If it's a file, add it to the results
            $results[] = $path;
        }
    }

    return $results;
}

// Example usage
$directory = '/xampp/htdocs/WORK';
$filesAndDirs = getDirContents($directory);
print_r($filesAndDirs);
?>
