package bgu.spl.mics;


import bgu.spl.mics.application.services.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
    private ConcurrentHashMap<String, SynchronousQueue<Message>> microServiceQueues;
    private LinkedList<MicroService> RoundRobinAttackersQ;
    private Semaphore lockRoundRobin = new Semaphore(1, true);
    private ConcurrentHashMap<Class<? extends Message>, LinkedList<MicroService>> subscriptionMap;
    private Semaphore subscriptionLock = new Semaphore(1, true);
    private ConcurrentHashMap<Message, Future> futureList;
    //making this class a singleton
    private static volatile MessageBusImpl instance;
    private static Object lock = new Object();

    //single shtok
    public static MessageBusImpl getInstance() {
        MessageBusImpl msgBus = instance;
        if (msgBus == null) {
            synchronized (lock) {
                msgBus = instance;
                if (msgBus == null)
                    instance = msgBus = new MessageBusImpl();
            }
        }
        return msgBus;
    }

    //Builder
    private MessageBusImpl() {
        RoundRobinAttackersQ = new LinkedList<>();
        subscriptionMap = new ConcurrentHashMap<>();

        microServiceQueues = new ConcurrentHashMap<>();
        futureList = new ConcurrentHashMap<>();
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {

        if (!subscriptionMap.containsKey(type)) {
            synchronized(this){// (subscriptionLock) {
                if (!subscriptionMap.containsKey(type))
                    subscriptionMap.put(type, new LinkedList<MicroService>());

            }
        }
        subscriptionMap.get(type).add(m);
    }


    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        if (!subscriptionMap.containsKey(type)) {
            synchronized(this){// (subscriptionLock) {
                if (!subscriptionMap.containsKey(type))
                    subscriptionMap.put(type, new LinkedList<MicroService>());

            }
        }
        subscriptionMap.get(type).add(m);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void complete(Event<T> e, T result) {
        futureList.get(e).resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        LinkedList<MicroService> list = subscriptionMap.get(b.getClass());
        Iterator<MicroService> it = list.iterator();
        while (it.hasNext()) {
            MicroService m = it.next();
            microServiceQueues.get(m.getName()).add(b);
        }
    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        Future future = new Future<>();
        LinkedList<MicroService>  list = subscriptionMap.get(e.getClass());
        if (list == null) {
            try {
                throw new IllegalAccessException("lama????");
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        } else if (list.size() == 1) {//e is DeactivationEvent or BombDestroyerEvent
            microServiceQueues.get(list.getFirst().getName()).add(e);
            futureList.put(e,future);
        } else {// e is an instance of AttackEvent
            synchronized (lockRoundRobin) {
                MicroService m = RoundRobinAttackersQ.removeFirst();
                microServiceQueues.get(m.getName()).add(e);
                RoundRobinAttackersQ.addLast(m);
                futureList.put(e,future);
            }
        }
        return future;
    }

    @Override
    public void register(MicroService m) {
        microServiceQueues.put(m.getName(), new SynchronousQueue<>());
        if (m instanceof HanSoloMicroservice)
            synchronized (lockRoundRobin){
                RoundRobinAttackersQ.addLast(m);
        }
        else if(m instanceof C3POMicroservice)
            synchronized (lockRoundRobin){
                RoundRobinAttackersQ.addLast(m);
            }
    }

    @Override
    public void unregister(MicroService m) {
        microServiceQueues.remove(m.getName());
    }

    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        return microServiceQueues.get(m.getName()).take();
    }
}