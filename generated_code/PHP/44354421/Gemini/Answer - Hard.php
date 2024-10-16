<?php
// Get the current YouTube URL from the user or another source
$currentYouTubeURL = "https://www.youtube.com/watch?v=SOME_VIDEO_ID"; 

// Extract the video ID from the YouTube URL using regular expressions
preg_match("/v=([^&]+)/", $currentYouTubeURL, $matches);
$videoID = $matches[1];

// Construct the live chat embed URL with the extracted video ID
$liveChatEmbedURL = "https://www.youtube.com/live_chat?v=$videoID&embed_domain=YOUR_DOMAIN_URL";

// Display the live chat embed URL or embed it directly into your HTML
echo "<iframe src='$liveChatEmbedURL' width='560' height='315' frameborder='0' allowfullscreen></iframe>";
