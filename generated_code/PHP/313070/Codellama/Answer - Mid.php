<?php
// Get the original image file
$originalImage = imagecreatefrompng('path/to/image.png');

// Set the new dimensions for the thumbnail
$newWidth = 257;
$newHeight = 197;

// Create a new empty image with the same color as the original image
$thumb = imagecreatetruecolor($newWidth, $newHeight);
imagealphablending($thumb, true);
imagesavealpha($thumb, true);

// Copy the original image into the thumbnail, preserving transparency
imagecopyresampled($thumb, $originalImage, 0, 0, 0, 0, $newWidth, $newHeight, $width, $height);

// Save the thumbnail to a new file
imagepng($thumb, 'path/to/thumbnail.png');
?>