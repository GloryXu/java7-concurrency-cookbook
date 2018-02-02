package chapter02.five;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintQueue {
    /**
     * ReentrantLock类允许使用递归调用。
     * 如果一个线程获取了锁并且进行了递归调用，它将继续持有这个锁，因此调用lock()也将立即返回
     */
    private final Lock queueLock = new ReentrantLock();

    public void printJob(Object document) {
        queueLock.lock();
        try {
            Long duration = (long) (Math.random() * 10000);
            System.out.println(Thread.currentThread().getName() + ":PrintQueue:Printing a Job during " + (duration/1000) + " seconds");;
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            queueLock.unlock();
        }
    }
}
