package gcewing.architecture.legacy.rendering;

class WindowModels {

    public final IModel centre;
    public final IModel[] centreEnd;
    public final IModel[] side;
    public final IModel[] end0;
    public final IModel[] end1;
    public final IModel glass;
    public final IModel[] glassEdge;

    public WindowModels(IModel centre, IModel[] centreEnd, IModel[] side, IModel[] end0, IModel[] end1, IModel glass,
            IModel[] glassEdge) {
        this.centre = centre;
        this.centreEnd = centreEnd;
        this.side = side;
        this.end0 = end0;
        this.end1 = end1;
        this.glass = glass;
        this.glassEdge = glassEdge;
    }
}
