package chapter02.eight;

public class Main {
    public static void main(String[] args) {
        FileMock fileMock = new FileMock(100, 20);
        Buffer buffer = new Buffer(20);
        Producer producer = new Producer(fileMock, buffer);
        Thread threadProducer = new Thread(producer, "Producer");

        Consumer[] consumer = new Consumer[3];
        Thread[] threadConsumers = new Thread[3];
        for (int i = 0;i<3;i++) {
            consumer[i] = new Consumer(buffer);
            threadConsumers[i] = new Thread(consumer[i],"Consumer " + i);
        }
        threadProducer.start();
        for (int i = 0;i<3;i++) {
            threadConsumers[i].start();
        }
    }
}
