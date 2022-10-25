package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
	private Ewoks ewoks;

    public C3POMicroservice() {
        super("C3PO");
        ewoks = Ewoks.getInstance();
    }

    @Override
    protected void initialize() {
        //Creating a AttackEvent callback implementation with a Lambda expression
        subscribeEvent(AttackEvent.class, (AttackEvent event) -> {
            while (event == null){
                try {
                    event.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            List<Integer> requiredEwoks = event.getSerials();
            ewoks.acquireEwoks(requiredEwoks);
            int duration = event.getDuration();
            try {
                Thread.sleep(duration);
            } catch (InterruptedException ignored) {}

            ewoks.releaseEwoks(requiredEwoks);
            complete(event,true);
            d.setC3POFinish(System.currentTimeMillis());
        });

        //Creating a TerminateBroadcast callback implementation with a Lambda expression
        subscribeBroadcast(TerminateBroadcast.class, (msg)-> {
            terminate();
            d.setC3POTerminate(System.currentTimeMillis());
        });

        //waiting for all MicroServices to be ready to start Leia
        try {
            initiated.getCountDownLatch().countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
