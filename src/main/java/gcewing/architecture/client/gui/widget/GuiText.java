package gcewing.architecture.client.gui.widget;

import net.minecraft.util.StatCollector;

public enum GuiText {

    // UI Text
    Sawbench,
    Makes,

    // Page Names
    Roofing,
    Rounded,
    Classical,
    Window,
    Arches,
    Railings,
    Other,

    // Tiles
    RoofTile,
    RoofOuterCorner,
    RoofInnerCorner,
    RoofRidge,
    RoofSmartRidge,
    RoofValley,
    RoofSmartValley,

    RoofOverhang,
    RoofOverhangOuterCorner,
    RoofOverhangInnerCorner,

    Cylinder,
    CylinderHalf,
    CylinderQuarter,
    CylinderLargeQuarter,
    AnticylinderLargeQuarter,
    Pillar,
    Post,
    Pole,

    BevelledOuterCorner,
    BevelledInnerCorner,

    PillarBase,
    DoricCapital,
    IonicCapital,
    CorinthianCapital,
    DoricTriglyph,
    DoricTriglyphCorner,
    DoricMetope,
    Architrave,
    ArchitraveCorner,

    WindowFrame,
    WindowCorner,
    WindowMullion,

    SphereFull,
    SphereHalf,
    SphereQuarter,
    SphereEighth,
    SphereEighthLarge,
    SphereEighthLargeRev,

    RoofOverhangGableLH,
    RoofOverhangGableRH,
    RoofOverhangGableEndLH,
    RoofOverhangGableEndRH,
    RoofOverhangRidge,
    RoofOverhangValley,

    CorniceLH,
    CorniceRH,
    CorniceEndLH,
    CorniceEndRH,
    CorniceRidge,
    CorniceValley,
    CorniceBottom,

    CladdingSheet,

    ArchD1,
    ArchD2,
    ArchD3A,
    ArchD3B,
    ArchD3C,
    ArchD4A,
    ArchD4B,
    ArchD4C,

    BanisterPlainBottom,
    BanisterPlain,
    BanisterPlainTop,

    BalustradeFancy,
    BalustradeFancyCorner,
    BalustradeFancyWithNewel,
    BalustradeFancyNewel,

    BalustradePlain,
    BalustradePlainOuterCorner,
    BalustradePlainWithNewel,

    BanisterPlainEnd,

    BanisterFancyNewelTall,

    BalustradePlainInnerCorner,
    BalustradePlainEnd,

    BanisterFancyBottom,
    BanisterFancy,
    BanisterFancyTop,
    BanisterFancyEnd,

    BanisterPlainInnerCorner,

    Slab,
    Stairs,
    StairsOuterCorner,
    StairsInnerCorner,

    SlopeTileA1,
    SlopeTileA2,
    SlopeTileB1,
    SlopeTileB2,
    SlopeTileB3,
    SlopeTileC1,
    SlopeTileC2,
    SlopeTileC3,
    SlopeTileC4,

    // UI Colors
    FontColor(0x404040),
    SelectedBgColor(0x66CCFF);

    private final String root;
    private final int color;

    GuiText() {
        this.root = "gui.architecturecraft";
        this.color = 0x000000;
    }

    GuiText(final String s) {
        this.root = s;
        this.color = 0x000000;
    }

    GuiText(final int hex) {
        this.root = "gui.architecturecraft";
        this.color = hex;
    }

    public int getColor() {
        String hex = StatCollector.translateToLocal(this.getUnlocalized());
        int color = this.color;
        if (hex.length() <= 6) {
            try {
                color = Integer.parseUnsignedInt(hex, 16);
            } catch (final NumberFormatException ignored) {

            }
        }
        return color;
    }

    public String getLocal() {
        return StatCollector.translateToLocal(this.getUnlocalized());
    }

    public String getUnlocalized() {
        return this.root + '.' + this;
    }
}
