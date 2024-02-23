package gcewing.architecture.network;

import java.io.DataOutput;

public interface ChannelOutput extends DataOutput {

    void write(byte[] b);

    void write(byte[] b, int off, int len);

    void write(int b);

    void writeBoolean(boolean v);

    void writeByte(int v);

    void writeBytes(String s);

    void writeChar(int v);

    void writeChars(String s);

    void writeDouble(double v);

    void writeFloat(float v);

    void writeInt(int v);

    void writeLong(long v);

    void writeShort(int v);

    void writeUTF(String s);

    void close();
}
