import cv2

# Set starting and ending positions of the video in milliseconds
start_position = 0
end_position = 1000

# Open the video file
vidcap = cv2.VideoCapture('Compton.mp4')

# Set the starting position of the video
vidcap.set(cv2.CAP_PROP_POS_MSEC, start_position)

# Loop through each frame in the video and save it as an image
while True:
    # Read the current frame from the video
    success, image = vidcap.read()
    
    # Check if we have reached the end of the video
    if not success:
        break
    
    # Get the current position of the video in milliseconds
    current_position = int(vidcap.get(cv2.CAP_PROP_POS_MSEC))
    
    # Check if we are within the desired range of positions
    if start_position <= current_position <= end_position:
        # Save the frame as an image
        cv2.imwrite("frame%d.jpg" % count, image)

# Release the video capture object
vidcap.release()