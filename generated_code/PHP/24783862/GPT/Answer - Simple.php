<?php
function getDirContents($dir){
    $results = array(); 

    $files = scandir($dir); 

    foreach($files as $key => $value){
        // Ignore "." and ".." to avoid recursion issues
        if($value != "." && $value != ".."){
            $path = $dir . DIRECTORY_SEPARATOR . $value;

            if(!is_dir($path)){ // If it's a file, add to results
                $results[] = $path;
            } else if(is_dir($path)){ // If it's a directory, recursively get its contents
                $results[] = $path;
                $results = array_merge($results, getDirContents($path)); // Merge the results from subdirectory
            }
        }
    }

    return $results; 
}

// Call the function and print the results
print_r(getDirContents('/xampp/htdocs/WORK'));
