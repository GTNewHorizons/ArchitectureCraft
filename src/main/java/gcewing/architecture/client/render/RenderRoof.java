// ------------------------------------------------------
//
// ArchitectureCraft - Roof Block Renderer
//
// ------------------------------------------------------

package gcewing.architecture.client.render;

import net.minecraft.util.EnumFacing;

import gcewing.architecture.client.render.target.IRenderTarget;
import gcewing.architecture.common.shape.Shape;
import gcewing.architecture.common.tile.TileShape;
import gcewing.architecture.compat.Trans3;
import gcewing.architecture.compat.Vector3;

public class RenderRoof extends RenderShape {

    protected final static Shape[] ridgeShapes = { Shape.RoofRidge, Shape.RoofSmartRidge };

    protected final static Shape[] ridgeOrSlopeShapes = { Shape.RoofRidge, Shape.RoofSmartRidge, Shape.RoofTile,
            Shape.RoofOuterCorner, Shape.RoofInnerCorner };

    protected final static Shape[] valleyShapes = { Shape.RoofValley, Shape.RoofSmartValley };

    protected final static Shape[] valleyOrSlopeShapes = { Shape.RoofValley, Shape.RoofSmartValley, Shape.RoofTile,
            Shape.RoofInnerCorner };

    protected EnumFacing face;
    protected boolean outerFace;
    protected final boolean renderBase;
    protected final boolean renderSecondary;

    public RenderRoof(TileShape te, ITexture[] textures, Trans3 t, IRenderTarget target, boolean renderBase,
            boolean renderSecondary) {
        super(te, textures, t, target);
        this.renderBase = renderBase;
        this.renderSecondary = renderSecondary;
    }

    public void render() {
        switch (te.shape) {
            case RoofTile:
            case RoofTileSE:
                renderSlope();
                break;
            case SlopeTileA1:
            case SlopeTileA1SE:
                renderSlopeA1();
                break;
            case SlopeTileA2:
            case SlopeTileA2SE:
                renderSlopeA2();
                break;
            case SlopeTileB1:
            case SlopeTileB1SE:
                renderSlopeB1();
                break;
            case SlopeTileB2:
            case SlopeTileB2SE:
                renderSlopeB2();
                break;
            case SlopeTileB3:
            case SlopeTileB3SE:
                renderSlopeB3();
                break;
            case SlopeTileC1:
            case SlopeTileC1SE:
                renderSlopeC1();
                break;
            case SlopeTileC2:
            case SlopeTileC2SE:
                renderSlopeC2();
                break;
            case SlopeTileC3:
            case SlopeTileC3SE:
                renderSlopeC3();
                break;
            case SlopeTileC4:
            case SlopeTileC4SE:
                renderSlopeC4();
                break;
            case RoofOuterCorner:
                renderOuterCorner();
                break;
            case RoofInnerCorner:
                renderInnerCorner();
                break;
            case RoofRidge:
                renderRidge();
                break;
            case RoofSmartRidge:
                renderSmartRidge();
                break;
            case RoofValley:
                renderValley();
                break;
            case RoofSmartValley:
                renderSmartValley();
                break;
        }
    }

    // -------------------------------------------------------------------------------------

    protected void renderSlope() {
        boolean valley = valleyAt(0, 0, 1);
        if (renderSecondary) {
            // Sloping face
            beginNegZSlope();
            if (valley) {
                beginTriangle();
                vertex(1, 1, 1, 0, 0);
                vertex(1, 0, 0, 0, 1);
                vertex(0.5, 0.5, 0.5, 0.5, 0.5);
                newTriangle();
                vertex(1, 0, 0, 0, 1);
                vertex(0, 0, 0, 1, 1);
                vertex(0.5, 0.5, 0.5, 0.5, 0.5);
                newTriangle();
                vertex(0, 0, 0, 1, 1);
                vertex(0, 1, 1, 1, 0);
                vertex(0.5, 0.5, 0.5, 0.5, 0.5);
                endFace();
                connectValleyBack();
            } else {
                beginQuad();
                vertex(1, 1, 1, 0, 0);
                vertex(1, 0, 0, 0, 1);
                vertex(0, 0, 0, 1, 1);
                vertex(0, 1, 1, 1, 0);
                endFace();
            }
        }
        // Other faces
        if (renderBase) {
            leftTriangle();
            rightTriangle();
            bottomQuad();
            if (!valley) backQuad();
        }
        if (renderSecondary) if (ridgeAt(0, 0, -1)) connectRidgeFront();
    }

    protected void renderSlopeA1() {
        renderVariableSlope(1.0, 0.5);

        renderVariableFaceLeft(0, 0.5);
        renderVariableTriangleLeft(0.5, 0.5);

        renderVariableFaceRight(0, 0.5);
        renderVariableTriangleRight(0.5, 0.5);

        renderVariableFrontFace(0.5);

        bottomQuad();
        backQuad();
    }

    protected void renderSlopeA2() {
        renderVariableSlope(0.5, 0);

        renderVariableTriangleLeft(0, 0.5);

        renderVariableTriangleRight(0, 0.5);

        bottomQuad();
        renderVariableBackFace(0.5);
    }

    protected void renderSlopeB1() {
        renderVariableSlope(1.0, 0.66666);

        renderVariableFaceLeft(0, 0.66666);
        renderVariableTriangleLeft(0.66666, 0.33333);

        renderVariableFaceRight(0, 0.66666);
        renderVariableTriangleRight(0.66666, 0.33333);

        renderVariableFrontFace(0.66666);

        bottomQuad();
        backQuad();
    }

    protected void renderSlopeB2() {
        renderVariableSlope(0.66666, 0.33333);

        renderVariableFaceLeft(0, 0.33333);
        renderVariableTriangleLeft(0.33333, 0.33333);

        renderVariableFaceRight(0, 0.33333);
        renderVariableTriangleRight(0.33333, 0.33333);

        renderVariableFrontFace(0.33333);

        bottomQuad();
        renderVariableBackFace(0.66666);
    }

    protected void renderSlopeB3() {
        renderVariableSlope(0.33333, 0);

        renderVariableTriangleLeft(0, 0.33333);

        renderVariableTriangleRight(0, 0.33333);

        bottomQuad();
        renderVariableBackFace(0.33333);
    }

    protected void renderSlopeC1() {
        renderVariableSlope(1, 0.75);

        renderVariableFaceLeft(0, 0.75);
        renderVariableTriangleLeft(0.75, 0.25);

        renderVariableFaceRight(0, 0.75);
        renderVariableTriangleRight(0.75, 0.25);

        renderVariableFrontFace(0.75);

        bottomQuad();
        backQuad();
    }

    protected void renderSlopeC2() {
        renderVariableSlope(0.75, 0.50);

        renderVariableFaceLeft(0, 0.50);
        renderVariableTriangleLeft(0.50, 0.25);

        renderVariableFaceRight(0, 0.50);
        renderVariableTriangleRight(0.50, 0.25);

        renderVariableFrontFace(0.50);

        bottomQuad();
        renderVariableBackFace(0.75);
    }

    protected void renderSlopeC3() {
        renderVariableSlope(0.50, 0.25);

        renderVariableFaceLeft(0, 0.25);
        renderVariableTriangleLeft(0.25, 0.25);

        renderVariableFaceRight(0, 0.25);
        renderVariableTriangleRight(0.25, 0.25);

        renderVariableFrontFace(0.25);

        bottomQuad();
        renderVariableBackFace(0.50);
    }

    protected void renderSlopeC4() {
        renderVariableSlope(0.25, 0);

        renderVariableTriangleLeft(0, 0.25);

        renderVariableTriangleRight(0, 0.25);

        bottomQuad();
        renderVariableBackFace(0.25);
    }

    // -------------------------------------------------------------------------------------

    protected void renderVariableSlope(double start, double end) {
        beginNegZSlope();
        // Front slope
        beginQuad();
        vertex(1, start, 1, 0, 0);
        vertex(1, end, 0, 0, 1);
        vertex(0, end, 0, 1, 1);
        vertex(0, start, 1, 1, 0);
        endFace();
    }

    protected void renderVariableTriangleLeft(double offset, double height) {
        beginPosXFace();
        beginTriangle();
        vertex(1, offset + height, 1, 0, 0);
        vertex(1, offset, 1, 0, 1 - height);
        vertex(1, offset, 0, 1, 1 - height);
        endFace();
    }

    protected void renderVariableTriangleRight(double offset, double height) {
        beginNegXFace();
        beginTriangle();
        vertex(0, offset + height, 1, 1, 0);
        vertex(0, offset, 0, 0, 1 - height);
        vertex(0, offset, 1, 1, 1 - height);
        endFace();
    }

    protected void renderVariableFaceLeft(double offset, double height) {
        beginNegXFace();
        beginQuad();
        vertex(0, offset + height, 0, 0, 1 - height);
        vertex(0, offset, 0, 0, 1);
        vertex(0, offset, 1, 1, 1);
        vertex(0, offset + height, 1, 1, 1 - height);
        endFace();
    }

    protected void renderVariableFaceRight(double offset, double height) {
        beginPosXFace();
        beginQuad();
        vertex(1, offset + height, 1, 0, 1 - height);
        vertex(1, offset, 1, 0, 1);
        vertex(1, offset, 0, 1, 1);
        vertex(1, offset + height, 0, 1, 1 - height);
        endFace();
    }

    protected void renderVariableFrontFace(double height) {
        beginNegZFace();
        beginQuad();
        vertex(1, height, 0, 0, 1 - height);
        vertex(1, 0, 0, 0, 1);
        vertex(0, 0, 0, 1, 1);
        vertex(0, height, 0, 1, 1 - height);
        endFace();
    }

    protected void renderVariableBackFace(double height) {
        beginPosZFace();
        beginQuad();
        vertex(0, height, 1, 0, 1 - height);
        vertex(0, 0, 1, 0, 1);
        vertex(1, 0, 1, 1, 1);
        vertex(1, height, 1, 1, 1 - height);
        endFace();
    }

    // -------------------------------------------------------------------------------------
    protected void renderOuterCorner() {
        if (renderSecondary) {
            // Front slope
            beginNegZSlope();
            beginTriangle();
            vertex(0, 1, 1, 1, 0);
            vertex(1, 0, 0, 0, 1);
            vertex(0, 0, 0, 1, 1);
            endFace();
            // Left slope
            beginPosXSlope();
            beginTriangle();
            vertex(0, 1, 1, 0, 0);
            vertex(1, 0, 1, 0, 1);
            vertex(1, 0, 0, 1, 1);
            endFace();
        }
        if (renderBase) {
            // Back
            beginPosZFace();
            beginTriangle();
            vertex(0, 1, 1, 0, 0);
            vertex(0, 0, 1, 0, 1);
            vertex(1, 0, 1, 1, 1);
            endFace();
            // Other faces
            rightTriangle();
            bottomQuad();
        }
        if (renderSecondary) {
            if (ridgeAt(0, 0, -1)) connectRidgeFront();
            if (ridgeAt(1, 0, 0)) connectRidgeLeft();
        }
    }

    protected void renderInnerCorner() {
        if (renderSecondary) {
            // Left slope
            beginPosXSlope();
            beginTriangle();
            vertex(0, 1, 0, 1, 0);
            vertex(0.5, 0.5, 0.5, 0.5, 0.5);
            vertex(1, 0, 0, 1, 1);
            endFace();
            // Front slope
            beginNegZSlope();
            beginTriangle();
            vertex(1, 1, 1, 0, 0);
            vertex(1, 0, 0, 0, 1);
            vertex(0.5, 0.5, 0.5, 0.5, 0.5);
            endFace();
        }
        if (renderBase) {
            // Front triangle
            beginNegZFace();
            beginTriangle();
            vertex(0, 1, 0, 1, 0);
            vertex(1, 0, 0, 0, 1);
            vertex(0, 0, 0, 1, 1);
            endFace();
            // Other faces
            leftTriangle();
            bottomQuad();
        }
        if (valleyAt(0, 0, 1)) connectValleyBack();
        else terminateValleyBack();
        if (valleyAt(-1, 0, 0)) connectValleyRight();
        else terminateValleyRight();
    }

    protected void renderRidge() {
        if (renderSecondary) {
            // Front slope
            beginNegZSlope();
            beginQuad();
            vertex(1, 0.5, 0.5, 0, 0.5);
            vertex(1, 0, 0, 0, 1);
            vertex(0, 0, 0, 1, 1);
            vertex(0, 0.5, 0.5, 1, 0.5);
            endFace();
            // Other slops
            ridgeBackSlope();
            ridgeFront(false);
            ridgeBack(false);
        }
        if (renderBase) {
            ridgeLeftFace();
            ridgeRightFace();
            bottomQuad();
        }
    }

    protected void renderSmartRidge() {
        if (renderSecondary) {
            ridgeLeft();
            ridgeRight();
            ridgeBack(true);
            ridgeFront(true);
        }
        if (renderBase) bottomQuad();
    }

    protected void renderValley() {
        connectValleyLeft();
        connectValleyRight();
        smartValleyFront();
        smartValleyBack();
        if (renderBase) bottomQuad();
    }

    protected void renderSmartValley() {
        smartValleyLeft();
        smartValleyRight();
        smartValleyFront();
        smartValleyBack();
        if (renderBase) bottomQuad();
    }

    // -------------------------------------------------------------------------------------

    protected void smartValleyLeft() {
        if (valleyOrSlopeAt(1, 0, 0)) connectValleyLeft();
        else terminateValleyLeft();
    }

    protected void terminateValleyLeft() {
        if (renderSecondary) {
            beginNegXSlope();
            beginTriangle();
            vertex(1, 1, 0, 0, 0);
            vertex(0.5, 0.5, 0.5, 0.5, 0.5);
            vertex(1, 1, 1, 1, 0);
            endFace();
        }
        if (renderBase) leftQuad();
    }

    protected void smartValleyRight() {
        if (valleyOrSlopeAt(-1, 0, 0)) connectValleyRight();
        else terminateValleyRight();
    }

    protected void terminateValleyRight() {
        if (renderSecondary) {
            beginPosXSlope();
            beginTriangle();
            vertex(0, 1, 1, 0, 0);
            vertex(0.5, 0.5, 0.5, 0.5, 0.5);
            vertex(0, 1, 0, 1, 0);
            endFace();
        }
        if (renderBase) rightQuad();
    }

    protected void smartValleyFront() {
        if (valleyOrSlopeAt(0, 0, -1)) connectValleyFront();
        else terminateValleyFront();
    }

    protected void terminateValleyFront() {
        if (renderSecondary) {
            beginPosZSlope();
            beginTriangle();
            vertex(0, 1, 0, 0, 0);
            vertex(0.5, 0.5, 0.5, 0.5, 0.5);
            vertex(1, 1, 0, 1, 0);
            endFace();
        }
        if (renderBase) frontQuad();
    }

    protected void smartValleyBack() {
        if (valleyOrSlopeAt(0, 0, 1)) connectValleyBack();
        else terminateValleyBack();
    }

    protected void terminateValleyBack() {
        if (renderSecondary) {
            beginNegZSlope();
            beginTriangle();
            vertex(1, 1, 1, 0, 0);
            vertex(0.5, 0.5, 0.5, 0.5, 0.5);
            vertex(0, 1, 1, 1, 0);
            endFace();
        }
        if (renderBase) backQuad();
    }

    // -------------------------------------------------------------------------------------

    protected void leftQuad() {
        beginPosXFace();
        beginQuad();
        vertex(1, 1, 1, 0, 0);
        vertex(1, 0, 1, 0, 1);
        vertex(1, 0, 0, 1, 1);
        vertex(1, 1, 0, 1, 0);
        endFace();
    }

    protected void rightQuad() {
        beginNegXFace();
        beginQuad();
        vertex(0, 1, 0, 0, 0);
        vertex(0, 0, 0, 0, 1);
        vertex(0, 0, 1, 1, 1);
        vertex(0, 1, 1, 1, 0);
        endFace();
    }

    protected void frontQuad() {
        beginNegZFace();
        beginQuad();
        vertex(1, 1, 0, 0, 0);
        vertex(1, 0, 0, 0, 1);
        vertex(0, 0, 0, 1, 1);
        vertex(0, 1, 0, 1, 0);
        endFace();
    }

    protected void backQuad() {
        beginPosZFace();
        beginQuad();
        vertex(0, 1, 1, 0, 0);
        vertex(0, 0, 1, 0, 1);
        vertex(1, 0, 1, 1, 1);
        vertex(1, 1, 1, 1, 0);
        endFace();
    }

    protected void bottomQuad() {
        beginBottomFace();
        beginQuad();
        vertex(0, 0, 1, 0, 0);
        vertex(0, 0, 0, 0, 1);
        vertex(1, 0, 0, 1, 1);
        vertex(1, 0, 1, 1, 0);
        endFace();
    }

    protected void leftTriangle() {
        beginPosXFace();
        beginTriangle();
        vertex(1, 1, 1, 0, 0);
        vertex(1, 0, 1, 0, 1);
        vertex(1, 0, 0, 1, 1);
        // vertex(1, 1, 1, 0, 0);
        endFace();
    }

    protected void rightTriangle() {
        beginNegXFace();
        beginTriangle();
        vertex(0, 1, 1, 1, 0);
        vertex(0, 0, 0, 0, 1);
        vertex(0, 0, 1, 1, 1);
        endFace();
    }

    protected void ridgeLeftFace() {
        beginPosXFace();
        beginTriangle();
        vertex(1, 0.5, 0.5, 0.5, 0.5);
        vertex(1, 0, 1, 0, 1);
        vertex(1, 0, 0, 1, 1);
        endFace();
    }

    protected void ridgeRightFace() {
        beginNegXFace();
        beginTriangle();
        vertex(0, 0.5, 0.5, 0.5, 0.5);
        vertex(0, 0, 0, 0, 1);
        vertex(0, 0, 1, 1, 1);
        endFace();
    }

    protected void ridgeBackFace() {
        beginPosZFace();
        beginTriangle();
        vertex(0.5, 0.5, 1, 0.5, 0.5);
        vertex(0, 0, 1, 0, 1);
        vertex(1, 0, 1, 1, 1);
        endFace();
    }

    protected void ridgeFrontFace() {
        beginNegZFace();
        beginTriangle();
        vertex(0.5, 0.5, 0, 0.5, 0.5);
        vertex(1, 0, 0, 0, 1);
        vertex(0, 0, 0, 1, 1);
        endFace();
    }

    protected void ridgeFrontSlope() {
        beginNegZSlope();
        beginTriangle();
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        vertex(1, 0, 0, 0, 1);
        vertex(0, 0, 0, 1, 1);
        endFace();
    }

    protected void ridgeBackSlope() {
        beginPosZSlope();
        beginQuad();
        vertex(0, 0.5, 0.5, 0, 0.5);
        vertex(0, 0, 1, 0, 1);
        vertex(1, 0, 1, 1, 1);
        vertex(1, 0.5, 0.5, 1, 0.5);
        endFace();
    }

    protected void ridgeLeft() {
        if (ridgeOrSlopeAt(1, 0, 0)) connectRidgeLeft();
        else {
            beginPosXSlope();
            beginTriangle();
            vertex(0.5, 0.5, 0.5, 0.5, 0.5);
            vertex(1, 0, 1, 0, 1);
            vertex(1, 0, 0, 1, 1);
            endFace();
        }
    }

    protected void connectRidgeLeft() {
        beginNegZSlope();
        beginTriangle();
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        vertex(1, 0.5, 0.5, 0, 0.5);
        vertex(1, 0, 0, 0, 1);
        endFace();
        beginPosZSlope();
        beginTriangle();
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        vertex(1, 0, 1, 1, 1);
        vertex(1, 0.5, 0.5, 1, 0.5);
        endFace();
    }

    protected void ridgeRight() {
        if (ridgeOrSlopeAt(-1, 0, 0)) connectRidgeRight();
        else {
            beginNegXSlope();
            beginTriangle();
            vertex(0.5, 0.5, 0.5, 0.5, 0.5);
            vertex(0, 0, 0, 0, 1);
            vertex(0, 0, 1, 1, 1);
            endFace();
        }
    }

    protected void connectRidgeRight() {
        beginNegZSlope();
        beginTriangle();
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        vertex(0, 0, 0, 1, 1);
        vertex(0, 0.5, 0.5, 1, 0.5);
        endFace();
        beginPosZSlope();
        beginTriangle();
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        vertex(0, 0.5, 0.5, 0, 0.5);
        vertex(0, 0, 1, 0, 1);
        endFace();
    }

    protected void ridgeFront(boolean fill) {
        if (ridgeOrSlopeAt(0, 0, -1)) connectRidgeFront();
        else if (fill) {
            beginNegZSlope();
            beginTriangle();
            vertex(0.5, 0.5, 0.5, 0.5, 0.5);
            vertex(1, 0, 0, 0, 1);
            vertex(0, 0, 0, 1, 1);
            endFace();
        }
    }

    protected void connectRidgeFront() {
        beginPosXSlope();
        beginTriangle();
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        vertex(1, 0, 0, 1, 1);
        vertex(0.5, 0.5, 0, 1, 0.5);
        endFace();
        beginNegXSlope();
        beginTriangle();
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        vertex(0.5, 0.5, 0, 0, 0.5);
        vertex(0, 0, 0, 0, 1);
        endFace();
    }

    protected void ridgeBack(boolean fill) {
        if (ridgeOrSlopeAt(0, 0, 1)) connectRidgeBack();
        else if (fill) {
            beginPosZSlope();
            beginTriangle();
            vertex(0.5, 0.5, 0.5, 0.5, 0.5);
            vertex(0, 0, 1, 0, 1);
            vertex(1, 0, 1, 1, 1);
            endFace();
        }
    }

    protected void connectRidgeBack() {
        beginPosXSlope();
        beginTriangle();
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        vertex(0.5, 0.5, 1, 0, 0.5);
        vertex(1, 0, 1, 0, 1);
        endFace();
        beginNegXSlope();
        beginTriangle();
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        vertex(0, 0, 1, 1, 1);
        vertex(0.5, 0.5, 1, 1, 0.5);
        endFace();
    }

    protected void connectValleyLeft() {
        beginPosZSlope();
        beginTriangle();
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        vertex(1, 0.5, 0.5, 1, 0.5);
        vertex(1, 1, 0, 1, 0);
        endFace();
        beginNegZSlope();
        beginTriangle();
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        vertex(1, 1, 1, 0, 0);
        vertex(1, 0.5, 0.5, 0, 0.5);
        endFace();
        valleyEndLeft();
    }

    protected void connectValleyRight() {
        beginPosZSlope();
        beginTriangle();
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        vertex(0, 1, 0, 0, 0);
        vertex(0, 0.5, 0.5, 0, 0.5);
        endFace();
        beginNegZSlope();
        beginTriangle();
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        vertex(0, 0.5, 0.5, 1, 0.5);
        vertex(0, 1, 1, 1, 0);
        endFace();
        valleyEndRight();
    }

    protected void connectValleyFront() {
        beginPosXSlope();
        beginTriangle();
        vertex(0, 1, 0, 1, 0);
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        vertex(0.5, 0.5, 0, 1, 0.5);
        endFace();
        beginNegXSlope();
        beginTriangle();
        vertex(1, 1, 0, 0, 0);
        vertex(0.5, 0.5, 0, 0, 0.5);
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        endFace();
        valleyEndFront();
    }

    protected void connectValleyBack() {
        beginPosXSlope();
        beginTriangle();
        vertex(0, 1, 1, 0, 0);
        vertex(0.5, 0.5, 1, 0, 0.5);
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        endFace();
        beginNegXSlope();
        beginTriangle();
        vertex(1, 1, 1, 1, 0);
        vertex(0.5, 0.5, 0.5, 0.5, 0.5);
        vertex(0.5, 0.5, 1, 1, 0.5);
        endFace();
        valleyEndBack();
    }

    protected void valleyEndLeft() {
        beginPosXFace();
        beginTriangle();
        vertex(1, 1, 1, 0, 0);
        vertex(1, 0, 1, 0, 1);
        vertex(1, 0.5, 0.5, 0.5, 0.5);
        newTriangle();
        vertex(1, 0, 1, 0, 1);
        vertex(1, 0, 0, 1, 1);
        vertex(1, 0.5, 0.5, 0.5, 0.5);
        newTriangle();
        vertex(1, 0, 0, 1, 1);
        vertex(1, 1, 0, 1, 0);
        vertex(1, 0.5, 0.5, 0.5, 0.5);
        endFace();
    }

    protected void valleyEndRight() {
        beginNegXFace();
        beginTriangle();
        vertex(0, 0, 1, 1, 1);
        vertex(0, 1, 1, 1, 0);
        vertex(0, 0.5, 0.5, 0.5, 0.5);
        newTriangle();
        vertex(0, 0, 0, 0, 1);
        vertex(0, 0, 1, 1, 1);
        vertex(0, 0.5, 0.5, 0.5, 0.5);
        newTriangle();
        vertex(0, 1, 0, 0, 0);
        vertex(0, 0, 0, 0, 1);
        vertex(0, 0.5, 0.5, 0.5, 0.5);
        endFace();
    }

    protected void valleyEndFront() {
        beginNegZFace();
        beginTriangle();
        vertex(1, 1, 0, 0, 0);
        vertex(1, 0, 0, 0, 1);
        vertex(0.5, 0.5, 0, 0.5, 0.5);
        newTriangle();
        vertex(1, 0, 0, 0, 1);
        vertex(1, 0, 0, 1, 1);
        vertex(0.5, 0.5, 0, 0.5, 0.5);
        newTriangle();
        vertex(0, 0, 0, 1, 1);
        vertex(0, 1, 0, 1, 0);
        vertex(0.5, 0.5, 0, 0.5, 0.5);
        endFace();
    }

    protected void valleyEndBack() {
        beginPosZFace();
        beginTriangle();
        vertex(0, 1, 1, 0, 0);
        vertex(0, 0, 1, 0, 1);
        vertex(0.5, 0.5, 1, 0.5, 0.5);
        newTriangle();
        vertex(0, 0, 1, 0, 1);
        vertex(1, 0, 1, 1, 1);
        vertex(0.5, 0.5, 1, 0.5, 0.5);
        newTriangle();
        vertex(1, 0, 1, 1, 1);
        vertex(1, 1, 1, 1, 0);
        vertex(0.5, 0.5, 1, 0.5, 0.5);
        endFace();
    }

    // -------------------------------------------------------------------------------------

    protected boolean ridgeAt(int dx, int dy, int dz) {
        return hasNeighbour(dx, dy, dz, ridgeShapes);
    }

    protected boolean ridgeOrSlopeAt(int dx, int dy, int dz) {
        return hasNeighbour(dx, dy, dz, ridgeOrSlopeShapes);
    }

    protected boolean valleyAt(int dx, int dy, int dz) {
        return hasNeighbour(dx, dy, dz, valleyShapes);
    }

    protected boolean valleyOrSlopeAt(int dx, int dy, int dz) {
        return hasNeighbour(dx, dy, dz, valleyOrSlopeShapes);
    }

    protected boolean hasNeighbour(int dx, int dy, int dz, Shape[] shapes) {
        Vector3 v = t.v(dx, dy, dz);
        EnumFacing dir = v.facing();
        TileShape nte = te.getConnectedNeighbourGlobal(dir);
        if (nte != null) {
            for (int i = 0; i < shapes.length; i++) if (nte.shape == shapes[i]) return true;
        }
        return false;
    }

    // -------------------------------------------------------------------------------------

    protected void beginTopFace() {
        beginOuterFaces(Vector3.unitY);
    }

    protected void beginBottomFace() {
        beginOuterFaces(Vector3.unitNY);
    }

    protected void beginPosXFace() {
        beginOuterFaces(Vector3.unitX);
    }

    protected void beginNegXFace() {
        beginOuterFaces(Vector3.unitNX);
    }

    protected void beginPosZFace() {
        beginOuterFaces(Vector3.unitZ);
    }

    protected void beginNegZFace() {
        beginOuterFaces(Vector3.unitNZ);
    }

    protected void beginPosXSlope() {
        beginInnerFaces(Vector3.unitPXPY);
    }

    protected void beginNegXSlope() {
        beginInnerFaces(Vector3.unitNXPY);
    }

    protected void beginPosZSlope() {
        beginInnerFaces(Vector3.unitPYPZ);
    }

    protected void beginNegZSlope() {
        beginInnerFaces(Vector3.unitPYNZ);
    }

    // -------------------------------------------------------------------------------------

    protected void beginInnerFaces(Vector3 n) {
        outerFace = false;
        normal(n);
        target.setTexture(textures[2]);
    }

    protected void beginOuterFaces(Vector3 n) {
        outerFace = true;
        normal(n);
        target.setTexture(textures[1]);
    }

    protected void beginTriangle() {
        target.beginTriangle();
    }

    protected void beginQuad() {
        target.beginQuad();
    }

    protected void newTriangle() {
        endFace();
        beginTriangle();
    }

    protected void newQuad() {
        endFace();
        beginQuad();
    }

    protected void endFace() {
        target.endFace();
    }

    protected void normal(Vector3 n) {
        Vector3 tn = t.v(n);
        face = tn.facing();
        target.setNormal(tn);
    }

    protected void vertex(double x, double y, double z, double u, double v) {
        Vector3 q = t.p(x - 0.5, y - 0.5, z - 0.5);
        target.addVertex(q, u, v);
    }

}
