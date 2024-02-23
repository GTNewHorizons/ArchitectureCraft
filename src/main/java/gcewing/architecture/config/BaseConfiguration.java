// ------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Configuration
//
// ------------------------------------------------------

package gcewing.architecture.config;

import java.io.File;
import java.util.Collection;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import cpw.mods.fml.common.registry.VillagerRegistry;
import gcewing.architecture.utils.BaseStringUtils;

public class BaseConfiguration extends Configuration {

    public boolean extended = false;
    int nextVillagerID = 100;

    public BaseConfiguration(File file) {
        super(file);
    }

    public String getString(String category, String key, String defaultValue) {
        return get(category, key, defaultValue).getString();
    }

    public String[] getStringList(String category, String key, String... defaultValueList) {
        String defaultValue = BaseStringUtils.join(",", defaultValueList);
        String value = getString(category, key, defaultValue);
        return BaseStringUtils.split(",", value);
    }

    public int getVillager(String key) {
        VillagerRegistry reg = VillagerRegistry.instance();
        Property prop = get("villagers", key, -1);
        int id = prop.getInt();
        if (id == -1) {
            id = allocateVillagerId(reg);
            prop.set(id);
        }
        reg.registerVillagerId(id);
        return id;
    }

    int allocateVillagerId(VillagerRegistry reg) {
        Collection<Integer> inUse = VillagerRegistry.getRegisteredVillagers();
        for (;;) {
            int id = nextVillagerID++;
            if (!inUse.contains(id)) return id;
        }
    }

    @Override
    public Property get(String category, String key, String defaultValue, String comment, Property.Type type) {
        if (!hasKey(category, key)) extended = true;
        return super.get(category, key, defaultValue, comment, type);
    }

}
