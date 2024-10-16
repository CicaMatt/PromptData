<?php
// Set cache expiration in seconds (180 minutes = 10800 seconds)
$cache_duration = 10800; // Adjust this based on your requirements

// Get the current time and calculate the cache expiration time
$timestamp = gmdate("D, d M Y H:i:s", time()) . " GMT";
$expiration = gmdate("D, d M Y H:i:s", time() + $cache_duration) . " GMT";

// Set HTTP caching headers
header("Content-Type: $documentMimeType; charset=$documentCharset");
header("Vary: Accept");
header("Content-Language: $currentLanguage");
header("Cache-Control: public, max-age=$cache_duration");
header("Expires: $expiration");
header("Last-Modified: $timestamp");
header("Pragma: public");

// Example of generating dynamic content
echo "This is cached content generated at: " . $timestamp;

// You can insert your dynamic page content here, and the headers will ensure caching happens for $cache_duration seconds
?>
