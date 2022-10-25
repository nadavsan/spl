package bgu.spl.mics.application.passiveObjects;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {

    private AtomicInteger attacksCounter;
    private String HanFinish,C3POFinish, R2D2Deactivate,LeiaTerminate,HanSoloTerminate,C3POTerminate,R2D2Terminate, LandoTerminate;
    private static volatile Diary instance;
    private static Object lock = new Object();


    private Diary(){
        HanFinish = "";
        HanSoloTerminate = "";
        C3POFinish = "";
        C3POTerminate = "";
        R2D2Deactivate = "";
        R2D2Terminate ="";
        LandoTerminate = "";
        LeiaTerminate = "";
        attacksCounter = new AtomicInteger(0);


    }

    public static Diary getInstance(){
        Diary d = instance;
        if(d == null){
            synchronized (lock){
                d = instance;
                if(d == null)
                    instance = d = new Diary();
            }
        }
        return d;
    }

    public void setHanFinish(long timeMillis) {
        HanFinish = ""+ timeMillis;
        attacksCounter.incrementAndGet();
    }

    public void setC3POFinish(long timeMillis){
        C3POFinish = ""+ timeMillis ;
        attacksCounter.incrementAndGet();
    }

    public void setR2D2Deactivate(long timeMillis) {
        R2D2Deactivate = ""+ timeMillis;;
    }

    public void setC3POTerminate(long timeMillis) {
        C3POTerminate = ""+ timeMillis ;
    }

    public void setHanSoloTerminate(long timeMillis) {
        HanSoloTerminate = ""+ timeMillis;
    }
    public void setR2D2Terminate(long timeMillis) {
        R2D2Terminate = ""+ timeMillis;
    }
    public void setLandoTerminate(long timeMillis) {
        LandoTerminate =  ""+ timeMillis;
    }

    public void setLeiaTerminate(long timeMillis) {
        LeiaTerminate = ""+ timeMillis;
    }
    public AtomicInteger getAttacksCounter(){
        return attacksCounter;
    }


}
