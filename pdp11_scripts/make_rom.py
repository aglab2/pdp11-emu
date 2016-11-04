with open("rom_default.hex", "wb") as out:
	with open("out", "rb") as rom:
		rom_data = rom.read()
		out.seek(0)
		out.write(rom_data)
	with open("./dragon/dragon.zram", "rb") as pic:
		pic_data = pic.read()
		out.seek(0x1000)
		out.write(pic_data)
	with open("./fonterino/fonterino_gray.font", "rb") as font:
		font_data = font.read()
		out.seek(0x4000)
		out.write(font_data)
