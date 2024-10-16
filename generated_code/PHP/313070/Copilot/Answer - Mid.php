<?php
$fileName = "../js/ajaxupload/tees/" . $fileName;

list($width, $height) = getimagesize($fileName);

$newwidth = 257;
$newheight = 197;

$thumb = imagecreatetruecolor($newwidth, $newheight);

// Preserve transparency
imagealphablending($thumb, false);
imagesavealpha($thumb, true);

$source = imagecreatefrompng($fileName);

// Preserve transparency in the source image
imagealphablending($source, true);
imagesavealpha($source, true);

imagecopyresampled($thumb, $source, 0, 0, 0, 0, $newwidth, $newheight, $width, $height);

imagepng($thumb, $newFilename);
