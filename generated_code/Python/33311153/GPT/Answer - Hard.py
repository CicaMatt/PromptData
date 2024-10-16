import cv2
import os

def extract_frames(video_file, output_folder):
    # Check if the video file exists
    if not os.path.isfile(video_file):
        raise FileNotFoundError(f"The video file '{video_file}' does not exist.")

    # Ensure output folder exists
    os.makedirs(output_folder, exist_ok=True)
    
    # Initialize video capture object
    vidcap = cv2.VideoCapture(video_file)
    
    if not vidcap.isOpened():
        raise IOError(f"Failed to open video file '{video_file}'. Please check the file format and path.")
    
    count = 0
    success, image = vidcap.read()

    # Iterate over video frames
    while success:
        # Safely handle the case where image is None
        if image is None:
            break
        
        # Construct frame filename
        frame_filename = os.path.join(output_folder, f"frame{count}.jpg")
        
        # Save frame as JPEG file
        try:
            cv2.imwrite(frame_filename, image)
        except Exception as e:
            raise IOError(f"Error writing frame {count} to file: {e}")
        
        # Read the next frame
        success, image = vidcap.read()
        
        # Increment the frame counter
        count += 1
        
        # Exit on 'ESC' key press
        if cv2.waitKey(10) == 27:
            print("Escape key hit, exiting...")
            break

    # Release video capture object to free resources
    vidcap.release()
    cv2.destroyAllWindows()

    print(f"Extraction completed. Total frames saved: {count}")

# Usage
# Provide the video file and the output directory to save frames
try:
    extract_frames('Compton.mp4', './frames_output')
except Exception as e:
    print(f"An error occurred: {e}")
