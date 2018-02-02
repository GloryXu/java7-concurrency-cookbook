package chapter02.four;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class EventStorage {
    private int maxSize;
    private List<Date> storage;

    public EventStorage() {
        maxSize = 10;
        storage = new LinkedList<>();
    }

    /**
     * 检查列表是不是满的，如果已满，就调用wait()方法挂起线程并等待空余空间出现。
     * 在这个方法的最后，调用notifyAll()方法唤醒所有因调用wait()方法进入休眠的线程
     */
    public synchronized void set() {
        while(storage.size() == maxSize) {
            try{
                /*
                 * 如果在同步代码块之外调用wait()方法，JVM将抛出IllegalMonitorStateException异常。
                 * 当一个线程调用wait()方法时，JVM将这个线程置入休眠，并且释放控制这个同步代码块的对象，同时允许其他线程执行这个
                 * 对象控制的其他同步代码块
                 *
                 * 为了唤醒这个线程，必须在这个对象控制的某个同步代码块中调用notify()或者notifyAll()方法
                 */
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        storage.add(new Date());
        System.out.printf("Set:%d",storage.size());
        notifyAll();
    }

    /**
     * 检查列表是不是有数据，如果没有，就调用wait()方法挂起线程并等待列表中数据的出现。
     * 在这个方法的最后，调用notifyAll()方法唤醒所有因调用wait()方法进入休眠的线程
     */
    public synchronized  void get() {
        while(storage.size() == 0) {
            try {
                /*
                 * 如果在同步代码块之外调用wait()方法，JVM将抛出IllegalMonitorStateException异常。
                 * 当一个线程调用wait()方法时，JVM将这个线程置入休眠，并且释放控制这个同步代码块的对象，同时允许其他线程执行这个
                 * 对象控制的其他同步代码块
                 *
                 * 为了唤醒这个线程，必须在这个对象控制的某个同步代码块中调用notify()或者notifyAll()方法,被唤醒的线程将重新检查条件
                 */
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("Get:%d",storage.size(),((LinkedList<?>)storage).poll());
        notifyAll();
    }
}
