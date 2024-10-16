import cv2
import os

def extract_frames(video_path, output_dir="frames", max_frames=None):
    """
    Extracts frames from a video and saves them as JPEG images.

    Args:
        video_path (str): The path to the video file.
        output_dir (str, optional): The directory to save the frames. Defaults to "frames".
        max_frames (int, optional): The maximum number of frames to extract. If None, extracts all frames.
    """

    # Create the output directory if it doesn't exist
    os.makedirs(output_dir, exist_ok=True)

    vidcap = cv2.VideoCapture(video_path)
    if not vidcap.isOpened():
        raise ValueError(f"Error opening video file: {video_path}")

    count = 0
    while True:
        success, image = vidcap.read()
        if not success:
            break

        if max_frames is not None and count >= max_frames:
            break

        output_path = os.path.join(output_dir, f"frame_{count:04d}.jpg")
        cv2.imwrite(output_path, image)
        count += 1

        if cv2.waitKey(1) == 27:  # Exit if Escape is hit
            break

    vidcap.release()
    cv2.destroyAllWindows()

if __name__ == "__main__":
    video_file = "Compton.mp4"  # Make sure this path is correct
    try:
        extract_frames(video_file)
        print("Frames extracted successfully!")
    except ValueError as e:
        print(f"Error: {e}")