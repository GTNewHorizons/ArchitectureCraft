package gcewing.architecture.client.render;

import static gcewing.architecture.compat.BlockCompatUtils.getWorldBlockState;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.ArchitectureCraftClient;
import gcewing.architecture.client.render.target.RenderTargetWorld;
import gcewing.architecture.common.block.BlockShape;
import gcewing.architecture.common.item.ArchitectureItemBlock;
import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.IBlockState;
import gcewing.architecture.compat.IOrientationHandler;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vec3i;
import gcewing.architecture.compat.Vector3;

public class PreviewRenderer {

    // Reusable tile
    public TileShape shapeTile = new TileShape();

    @SubscribeEvent
    public void onDrawBlockHighlight(DrawBlockHighlightEvent e) {
        if (!e.isCanceled()) {
            EntityPlayer player = e.player;
            World world = player.getEntityWorld();
            ItemStack stack = player.getHeldItem();

            if (stack == null || !(stack.getItem() instanceof ArchitectureItemBlock)) {
                return;
            }

            MovingObjectPosition target = e.target;
            BlockPos pos = new BlockPos(target.blockX, target.blockY, target.blockZ);
            EnumFacing sideHitEnum = EnumFacing.faceList[target.sideHit];
            BlockPos offset = pos.offset(sideHitEnum);
            if (target.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK
                    || !world.isAirBlock(offset.x, offset.y, offset.z))
                return;

            float hitX = (float) target.hitVec.xCoord;
            float hitY = (float) target.hitVec.yCoord;
            float hitZ = (float) target.hitVec.zCoord;
            Tessellator tess = Tessellator.instance;
            RenderTargetWorld renderTarget = new RenderTargetWorld(world, offset, tess, null);
            ShapeRenderDispatch shapeDispatcher = ArchitectureCraftClient.shapeRenderDispatch;
            BlockShape blockShape = ArchitectureCraft.content.blockShape;
            IOrientationHandler oh = blockShape.getOrientationHandler();
            IBlockState state = oh.onBlockPlaced(
                    blockShape,
                    world,
                    offset,
                    sideHitEnum,
                    hitX,
                    hitY,
                    hitZ,
                    blockShape.getDefaultState(),
                    player);

            shapeTile.xCoord = offset.x;
            shapeTile.yCoord = offset.y;
            shapeTile.zCoord = offset.z;
            shapeTile.setWorldObj(world);
            shapeTile.baseBlockState = null;
            shapeTile.readFromItemStack(stack);
            simulatePlacement(player, world, shapeTile, target);
            Trans3 translation = Trans3.blockCenter(offset)
                    .t(shapeTile.localToGlobalTransformation(Vector3.zero, state));

            double tX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) e.partialTicks;
            double tY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) e.partialTicks;
            double tZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) e.partialTicks;

            GL11.glPushMatrix();
            GL11.glTranslated(-tX, -tY, -tZ);
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE, GL11.GL_ZERO);
            tess.startDrawingQuads();
            shapeDispatcher.renderShapeTE(shapeTile, renderTarget, translation, true, false);
            tess.draw();
            GL11.glDepthMask(true);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    static final Vector3 hitVec = new Vector3(0, 0, 0);

    private void simulatePlacement(EntityPlayer player, IBlockAccess world, TileShape te, MovingObjectPosition hitPos) {
        final EnumFacing face = EnumFacing.faceList[hitPos.sideHit];
        final Vec3i d = Vector3.getDirectionVec(face);
        hitVec.set(
                hitPos.hitVec.xCoord - hitPos.blockX - d.getX() - 0.5,
                hitPos.hitVec.yCoord - hitPos.blockY - d.getY() - 0.5,
                hitPos.hitVec.zCoord - hitPos.blockZ - d.getZ() - 0.5);

        final BlockPos npos = new BlockPos(hitPos.blockX, hitPos.blockY, hitPos.blockZ).offset(face);
        final IBlockState nstate = getWorldBlockState(world, npos);
        final TileEntity nte = world.getTileEntity(npos.x, npos.y, npos.z);
        te.shape.orientOnPlacement(player, te, npos, nstate, nte, face, hitVec);
    }

}
