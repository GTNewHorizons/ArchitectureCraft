// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Data Channel Networking
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.common.network;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler.OutboundTarget;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.channel.ChannelHandler;

public class DataChannel {

    public final String name;
    public final List handlers = new ArrayList();
    protected final EnumMap<Side, FMLEmbeddedChannel> pipes;

    public DataChannel(String name, Object... handlers) {
        this.name = name;
        ChannelHandler handler = new DataHandler(this);
        pipes = NetworkRegistry.INSTANCE.newChannel(name, handler);
        this.handlers.add(this);
        this.handlers.addAll(Arrays.asList(handlers));
    }

    protected ChannelOutput openTarget(String message, Side fromSide, OutboundTarget target) {
        return openTarget(message, fromSide, target, null);
    }

    protected ChannelOutput openTarget(String message, Side fromSide, OutboundTarget target, Object arg) {
        ChannelOutput out = new DataPacket(this, fromSide, target, arg);
        out.writeUTF(message);
        return out;
    }

    public ChannelOutput openServer(String message) {
        return openTarget(message, Side.CLIENT, OutboundTarget.TOSERVER);
    }

    public ChannelOutput openPlayer(EntityPlayer player, String message) {
        return openTarget(message, Side.SERVER, OutboundTarget.PLAYER, player);
    }

    public ChannelOutput openAllPlayers(String message) {
        return openTarget(message, Side.SERVER, OutboundTarget.ALL);
    }

    public ChannelOutput openAllAround(NetworkRegistry.TargetPoint point, String message) {
        return openTarget(message, Side.SERVER, OutboundTarget.ALLAROUNDPOINT, point);
    }

    public ChannelOutput openDimension(int dimensionId, String message) {
        return openTarget(message, Side.SERVER, OutboundTarget.DIMENSION, dimensionId);
    }

    public ChannelOutput openServerContainer(String message) {
        ChannelOutput out = openServer(".container.");
        out.writeUTF(message);
        return out;
    }

    public ChannelOutput openClientContainer(EntityPlayer player, String message) {
        ChannelOutput out = openPlayer(player, ".container.");
        out.writeUTF(message);
        return out;
    }

    @ServerMessageHandler(".container.")
    public void onServerContainerMessage(EntityPlayer player, ChannelInput data) {
        String message = data.readUTF();
        doServerDispatch(player.openContainer, message, player, data);
    }

    @SideOnly(Side.CLIENT)
    @ClientMessageHandler(".container.")
    public void onClientContainerMessage(ChannelInput data) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        String message = data.readUTF();
        doClientDispatch(player.openContainer, message, data);
    }

    protected void onReceiveFromClient(EntityPlayer player, ChannelInput data) {
        String message = data.readUTF();
        for (Object h : handlers) if (serverDispatch(h, message, player, data)) return;
        System.out.printf("No ServerMessageHandler for '%s' found in registered handlers of %s\n", message, this);
    }

    public static void doServerDispatch(Object handler, String message, EntityPlayer player, ChannelInput data) {
        if (!serverDispatch(handler, message, player, data))
            System.out.printf("No ServerMessageHandler for '%s' found in %s\n", message, handler.getClass().getName());
    }

    public static boolean serverDispatch(Object handler, String message, EntityPlayer player, ChannelInput data) {
        if (handler != null) {
            // Method meth = findHandlerMethod(handler, message, HandlerAnnotation.SERVER, serverHandlerCaches);
            Method meth = HandlerMap.SERVER.get(handler, message);
            if (meth != null) {
                try {
                    meth.invoke(handler, player, data);
                } catch (Exception e) {
                    throw new RuntimeException(
                            String.format(
                                    "Exception while calling server-side handler %s.%s for message %s",
                                    handler.getClass().getName(),
                                    meth.getName(),
                                    message),
                            e);
                }
                return true;
            }
        }
        return false;
    }

    protected void onReceiveFromServer(ChannelInput data) {
        String message = data.readUTF();
        for (Object h : handlers) if (clientDispatch(h, message, data)) return;
        System.out.printf("No ClientMessageHandler for '%s' found in registered handlers\n", message);
    }

    public static void doClientDispatch(Object handler, String message, ChannelInput data) {
        if (!clientDispatch(handler, message, data))
            System.out.printf("No ClientMessageHandler for '%s' found in %s\n", message, handler.getClass().getName());
    }

    public static boolean clientDispatch(Object handler, String message, ChannelInput data) {
        if (handler != null) {
            // Method meth = findHandlerMethod(handler, message, HandlerAnnotation.CLIENT, clientHandlerCaches);
            Method meth = HandlerMap.CLIENT.get(handler, message);
            if (meth != null) {
                try {
                    meth.invoke(handler, data);
                } catch (Exception e) {
                    throw new RuntimeException(
                            String.format(
                                    "Exception while calling client-side handler %s.%s for message %s",
                                    handler.getClass().getName(),
                                    meth.getName(),
                                    message),
                            e);
                }
                return true;
            }
        }
        return false;
    }
}
