package chapter02.six;

public class Main {
    public static void main(String[] args) {
        PricesInfo pricesInfo = new PricesInfo();
        Thread[] threadReader = new Thread[5];
        Reader reader[] = new Reader[5];
        for(int i = 0;i<5;i++) {
            reader[i] = new Reader(pricesInfo);
            threadReader[i] = new Thread(reader[i]);
        }

        Writer writer = new Writer(pricesInfo);
        Thread threadWrite = new Thread(writer);
        for(int i = 0;i<5;i++) {
            threadReader[i].start();
        }
        threadWrite.start();
    }
}
