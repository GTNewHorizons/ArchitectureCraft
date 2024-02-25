package gcewing.architecture.common.shape;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;

import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.client.render.ITexture;
import gcewing.architecture.client.render.model.IArchitectureModel;
import gcewing.architecture.client.render.model.ModelFace;
import gcewing.architecture.client.render.model.ObjJsonModel;
import gcewing.architecture.client.render.target.IRenderTarget;
import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.IBlockState;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.util.Utils;

public class Model extends ShapeKind {

    protected final String modelName;
    private IArchitectureModel model;

    public Model(String name, Object[] profiles) {
        this.modelName = "shape/" + name + ".objson";
        this.profiles = profiles;
    }

    @Override
    public boolean secondaryDefaultsToBase() {
        return true;
    }

    @Override
    public AxisAlignedBB getBounds(TileShape te, IBlockAccess world, BlockPos pos, IBlockState state, Entity entity,
            Trans3 t) {
        return t.t(getModel().getBounds());
    }

    public void renderShape(TileShape te, ITexture[] textures, IRenderTarget target, Trans3 t, boolean renderBase,
            boolean renderSecondary) {
        IArchitectureModel model = getModel();
        model.render(t, target, textures);
    }

    protected IArchitectureModel getModel() {
        if (model == null) model = ArchitectureCraft.mod.getModel(modelName);
        return model;
    }

    @Override
    public boolean acceptsCladding() {
        ObjJsonModel model = (ObjJsonModel) getModel();
        for (ModelFace face : model.faces) if (face.texture >= 2) return true;
        return false;
    }

    @Override
    public void addCollisionBoxesToList(TileShape te, IBlockAccess world, BlockPos pos, IBlockState state,
            Entity entity, Trans3 t, List list) {
        if (te.shape.occlusionMask == 0) getModel().addBoxesToList(t, list);
        else super.addCollisionBoxesToList(te, world, pos, state, entity, t, list);
    }

    @Override
    public double placementOffsetX() {
        List<AxisAlignedBB> list = new ArrayList<>();
        getModel().addBoxesToList(Trans3.ident, list);
        AxisAlignedBB bounds = Utils.unionOfBoxes(list);
        return 0.5 * (1 - (bounds.maxX - bounds.minX));
    }

}
