import cv2

vidcap = cv2.VideoCapture('Compton.mp4')
success, image = vidcap.read()
count = 0

while success:
    cv2.imwrite("frame%d.jpg" % count, image)  # save frame as JPEG file
    success, image = vidcap.read()  # read the next frame
    if cv2.waitKey(10) == 27:  # exit if Escape is hit
        break
    count += 1
    if count >= 1000:  # limit the frames to 1000
        break
