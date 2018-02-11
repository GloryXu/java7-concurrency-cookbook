package chapter03.two;

import java.util.concurrent.Semaphore;

public class PrintQueue {
    private final Semaphore semaphore;

    public PrintQueue() {
        // 信号量的初始值是1，所有创建的就是二进制信号量，
        // 所以只能保护一个共享资源的访问
        this.semaphore = new Semaphore(1);
        // 是否公平
//        this.semaphore = new Semaphore(1,true);
    }

    public void printJob(Object document) {
        try{
            semaphore.acquire();
            long duration = (long) (Math.random() * 10);
            System.out.printf("%s: PrintQueue:Printing a Job during %d seconds\n",Thread.currentThread().getName(),duration);
            Thread.sleep(duration);
        }catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            semaphore.release();
        }
    }
}
