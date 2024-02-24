// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.8 - Model
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.client.render.model;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

import com.google.gson.Gson;

import gcewing.architecture.client.render.ITexture;
import gcewing.architecture.client.render.target.IRenderTarget;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;

public class ObjJsonModel implements IArchitectureModel {

    public double[] bounds;
    public ModelFace[] faces;
    public double[][] boxes;

    static final Gson gson = new Gson();

    public static ObjJsonModel fromResource(ResourceLocation location) {
        // Can't use resource manager because this needs to work on the server
        String path = String.format("/assets/%s/%s", location.getResourceDomain(), location.getResourcePath());
        InputStream in = ObjJsonModel.class.getResourceAsStream(path);
        if (in == null) throw new RuntimeException(String.format("Cannot find resource %s", path));
        ObjJsonModel model = gson.fromJson(new InputStreamReader(in), ObjJsonModel.class);
        model.prepare();
        return model;
    }

    public AxisAlignedBB getBounds() {
        return AxisAlignedBB.getBoundingBox(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
    }

    void prepare() {
        for (ModelFace face : faces) {
            double[][] p = face.vertices;
            int[] t = face.triangles[0];
            // face.centroid = Vector3.average(p[t[0]], p[t[1]], p[t[2]]);
            face.normal = Vector3.unit(Vector3.sub(p[t[1]], p[t[0]]).cross(Vector3.sub(p[t[2]], p[t[0]])));
        }
    }

    public void addBoxesToList(Trans3 t, List list) {
        if (boxes != null && boxes.length > 0) {
            for (int i = 0; i < boxes.length; i++) addBoxToList(boxes[i], t, list);
        } else addBoxToList(bounds, t, list);
    }

    protected void addBoxToList(double[] b, Trans3 t, List list) {
        t.addBox(b[0], b[1], b[2], b[3], b[4], b[5], list);
    }

    public void render(Trans3 t, IRenderTarget renderer, ITexture... textures) {
        Vector3 p, n;
        for (ModelFace face : faces) {
            int k = face.texture;
            if (k >= textures.length) k = textures.length - 1;
            ITexture tex = textures[k];
            if (tex != null) {
                renderer.setTexture(tex);
                for (int[] tri : face.triangles) {
                    renderer.beginTriangle();
                    for (int i = 0; i < 3; i++) {
                        int j = tri[i];
                        double[] c = face.vertices[j];
                        p = t.p(c[0], c[1], c[2]);
                        n = t.v(c[3], c[4], c[5]);
                        renderer.setNormal(n);
                        renderer.addVertex(p, c[6], c[7]);
                    }
                    renderer.endFace();
                }
            }
        }
    }

}
