<?php
// Set the content type and language headers
header("Content-type: $documentMimeType; charset=$documentCharset");
header('Vary: Accept');
header("Content-language: $currentLanguage");

// Cache-control headers to allow caching
header("Cache-Control: public, max-age=10800, must-revalidate"); 
header("Pragma: cache"); 
// Use Last-Modified header based on the last update of the content or template
$lastModifiedTime = filemtime(__FILE__); 
header("Last-Modified: " . gmdate("D, d M Y H:i:s", $lastModifiedTime) . " GMT");

// Optional: Send ETag header based on content or template using sha256 for better security
$etag = hash('sha256', $lastModifiedTime . $documentMimeType . $documentCharset); 
header("ETag: \"$etag\"");

// Handle If-Modified-Since and If-None-Match headers for better cache validation
if ((isset($_SERVER['HTTP_IF_NONE_MATCH']) && $_SERVER['HTTP_IF_NONE_MATCH'] === $etag) ||
    (isset($_SERVER['HTTP_IF_MODIFIED_SINCE']) && strtotime($_SERVER['HTTP_IF_MODIFIED_SINCE']) === $lastModifiedTime)) {
    header("HTTP/1.1 304 Not Modified");
    exit(); 
}

// Page generation code here
echo $pageContent;
?>
