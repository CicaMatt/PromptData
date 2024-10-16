import cv2
vidcap = cv2.VideoCapture('Compton.mp4')
success,image = vidcap.read()
count = 0
while success:
    # Check if the video capture device is valid and can be opened
    if not vidcap.isOpened():
        break
    
    # Save each frame as a unique JPEG file using the current frame number
    cv2.imwrite("frame%d.jpg" % count, image)
    
    # Increment the frame counter and check for Escape key press
    count += 1
    if cv2.waitKey(10) == 27:
        break

# Release resources before exiting the program
vidcap.release()