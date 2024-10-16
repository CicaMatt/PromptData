<?php
// Set the cache level to "private"
session_cache_limiter('private');

// Set the maximum age for the cache to 180 seconds (3 minutes)
session_cache_expire(180);

// Send the HTTP headers
header("Content-type: $documentMimeType; charset=$documentCharset");
header('Vary: Accept');
header("Content-language: $currentLanguage");