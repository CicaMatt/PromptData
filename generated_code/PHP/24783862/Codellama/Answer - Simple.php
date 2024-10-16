<?php
function getDirContents($dir) {
    $results = array();
    foreach (scandir($dir) as $key => $value) {
        if (!is_dir($dir . DIRECTORY_SEPARATOR . $value)) {
            $results[] = $value;
        } elseif (is_dir($dir . DIRECTORY_SEPARATOR . $value)) {
            $results[] = $value;
            $results = array_merge($results, getDirContents($dir . DIRECTORY_SEPARATOR . $value));
        }
    }
    return $results;
}