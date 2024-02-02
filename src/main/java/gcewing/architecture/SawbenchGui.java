// ------------------------------------------------------------------------------
//
// Greg's Blocks - SawbenchGui
//
// ------------------------------------------------------------------------------

package gcewing.architecture;

import static gcewing.architecture.BaseBlockUtils.*;
import static gcewing.architecture.BaseUtils.*;
import static gcewing.architecture.SawbenchContainer.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.player.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;

import gcewing.architecture.BaseDataChannel.*;
import gcewing.architecture.BaseModClient.*;

public class SawbenchGui extends BaseGui.Screen {

    public static int pageMenuLeft = 176;
    public static int pageMenuTop = 19;
    public static int pageMenuWidth = 58;
    public static int pageMenuRowHeight = 10;
    public static float pageMenuScale = 1;

    public static int shapeMenuLeft = 44;
    public static int shapeMenuTop = 23;
    public static int shapeMenuMargin = 4;
    public static int shapeMenuCellSize = 24;
    public static int shapeMenuRows = 4, shapeMenuCols = 5;
    public static int shapeMenuWidth = shapeMenuCols * shapeMenuCellSize;
    public static int shapeMenuHeight = shapeMenuRows * shapeMenuCellSize;
    public static int selectedShapeTitleLeft = 40;
    public static int selectedShapeTitleTop = 128;
    public static int selectedShapeTitleRight = 168;
    public static int materialUsageLeft = 7;
    public static int materialUsageTop = 82;
    public static float shapeMenuScale = 2;
    public static float shapeMenuItemScale = 2;
    public static float shapeMenuItemUSize = 40, shapeMenuItemVSize = 45;
    public static float shapeMenuItemWidth = shapeMenuItemUSize / shapeMenuItemScale;
    public static float shapeMenuItemHeight = shapeMenuItemVSize / shapeMenuItemScale;

    public int textColor;
    public int selectedShapeBackgroundColor;
    public String localizedSawbenchName;
    public String localizedMakes;
    public String[] localizedPageNames;
    public List<String[]> localizedShapeNames;

    SawbenchTE te;

    public static SawbenchGui create(EntityPlayer player, World world, BlockPos pos) {
        TileEntity te = getWorldTileEntity(world, pos);
        if (te instanceof SawbenchTE) return new SawbenchGui(player, (SawbenchTE) te);
        else return null;
    }

    public SawbenchGui(EntityPlayer player, SawbenchTE te) {
        super(new SawbenchContainer(player, te));
        this.te = te;
        initLocalizationAndColor();
    }

    private void initLocalizationAndColor() {
        textColor = GuiText.FontColor.getColor();
        selectedShapeBackgroundColor = GuiText.SelectedBgColor.getColor();
        localizedSawbenchName = GuiText.Sawbench.getLocal();
        localizedMakes = GuiText.Makes.getLocal();
        localizedPageNames = new String[te.pages.length];
        localizedShapeNames = new ArrayList<String[]>(te.pages.length);

        for (int i = 0; i < te.pages.length; i++) {
            localizedPageNames[i] = te.pages[i].getTitle();
            te.pages[i].updateShapeNames();
            localizedShapeNames.add(te.pages[i].getShapeNames());
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
        bindTexture("gui/shapemenu_items.png", 512, 512);
        int p = te.selectedPage;
        if (p >= 0 && p < te.pages.length) {
            ShapePage page = te.pages[p];
            if (page != null) {
                Shape[] shapes = page.shapes;
                for (int i = 0; i < shapes.length; i++) {
                    Shape shape = shapes[i];
                    int mrow = i / shapeMenuCols, mcol = i % shapeMenuCols;
                    int id = shape.id;
                    int trow = id / 10, tcol = id % 10;
                    // System.out.printf("SawbenchGUI: Item %s: Rendering shape id %s from (%s, %s) at (%s, %s)\n",
                    // i, id, trow, tcol, mrow, mcol);
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
        // System.out.printf("SawbenchGui.drawShapeSelection: sel=%d x=%d y=%d\n", i, x, y);
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
        // System.out.printf("SawbenchGui.mousePressed: %d, %d, %d\n", x, y, btn);
        if (x >= pageMenuLeft && y >= pageMenuTop && x < pageMenuLeft + pageMenuWidth)
            clickPageMenu(x - pageMenuLeft, y - pageMenuTop);
        else if (x >= shapeMenuLeft && y >= shapeMenuTop
                && x < shapeMenuLeft + shapeMenuWidth
                && y < shapeMenuTop + shapeMenuHeight)
            clickShapeMenu(x - shapeMenuLeft, y - shapeMenuTop);
        else super.mousePressed(x, y, btn);
    }

    void clickPageMenu(int x, int y) {
        // System.out.printf("SawbenchGui.clickPageMenu: %d, %d\n", x, y);
        int i = y / pageMenuRowHeight;
        if (i >= 0 && i < te.pages.length) sendSelectShape(i, te.selectedSlots[i]);
    }

    void clickShapeMenu(int x, int y) {
        // System.out.printf("SawbenchGui.clickShapeMenu: %d, %d\n", x, y);
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
