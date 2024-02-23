package gcewing.architecture.blocks;

import java.util.Collection;

import net.minecraft.block.Block;

import com.google.common.collect.ImmutableMap;

class MetaBlockState implements IBlockState {

    protected final Block block;
    public final int meta;

    public MetaBlockState(Block block, int meta) {
        this.block = block;
        this.meta = meta;
    }

    public Collection<IProperty> getPropertyNames() {
        return null;
    }

    public <T extends Comparable<T>> T getValue(IProperty<T> property) {
        return null;
    }

    public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
        return null;
    }

    public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property) {
        return null;
    }

    public ImmutableMap<IProperty, Comparable> getProperties() {
        return null;
    }

    public Block getBlock() {
        return block;
    }

}
