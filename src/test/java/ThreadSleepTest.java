public class ThreadSleepTest {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("hahahahahhah");
            }
        });
        thread.start();
        try {
            thread.sleep(10000);//这里sleep的就是main线程，而非thread线程
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main hahhah");
    }
}
