package gcewing.architecture.items;

import net.minecraft.item.ItemStack;

import gcewing.architecture.blocks.ITextureConsumer;
import gcewing.architecture.rendering.ModelSpec;

public interface IItem extends ITextureConsumer {

    ModelSpec getModelSpec(ItemStack stack);

    int getNumSubtypes();
}
