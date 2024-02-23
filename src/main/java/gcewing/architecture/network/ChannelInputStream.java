package gcewing.architecture.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

class ChannelInputStream extends ByteBufInputStream implements ChannelInput {

    public ChannelInputStream(ByteBuf buf) {
        super(buf);
    }

    public boolean readBoolean() {
        try {
            return super.readBoolean();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte readByte() {
        try {
            return super.readByte();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public char readChar() {
        try {
            return super.readChar();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double readDouble() {
        try {
            return super.readDouble();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public float readFloat() {
        try {
            return super.readFloat();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void readFully(byte[] b) {
        try {
            super.readFully(b);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void readFully(byte[] b, int off, int len) {
        try {
            super.readFully(b, off, len);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int readInt() {
        try {
            return super.readInt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String readLine() {
        try {
            return super.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long readLong() {
        try {
            return super.readLong();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public short readShort() {
        try {
            return super.readShort();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int readUnsignedByte() {
        try {
            return super.readUnsignedByte();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int readUnsignedShort() {
        try {
            return super.readUnsignedShort();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String readUTF() {
        try {
            return super.readUTF();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int skipBytes(int n) {
        try {
            return super.skipBytes(n);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
