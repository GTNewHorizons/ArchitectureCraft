package gcewing.architecture.common.shape;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import net.minecraft.util.EnumFacing;

import gcewing.architecture.client.render.ITexture;
import gcewing.architecture.client.render.target.IRenderTarget;
import gcewing.architecture.compat.Vector3;

final class FaceCoverage implements IRenderTarget {

    private static final double EPSILON = 1e-7;

    private final EnumFacing side;
    private final double plane;
    private final Area uncovered = new Area(new Rectangle2D.Double(-0.5, -0.5, 1, 1));
    private final Path2D.Double polygon = new Path2D.Double();
    private boolean coplanar;
    private int vertices;

    FaceCoverage(EnumFacing side) {
        this.side = side;
        this.plane = 0.5 * (side.getFrontOffsetX() + side.getFrontOffsetY() + side.getFrontOffsetZ());
    }

    boolean isCovered() {
        return uncovered.isEmpty();
    }

    @Override
    public void beginTriangle() {
        polygon.reset();
        coplanar = true;
        vertices = 0;
    }

    @Override
    public void beginQuad() {
        beginTriangle();
    }

    @Override
    public void addVertex(Vector3 point, double u, double v) {
        switch (side) {
            case DOWN, UP -> addPoint(point.y, point.x, point.z);
            case NORTH, SOUTH -> addPoint(point.z, point.x, point.y);
            case WEST, EAST -> addPoint(point.x, point.z, point.y);
        }
    }

    private void addPoint(double depth, double x, double y) {
        if (Math.abs(depth - plane) > EPSILON) coplanar = false;
        if (!coplanar || isCovered()) return;
        x = snapToEdge(x);
        y = snapToEdge(y);
        if (vertices++ == 0) polygon.moveTo(x, y);
        else polygon.lineTo(x, y);
    }

    private static double snapToEdge(double coordinate) {
        double edge = Math.copySign(0.5, coordinate);
        return Math.abs(coordinate - edge) <= EPSILON ? edge : coordinate;
    }

    @Override
    public void endFace() {
        if (!coplanar || vertices < 3) return;
        polygon.closePath();
        uncovered.subtract(new Area(polygon));
    }

    @Override
    public void addProjectedVertex(Vector3 point, EnumFacing face) {
        addVertex(point, 0, 0);
    }

    @Override
    public boolean isRenderingBreakEffects() {
        return false;
    }

    @Override
    public void setTexture(ITexture texture) {}

    @Override
    public void setColor(double r, double g, double b, double a) {}

    @Override
    public void setNormal(Vector3 normal) {}
}
