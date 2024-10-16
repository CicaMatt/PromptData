<?php

class ImageResizer 
{
    private $fileName;
    private $newWidth;
    private $newHeight;
    private $thumb;

    public function __construct($fileName, $newWidth = 257, $newHeight = 197) 
    {
        $this->fileName = "../js/ajaxupload/tees/" . $fileName;
        $this->newWidth = $newWidth;
        $this->newHeight = $newHeight;
    }

    // Function to get original image dimensions
    private function getOriginalDimensions() 
    {
        return getimagesize($this->fileName);
    }

    // Function to create a thumbnail
    public function createThumbnail($newFilename) 
    {
        list($width, $height) = $this->getOriginalDimensions();

        // Create a new image with the specified dimensions
        $this->thumb = imagecreatetruecolor($this->newWidth, $this->newHeight);

        // Create the source image from the file
        $source = imagecreatefrompng($this->fileName);

        // Copy the original image to the thumbnail
        imagecopyresized($this->thumb, $source, 0, 0, 0, 0, $this->newWidth, $this->newHeight, $width, $height);

        // Set alpha blending and save alpha
        imagealphablending($this->thumb, true);
        imagesavealpha($this->thumb, true);

        // Save the thumbnail to a new file
        imagepng($this->thumb, $newFilename);
    }

    // Destructor to free up memory
    public function __destruct() 
    {
        if ($this->thumb) {
            imagedestroy($this->thumb);
        }
    }
}