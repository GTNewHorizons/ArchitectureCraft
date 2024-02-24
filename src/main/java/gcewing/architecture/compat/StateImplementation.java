package gcewing.architecture.compat;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import net.minecraft.block.Block;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

import gcewing.architecture.legacy.blocks.blocks.IBlockState;
import gcewing.architecture.legacy.blocks.blocks.IProperty;

public class StateImplementation extends BlockStateBase {

    private final Block block;
    private final ImmutableMap<IProperty, Comparable> properties;
    protected ImmutableTable<IProperty, Comparable, IBlockState> propertyValueTable;

    protected StateImplementation(Block blockIn, ImmutableMap<IProperty, Comparable> propertiesIn) {
        this.block = blockIn;
        this.properties = propertiesIn;
    }

    public Collection<IProperty> getPropertyNames() {
        return Collections.<IProperty>unmodifiableCollection(this.properties.keySet());
    }

    public <T extends Comparable<T>> T getValue(IProperty<T> property) {
        if (!this.properties.containsKey(property)) {
            throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this);
        } else {
            return property.getValueClass().cast(this.properties.get(property));
        }
    }

    protected StateImplementation(Block blockIn, ImmutableMap<IProperty, Comparable> propertiesIn,
            ImmutableTable<IProperty, Comparable, IBlockState> propertyValueTable) {
        this.block = blockIn;
        this.properties = propertiesIn;
        this.propertyValueTable = propertyValueTable;
    }

    public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
        if (!this.properties.containsKey(property)) {
            throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this);
        } else if (!property.getAllowedValues().contains(value)) {
            throw new IllegalArgumentException(
                    "Cannot set property " + property
                            + " to "
                            + value
                            + " on block "
                            + Block.blockRegistry.getNameForObject(this.block)
                            + ", it is not an allowed value");
        } else {
            return this.properties.get(property) == value ? this : this.propertyValueTable.get(property, value);
        }
    }

    public ImmutableMap<IProperty, Comparable> getProperties() {
        return this.properties;
    }

    public Block getBlock() {
        return this.block;
    }

    public boolean equals(Object p_equals_1_) {
        return this == p_equals_1_;
    }

    public int hashCode() {
        return this.properties.hashCode();
    }

    public void buildPropertyValueTable(Map<Map<IProperty, Comparable>, StateImplementation> map) {
        if (this.propertyValueTable != null) {
            throw new IllegalStateException();
        } else {
            Table<IProperty, Comparable, IBlockState> table = HashBasedTable
                    .<IProperty, Comparable, IBlockState>create();

            for (IProperty<? extends Comparable> iproperty : this.properties.keySet()) {
                for (Comparable comparable : iproperty.getAllowedValues()) {
                    if (comparable != this.properties.get(iproperty)) {
                        table.put(iproperty, comparable, map.get(this.getPropertiesWithValue(iproperty, comparable)));
                    }
                }
            }

            this.propertyValueTable = ImmutableTable.<IProperty, Comparable, IBlockState>copyOf(table);
        }
    }

    private Map<IProperty, Comparable> getPropertiesWithValue(IProperty property, Comparable value) {
        Map<IProperty, Comparable> map = Maps.<IProperty, Comparable>newHashMap(this.properties);
        map.put(property, value);
        return map;
    }

    public ImmutableTable<IProperty, Comparable, IBlockState> getPropertyValueTable() {
        /** Lookup-table for IBlockState instances. This is a Table<Property, Value, State>. */
        return propertyValueTable;
    }
}
