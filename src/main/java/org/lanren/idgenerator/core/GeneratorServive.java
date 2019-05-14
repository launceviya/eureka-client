package org.lanren.idgenerator.core;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @version V1.0
 * @ProjectName:id-generator
 * @Description:
 * @Copyright: Copyright (c) 2019
 * @Company:鲸力智享（北京）科技有限公司
 * @author: Lan Yuan
 * @email: yuan.lan@jingli365.com
 * @date 2019-05-13 16:06
 */
public class GeneratorServive {

    private static final long START_TIMESTAMP = LocalDateTime.of(2000, 01, 01, 00, 00, 00).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    private static final AtomicLong ATOMIC_LONG = new AtomicLong(1L);
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");

    private synchronized static String generatorId() {
        StringBuilder stringBuilder = new StringBuilder();
        long time = System.currentTimeMillis() - START_TIMESTAMP;
        String nowDate = SIMPLE_DATE_FORMAT.format(new Date());
        stringBuilder.append(nowDate);
        stringBuilder.append(time);
        stringBuilder.append(ATOMIC_LONG.getAndIncrement());
        return stringBuilder.toString();
    }


    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1000, 1000, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>(500), new ThreadPoolExecutor.AbortPolicy());
        Set<String> set = new HashSet<>();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1000, () -> System.out.println("都做完了，set中共：" + set.size()));
        for (int i = 0; i < 1000; i++) {
            threadPoolExecutor.submit(() -> {
                try {
                    set.add(generatorId());
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        threadPoolExecutor.shutdown();
    }
}
