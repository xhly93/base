package sharing.school.ctq.util.log;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import sharing.school.ctq.util.log.LogSaveUtils;

public class LogThreadPool {

    private static ExecutorService executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), new ThreadFactory() {
        
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "LogThreadPool #" + mCount.getAndIncrement());
        }
    });

    public static void addTask(LogSaveUtils logSaveUtils) {
        executor.execute(logSaveUtils);
    }


}
