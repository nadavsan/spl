package bgu.spl.mics.application.services;

import java.util.LinkedHashMap;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.messages.AttackEvent;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	private LinkedHashMap<Attack, Future> futureMap;
	
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
		futureMap = new LinkedHashMap<>();
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast tb) ->{
            terminate();
            d.setLeiaTerminate(System.currentTimeMillis());
        } );
    	for(Attack a: attacks){
    	    futureMap.put(a,sendEvent(new AttackEvent(a)));
        }
        for(Attack a: attacks){
            futureMap.get(a).get();
        }
        Future<Boolean> f = sendEvent(new DeactivationEvent());

    }
}
