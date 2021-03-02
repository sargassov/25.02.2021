package cars;

import races.Race;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
    public static boolean isStarted;
    private static CyclicBarrier barrier;
    private Race race;
    private int speed;
    private String name;
    private final Object mon = new Object();
    private static CountDownLatch startLatch;
    private static CountDownLatch readyLatch;
    private static CountDownLatch finishLatch;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed, CyclicBarrier barr, CountDownLatch sLatch,
               CountDownLatch rLatch, CountDownLatch fLatch) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        barrier = barr;
        startLatch = sLatch;
        readyLatch = rLatch;
        finishLatch = fLatch;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            barrier.await();
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            barrier.await();
            startLatch.countDown();
            readyLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        finishLatch.countDown();
    }
}
