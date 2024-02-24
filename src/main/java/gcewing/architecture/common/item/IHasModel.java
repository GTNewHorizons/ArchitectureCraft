package gcewing.architecture.common.item;

import net.minecraft.item.ItemStack;

import gcewing.architecture.legacy.blocks.ITextureConsumer;
import gcewing.architecture.legacy.rendering.ModelSpec;

public interface IHasModel extends ITextureConsumer {

    ModelSpec getModelSpec(ItemStack stack);
}
