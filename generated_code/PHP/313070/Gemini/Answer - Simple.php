<?php
$fileName= "../js/ajaxupload/tees/".$fileName;

list($width, $height) = getimagesize($fileName);

$newwidth = 257;
$newheight = 197;

$thumb = imagecreatetruecolor($newwidth, $newheight);

$transparent = imagecolorallocatealpha($thumb, 0, 0, 0, 127); // Preserve transparency

imagefill($thumb, 0, 0, $transparent);
imagecolortransparent($thumb, $transparent);
imagesavealpha($thumb, true);

$source = imagecreatefrompng($fileName);

// Copy the image while maintaining transparency
imagecopyresized($thumb, $source, 0, 0, 0, 0, $newwidth, $newheight, $width, $height);

imagepng($thumb,$newFilename);

imagedestroy($thumb); // Clean up thumbnail
imagedestroy($source); // Clean up source