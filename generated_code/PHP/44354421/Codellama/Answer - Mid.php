<?php
$youtube = new Google_Service_YouTube($client);
$channelId = "YOUR_CHANNEL_ID"; 
$liveStreamingId = $youtube->liveBroadcasts->listLiveBroadcasts("id,snippet", array('broadcastStatus' => 'active', 'mine' => true))[0]['id'];
$embedUrl = "https://www.youtube.com/embed/live_stream?channel={$channelId}&feature=chat";
echo "<iframe src='{$embedUrl}' width='100%' height='100%' frameborder='0' allowfullscreen></iframe>";
