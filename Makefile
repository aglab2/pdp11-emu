all:
	make -C pdp11_sources
	python make_rom.py
