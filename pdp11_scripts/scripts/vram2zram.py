from sys import argv
import struct

with open(argv[1], "rb") as f:
	with open(argv[1]+".zram", "wb") as out:
		oldWord = f.read(2)
		wordCounter = 1
		print(oldWord, wordCounter)
		curWord = f.read(2)
		while curWord:
			if (curWord == oldWord):
				wordCounter += 1
			else:
				out.write(struct.pack("<H", wordCounter))
				out.write(struct.pack("B", oldWord[1])) #Convert to little-endian for better compatibility
				out.write(struct.pack("B", oldWord[0]))
				wordCounter = 1
				oldWord = curWord			
			print(curWord, wordCounter)	
			curWord = f.read(2)
		out.write(struct.pack("<H", wordCounter))
		out.write(struct.pack("B", oldWord[1])) #Convert to little-endian for better compatibility
		out.write(struct.pack("B", oldWord[0]))
