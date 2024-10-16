<?php
// Function to fetch the current live stream ID of a YouTube channel
function get_current_live_stream_id($channel_id, $api_key) {
  $url = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=$channel_id&eventType=live&type=video&key=$api_key";
  $response = file_get_contents($url);
  $data = json_decode($response, true);

  if ($data['pageInfo']['totalResults'] > 0) {
    return $data['items'][0]['id']['videoId'];
  } else {
    return null; 
  }
}

// Assuming you have the channel ID and API key stored
$channel_id = 'YOUR_CHANNEL_ID';
$api_key = 'YOUR_API_KEY';
$domain_url = 'YOUR_DOMAIN_URL'; 

// Fetch the current live stream ID
$live_stream_id = get_current_live_stream_id($channel_id, $api_key);

// Construct the live chat embed URL dynamically
if ($live_stream_id) {
  $live_chat_url = "https://www.youtube.com/live_chat?v=$live_stream_id&embed_domain=$domain_url";
} else {
  $live_chat_url = ""; 
}

// Output the embed code (you might want to wrap this in HTML)
echo "<iframe src='$live_chat_url' allowfullscreen></iframe>";
