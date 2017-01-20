all:
	make -C pdp11_sources
	python make_rom.py default.rom source.pdp
	python make_rom.py keyboard.rom kb.pdp
	python make_rom.py picture.rom pic.pdp