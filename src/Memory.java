import java.nio.ByteBuffer;

/**
 * Created by aglab2(Denis Kopyrin) on 07/10/16.
 */

public class Memory {
    private byte[] raw_data = new byte[65535];
    private ByteBuffer buffer = ByteBuffer.wrap(raw_data);

    short fetch_data(short address) {
        return buffer.getShort(address);
    }

    void load_data(short address, short value) {
        buffer.putShort(address, value);
    }
}
