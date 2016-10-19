package romloader;

import memory.primitives.Word;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by aglab2 on 10/19/16.
 */
public class RomLoader {
    public Word[] rom;

    public RomLoader(String fileName) throws IOException{
        Path path = Paths.get(fileName);
        byte[] data = Files.readAllBytes(path);
        if (data.length % 2 != 0) {
            throw new IOException();
        }

        int romLength = data.length / 2;
        rom = new Word[romLength];

        DataInputStream romStream = new DataInputStream(new ByteArrayInputStream(data));
        for (int index = 0; index < romLength; index++)
            rom[index] = new Word(romStream.readShort());
    }
}
