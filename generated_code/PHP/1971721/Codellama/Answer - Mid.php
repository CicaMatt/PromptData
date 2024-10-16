<?php

// Define the maximum time (in seconds) that a page should be cached.
define('CACHE_TIME', 180);

// Get the current timestamp and check if it has been at least CACHE_TIME seconds since the last cache update.
$lastCacheUpdate = $_SERVER['REQUEST_TIME'] - CACHE_TIME;
if ($lastCacheUpdate > filemtime('cache/index.php')) {
  // If the cache is older than CACHE_TIME seconds, regenerate the cache.
  require 'init.php';
  ob_start();
  include 'index.php';
  $content = ob_get_clean();
  file_put_contents('cache/index.php', $content);
} else {
  // If the cache is fresh, read it from disk and send it to the client.
  require 'cache/index.php';
}