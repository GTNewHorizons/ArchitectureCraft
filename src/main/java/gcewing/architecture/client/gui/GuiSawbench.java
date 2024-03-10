// ------------------------------------------------------------------------------
//
// Greg's Blocks - SawbenchGui
//
// ------------------------------------------------------------------------------

package gcewing.architecture.client.gui;

import static gcewing.architecture.compat.BlockCompatUtils.getWorldTileEntity;
import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import gcewing.architecture.ArchitectureCraft;
import gcewing.architecture.client.gui.widget.GuiText;
import gcewing.architecture.client.gui.widget.Screen;
import gcewing.architecture.common.network.ChannelOutput;
import gcewing.architecture.common.shape.Shape;
import gcewing.architecture.common.shape.ShapePage;
import gcewing.architecture.common.tile.TileSawbench;
import gcewing.architecture.compat.BlockPos;

public class GuiSawbench extends Screen {

    public static final int pageMenuLeft = 176;
    public static final int pageMenuTop = 19;
    public static final int pageMenuWidth = 58;
    public static final int pageMenuRowHeight = 10;
    public static float pageMenuScale = 1;

    public static final int shapeMenuLeft = 44;
    public static final int shapeMenuTop = 23;
    public static final int shapeMenuMargin = 4;
    public static final int shapeMenuCellSize = 24;
    public static final int shapeMenuRows = 4;
    public static final int shapeMenuCols = 5;
    public static final int shapeMenuWidth = shapeMenuCols * shapeMenuCellSize;
    public static final int shapeMenuHeight = shapeMenuRows * shapeMenuCellSize;
    public static final int selectedShapeTitleLeft = 40;
    public static final int selectedShapeTitleTop = 128;
    public static final int selectedShapeTitleRight = 168;
    public static final int materialUsageLeft = 7;
    public static final int materialUsageTop = 82;
    public static final float shapeMenuScale = 2;
    public static final float shapeMenuItemScale = 2;
    public static final float shapeMenuItemUSize = 40;
    public static final float shapeMenuItemVSize = 45;
    public static final float shapeMenuItemWidth = shapeMenuItemUSize / shapeMenuItemScale;
    public static final float shapeMenuItemHeight = shapeMenuItemVSize / shapeMenuItemScale;

    public int textColor;
    public int selectedShapeBackgroundColor;
    public String localizedSawbenchName;
    public String localizedMakes;
    public String[] localizedPageNames;
    public List<String[]> localizedShapeNames;

    final TileSawbench te;

    public static GuiSawbench create(EntityPlayer player, World world, BlockPos pos) {
        TileEntity te = getWorldTileEntity(world, pos);
        if (te instanceof TileSawbench) return new GuiSawbench(player, (TileSawbench) te);
        else return null;
    }

    public GuiSawbench(EntityPlayer player, TileSawbench te) {
        super(new ContainerSawbench(player, te));
        this.te = te;
        initLocalizationAndColor();
    }

    private void initLocalizationAndColor() {
        textColor = GuiText.FontColor.getColor();
        selectedShapeBackgroundColor = GuiText.SelectedBgColor.getColor();
        localizedSawbenchName = GuiText.Sawbench.getLocal();
        localizedMakes = GuiText.Makes.getLocal();
        localizedPageNames = new String[TileSawbench.pages.length];
        localizedShapeNames = new ArrayList<>(TileSawbench.pages.length);

        for (int i = 0; i < TileSawbench.pages.length; i++) {
            localizedPageNames[i] = TileSawbench.pages[i].getTitle();
            TileSawbench.pages[i].updateShapeNames();
            localizedShapeNames.add(TileSawbench.pages[i].getShapeNames());
        }
    }

    @Override
    protected void drawBackgroundLayer() {
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture("gui/gui_sawbench.png", 256, 256);
        drawTexturedRect(0, 0, this.xSize, this.ySize, 0, 0);
        drawShapeMenu();
        drawShapeSelection();
        drawPageMenu();
        drawSelectedShapeTitle();
        fontRendererObj.drawString(localizedSawbenchName, 7, 7, textColor);
    }

    void drawPageMenu() {
        glPushMatrix();
        glTranslatef(pageMenuLeft, pageMenuTop, 0);
        gSave();
        setColor(selectedShapeBackgroundColor);
        drawRect(0, te.selectedPage * pageMenuRowHeight, pageMenuWidth, pageMenuRowHeight);
        gRestore();
        for (int i = 0; i < this.localizedPageNames.length; i++) {
            drawString(this.localizedPageNames[i], 1, 1);
            glTranslatef(0, pageMenuRowHeight, 0);
        }
        glPopMatrix();
    }

    void drawShapeMenu() {
        gSave();
        glPushMatrix();
        glDisable(GL_ALPHA_TEST);
        glEnable(GL_BLEND);
        glTranslatef(shapeMenuLeft, shapeMenuTop, 0);
        bindTexture("gui/shapemenu_bg.png", 256, 256);
        double w = shapeMenuWidth + 2 * shapeMenuMargin;
        double h = shapeMenuHeight + 2 * shapeMenuMargin;
        drawTexturedRect(-shapeMenuMargin, -shapeMenuMargin, w, h, 0, 0, shapeMenuScale * w, shapeMenuScale * h);

        int p = te.selectedPage;
        if (p >= 0 && p < TileSawbench.pages.length) {
            ShapePage page = TileSawbench.pages[p];
            if (page != null) {
                Shape[] shapes = page.shapes;
                for (int i = 0; i < shapes.length; i++) {
                    Shape shape = shapes[i];
                    int mrow = i / shapeMenuCols, mcol = i % shapeMenuCols;
                    int id = shape.id;
                    int textureSheet = id / 110;
                    bindTexture("gui/shapemenu_items_" + textureSheet + ".png", 512, 512);
                    int trow = (id % 110) / 10, tcol = (id % 110) % 10;
                    drawTexturedRect(
                            (mcol + 0.5) * shapeMenuCellSize - 0.5 * shapeMenuItemWidth,
                            (mrow + 0.5) * shapeMenuCellSize - 0.5 * shapeMenuItemHeight,
                            shapeMenuItemWidth,
                            shapeMenuItemHeight,
                            tcol * shapeMenuItemUSize,
                            trow * shapeMenuItemVSize,
                            shapeMenuItemUSize,
                            shapeMenuItemVSize);
                }
            }
        }
        glPopMatrix();
        gRestore();
    }

    void drawShapeSelection() {
        int i = te.selectedSlots[te.selectedPage];
        int row = i / shapeMenuCols;
        int col = i % shapeMenuCols;
        int x = shapeMenuLeft + shapeMenuCellSize * col;
        int y = shapeMenuTop + shapeMenuCellSize * row;
        drawTexturedRect(x, y, 24.5, 24.5, 44, 23, 49, 49);
    }

    void drawSelectedShapeTitle() {
        int pageIndex = te.getSelectedPageIndex();
        int shapeIndex = te.getSelectedShapeIndex();
        if (pageIndex != -1 && shapeIndex != -1) {
            String shapeName = localizedShapeNames.get(pageIndex)[shapeIndex];
            int x = selectedShapeTitleLeft;
            int w = fontRendererObj.getStringWidth(shapeName);
            if (x + w > selectedShapeTitleRight) x = selectedShapeTitleRight - w;
            drawString(shapeName, x, selectedShapeTitleTop);
            glPushMatrix();
            glTranslatef(materialUsageLeft, materialUsageTop, 0);
            glScalef(0.5f, 0.5f, 1.0f);
            drawString(String.format("%s %s %s", te.materialMultiple(), localizedMakes, te.resultMultiple()), 0, 0);
            glPopMatrix();
        }
    }

    @Override
    protected void mousePressed(int x, int y, int btn) {
        if (x >= pageMenuLeft && y >= pageMenuTop && x < pageMenuLeft + pageMenuWidth)
            clickPageMenu(x - pageMenuLeft, y - pageMenuTop);
        else if (x >= shapeMenuLeft && y >= shapeMenuTop
                && x < shapeMenuLeft + shapeMenuWidth
                && y < shapeMenuTop + shapeMenuHeight)
            clickShapeMenu(x - shapeMenuLeft, y - shapeMenuTop);
        else super.mousePressed(x, y, btn);
    }

    void clickPageMenu(int x, int y) {
        int i = y / pageMenuRowHeight;
        if (i >= 0 && i < TileSawbench.pages.length) sendSelectShape(i, te.selectedSlots[i]);
    }

    void clickShapeMenu(int x, int y) {
        int row = y / shapeMenuCellSize;
        int col = x / shapeMenuCellSize;
        if (row >= 0 && row < shapeMenuRows && col >= 0 && col < shapeMenuCols) {
            int i = row * shapeMenuCols + col;
            sendSelectShape(te.selectedPage, i);
        }
    }

    protected void sendSelectShape(int page, int slot) {
        ChannelOutput data = ArchitectureCraft.channel.openServerContainer("SelectShape");
        data.writeInt(page);
        data.writeInt(slot);
        data.close();
    }

}
