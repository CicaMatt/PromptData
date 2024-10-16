import cv2

video_path = 'Compton.mp4'  # Make sure this path is correct
vidcap = cv2.VideoCapture(video_path)
success, image = vidcap.read()
count = 0

while success:
    success, image = vidcap.read()
    if success:
        cv2.imwrite(f"frame{count}.jpg", image) 
        count += 1
    if cv2.waitKey(10) == 27: 
        break

vidcap.release()
cv2.destroyAllWindows()