<?php

$fileName = "../js/ajaxupload/tees/" . $fileName;

list($width, $height) = getimagesize($fileName);

$newwidth = 257;
$newheight = 197;

// Create a new true color image with the desired size
$thumb = imagecreatetruecolor($newwidth, $newheight);

// Turn off the blending mode to preserve transparency
imagealphablending($thumb, false);

// Create a transparent background color for the thumbnail
$transparentColor = imagecolorallocatealpha($thumb, 0, 0, 0, 127);
imagefill($thumb, 0, 0, $transparentColor);

// Enable the saving of full alpha channel information
imagesavealpha($thumb, true);

// Load the source PNG image
$source = imagecreatefrompng($fileName);

// Resize the PNG image and preserve transparency
imagecopyresampled($thumb, $source, 0, 0, 0, 0, $newwidth, $newheight, $width, $height);

// Save the new image as PNG and output it to the destination file
imagepng($thumb, $newFilename);

// Free up memory
imagedestroy($thumb);
imagedestroy($source);

?>
