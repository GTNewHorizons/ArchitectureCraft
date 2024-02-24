package gcewing.architecture.common.item;

import net.minecraft.item.ItemStack;

import gcewing.architecture.client.texture.ITextureConsumer;
import gcewing.architecture.common.render.ModelSpec;

public interface IHasModel extends ITextureConsumer {

    ModelSpec getModelSpec(ItemStack stack);
}
