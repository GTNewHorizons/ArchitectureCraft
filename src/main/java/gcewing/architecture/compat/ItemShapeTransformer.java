package gcewing.architecture.compat;

import static gcewing.architecture.compat.BlockCompatUtils.getNameForBlock;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.postea.api.IItemStackTransformationHandler;
import com.gtnewhorizons.postea.utility.BlockConversionInfo;
import com.gtnewhorizons.postea.utility.IDRegistry;
import com.gtnewhorizons.postea.utility.TransformerRegistry;

public class ItemShapeTransformer implements IItemStackTransformationHandler {

    @Override
    public boolean apply(String originalId, NBTTagCompound stack) {
        if (stack.hasKey("tag")) {
            NBTTagCompound tag = stack.getCompoundTag("tag");

            String blockName = tag.getString("BaseName");
            if (blockName == null) return false;

            int data = tag.getInteger("BaseData");
            BlockConversionInfo converted = TransformerRegistry
                    .getBlockReplacement(IDRegistry.getBlockId(blockName), data, null, 0, 0, 0);

            if (converted != null) {
                tag.setInteger("BaseData", converted.metadata);
                tag.setString("BaseName", getNameForBlock(Block.getBlockById(converted.blockID)));
                stack.setTag("tag", tag);
                return true;
            }
        }
        return false;
    }
}
