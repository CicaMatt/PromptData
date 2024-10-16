<?php
// Replace with your YouTube Data API key and channel ID
$apiKey = 'YOUR_YOUTUBE_API_KEY';
$channelId = 'YOUR_CHANNEL_ID';

// YouTube API URL to get the current live video
$apiUrl = "https://www.googleapis.com/youtube/v3/search?part=id&channelId={$channelId}&eventType=live&type=video&key={$apiKey}";

// Initialize cURL session
$curl = curl_init();

// Set cURL options
curl_setopt($curl, CURLOPT_URL, $apiUrl);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);

// Execute the request and get the response
$response = curl_exec($curl);
curl_close($curl);

// Parse the response
$data = json_decode($response, true);

// Check if there's an active live stream
if (!empty($data['items'])) {
    $videoId = $data['items'][0]['id']['videoId'];
    // Your domain URL for embedding
    $domainUrl = 'YOUR_DOMAIN_URL';
    
    // Live chat embed URL
    $liveChatUrl = "https://www.youtube.com/live_chat?v={$videoId}&embed_domain={$domainUrl}";
    
    // Output the iframe embed code for live chat
    echo '<iframe src="' . $liveChatUrl . '" width="400" height="600"></iframe>';
} else {
    echo "No live stream is currently active.";
}

