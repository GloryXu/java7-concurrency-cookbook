package chapter04.six;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TimeOutTest {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<TimeOutTask> taskList = new ArrayList<>();
        for(int i = 0;i<3;i++) {
            TimeOutTask task = new TimeOutTask(Integer.toString(i));
            taskList.add(task);
        }
        List<Future<Result>> resultList = null;
            // 等待所有任务的完成（阻塞）
        try {
            resultList = executor.invokeAll(taskList, 5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();

        System.out.println("Main: Printing the results");
        for(int i = 0;i<resultList.size();i++) {
            Future<Result> future = resultList.get(i);
            try{
                Result result = future.get();
                System.out.println("------------------------" + future.isDone());
                System.out.println(result.getName() + ": " + result.getValue());
            } catch (Exception e) {

            }
        }
    }
}
