import cv2

# Update with the correct path if needed
video_path = 'Compton.mp4'  
vidcap = cv2.VideoCapture(video_path)

success, image = vidcap.read()
count = 0

while success:
    success, image = vidcap.read()
    cv2.imwrite("frame%d.jpg" % count, image) 
    if cv2.waitKey(10) == 27: 
        break
    count += 1

# Release the video capture object when done
vidcap.release()  
# Close any OpenCV windows that might be open
cv2.destroyAllWindows()  