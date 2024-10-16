import cv2
import os

# Function to safely extract frames from a video
def extract_frames(video_file, output_folder):
    try:
        # Ensure video file exists
        if not os.path.exists(video_file):
            print(f"Error: The video file {video_file} does not exist.")
            return
        
        # Create output directory if it doesn't exist
        if not os.path.exists(output_folder):
            os.makedirs(output_folder)

        # Open the video file
        vidcap = cv2.VideoCapture(video_file)

        # Check if video opened successfully
        if not vidcap.isOpened():
            print(f"Error: Unable to open video file {video_file}")
            return

        count = 0
        success, image = vidcap.read()

        while success:
            # Save the current frame as a JPEG file
            output_filename = os.path.join(output_folder, f"frame{count}.jpg")
            cv2.imwrite(output_filename, image)

            # Read the next frame
            success, image = vidcap.read()

            # Increment frame counter
            count += 1

            # Exit on Escape key press
            if cv2.waitKey(10) == 27:
                print("Escape key pressed. Exiting.")
                break
        
        # Release the video capture object
        vidcap.release()
        print(f"Done! {count} frames extracted to {output_folder}")

    except Exception as e:
        print(f"An error occurred: {e}")

# Usage example
video_file = 'Compton.mp4'
output_folder = 'frames'

extract_frames(video_file, output_folder)
