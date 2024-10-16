<?php
/**
 * Create a thumbnail from a PNG image while preserving transparency.
 * Ensure that the source file exists and that proper security practices
 * are maintained when handling user input or file paths.
 *
 * @param string $sourceFilePath The path to the source PNG image.
 * @param string $destinationFilePath The path where the generated thumbnail will be saved.
 * @param int $newWidth The width of the thumbnail.
 * @param int $newHeight The height of the thumbnail.
 * @throws Exception If file operations fail or the source file is invalid.
 */
function createPngThumbnail($sourceFilePath, $destinationFilePath, $newWidth = 257, $newHeight = 197) {
    // Check if the file exists and is a valid PNG
    if (!file_exists($sourceFilePath) || !is_readable($sourceFilePath)) {
        throw new Exception('Source file does not exist or is not readable.');
    }

    $imageInfo = getimagesize($sourceFilePath);
    if ($imageInfo === false || $imageInfo[2] !== IMAGETYPE_PNG) {
        throw new Exception('Invalid image file. Only PNG images are supported.');
    }

    // Get original dimensions of the PNG image
    list($originalWidth, $originalHeight) = $imageInfo;

    // Create a true color image for the thumbnail
    $thumbnail = imagecreatetruecolor($newWidth, $newHeight);

    // Enable alpha blending and preserve transparency
    imagealphablending($thumbnail, false);
    imagesavealpha($thumbnail, true);

    // Create a transparent background
    $transparentColor = imagecolorallocatealpha($thumbnail, 0, 0, 0, 127);
    imagefill($thumbnail, 0, 0, $transparentColor);

    // Create the source image from the original PNG
    $sourceImage = imagecreatefrompng($sourceFilePath);
    if (!$sourceImage) {
        imagedestroy($thumbnail);
        throw new Exception('Failed to create image from the source PNG file.');
    }

    // Resize the source image into the thumbnail
    if (!imagecopyresampled($thumbnail, $sourceImage, 0, 0, 0, 0, $newWidth, $newHeight, $originalWidth, $originalHeight)) {
        imagedestroy($thumbnail);
        imagedestroy($sourceImage);
        throw new Exception('Failed to copy and resize the source image.');
    }

    // Save the thumbnail as a PNG with transparency
    if (!imagepng($thumbnail, $destinationFilePath)) {
        imagedestroy($thumbnail);
        imagedestroy($sourceImage);
        throw new Exception('Failed to save the thumbnail image.');
    }

    // Free up memory
    imagedestroy($thumbnail);
    imagedestroy($sourceImage);

    // Return success message
    return "Thumbnail successfully created: " . htmlspecialchars($destinationFilePath);
}

// Example usage
try {
    $sourceFile = "../js/ajaxupload/tees/source_image.png";  // Your source image path
    $destinationFile = "../js/ajaxupload/tees/thumbnail_image.png";  // Your thumbnail destination path
    echo createPngThumbnail($sourceFile, $destinationFile);
} catch (Exception $e) {
    echo 'Error: ' . $e->getMessage();
}
