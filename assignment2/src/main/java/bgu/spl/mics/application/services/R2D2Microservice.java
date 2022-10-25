package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;


/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private long sleepT;

    public R2D2Microservice(long duration) {
        super("R2D2");
        sleepT = duration;
    }

    @Override
    protected void initialize() {
        //Creating a DeactivationEvent callback implementation with a Lambda expression
        subscribeEvent(DeactivationEvent.class, (msg) -> {
            try {
                Thread.sleep(sleepT);
            } catch (InterruptedException ignored) {}
            complete(msg,true);
            d.setR2D2Deactivate(System.currentTimeMillis());
            sendEvent(new BombDestroyerEvent());
        });

        //Creating a TerminateBroadcast callback implementation with a Lambda expression
        subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast tb) ->{
            terminate();
            d.setR2D2Terminate(System.currentTimeMillis());
        });

        //waiting for all MicroServices to be ready to start Leia
        try {
            initiated.getCountDownLatch().countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
