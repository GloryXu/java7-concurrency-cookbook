package chapter02.eight;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    // 存放共享数据
    private LinkedList<String> buffer;
    // 存放buffer的长度
    private int maxSize;
    // 用来对修改buffer的代码块进行控制
    private ReentrantLock lock;
    private Condition lines;
    private Condition space;
    // 表明缓冲区是否还有数据
    private boolean pendingLines;

    public Buffer(int maxSize) {
        this.maxSize = maxSize;
        buffer = new LinkedList<>();
        lock = new ReentrantLock();
        lines = lock.newCondition();
        space = lock.newCondition();
        pendingLines = true;
    }

    public void insert(String line) {
        lock.lock();
        try {
            while(buffer.size() == maxSize) {
                // 等待空位出现
                space.await();
            }
            buffer.offer(line);
            System.out.printf("%s: Insert Line:%d\n",Thread.currentThread().getName(), buffer.size());
            // 唤醒所有等待缓冲区有数据的线程
            lines.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public String get() {
        String line = null;
        lock.lock();
        try {
            while((buffer.size() == 0) && (hasPendingLines())) {
                lines.await();
            }
            if(hasPendingLines()) {
                line = buffer.poll();
                System.out.printf("%s:Line Readed:%d\n",Thread.currentThread().getName(),buffer.size());
                space.signalAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return line;
    }

    public void setPendingLines(boolean pendingLines) {
        this.pendingLines = pendingLines;
    }

    public boolean hasPendingLines() {
        return pendingLines || buffer.size()>0;
    }
}
