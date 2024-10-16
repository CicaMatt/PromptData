<?php
$fileName = "../js/ajaxupload/tees/" . $fileName;
list($width, $height) = getimagesize($fileName);
$newwidth = 257;
$newheight = 197;

// Create a new image from the original file
$source = imagecreatefrompng($fileName);

// Create a new thumbnail image with transparency
$thumb = imagecreatetruecolor($newwidth, $newheight);
imagealphablending($thumb, true);
imagesavealpha($thumb, true);

// Copy the original image to the thumbnail image with resizing
imagecopyresized($thumb, $source, 0, 0, 0, 0, $newwidth, $newheight, $width, $height);

// Save the thumbnail image with transparency
imagepng($thumb, $newFilename);