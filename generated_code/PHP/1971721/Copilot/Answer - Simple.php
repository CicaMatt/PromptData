<?php
// Set the content type and language
header("Content-type: $documentMimeType; charset=$documentCharset");
header("Content-language: $currentLanguage");

// Set caching headers
header('Cache-Control: public, max-age=86400'); // Cache for 1 day
header('Expires: ' . gmdate('D, d M Y H:i:s', time() + 86400) . ' GMT'); // 1 day from now
$etag = md5_file(__FILE__);
header('ETag: "' . $etag . '"');
header('Last-Modified: ' . gmdate('D, d M Y H:i:s', filemtime(__FILE__)) . ' GMT');

// Additional headers
header('Vary: Accept');
