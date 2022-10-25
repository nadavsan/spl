package bgu.spl.mics;


import java.util.concurrent.CountDownLatch;

public class Initiated {
    private static volatile Initiated instance;
    private static Object lock = new Object();
    private CountDownLatch countDownLatch;

    private Initiated(int num){
        countDownLatch = new CountDownLatch(num);
    }
    private Initiated(){
        countDownLatch = null;
    }

    public static Initiated getInstance(int num) {
        Initiated initiated = instance;
        if (initiated == null) {
            synchronized (lock) {
                initiated = instance;
                if (initiated == null)
                    instance = initiated = new Initiated(num);
            }
        }
        return initiated;
    }
    public static Initiated getInstance() {
        Initiated initiated = instance;
        if (initiated == null) {
            synchronized (lock) {
                initiated = instance;
                if (initiated == null)
                    instance = initiated = new Initiated();
            }
        }
        return initiated;
    }
    public CountDownLatch getCountDownLatch(){
        return countDownLatch;
    }
}
