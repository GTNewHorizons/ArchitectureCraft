package gcewing.architecture.shapes;

import java.util.List;

import gcewing.architecture.compat.Trans3;

public class MullionWindow extends PlainWindow {

    @Override
    protected void addCentreBoxesToList(double r, double s, Trans3 t, List list) {
        t.addBox(-r, -0.5, -s, r, 0.5, s, list);
    }

    @Override
    protected void addGlassBoxesToList(double r, double s, double w, double[] e, Trans3 t, List list) {
        t.addBox(-e[3], -e[0], -w, -r, e[2], w, list);
        t.addBox(r, -e[0], -w, e[1], e[2], w, list);
    }

}
