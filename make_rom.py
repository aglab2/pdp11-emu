def put_region(rom, name, offset):
	with open(name, "rb") as source:
		data = source.read()
		rom.seek(offset)
		rom.write(data)

with open("./resources/default.rom", "wb") as out:
	put_region(out, "./pdp11_sources/source.pdp", 0x0000)
	put_region(out, "./pdp11_scripts/dragon/dragon.zram", 0x1000)
	put_region(out, "./pdp11_scripts/fonterino/fonterino_gray.font", 0x4000)
	put_region(out, "./pdp11_sources/keyboardh.pdp", 0x7000)