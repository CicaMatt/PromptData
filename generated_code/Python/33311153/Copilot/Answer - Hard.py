import cv2
import os

def extract_frames(video_path, output_folder, frame_limit=1000):
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)

    vidcap = cv2.VideoCapture(video_path)
    if not vidcap.isOpened():
        print(f"Error: Unable to open video file {video_path}")
        return

    count = 0
    while count < frame_limit:
        success, image = vidcap.read()
        if not success:
            break

        frame_path = os.path.join(output_folder, f"frame{count}.jpg")
        cv2.imwrite(frame_path, image)
        count += 1

        if cv2.waitKey(10) == 27:  # exit if Escape is hit
            break

    vidcap.release()
    print(f"Extracted {count} frames to {output_folder}")

# Usage
extract_frames('Compton.mp4', 'output_frames')
