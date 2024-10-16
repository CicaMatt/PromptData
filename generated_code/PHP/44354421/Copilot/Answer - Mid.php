<?php
// Your YouTube API key
$apiKey = 'YOUR_YOUTUBE_API_KEY';
// Your YouTube channel ID
$channelId = 'YOUR_CHANNEL_ID';

// Fetch the live video ID
$apiUrl = "https://www.googleapis.com/youtube/v3/search?part=id&channelId={$channelId}&eventType=live&type=video&key={$apiKey}";
$response = file_get_contents($apiUrl);
$data = json_decode($response, true);

if (!empty($data['items'])) {
    $videoId = $data['items'][0]['id']['videoId'];
    $embedDomain = 'YOUR_DOMAIN_URL';

    // Embed URLs
    $liveStreamUrl = "https://www.youtube.com/embed/live_stream?channel={$channelId}";
    $liveChatUrl = "https://www.youtube.com/live_chat?v={$videoId}&embed_domain={$embedDomain}";

    echo "Live Stream URL: <iframe src='{$liveStreamUrl}'></iframe>";
    echo "Live Chat URL: <iframe src='{$liveChatUrl}'></iframe>";
} else {
    echo "No live stream currently available.";
}
?>
