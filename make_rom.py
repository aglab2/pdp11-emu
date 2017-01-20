import sys

if len(sys.argv) < 3:
	print ("Not enough arguments! Provide rom name and pdp source name")
	exit(1)
	
def put_region(rom, name, offset):
	with open(name, "rb") as source:
		data = source.read()
		rom.seek(offset)
		rom.write(data)

with open("./resources/" + sys.argv[1], "wb") as out:
	put_region(out, "./pdp11_sources/" + sys.argv[2], 0x0000)
	put_region(out, "./pdp11_scripts/dragon/dragon.zram", 0x1000)
	put_region(out, "./pdp11_scripts/fonterino/fonterino_gray.font", 0x4000)
	put_region(out, "./pdp11_sources/keyboardh.pdp", 0x7000)