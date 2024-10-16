<?php
// Get the current YouTube channel ID
$channelId = 'YOUR_CHANNEL_ID';

// Get the latest live stream ID for the specified channel
$youtube = new Google_Service_YouTube($client);
$liveStreamsResponse = $youtube->liveStreams->listLiveStreams('id,snippet', array(
  'channelId' => $channelId,
  'type' => 'live'
));
$latestLiveStreamId = $liveStreamsResponse[0]['id'];

// Generate the embed URL with the updated video ID
$embedUrl = 'https://www.youtube.com/embed/live_stream?videoId=' . $latestLiveStreamId;
