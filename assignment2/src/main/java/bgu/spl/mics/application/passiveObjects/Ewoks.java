package bgu.spl.mics.application.passiveObjects;


import java.security.PublicKey;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private static volatile Ewoks instance;
    private static Object lock = new Object();
    private List<Ewok> ewoks;

    private Ewoks(){
        ewoks = null;
    }

    private Ewoks(int amount){
        ewoks = new LinkedList<>();
        for (int i = 1; i <= amount; i++)
            ewoks.add(new Ewok(i));
    }

    // static method to create instance of Ewoks class
    public static Ewoks getInstance() {
        Ewoks e = instance;
        if (e == null) {
            synchronized (lock) {
                e = instance;
                if (e == null)
                    instance = e = new Ewoks();
            }lock.notifyAll();
        }
        return e;
    }

    public static Ewoks getInstance(int amount) {
        Ewoks e = instance;
        if (e == null) {
            synchronized (lock) {
                e = instance;
                if (e == null)
                    instance = e = new Ewoks(amount);
            }
        }
        return e;
    }

    public void acquireEwoks(List<Integer> list) {
            for (Integer n : list)
                ewoks.get(n - 1).acquire();
    }
    public void releaseEwoks(List<Integer> list){
            for (Integer n : list){
                ewoks.get(n - 1).release();
            }
    }
}

