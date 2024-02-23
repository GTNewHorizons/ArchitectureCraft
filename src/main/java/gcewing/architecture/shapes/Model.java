package gcewing.architecture.shapes;

import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.BaseModClient;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.interfaces.IBlockState;
import gcewing.architecture.rendering.BaseModel;
import gcewing.architecture.rendering.Face;
import gcewing.architecture.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.List;

public class Model extends ShapeKind {

    protected final String modelName;
    private BaseModClient.IModel model;

    public Model(String name, Object[] profiles) {
        this.modelName = "shape/" + name + ".smeg";
        this.profiles = profiles;
    }

    @Override
    public boolean secondaryDefaultsToBase() {
        return true;
    }

    @Override
    public AxisAlignedBB getBounds(ShapeTE te, IBlockAccess world, BlockPos pos, IBlockState state, Entity entity, Trans3 t) {
        return t.t(getModel().getBounds());
    }

    public void renderShape(ShapeTE te, BaseModClient.ITexture[] textures, BaseModClient.IRenderTarget target, Trans3 t, boolean renderBase,
            boolean renderSecondary) {
        BaseModClient.IModel model = getModel();
        model.render(t, target, textures);
    }

    protected BaseModClient.IModel getModel() {
        if (model == null) model = ArchitectureCraft.mod.getModel(modelName);
        return model;
    }

    @Override
    public boolean acceptsCladding() {
        BaseModel model = (BaseModel) getModel();
        for (Face face : model.faces) if (face.texture >= 2) return true;
        return false;
    }

    @Override
    public void addCollisionBoxesToList(ShapeTE te, IBlockAccess world, BlockPos pos, IBlockState state, Entity entity, Trans3 t, List list) {
        if (te.shape.occlusionMask == 0) getModel().addBoxesToList(t, list);
        else super.addCollisionBoxesToList(te, world, pos, state, entity, t, list);
    }

    @Override
    public double placementOffsetX() {
        List<AxisAlignedBB> list = new ArrayList<>();
        getModel().addBoxesToList(Trans3.ident, list);
        AxisAlignedBB bounds = Utils.unionOfBoxes(list);
        if (Shape.debugPlacement) {
            for (AxisAlignedBB box : list) System.out.printf("ShapeKind.Model.placementOffsetX: %s\n", box);
            System.out.printf("ShapeKind.Model.placementOffsetX: bounds = %s\n", bounds);
        }
        return 0.5 * (1 - (bounds.maxX - bounds.minX));
    }

}
