package gcewing.architecture.common.render;

import gcewing.architecture.compat.Vector3;

public class ModelSpec {

    public final String modelName;
    public final String[] textureNames;
    public final Vector3 origin;

    public ModelSpec(String model, String... textures) {
        this(model, Vector3.zero, textures);
    }

    public ModelSpec(String model, Vector3 origin, String... textures) {
        modelName = model;
        textureNames = textures;
        this.origin = origin;
    }
}
