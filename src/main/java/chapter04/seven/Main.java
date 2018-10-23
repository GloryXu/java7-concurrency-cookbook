package chapter04.seven;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

        System.out.printf("Main:Starting at: %s\n", new Date());
        for (int i = 0;i<5;i++) {
            Task task = new Task("Task " + i);
            executor.schedule(task, i + 1, TimeUnit.SECONDS);
        }

        // 该参数被设置，在shutdown时，待处理的任务将不会被执行
//        executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        executor.shutdown();

        try {
            // 等待所有任务结束
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Main: Ends at: %s\n", new Date());
    }

}
