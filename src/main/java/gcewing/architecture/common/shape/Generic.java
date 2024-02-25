package gcewing.architecture.common.shape;

public enum Generic {

    End,
    LeftEnd,
    RightEnd,
    OffsetBottom,
    OffsetTop;

    static {
        Profile.declareOpposite(LeftEnd, RightEnd);
        Profile.declareOpposite(OffsetBottom, OffsetTop);
    }

    public static Generic[] eeStraight = { null, null, null, null, End, End };
    public static final Generic[] lrStraight = { null, null, null, null, RightEnd, LeftEnd };
    public static Generic[] eeCorner = { null, null, null, End, End, null };
    public static final Generic[] lrCorner = { null, null, null, LeftEnd, RightEnd, null };
    public static final Generic[] rlCorner = { null, null, RightEnd, null, null, LeftEnd };
    public static Generic[] tOffset = { null, OffsetTop, null, null, null, null };
    public static Generic[] bOffset = { OffsetBottom, null, null, null, null, null };
    public static final Generic[] tbOffset = { OffsetBottom, OffsetTop, null, null, null, null };

}
