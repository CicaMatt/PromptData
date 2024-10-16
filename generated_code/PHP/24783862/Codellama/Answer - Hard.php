<?php
function getDirContents($dir) {
    $results = array();
    foreach (scandir($dir) as $file) {
        if (!is_dir(realpath("$dir/$file"))) {
            $results[] = realpath("$dir/$file");
        } elseif (is_dir(realpath("$dir/$file"))) {
            $results[] = realpath("$dir/$file");
            getDirContents(realpath("$dir/$file"));
        }
    }
    return $results;
}

print_r(getDirContents('/xampp/htdocs/WORK'));