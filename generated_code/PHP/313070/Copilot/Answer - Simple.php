<?php
$fileName = "../js/ajaxupload/tees/" . $fileName;

list($width, $height) = getimagesize($fileName);

$newwidth = 257;
$newheight = 197;

$thumb = imagecreatetruecolor($newwidth, $newheight);

// Enable alpha blending and save alpha flag for the thumbnail
imagealphablending($thumb, false);
imagesavealpha($thumb, true);

$source = imagecreatefrompng($fileName);

// Enable alpha blending for the source image
imagealphablending($source, true);

// Use imagecopyresampled instead of imagecopyresized for better quality
imagecopyresampled($thumb, $source, 0, 0, 0, 0, $newwidth, $newheight, $width, $height);

// Save the thumbnail with alpha channel
imagepng($thumb, $newFilename);

// Free up memory
imagedestroy($thumb);
imagedestroy($source);
