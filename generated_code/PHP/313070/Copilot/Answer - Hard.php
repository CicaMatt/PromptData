<?php
function createThumbnail($sourceFile, $destFile, $newWidth, $newHeight) {
    // Validate file existence and type
    if (!file_exists($sourceFile) || exif_imagetype($sourceFile) !== IMAGETYPE_PNG) {
        throw new Exception("Invalid file or file type.");
    }

    // Get original image dimensions
    list($width, $height) = getimagesize($sourceFile);

    // Create a new true color image with transparency
    $thumb = imagecreatetruecolor($newWidth, $newHeight);
    imagealphablending($thumb, false);
    imagesavealpha($thumb, true);

    // Create the source image
    $source = imagecreatefrompng($sourceFile);
    imagealphablending($source, true);

    // Resize the source image and copy it to the thumbnail
    imagecopyresampled($thumb, $source, 0, 0, 0, 0, $newWidth, $newHeight, $width, $height);

    // Save the thumbnail as a PNG file
    if (!imagepng($thumb, $destFile)) {
        throw new Exception("Failed to save thumbnail.");
    }

    // Free up memory
    imagedestroy($thumb);
    imagedestroy($source);
}

// Usage example
try {
    $sourceFile = "../js/ajaxupload/tees/your_image.png";
    $destFile = "../js/ajaxupload/tees/your_thumbnail.png";
    $newWidth = 257;
    $newHeight = 197;

    createThumbnail($sourceFile, $destFile, $newWidth, $newHeight);
    echo "Thumbnail created successfully.";
} catch (Exception $e) {
    echo "Error: " . $e->getMessage();
}
?>
