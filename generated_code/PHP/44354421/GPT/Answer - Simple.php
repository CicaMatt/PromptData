<?php
// Replace with your YouTube API Key and Channel ID
$apiKey = 'YOUR_YOUTUBE_API_KEY';
$channelId = 'YOUR_CHANNEL_ID';

// Get the live stream video ID
$apiUrl = "https://www.googleapis.com/youtube/v3/search?part=id&channelId=$channelId&eventType=live&type=video&key=$apiKey";
$response = file_get_contents($apiUrl);
$data = json_decode($response, true);

// Check if there is a live stream currently active
if (!empty($data['items'])) {
    $videoId = $data['items'][0]['id']['videoId'];
    
    // Embed the live chat
    echo '<iframe src="https://www.youtube.com/live_chat?v=' . $videoId . '&embed_domain=' . $_SERVER['HTTP_HOST'] . '" width="400" height="600" frameborder="0"></iframe>';
} else {
    echo 'No live stream currently active.';
}
?>
