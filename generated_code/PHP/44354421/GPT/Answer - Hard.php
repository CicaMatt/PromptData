<?php
// Your YouTube API key and channel ID
$apiKey = 'YOUR_YOUTUBE_API_KEY';
$channelId = 'YOUR_CHANNEL_ID';

// Function to get the live video ID
function getLiveVideoId($apiKey, $channelId) {
    $apiUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId={$channelId}&type=video&eventType=live&key={$apiKey}";

    // Use cURL to send request to YouTube Data API
    $curl = curl_init();
    curl_setopt($curl, CURLOPT_URL, $apiUrl);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, true);
    $response = curl_exec($curl);
    curl_close($curl);

    // Parse the response
    $data = json_decode($response, true);

    // Check if a live stream is active and return the video ID
    if (!empty($data['items'])) {
        return $data['items'][0]['id']['videoId'];
    } else {
        return null; // No live stream is active
    }
}

// Get the current live stream video ID
$videoId = getLiveVideoId($apiKey, $channelId);

if ($videoId) {
    // Generate live chat embed URL
    $domainUrl = $_SERVER['HTTP_HOST']; // Automatically detect the current domain
    $chatEmbedUrl = "https://www.youtube.com/live_chat?v={$videoId}&embed_domain={$domainUrl}";

    // Display the embed iframe for the live chat
    echo '<iframe src="' . htmlspecialchars($chatEmbedUrl) . '" width="400" height="600" frameborder="0"></iframe>';
} else {
    echo "No live stream is currently active.";
}
?>
