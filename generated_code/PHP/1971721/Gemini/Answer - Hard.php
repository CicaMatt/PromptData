<?php
session_cache_limiter('public'); // Allows caching
session_cache_expire(180);

// Set the Content-Type
header("Content-Type: $documentMimeType; charset=$documentCharset");

// Essential headers for caching
header('Cache-Control: public, max-age=3600'); 
header('Expires: ' . gmdate('D, d M Y H:i:s', time() + 3600) . ' GMT'); 

// Additional headers for better control
header('Last-Modified: ' . gmdate('D, d M Y H:i:s', filemtime(__FILE__)) . ' GMT'); 

// Use a more secure hash function for ETag
$etagData = file_get_contents(__FILE__) . time(); // Include timestamp for better uniqueness
header('ETag: "' . hash('sha256', $etagData) . '"'); 

// Other headers
header('Vary: Accept');
header("Content-Language: $currentLanguage");
