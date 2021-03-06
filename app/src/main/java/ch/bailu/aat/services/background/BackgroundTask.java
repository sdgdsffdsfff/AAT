package ch.bailu.aat.services.background;

import ch.bailu.aat.services.ServiceContext;

public abstract class BackgroundTask implements ThreadControl {
    
    public static final BackgroundTask NULL = new BackgroundTask() {

        @Override
        public long bgOnProcess(ServiceContext sc) {
            return 0;
        }
    };

/*
    public static final BackgroundTask NULL_PROCESSED = new BackgroundTask() {

        @Override
        public long bgOnProcess(ServiceContext sc) {
            return 0;
        }

        public boolean canContinue() {
            return false;
        }
    };
*/

    private boolean processing = true;
    
    
    @Override
    public boolean canContinue() {
        return processing;
    }
    
    

    public abstract long bgOnProcess(ServiceContext sc);
 
    
    public synchronized void stopProcessing() {
        processing =false;
    }



    public ThreadControl getThreadControl() {
        return this;
    }

    public void onInsert() {}
    public void onRemove() {}
}
