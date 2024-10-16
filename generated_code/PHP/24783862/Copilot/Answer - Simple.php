<?php
function getDirContents($dir){
    $results = array();
    $files = scandir($dir);

    foreach($files as $key => $value){
        $path = $dir . DIRECTORY_SEPARATOR . $value;
        if(!is_dir($path)){
            $results[] = $path;
        } else if($value != "." && $value != "..") {
            $results[] = $path;
            $results = array_merge($results, getDirContents($path));
        }
    }

    return $results;
}

print_r(getDirContents('/xampp/htdocs/WORK'));
