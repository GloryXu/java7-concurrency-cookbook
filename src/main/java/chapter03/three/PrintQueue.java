package chapter03.three;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintQueue {
    private boolean freePrinters[];
    private Lock lockPrinters;
    private final Semaphore semaphore;

    public PrintQueue() {
        /**
         * 允许三个线程对临界区进行访问
         */
        this.semaphore = new Semaphore(3);
        freePrinters = new boolean[3];
        for(int i = 0;i<3;i++) {
            freePrinters[i] = true;
        }
        lockPrinters = new ReentrantLock();
    }

    public void printJob(Object document) {
        try{
            semaphore.acquire();
            int assignedPrinter = getPrinter();
            long duration = (long) (Math.random() * 10);
            System.out.printf("%s: PrintQueue:Printing a Job in Printer assignedPrinter %d during %d seconds\n",
                    Thread.currentThread().getName(),assignedPrinter, duration);
            TimeUnit.SECONDS.sleep(duration);
            freePrinters[assignedPrinter] = true;
        }catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            semaphore.release();
        }
    }

    private int getPrinter() {
        int ret = -1;
        try {
            lockPrinters.lock();
            for (int i = 0;i<freePrinters.length;i++) {
                if (freePrinters[i]) {
                    ret = i;
                    freePrinters[i] = false;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockPrinters.unlock();
        }
        return ret;
    }
}
