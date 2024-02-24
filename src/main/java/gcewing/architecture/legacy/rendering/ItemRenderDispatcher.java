package gcewing.architecture.legacy.rendering;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import gcewing.architecture.compat.Trans3;
import gcewing.architecture.legacy.BaseModClient;

public class ItemRenderDispatcher implements IItemRenderer {

    private final BaseModClient baseModClient;

    public ItemRenderDispatcher(BaseModClient baseModClient) {
        this.baseModClient = baseModClient;
    }

    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        ICustomRenderer renderer = (ICustomRenderer) baseModClient.itemRenderers.get(stack.getItem());
        if (renderer == null) {
            renderer = baseModClient.getModelRendererForItemStack(stack);
        }
        if (renderer != null) {
            Trans3 t;
            switch (type) {
                case ENTITY:
                    t = BaseModClient.entityTrans;
                    break;
                case EQUIPPED:
                    t = BaseModClient.equippedTrans;
                    break;
                case EQUIPPED_FIRST_PERSON:
                    t = BaseModClient.firstPersonTrans;
                    break;
                case INVENTORY:
                    t = BaseModClient.inventoryTrans;
                    glEnable(GL_BLEND);
                    glEnable(GL_CULL_FACE);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    break;
                default:
                    return;
            }
            BaseModClient.glTarget.start(false);
            renderer.renderItemStack(stack, BaseModClient.glTarget, t);
            BaseModClient.glTarget.finish();
            switch (type) {
                case INVENTORY:
                    glDisable(GL_BLEND);
                    glDisable(GL_CULL_FACE);
                    break;
            }
        }
    }

}
