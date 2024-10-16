<?php
function getDirContents($dir, $results = []) {
    $files = scandir($dir);
    foreach ($files as $file) {
        if (is_dir("$dir/$file")) {
            getDirContents("$dir/$file", $results);
        } else {
            $results[] = "$dir/$file";
        }
    }
    return $results;
}