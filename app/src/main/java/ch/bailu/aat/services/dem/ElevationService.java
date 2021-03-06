package ch.bailu.aat.services.dem;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.services.dem.loader.Dem3Loader;
import ch.bailu.aat.services.dem.loader.Dem3Tiles;
import ch.bailu.aat.services.dem.tile.ElevationProvider;
import ch.bailu.aat.services.dem.updater.ElevationUpdaterClient;
import ch.bailu.aat.services.dem.updater.ElevationUpdater;

public class ElevationService extends VirtualService implements ElevationProvider{

    private final ElevationUpdater updater;
    private final Dem3Loader loader;


    public ElevationService(ServiceContext sc) {
        super(sc);

        Dem3Tiles tiles = new Dem3Tiles();
        loader = new Dem3Loader(sc, tiles);
        updater = new ElevationUpdater(sc, loader, tiles);

    }

    @Override
    public void appendStatusText(StringBuilder builder) {

    }



    public void requestElevationUpdates(ElevationUpdaterClient e, SrtmCoordinates[] c) {
        updater.requestElevationUpdates(e, c);
    }

    public void requestElevationUpdates() {
        updater.requestElevationUpdates();
    }


    public void cancelElevationUpdates(ElevationUpdaterClient e) {
        updater.cancelElevationUpdates(e);
    }



    @Override
    public short getElevation(int laE6, int loE6) {
        return loader.getElevation(laE6, loE6);
    }


    @Override
    public void close() {
        updater.close();
        loader.close();
    }
}
