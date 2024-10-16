<?php
function GetImageFromUrl($link)
{
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_POST, 0);
    curl_setopt($ch, CURLOPT_URL, $link);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    $result = curl_exec($ch);
    curl_close($ch);
    return $result;
}

$sourcecode = GetImageFromUrl($iticon);
$savefile = fopen(' /img/uploads/' . $iconfilename, 'w');
fwrite($savefile, $sourcecode);
fclose($savefile);