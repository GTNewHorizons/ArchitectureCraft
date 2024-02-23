// -----------------------------------------------------------------
//
// ArchitectureCraft - Window frame renderer
//
// -----------------------------------------------------------------

package gcewing.architecture.rendering;

import java.util.Arrays;

import net.minecraft.util.EnumFacing;

import gcewing.architecture.ArchitectureCraftClient;
import gcewing.architecture.BaseModClient.IModel;
import gcewing.architecture.BaseModClient.IRenderTarget;
import gcewing.architecture.BaseModClient.ITexture;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.shapes.ShapeTE;
import gcewing.architecture.shapes.Window;

public class RenderWindow extends RenderShape {

    protected static ArchitectureCraftClient client;

    protected static IModel model(String name) {
        if (name != null) return client.getModel("shape/window_" + name + ".smeg");
        else return null;
    }

    protected static IModel[] models(String... names) {
        IModel[] result = new IModel[names.length];
        for (int i = 0; i < names.length; i++) result[i] = model(names[i]);
        return result;
    }

    protected static IModel[] models(int n, String name) {
        IModel[] result = new IModel[n];
        IModel m = model(name);
        Arrays.fill(result, m);
        return result;
    }

    protected static WindowModels frameModels, cornerModels, mullionModels;

    public static void init(ArchitectureCraftClient client) {
        RenderWindow.client = client;

        frameModels = new WindowModels(
                null,
                null,
                models(4, "frame_side"),
                models(4, "frame_end0"),
                models(4, "frame_end1"),
                model("glass"),
                models(4, "glass_edge"));

        cornerModels = new WindowModels(
                model("corner_centre"),
                models("corner_centre_end0", null, "corner_centre_end2", null),
                models("corner_topbot", "frame_side", "corner_topbot", "frame_side"),
                models(4, "frame_end0"),
                models("corner_topbot_end1", "frame_end1", "corner_topbot_end1", "frame_end1"),
                model("corner_glass"),
                models("corner_glass_edge", "glass_edge", "corner_glass_edge", "glass_edge"));

        mullionModels = new WindowModels(
                model("mullion_centre"),
                models("mullion_centre_end0", null, "mullion_centre_end2", null),
                models("mullion_topbot", "frame_side", "mullion_topbot", "frame_side"),
                models(4, "frame_end0"),
                models(4, "frame_end1"),
                model("glass"),
                models("mullion_glass_edge", "glass_edge", "mullion_glass_edge", "glass_edge"));

    }

    protected final boolean renderBase;
    protected final boolean renderSecondary;
    protected final Window kind;

    public RenderWindow(ShapeTE te, ITexture[] textures, Trans3 t, IRenderTarget target, boolean renderBase,
            boolean renderSecondary) {
        super(te, textures, t, target);
        this.renderBase = renderBase;
        this.renderSecondary = renderSecondary;
        this.kind = (Window) te.shape.kind;
    }

    public void render() {
        switch (te.shape) {
            case WindowFrame:
                renderWindow(frameModels);
                break;
            case WindowCorner:
                renderWindow(cornerModels);
                break;
            case WindowMullion:
                renderWindow(mullionModels);
                break;
        }
    }

    protected void renderWindow(WindowModels models) {
        boolean[][] frame = getFrameFlags();
        if (renderBase) renderModel(t, models.centre);
        for (int i = 0; i <= 3; i++) {
            int j = (i - 1) & 3;
            int k = (i + 1) & 3;
            Trans3 ts = t.t(kind.frameTrans[i]);
            if (renderBase) {
                if (frame[i][1]) renderModel(ts, models.side[i]);
                else if (models.centreEnd != null) renderModel(t, models.centreEnd[i]);
                if (frame[i][1] && !frame[j][1] || frame[i][0] && frame[j][2]) renderModel(ts, models.end0[i]);
                if (frame[i][1] && !frame[k][1] || frame[i][2] && frame[k][0]) renderModel(ts, models.end1[i]);
            }
            if (renderSecondary && !frame[i][1] && !frame[i][3]) renderModel(ts, models.glassEdge[i]);
        }
        if (renderSecondary) {
            renderModel(t, models.glass);
        }
    }

    protected void renderModel(Trans3 t, IModel model) {
        if (model != null) model.render(t, target, textures);
    }

    //
    // Layout of frame presence flags from perspective of side i.
    //
    // | |
    // | |
    // [i-1][0] [i+1][2]
    // | |
    // | |
    // ----------+-----------+----------
    // | |
    // [i-1][1] [i+1][1]
    // | |
    // | |
    // [i][0] | [i][1] | [i][2]
    // ----------+===========+----------
    // | i |
    // | |
    // [i-1][2] [i+1][0]
    // | |
    // | |
    //
    // frame[i][3] == glass in neighbour i
    //

    protected boolean[][] getFrameFlags() {
        boolean[][] frame = new boolean[4][4];
        if (blockWorld == null) {
            for (int i = 0; i <= 3; i++) frame[i][1] = true;
        } else {
            EnumFacing[] gdir = new EnumFacing[4];
            ShapeTE[] neighbour = new ShapeTE[4];
            for (int i = 0; i <= 3; i++) gdir[i] = t.t(kind.frameSides[i]);
            for (int i = 0; i <= 3; i++) {
                if (kind.frameAlways[i]) frame[i][1] = true;
                else {
                    ShapeTE nte = getConnectedNeighbourGlobal(te, gdir[i]);
                    if (nte == null) frame[i][1] = true;
                    else {
                        int j = (i - 1) & 3;
                        int k = (i + 1) & 3;
                        if (getConnectedNeighbourGlobal(nte, gdir[j]) == null) frame[j][2] = true;
                        if (getConnectedNeighbourGlobal(nte, gdir[k]) == null) frame[k][0] = true;
                        if (nte.secondaryBlockState != null) frame[i][3] = true;
                    }
                }
            }
        }
        // dumpFrameFlags(frame);
        return frame;
    }

    protected void dumpFrameFlags(boolean[][] frame) {
        if (te != null && te.secondaryBlockState != null) {
            System.out.print("RenderWindow.getFrameFlags:\n");
            for (int i = 0; i <= 3; i++)
                System.out.printf("Side %s: %s %s %s\n", i, frame[i][0], frame[i][1], frame[i][2]);
        }
    }

    protected ShapeTE getConnectedNeighbourGlobal(ShapeTE te, EnumFacing globalDir) {
        return kind.getConnectedWindowGlobal(te, globalDir);
    }

    protected void debug(String fmt, Object... args) {
        if (blockWorld != null && te.secondaryBlockState != null) System.out.printf(fmt, args);
    }

}
