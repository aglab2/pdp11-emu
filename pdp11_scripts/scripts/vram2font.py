from sys import argv
import struct

with open(argv[1], "rb") as f:
	with open(argv[1]+".font", "wb") as out:
		n = 0	
		curWord = f.read(2)
		while curWord:
			j = n % 95
			i = int((n - j) / 95)
			out.seek(2*(16*j + i))
			print(curWord[1])
			out.write(struct.pack("B", curWord[1])) #Convert to little-endian for better compatibility
			out.write(struct.pack("B", curWord[0]))
			curWord = f.read(2)
			n += 1
