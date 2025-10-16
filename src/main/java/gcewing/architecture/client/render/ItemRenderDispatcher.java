package gcewing.architecture.client.render;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import gcewing.architecture.ArchitectureCraftClient;
import gcewing.architecture.compat.Trans3;

public class ItemRenderDispatcher implements IItemRenderer {

    private final ArchitectureCraftClient baseModClient;

    public ItemRenderDispatcher(ArchitectureCraftClient baseModClient) {
        this.baseModClient = baseModClient;
    }

    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        ICustomRenderer renderer = baseModClient.itemRenderers.get(stack.getItem());
        if (renderer == null) {
            renderer = baseModClient.getModelRendererForItemStack(stack);
        }
        if (renderer != null) {
            Trans3 t;
            switch (type) {
                case ENTITY:
                    t = ArchitectureCraftClient.entityTrans;
                    break;
                case EQUIPPED:
                    t = ArchitectureCraftClient.equippedTrans;
                    break;
                case EQUIPPED_FIRST_PERSON:
                    t = ArchitectureCraftClient.firstPersonTrans;
                    break;
                case INVENTORY:
                    t = ArchitectureCraftClient.inventoryTrans;
                    glEnable(GL_BLEND);
                    glEnable(GL_CULL_FACE);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    break;
                default:
                    return;
            }
            ArchitectureCraftClient.glTarget.start(false);
            renderer.renderItemStack(stack, ArchitectureCraftClient.glTarget, t);
            ArchitectureCraftClient.glTarget.finish();
            if (type == IItemRenderer.ItemRenderType.INVENTORY) {
                glDisable(GL_BLEND);
            }
        }
    }

}
