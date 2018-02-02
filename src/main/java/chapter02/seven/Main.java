package chapter02.seven;

public class Main {
    public static void main(String[] args) {
        PrintQueue printQueue = new PrintQueue();
        Thread[] threads = new Thread[10];
        for (int i = 0;i<10;i++) {
            threads[i] = new Thread(new Job(printQueue));
        }

        for(int j = 0;j<10;j++) {
            threads[j].start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
