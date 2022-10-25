package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.Semaphore;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
    int serialNumber;
    boolean available;
    private Semaphore lock = new Semaphore(1, true);

    public Ewok(){
        serialNumber = -1;
        available = false;
    }

    public Ewok(int serialNumber){
        this.serialNumber = serialNumber;
        available = true;
    }
    /**
     * Acquires an Ewok
     */
    public void acquire() {
        while(!available){
            boolean acquired = lock.tryAcquire();
            if(acquired)
                available = false;
        }
        if (available) {
            try {
                lock.acquire();
                available = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * release an Ewok
     */
    public void release() {
        available = true;
        lock.release();

    }

    /*
    *we added a getter to available for the tests
    */
    public boolean getAvailable(){return available;}



}
