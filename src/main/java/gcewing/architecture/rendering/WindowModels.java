package gcewing.architecture.rendering;

import gcewing.architecture.BaseModClient;

class WindowModels {

    public final BaseModClient.IModel centre;
    public final BaseModClient.IModel[] centreEnd;
    public final BaseModClient.IModel[] side;
    public final BaseModClient.IModel[] end0;
    public final BaseModClient.IModel[] end1;
    public final BaseModClient.IModel glass;
    public final BaseModClient.IModel[] glassEdge;

    public WindowModels(BaseModClient.IModel centre, BaseModClient.IModel[] centreEnd, BaseModClient.IModel[] side, BaseModClient.IModel[] end0,
            BaseModClient.IModel[] end1, BaseModClient.IModel glass, BaseModClient.IModel[] glassEdge) {
        this.centre = centre;
        this.centreEnd = centreEnd;
        this.side = side;
        this.end0 = end0;
        this.end1 = end1;
        this.glass = glass;
        this.glassEdge = glassEdge;
    }
}
