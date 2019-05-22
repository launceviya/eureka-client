package org.lanren.idgenerator.core;

import org.apache.commons.lang.StringUtils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class test {
    private static String str = null;
    private Object queue = new Object();

    public static void main(String[] args) {
        test test = new test();
        Producer producer = test.new Producer();
        Consumer consumer = test.new Consumer();

        producer.run();
        consumer.start();
    }

    class Consumer extends Thread {
        @Override
        public void run() {
            consume();
        }
        private void consume() {
            while (StringUtils.isBlank(str)) {
                synchronized (queue) {
                    while (StringUtils.isBlank(str)) {
                        try {
                            System.out.println("数据为空，等待数据");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("str=="+str);
                }
            }
        }
    }

    class Producer {
        public void run() {
            int num = 5;
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(num, num, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(10));
            for (int i = 0; i < num; i++) {
                int finalI = i;
                threadPoolExecutor.submit(() -> {
                    randomNum(finalI);
                });
            }
            threadPoolExecutor.shutdown();
        }
    }

    public void randomNum(int num) {
        int count = 0;
        System.out.println(">>>" + num + "任务启动");
        while (StringUtils.isBlank(str)) {
            synchronized (queue) {
                if(StringUtils.isBlank(str)){
                    int x = (int) (Math.random() * 100000);
                    if (x == 5655) {
                        randomNum(num + "线程执行完毕，执行" + count+"次结果为" + x);
                        queue.notify();
                    }
                    count++;
                }
            }
        }
        System.out.println("线程" + num +"执行次数："+count);
    }

    private void randomNum(String finalI) {
        str = finalI;
    }
}
