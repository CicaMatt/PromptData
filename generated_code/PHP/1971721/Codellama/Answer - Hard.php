<?php
// Set cache headers
header("Cache-Control: max-age=180"); 
header("Vary: Accept");
header("Content-language: $currentLanguage");

// Output the cached content
if (isset($_SERVER['HTTP_IF_MODIFIED_SINCE']) &&
    strtotime($_SERVER['HTTP_IF_MODIFIED_SINCE']) == filemtime($cachedFile)) {
  header("Not-Modified", true, 304);
  exit;
}

// If the page is not cached or if it has been modified since the last cache, regenerate the content and store it
$cachedContent = generateCachedContent();
file_put_contents($cachedFile, $cachedContent);

// Output the cached content
echo $cachedContent;