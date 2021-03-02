package stages;

import cars.Car;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {

    private static int FULL_TUNNEL;
    private static Semaphore semaphore;

    public Tunnel(int full_tunnel) {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
        FULL_TUNNEL = full_tunnel;
        semaphore = new Semaphore(FULL_TUNNEL);
    }
    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                semaphore.acquire();
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
                semaphore.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
