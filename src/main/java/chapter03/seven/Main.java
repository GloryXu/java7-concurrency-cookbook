package chapter03.seven;

public class Main {
    public static void main(String[] args) {
        MyPhaser phaser = new MyPhaser();
        Student[] students = new Student[5];
        for(int i = 0;i<students.length;i++) {
            students[i] = new Student(phaser);
            // N个线程干事儿就resister N次
            phaser.register();
        }
        Thread[] threads = new Thread[students.length];
        for(int i = 0;i<students.length;i++) {
            threads[i] = new Thread(students[i], "Student " + i);
            threads[i].start();
        }

        for(int i = 0;i<threads.length;i++) {
            try {
                // 等待所有线程都运行结束
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("Main: The phaser has finished:%s.\n", phaser.isTerminated());
    }
}
