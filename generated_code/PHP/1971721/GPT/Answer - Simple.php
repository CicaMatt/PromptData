<?php
session_cache_limiter('public'); 
session_cache_expire(180);

$documentMimeType = "text/html"; 
$documentCharset = "UTF-8"; 
$currentLanguage = "en"; 

header("Content-type: $documentMimeType; charset=$documentCharset");
header("Content-language: $currentLanguage");
header("Vary: Accept, Accept-Encoding"); 

// Cache-Control: public, max-age=1 hour (3600 seconds)
header("Cache-Control: public, max-age=3600");

// Expires header for HTTP 1.0 clients (1 hour in the future)
header("Expires: " . gmdate("D, d M Y H:i:s", time() + 3600) . " GMT");

// ETag to validate cache
$content = "Your page content here"; 
$etag = md5($content); 
header("ETag: \"$etag\"");

// Last-Modified header based on file or content modification time
$filePath = "path/to/your/file"; 
header("Last-Modified: " . gmdate("D, d M Y H:i:s", filemtime($filePath)) . " GMT");
