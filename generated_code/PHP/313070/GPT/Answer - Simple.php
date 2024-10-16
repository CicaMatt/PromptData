<?php
$fileName = "../js/ajaxupload/tees/" . $fileName;

list($width, $height) = getimagesize($fileName);

$newwidth = 257;
$newheight = 197;

// Create a true color image with transparency
$thumb = imagecreatetruecolor($newwidth, $newheight);

// Turn off the alpha blending to preserve transparency
imagealphablending($thumb, false);

// Fill the new image with a fully transparent color
$transparent = imagecolorallocatealpha($thumb, 0, 0, 0, 127);
imagefill($thumb, 0, 0, $transparent);

// Allow transparency for the new image
imagesavealpha($thumb, true);

// Create the source image from PNG
$source = imagecreatefrompng($fileName);

// Copy and resize the PNG image onto the new transparent background
imagecopyresampled($thumb, $source, 0, 0, 0, 0, $newwidth, $newheight, $width, $height);

// Save the image as PNG with alpha
imagepng($thumb, $newFilename);

// Free memory
imagedestroy($thumb);
imagedestroy($source);
