//------------------------------------------------------------------------------------------------
//
//   Greg's Mod Base for 1.8 - Rendering target generating a baked model
//
//------------------------------------------------------------------------------------------------

package gcewing.architecture;

import java.util.*;
import java.nio.*;
//import javax.vecmath.Vector3f;
import org.lwjgl.util.vector.Vector3f;
import net.minecraft.client.renderer.Tessellator;

import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.*;

import net.minecraftforge.client.model.*;

import gcewing.architecture.BaseModClient.ITexture;

public class BaseBakedRenderTarget extends BaseRenderTarget {

	// It seems to be necessary to put the padding byte *before* the
	// normal bytes, even though DefaultVertexFormats.ITEM says it
	// should be after.
	protected static VertexFormat theFormat = new VertexFormat();
	static {
		theFormat.addElement(POSITION_3F);
		theFormat.addElement(COLOR_4UB);
		theFormat.addElement(TEX_2F);
		theFormat.addElement(PADDING_1B);
		theFormat.addElement(NORMAL_3B);
	}

	protected static List<BakedQuad> emptyQuads = new ArrayList<BakedQuad>();
	protected static List<List<BakedQuad>> faceQuads = new ArrayList<List<BakedQuad>>();
	static {
		for (int i = 0; i < 6; i++)
			faceQuads.add(emptyQuads);
	}

	//protected VertexFormat format = Attributes.DEFAULT_BAKED_FORMAT;
	//protected VertexFormat format = DefaultVertexFormats.ITEM;
	protected VertexFormat format = theFormat;
	protected int bytesPerVertex = format.getNextOffset();
	protected int intsPerVertex = bytesPerVertex / 4;
	protected ByteBuffer buf = ByteBuffer.allocate(bytesPerVertex * 4);
	
	protected List<BakedQuad> quads;
	
	public BaseBakedRenderTarget() {
		this(0, 0, 0, null);
	}
	
	public BaseBakedRenderTarget(BlockPos pos) {
		this(pos, null);
	}
	
	public BaseBakedRenderTarget(TextureAtlasSprite overrideIcon) {
		this(0, 0, 0, overrideIcon);
	}
	
	public BaseBakedRenderTarget(BlockPos pos, TextureAtlasSprite overrideIcon) {
		this(pos.getX(), pos.getY(), pos.getZ(), overrideIcon);
	}

	public BaseBakedRenderTarget(double x, double y, double z, TextureAtlasSprite overrideIcon) {
		super(x, y, z, overrideIcon);
		quads = new ArrayList<BakedQuad>();
	}
	
	//--------------------------------- IRenderTarget ------------------------------------------

	@Override
	protected void setMode(int m) {
		super.setMode(m);
		buf.clear();
	}
	
	@Override
	public void endFace() {
		super.endFace();
		buf.flip();
		int intsPerQuad = intsPerVertex * 4;
		int[] data = new int[intsPerQuad];
		IntBuffer intBuf = buf.asIntBuffer();
		int n = intBuf.limit();
		intBuf.get(data, 0, n);
		while (n < intsPerQuad) {
			data[n] = data[n - intsPerVertex];
			++n;
		}
		prescrambleVertexColors(data);
		//dumpVertexData(data, n);
		//quads.add(new BakedQuad(data, 0, normal.facing()));
		//System.out.printf("BaseBakedRenderTarget.endFace: Adding quad facing %s\n", face);
		//quads.add(new BakedQuad(data, 0, face));
		quads.add(new IColoredBakedQuad.ColoredBakedQuad(data, 0, EnumFacing.UP));
	}
	
//	protected void dumpVertexData(int[] data, int n) {
//		System.out.printf("BaseRenderTarget.endFace: Vertex data:\n");
//		for (int i = 0; i < n; i++) {
//			System.out.printf("%08x\n", data[i]);
//			if ((i + 1) % intsPerVertex == 0)
//				System.out.printf("\n");
//		}
//	}
	
	protected void dumpVertexData(int[] data, int n) {
		System.out.printf("BaseBakedRenderTarget.endFace: Vertex data:\n");
		for (int i = 0; i < 4; i++) {
			int k = i * intsPerVertex;
			System.out.printf("[%s] coords (%.3f,%.3f,%.3f) color %08x\n",
				i,
				Float.intBitsToFloat(data[k]),
				Float.intBitsToFloat(data[k+1]),
				Float.intBitsToFloat(data[k+2]),
				data[k+3]);
		}
	}
	
	protected void prescrambleVertexColors(int[] data) {
		// Compensate for bug in Forge 11.14.1.1371 that puts color data in wrong order
		int[] c = new int[4];
		for (int i = 0; i < 4; i++)
			c[i] = data[i * intsPerVertex + 3];
		for (int i = 0; i < 4; i++)
			data[i * intsPerVertex + 3] = c[3 - i];
	}

	//------------------------------------------------------------------------------------------
	
	static protected ItemCameraTransforms transforms = new ItemCameraTransforms(
		new ItemTransformVec3f(
			new Vector3f(10f, -45f, 170f),
			new Vector3f(0f, 1.5f/16f, -2.75f/16f),
			new Vector3f(0.375f, 0.375f, 0.375f)), // thirdPerson
		ItemCameraTransforms.DEFAULT.firstPerson,
		ItemCameraTransforms.DEFAULT.head,
		ItemCameraTransforms.DEFAULT.gui,
		ItemCameraTransforms.DEFAULT.ground,
		ItemCameraTransforms.DEFAULT.fixed
	);

	public IBakedModel getBakedModel() {
		return getBakedModel(null);
	}

	public IBakedModel getBakedModel(TextureAtlasSprite particleTexture) {
		//System.out.printf("BaseRenderTarget.getBakedModel: Returning model with %s quads\n", quads.size());
		//System.out.printf("BaseRenderTarget.getBakedModel: %s of %s unprocessed vertices\n", vertexCount, verticesPerFace);
		//System.out.printf("BaseBakedRenderTarget.getBakedModel: WorldRenderer is using vertex format %s\n",
		//	Tessellator.getInstance().getWorldRenderer().getVertexFormat());
		if (verticesPerFace != 0)
			throw new IllegalStateException("Rendering ended with incomplete face");
		return new SimpleBakedModel(quads, faceQuads, false, true, particleTexture, transforms);
	}
	
	protected void rawAddVertex(Vector3 p, double u, double v) {
		//System.out.printf("BaseBakedRenderTarget.rawAddVertex: color (%.3f,%.3f,%.3f,%.3f) normal %s\n",
		//	red, green, blue, alpha, normal);
		//System.out.printf("BaseBakedRenderTarget.rawAddVertex:\n");
		for (VertexFormatElement e : (List<VertexFormatElement>)format.getElements()) {
			//System.out.printf("%s\n", e);
			switch(e.getUsage()) {
				case POSITION:
					putElement(e, p.x, p.y, p.z);
					break;
				case COLOR:
					//putElement(e, alpha, shade * blue, shade * green, shade * red);
					putElement(e, alpha, blue, green, red);
					break;
				case NORMAL:
					//System.out.printf("BaseBakedRenderTarget.rawAddVertex: NORMAL %s\n", normal);
					putElement(e, normal.x, normal.y, normal.z);
					break;
				case UV:
					putElement(e, u, v);
					break;
				default:
					putElement(e);
			}
		}
	}
		
	protected void putElement(VertexFormatElement e, Number... ns) {
		Number n;
		for(int i = 0; i < e.getElementCount(); i++) {
			if (i < ns.length)
				n = ns[i];
			else
				n = 0;
			switch (e.getType()) {
				case BYTE:
					buf.put((byte)(n.floatValue() * 0x7f));
					break;
				case UBYTE:
					buf.put((byte)(n.floatValue() * 0xff));
					break;
				case SHORT:
					buf.putShort((short)(n.floatValue() * 0x7fff));
					break;
				case USHORT:
					buf.putShort((short)(n.floatValue() * 0xffff));
					break;
				case INT:
					buf.putInt((int)(n.doubleValue() * 0x7fffffff));
					break;
				case UINT:
					buf.putInt((int)(n.doubleValue() * 0xffffffff));
					break;
				case FLOAT:
					buf.putFloat(n.floatValue());
					break;
			}
		}
	}
	
}
