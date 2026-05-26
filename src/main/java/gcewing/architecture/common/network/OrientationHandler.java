package gcewing.architecture.common.network;

import net.minecraft.entity.player.EntityPlayer;

import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.BlockPos;

public class OrientationHandler {

    @ServerMessageHandler("SetOrientation")
    public void onSetOrientation(EntityPlayer player, ChannelInput data) {
        int x = data.readInt();
        int y = data.readInt();
        int z = data.readInt();
        byte side = data.readByte();
        byte turn = data.readByte();

        if (side < 0 || side > 5 || turn < 0 || turn > 3) return;

        double dist = player.getDistanceSq(x + 0.5, y + 0.5, z + 0.5);
        if (dist > 100.0) return;

        TileShape te = TileShape.get(player.worldObj, new BlockPos(x, y, z));
        if (te != null) {
            te.setSide(side);
            te.setTurn(turn);
            te.markChanged();
        }
    }
}
