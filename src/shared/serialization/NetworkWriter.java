package shared.serialization;

import shared.exceptions.NetworkWriterException;

import java.nio.ByteBuffer;

public class NetworkWriter {
    private ByteBuffer byteBuffer;

    public NetworkWriter() {
        byteBuffer = ByteBuffer.allocate(512);
    }

    public NetworkWriter(int bufferSize) {
        byteBuffer = ByteBuffer.allocate(bufferSize);
    }

    public void write(int value) {
        byteBuffer.putInt(value);
    }

    public void write(char value) {
        byteBuffer.putChar(value);
    }

    public void write(float value) {
        byteBuffer.putFloat(value);
    }

    public void write(long value) {
        byteBuffer.putLong(value);
    }

    public void write(short value) {
        byteBuffer.putShort(value);
    }

    public void write(byte value) {
        byteBuffer.put(value);
    }

    public void write(boolean value) {
        byteBuffer.put(value == true ? (byte) 1 : (byte) 0);
    }

    public void write(double value) {
        byteBuffer.putDouble(value);
    }

    public void write(byte[] byteArray) {
        byteBuffer.put(byteArray);
    }

    /**
     * Reading / Writing strings can be slow if done hundred times per frame. Try not to send strings as much
     * as possible. Primitive types are much more efficient for speed.
     * @param string
     * @throws NetworkWriterException If the string is over 256 bytes. If long strings are needed then add a byte array.
     */
    public void write(String string) throws NetworkWriterException {
        byte[] bytes = string.getBytes();
        if (bytes.length > 256) {
            throw new NetworkWriterException("Writing strings aver 256 bytes is not supported.");
        }

        byteBuffer.putShort((short) string.length());
        byteBuffer.put(bytes);
    }

    /**
     * Returns an array containing the written bytes. The array will only contain written bytes, and may be smaller
     * than the initialized buffer size if not all the bytes are used. After this operation the backing array will be
     * cleared and position will be set to 0. This Writer can then be reused.
     * @return Byte array containing written bytes
     */
    public byte[] getBytesAndReset() {
        byte[] bytes = new byte[byteBuffer.position()];
        byte[] bufferBytes = byteBuffer.array();

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = bufferBytes[i];
            bufferBytes[i] = 0;
        }
        byteBuffer.position(0);

        return bytes;
    }
}
