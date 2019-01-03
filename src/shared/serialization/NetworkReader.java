package shared.serialization;

import shared.exceptions.NetworkWriterException;

import java.nio.ByteBuffer;

public class NetworkReader {
    private ByteBuffer byteBuffer;

    public NetworkReader() {}

    public NetworkReader(byte[] bytes) {
        byteBuffer = ByteBuffer.wrap(bytes);
    }

    public void setBuffer(byte[] bytes) {
        byteBuffer = ByteBuffer.wrap(bytes);
    }

    public boolean readBoolean() {
        return byteBuffer.get() == 1;
    }

    public float readFloat() {
        return byteBuffer.getFloat();
    }

    public short readShort() {
        return byteBuffer.getShort();
    }

    public long readLong() {
        return byteBuffer.getLong();
    }

    public int readInt() {
        return byteBuffer.getInt();
    }

    public byte[] readBytes(int byteAmt) {
        byte[] bytes = new byte[byteAmt];
        byteBuffer.get(bytes, 0, byteAmt);
        return bytes;
    }

    public String readString() {
        short stringLength = byteBuffer.getShort();
        byte[] bytes = new byte[stringLength];
        byteBuffer.get(bytes, 0, stringLength);

        return new String(bytes);
    }

    public char readChar() {
        return byteBuffer.getChar();
    }

    public double readDouble() {
        return byteBuffer.getDouble();
    }

    public byte readByte() {
        return byteBuffer.get();
    }

    public byte[] getBytes() {
        return byteBuffer.array();
    }
}
