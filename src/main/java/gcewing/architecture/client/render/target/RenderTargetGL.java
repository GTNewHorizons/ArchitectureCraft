// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 version B - OpenGL rendering target
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.client.render.target;

import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LIGHTING_BIT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_BIT;
import static org.lwjgl.opengl.GL11.GL_TRANSFORM_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3d;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

import gcewing.architecture.ArchitectureCraftClient;
import gcewing.architecture.client.render.ITexture;
import gcewing.architecture.compat.Vector3;

public class RenderTargetGL extends RenderTargetBase {

    protected boolean usingLightmap;
    protected int glMode;
    protected int emissiveMode;
    protected int texturedMode;

    public RenderTargetGL() {
        super(0, 0, 0, null);
    }

    public void start(boolean usingLightmap) {
        this.usingLightmap = usingLightmap;
        glPushAttrib(GL_LIGHTING_BIT | GL_TEXTURE_BIT | GL_TRANSFORM_BIT);
        glEnable(GL_RESCALE_NORMAL);
        glShadeModel(GL_SMOOTH);
        glMode = 0;
        emissiveMode = -1;
        texturedMode = -1;
        texture = null;
    }

    @Override
    public void setTexture(ITexture tex) {
        if (texture != tex) {
            super.setTexture(tex);
            ResourceLocation loc = tex.location();
            if (loc != null) {
                setGLMode(0);
                ArchitectureCraftClient.bindTexture(loc);
            }
            setTexturedMode(!tex.isSolid());
            setEmissiveMode(tex.isEmissive());
        }
    }

    protected void setEmissiveMode(boolean state) {
        int mode = state ? 1 : 0;
        if (emissiveMode != mode) {
            glSetEnabled(GL_LIGHTING, !state);
            if (usingLightmap) setLightmapEnabled(!state);
            emissiveMode = mode;
        }
    }

    protected void setTexturedMode(boolean state) {
        int mode = state ? 1 : 0;
        if (texturedMode != mode) {
            setGLMode(0);
            glSetEnabled(GL_TEXTURE_2D, state);
            texturedMode = mode;
        }
    }

    protected void setLightmapEnabled(boolean state) {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        glSetEnabled(GL_TEXTURE_2D, state);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    protected void glSetEnabled(int mode, boolean state) {
        if (state) glEnable(mode);
        else glDisable(mode);
    }

    @Override
    protected void rawAddVertex(Vector3 p, double u, double v) {
        setGLMode(verticesPerFace);
        glColor4f(r(), g(), b(), a());
        glNormal3d(normal.x, normal.y, normal.z);
        glTexCoord2d(u, v);
        glVertex3d(p.x, p.y, p.z);
    }

    protected void setGLMode(int mode) {
        if (glMode != mode) {
            if (glMode != 0) {
                glEnd();
            }
            glMode = mode;
            switch (glMode) {
                case 0:
                    break;
                case 3:
                    glBegin(GL_TRIANGLES);
                    break;
                case 4:
                    glBegin(GL_QUADS);
                    break;
                default:
                    throw new IllegalStateException(String.format("Invalid glMode %s", glMode));
            }
        }
    }

    @Override
    public void finish() {
        setGLMode(0);
        setEmissiveMode(false);
        setTexturedMode(true);
        glPopAttrib();
        super.finish();
    }

}
