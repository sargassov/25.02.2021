import cars.Car;
import races.Race;
import stages.Road;
import stages.Tunnel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Program {

//    Организуем гонки:
//    Все участники должны стартовать одновременно, несмотря на то, что на подготовку у каждого из них уходит разное время.
//    В туннель не может заехать одновременно больше половины участников (условность).
//    Попробуйте всё это синхронизировать.
//    Только после того как все завершат гонку, нужно выдать объявление об окончании.
//    Можете корректировать классы (в т.ч. конструктор машин) и добавлять объекты классов из пакета util.concurrent.
//    Пример выполнения кода до корректировки:

    //private static boolean isStarted = false;
    private static final int CARS_COUNT = 4;
    private static final int SIGNAL_TO_START = 1;
    private static final int HALF_OF_CARS_COUNT = CARS_COUNT / 2;
    private static CyclicBarrier barrier = new CyclicBarrier(CARS_COUNT);
    private static ExecutorService service;
    private static CountDownLatch startLatch = new CountDownLatch(CARS_COUNT);
    private static CountDownLatch readyLatch = new CountDownLatch(SIGNAL_TO_START);
    private static CountDownLatch finishLatch = new CountDownLatch(CARS_COUNT);
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        service = Executors.newFixedThreadPool(CARS_COUNT);
        Race race = new Race(new Road(60), new Tunnel(HALF_OF_CARS_COUNT), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
       // barrier.await();
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), barrier, startLatch, readyLatch, finishLatch);
        }
        for (int i = 0; i < cars.length; i++) {
            service.execute(cars[i]);
        }
        startLatch.await();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        readyLatch.countDown();
        finishLatch.await();
        service.shutdown();
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
