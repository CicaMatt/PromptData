<?php
$fileName = "../js/ajaxupload/tees/" . $fileName;

list($width, $height) = getimagesize($fileName);

$newwidth = 257;
$newheight = 197;

$thumb = imagecreatetruecolor($newwidth, $newheight);

// Preserve transparency
$transparent = imagecolorallocatealpha($thumb, 0, 0, 0, 127);
imagefill($thumb, 0, 0, $transparent);
imagecolortransparent($thumb, $transparent);
imagesavealpha($thumb, true);

$source = imagecreatefrompng($fileName);

imagecopyresized($thumb, $source, 0, 0, 0, 0, $newwidth, $newheight, $width, $height);

imagepng($thumb, $newFilename);

imagedestroy($thumb);
imagedestroy($source);