package chapter03.four;

import java.util.concurrent.CountDownLatch;

public class Videoconference implements Runnable {
    private final CountDownLatch contoller;

    public Videoconference(int number) {
        contoller = new CountDownLatch(number);
    }

    @Override
    public void run() {
        System.out.printf("VideoConference:Initialization:%d participants.\n",contoller.getCount());
        try{
            contoller.await();
            System.out.printf("VideoConference:All the participants have come\n");
            System.out.printf("VideoConference:Let's start...\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void arrive(String name) {
        System.out.printf("%s has arrived.", name);
        contoller.countDown();
        System.out.printf("VideoConference:Waiting for %d participants.\n",contoller.getCount());
    }
}
