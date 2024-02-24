package gcewing.architecture.common.network;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;

class DataPacket implements ChannelOutput {

    final ByteBufOutputStream out;
    final DataChannel channel;
    final Side side;
    final FMLOutboundHandler.OutboundTarget target;
    final Object arg;

    DataPacket(DataChannel channel, Side side, FMLOutboundHandler.OutboundTarget target, Object arg) {
        out = new ByteBufOutputStream(Unpooled.buffer());
        this.channel = channel;
        this.side = side;
        this.target = target;
        this.arg = arg;
    }

    public void write(byte[] b) {
        try {
            out.write(b);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void write(byte[] b, int off, int len) {
        try {
            out.write(b, off, len);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void write(int b) {
        try {
            out.write(b);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeBoolean(boolean v) {
        try {
            out.writeBoolean(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeByte(int v) {
        try {
            out.writeByte(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeBytes(String s) {
        try {
            out.writeBytes(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeChar(int v) {
        try {
            out.writeChar(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeChars(String s) {
        try {
            out.writeChars(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeDouble(double v) {
        try {
            out.writeDouble(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeFloat(float v) {
        try {
            out.writeFloat(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeInt(int v) {
        try {
            out.writeInt(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeLong(long v) {
        try {
            out.writeLong(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeShort(int v) {
        try {
            out.writeShort(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeUTF(String s) {
        try {
            out.writeUTF(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ByteBuf payload = out.buffer();
        Packet pkt = new FMLProxyPacket(new PacketBuffer(payload), channel.name);
        FMLEmbeddedChannel pipe = channel.pipes.get(side);
        pipe.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(target);
        pipe.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(arg);
        pipe.writeAndFlush(pkt).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

}
