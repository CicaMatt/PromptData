<?php
$channelId = 'YOUR_CHANNEL_ID';
$apiKey = 'YOUR_API_KEY';
$apiUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId={$channelId}&type=video&eventType=live&key={$apiKey}";

$response = file_get_contents($apiUrl);
$data = json_decode($response, true);

if (!empty($data['items'])) {
    $videoId = $data['items'][0]['id']['videoId'];
    $embedUrl = "https://www.youtube.com/live_chat?v={$videoId}&embed_domain=YOUR_DOMAIN";
    echo "<iframe width='350px' height='500px' src='{$embedUrl}'></iframe>";
} else {
    echo "No live stream currently available.";
}
?>
