from sys import argv
import struct

#colorsDict = {0: 0, 1: 2, 2: 3, 3: 1} #dragon
colorsDict = {0: 0, 1: 3, 2: 0, 3: 0} #apple, font


with open(argv[1], "rb") as f:
	with open(argv[1]+".vram", "wb") as out:
		byteColors = f.read(4)
		print(byteColors)
		while byteColors:
			colors = [colorsDict[i] for i in byteColors]
			byte = colors[0] << 6 | colors[1] << 4 | colors[2] << 2 | colors[3]
			print (format(byte, '08b'))
			print (colors)
			print ()
			out.write(struct.pack("B", byte))
			byteColors = f.read(4)
