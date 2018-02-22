package chapter03.six;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class FileSearch implements Runnable {
    private String initPath;
    private String end;
    private List<String> results;
    private Phaser phaser;

    public FileSearch(String initPath, String end, Phaser phaser) {
        this.initPath = initPath;
        this.end = end;
        this.phaser = phaser;
        results = new ArrayList<>();
    }

    private void directoryProcess(File file) {
        File[] list = file.listFiles();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                if (list[i].isDirectory()) {
                    directoryProcess(list[i]);
                } else {
                    fileProcess(list[i]);
                }
            }
        }
    }

    private void fileProcess(File file) {
        if (file.getName().endsWith(end)) {
            results.add(file.getAbsolutePath());
        }
    }

    private void filterResults() {
        List<String> newResults = new ArrayList<>();
        long actualDate = new Date().getTime();
        for (int i = 0; i < results.size(); i++) {
            File file = new File(results.get(i));
            long fileDate = file.lastModified();
            if (actualDate - fileDate < TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)) {
                newResults.add(results.get(i));
            }
        }
        results = newResults;
    }

    private boolean checkResults() {
        /*
         * 调用Phaser对象的arriveAndDeregister()方法，来通知Phaser对象当前线程已经结束这个阶段，
         * 并且将不再参与接下来的阶段操作
         */
        if(results.isEmpty()) {
            System.out.printf("%s: Phase %d:0 results.\n",Thread.currentThread().getName(), phaser.getPhase());
            System.out.printf("%s: Phase %d: End.\n",Thread.currentThread().getName(), phaser.getPhase());
            /*
             * 通知phaser对象参与同步的线程少了一个
             */
            phaser.arriveAndDeregister();
            return false;
        /*
         * 调用Phaser对象的arriveAndAwaitAdvance()方法，来通知Phaser对象当前线程已经完成了当前阶段，
         * 需要被阻塞知道其他线程也都完成当前阶段
         */
        } else {
            System.out.printf("%s: Phase %d: %d results.\n", Thread.currentThread().getName(), phaser.getPhase(), results.size());
            phaser.arriveAndAwaitAdvance();
            return true;
        }
    }

    /**
     * 将集元素打印到控制台
     */
    private void showInfo() {
        for(int i = 0;i<results.size();i++) {
            File file = new File(results.get(i));
            System.out.printf("%s: %s\n",Thread.currentThread().getName(), file.getAbsoluteFile());
        }
        phaser.arriveAndAwaitAdvance();
    }

    @Override
    public void run() {
        phaser.arriveAndAwaitAdvance();
        System.out.printf("%s: Starting.\n", Thread.currentThread().getName());
        File file = new File(initPath);
        if(file.isDirectory()) {
            directoryProcess(file);
        }
        if(!checkResults()) {
            return;
        }
        filterResults();
        if(!checkResults()) {
            return;
        }
        showInfo();
        phaser.arriveAndAwaitAdvance();
        System.out.printf("%s: Work completed.\n", Thread.currentThread().getName());
    }
}
