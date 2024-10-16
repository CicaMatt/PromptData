<?php

// Custom exceptions for better error handling
class FileUploadException extends Exception {}
class FileSizeException extends FileUploadException {}
class FileTypeException extends FileUploadException {}
class FileMoveException extends FileUploadException {}

class FileUploader
{
    private array $allowedFileTypes;
    private int $maxFileSize;

    public function __construct(array $allowedFileTypes, int $maxFileSize)
    {
        $this->allowedFileTypes = $allowedFileTypes;
        $this->maxFileSize = $maxFileSize;
    }

    /**
     * Handles file upload and throws exceptions in case of failure.
     *
     * @param array $file The file array from a safe getter method
     * @param string $targetDirectory The directory where the file will be moved
     *
     * @throws FileUploadException if any file upload validation fails
     * @return string Success message
     */
    public function uploadFile(array $file, string $targetDirectory): string
    {
        // Ensure the upload was successful
        if ($file['error'] !== UPLOAD_ERR_OK) {
            throw new FileUploadException('File upload error: ' . $file['error']);
        }

        // Validate file size
        if ($file['size'] > $this->maxFileSize) {
            throw new FileSizeException('File size exceeds the 2MB limit.');
        }

        // Validate file type
        if (!in_array($file['type'], $this->allowedFileTypes)) {
            throw new FileTypeException('Invalid file type.');
        }

        // Set target file path
        $targetFilePath = $targetDirectory . '/' . basename($file['name']);

        // Try to move the uploaded file to the target directory
        if (!move_uploaded_file($file['tmp_name'], $targetFilePath)) {
            throw new FileMoveException('Failed to move uploaded file.');
        }

        // Return success message if upload is successful
        return 'Upload Complete!';
    }

    /**
     * Safely retrieve the uploaded file from $_FILES superglobal.
     *
     * @param string $key The key of the file in the $_FILES array
     * @return array|null The file array or null if not present
     */
    public function getFile(string $key): ?array
    {
        return $_FILES[$key] ?? null;
    }

    /**
     * Safely retrieve the document root from $_SERVER superglobal.
     *
     * @return string The document root directory
     */
    public function getDocumentRoot(): string
    {
        return $_SERVER['DOCUMENT_ROOT'] ?? '';
    }
}

try {
    // Initialize allowed file types and max file size
    $allowedFileTypes = ['image/jpeg', 'image/png', 'application/pdf'];
    $maxFileSize = 2 * 1024 * 1024; // 2MB

    // Instantiate the FileUploader class
    $uploader = new FileUploader($allowedFileTypes, $maxFileSize);

    // Directory to upload files
    $uploadDirectory = $uploader->getDocumentRoot() . '/uploads';

    // Check if a file is uploaded using the safe getter
    $file = $uploader->getFile('file');
    if ($file === null) {
        throw new FileUploadException('No file uploaded.');
    }

    // Call the uploadFile function
    $result = $uploader->uploadFile($file, $uploadDirectory);

    // Display success message
    echo $result;

} catch (FileUploadException $e) {
    // Log the error details for debugging (don't expose this info to users in production)
    error_log($e->getMessage());

    // Display a generic error message to the user
    echo 'An error occurred during file upload. Please try again.';
}
