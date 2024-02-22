package gcewing.architecture.interfaces;

import net.minecraft.item.ItemStack;

import gcewing.architecture.rendering.ModelSpec;

public interface IItem extends ITextureConsumer {

    ModelSpec getModelSpec(ItemStack stack);

    int getNumSubtypes();
}
