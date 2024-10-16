import cv2

# Open the video file
vidcap = cv2.VideoCapture('Compton.mp4')
success, image = vidcap.read()
count = 0

# Loop until no more frames are available
while success:
    if image is not None:
        # Save the frame as a JPEG file
        cv2.imwrite("frame%d.jpg" % count, image)

    # Read the next frame
    success, image = vidcap.read()

    # Increment the frame count
    count += 1

    # Exit if the escape key is pressed (ESC key has ASCII code 27)
    if cv2.waitKey(10) == 27:
        break
