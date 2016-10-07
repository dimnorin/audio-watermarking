# audio-watermarking
Audio Water Marking Utility with LSB algorithm

Application usage:
```
Usage: java -jar watermark.jar [-w] [-c] input_file water_mark_msg password 

Options:
	 -w			 Watermarking (input_file).
	 -c			 Check if (input_file) was watermarked.
```
Where:

  - input_file - input audio file in wav format which you want to water mark.
  - water_mark_msg - message to be added into file.
  - password - password to encrypt message.

