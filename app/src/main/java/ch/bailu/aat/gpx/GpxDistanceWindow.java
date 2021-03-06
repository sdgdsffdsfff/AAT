package ch.bailu.aat.gpx;

import android.content.Context;

import ch.bailu.aat.description.DistanceDescription;

public class GpxDistanceWindow extends GpxWindow {

    private final float distanceLimit;


    public GpxDistanceWindow(GpxPointNode node, float limit) {
        super(node);
        distanceLimit = limit;
    }


    public GpxDistanceWindow(GpxList list) {
        super((GpxPointNode) list.getPointList().getFirst());
        distanceLimit = getDistanceLimit(list.getDelta().getDistance());
    }


    @Override
    protected boolean overLmit() {
        return getDistance() > distanceLimit;
    }


    public String getLimitAsString(Context c) {
        if (distanceLimit > 0)
            return new DistanceDescription(c).getDistanceDescription(distanceLimit);
        return "";

    }

    private static GpxDistanceWindow createDistanceWindow(GpxList list) {
        final float distance = list.getDelta().getDistance();
        final GpxPointNode first = (GpxPointNode) list.getPointList().getFirst();
        final int limit = getDistanceLimit(distance);

        return new GpxDistanceWindow(first, limit);
    }


    private static int getDistanceLimit(float distance) {

        if (distance > 60000) return 2000;
        if (distance > 30000) return 1000;
        if (distance > 10000) return 500;
        if (distance > 5000) return 100;
        if (distance > 2000) return 50;
        if (distance > 1000) return 10;
        if (distance > 0) return 5;
        return 0;
    }

}
