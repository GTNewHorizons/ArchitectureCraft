package gcewing.architecture.client.render.model;

public class WindowModel {

    public final IArchitectureModel centre;
    public final IArchitectureModel[] centreEnd;
    public final IArchitectureModel[] side;
    public final IArchitectureModel[] end0;
    public final IArchitectureModel[] end1;
    public final IArchitectureModel glass;
    public final IArchitectureModel[] glassEdge;

    public WindowModel(IArchitectureModel centre, IArchitectureModel[] centreEnd, IArchitectureModel[] side,
            IArchitectureModel[] end0, IArchitectureModel[] end1, IArchitectureModel glass,
            IArchitectureModel[] glassEdge) {
        this.centre = centre;
        this.centreEnd = centreEnd;
        this.side = side;
        this.end0 = end0;
        this.end1 = end1;
        this.glass = glass;
        this.glassEdge = glassEdge;
    }
}
