package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private long sleepT;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.sleepT = duration;
    }

    @Override
    protected void initialize() {

        //Creating a BombDestroyer callback implementation with a Lambda expression
       subscribeEvent(BombDestroyerEvent.class,(BombDestroyerEvent b) ->
       {try{
           Thread.sleep(sleepT);
       } catch (InterruptedException ignored) {}
       complete(b, true);
       TerminateBroadcast tb = new TerminateBroadcast();
       sendBroadcast(tb);
       terminate();
       d.setLandoTerminate(System.currentTimeMillis());
       });

        try {
            initiated.getCountDownLatch().countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
