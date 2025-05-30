// ------------------------------------------------------------------------------------------------
//
// Greg's Mod Base for 1.7 Version B - Rendering target rendering to tessellator
//
// ------------------------------------------------------------------------------------------------

package gcewing.architecture.client.render.target;

import static gcewing.architecture.util.Utils.ifloor;
import static gcewing.architecture.util.Utils.iround;
import static java.lang.Math.floor;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import gcewing.architecture.ArchitectureCraftClient;
import gcewing.architecture.client.render.ITexture;
import gcewing.architecture.compat.BlockPos;
import gcewing.architecture.compat.Vector3;

public class RenderTargetWorld extends RenderTargetBase {

    protected final IBlockAccess world;
    protected final BlockPos blockPos;
    protected final Block block;
    protected final Tessellator tess;
    protected final float cmr = 1;
    protected final float cmg = 1;
    protected final float cmb = 1;
    protected final boolean ao;
    protected boolean axisAlignedNormal;
    protected boolean renderingOccurred;
    protected float vr, vg, vb, va; // Colour to be applied to next vertex
    protected int vlm1, vlm2; // Light map values to be applied to next vertex
    protected boolean emissive;
    protected boolean inPreview;

    public RenderTargetWorld(IBlockAccess world, BlockPos pos, Tessellator tess, IIcon overrideIcon,
            boolean inPreview) {
        super(pos.getX(), pos.getY(), pos.getZ(), overrideIcon);
        this.world = world;
        this.blockPos = pos;
        this.block = world.getBlock(pos.x, pos.y, pos.z);
        this.tess = tess;
        this.inPreview = inPreview;
        ao = Minecraft.isAmbientOcclusionEnabled() && block.getLightValue() == 0;
        expandTrianglesToQuads = true;
    }

    // ---------------------------- IRenderTarget ----------------------------

    @Override
    public void setNormal(Vector3 n) {
        super.setNormal(n);
        axisAlignedNormal = n.dot(face) >= 0.99;
    }

    protected void rawAddVertex(Vector3 p, double u, double v) {
        lightVertex(p);
        tess.setColorRGBA_F(vr, vg, vb, va);
        tess.setTextureUV(u, v);
        tess.setBrightness((vlm1 << 16) | vlm2);
        tess.addVertex(p.x, p.y, p.z);
        renderingOccurred = true;
    }

    @Override
    public void setTexture(ITexture tex) {
        if (texture != tex) {
            super.setTexture(tex);
            emissive = tex.isEmissive();

            if (!inPreview && ArchitectureCraftClient.angelicaCompat != null) {
                Block b = tex.baseBlock();
                if (b != null) ArchitectureCraftClient.angelicaCompat.setShaderMaterialOverride(b, tex.baseMeta());
            }
        }
    }

    // -----------------------------------------------------------------------------------------

    protected void lightVertex(Vector3 p) {
        // TODO: Colour multiplier
        if (ao && !emissive) aoLightVertex(p);
        else brLightVertex(p);
    }

    protected void aoLightVertex(Vector3 v) {
        Vector3 n = normal;
        double brSum1 = 0, brSum2 = 0, lvSum = 0, wt = 0;
        // Sample a unit cube offset half a block in the direction of the normal
        double vx = v.x + 0.5 * n.x;
        double vy = v.y + 0.5 * n.y;
        double vz = v.z + 0.5 * n.z;
        // Examine 8 neighbouring blocks
        for (int dx = -1; dx <= 1; dx += 2) for (int dy = -1; dy <= 1; dy += 2) for (int dz = -1; dz <= 1; dz += 2) {
            int X = ifloor(vx + 0.5 * dx);
            int Y = ifloor(vy + 0.5 * dy);
            int Z = ifloor(vz + 0.5 * dz);
            BlockPos pos = new BlockPos(X, Y, Z);
            // Calculate overlap of sampled block with sampling cube
            double wox = (dx < 0) ? (X + 1) - (vx - 0.5) : (vx + 0.5) - X;
            double woy = (dy < 0) ? (Y + 1) - (vy - 0.5) : (vy + 0.5) - Y;
            double woz = (dz < 0) ? (Z + 1) - (vz - 0.5) : (vz + 0.5) - Z;
            // System.out.printf("wox = %.3f woy = %.3f woz = %.3f\n", wox, woy, woz);
            // Take weighted sample of brightness and light value
            double w = wox * woy * woz;
            if (w > 0) {
                int br;
                try {
                    br = block.getMixedBrightnessForBlock(world, pos.x, pos.y, pos.z);
                } catch (RuntimeException e) {
                    throw e;
                }
                float lv;
                if (!pos.equals(blockPos)) lv = world.getBlock(pos.x, pos.y, pos.z).getAmbientOcclusionLightValue();
                else lv = 1.0f;
                if (br != 0) {
                    double br1 = ((br >> 16) & 0xff) / 240.0;
                    double br2 = (br & 0xff) / 240.0;
                    brSum1 += w * br1;
                    brSum2 += w * br2;
                    wt += w;
                }
                lvSum += w * lv;
            }
        }
        int brv;
        if (wt > 0) brv = (iround(brSum1 / wt * 0xf0) << 16) | iround(brSum2 / wt * 0xf0);
        else brv = block.getMixedBrightnessForBlock(world, blockPos.x, blockPos.y, blockPos.z);
        float lvv = (float) lvSum;
        setLight(shade * lvv, brv);
    }

    protected void brLightVertex(Vector3 p) {
        if (emissive) {
            setLight(1.0f, 240);
            return;
        }
        Vector3 n = normal;
        BlockPos pos;
        if (axisAlignedNormal) pos = new BlockPos(
                (int) floor(p.x + 0.01 * n.x),
                (int) floor(p.y + 0.01 * n.y),
                (int) floor(p.z + 0.01 * n.z));
        else pos = blockPos;
        int br = block.getMixedBrightnessForBlock(world, pos.x, pos.y, pos.z);
        setLight(shade, br);
    }

    protected void setLight(float shadow, int br) {
        vr = shadow * cmr * r();
        vg = shadow * cmg * g();
        vb = shadow * cmb * b();
        va = a();
        vlm1 = br >> 16;
        vlm2 = br & 0xffff;
    }

    public boolean end() {
        super.finish();
        if (!inPreview && ArchitectureCraftClient.angelicaCompat != null)
            ArchitectureCraftClient.angelicaCompat.resetShaderMaterialOverride();

        return renderingOccurred;
    }

    public void setRenderingOccurred() {
        renderingOccurred = true;
    }

}
