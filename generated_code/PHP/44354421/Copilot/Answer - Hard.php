<?php
// Define a custom exception class for when the video ID is not found
class VideoIdNotFoundException extends Exception 
{}

function getLiveVideoID($channelId) 
{
    $videoId = null;
    
    // Fetch the YouTube live stream embed page content
    $data = file_get_contents('https://www.youtube.com/embed/live_stream?channel=' . $channelId);
    
    // Use regex to find the video ID
    if (preg_match('/"VIDEO_ID":"(.*?)"/', $data, $matches)) {
        $videoId = $matches[1];
    } else {
        // Throw the custom exception if the video ID is not found
        throw new VideoIdNotFoundException('Could not find video ID for the provided channel.');
    }
    
    return $videoId;
}

try {
    // Replace with your actual YouTube channel ID and domain URL
    $channelId = 'YOUR_CHANNEL_ID'; 
    $videoId = getLiveVideoID($channelId);
    
    $domainUrl = 'YOUR_DOMAIN_URL'; 
    $chatUrl = 'https://www.youtube.com/live_chat?v=' . $videoId . '&embed_domain=' . $domainUrl;
    
    echo 'The Chat URL is: ' . $chatUrl;
} catch (VideoIdNotFoundException $e) {
    // Catch and handle the custom exception for video ID not found
    echo 'ERROR: ' . $e->getMessage();
} catch (Exception $e) {
    // Catch any other generic exceptions
    echo 'An unexpected error occurred: ' . $e->getMessage();
}
?>
