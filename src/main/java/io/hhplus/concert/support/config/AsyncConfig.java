package io.hhplus.concert.support.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {
    @Value("${spring.async.execution.pool.core-size}")
    int threadPoolCoreSize;

    @Value("${spring.async.execution.pool.max-size}")
    int threadPoolMaxSize;

    @Value("${spring.async.execution.pool.queue-capacity}")
    int threadPoolQueueCapacity;

    @Override
    public AsyncTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("async-exec-");
        executor.setCorePoolSize(threadPoolCoreSize);
        executor.setMaxPoolSize(threadPoolMaxSize);
        executor.setQueueCapacity(threadPoolQueueCapacity);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler(){
            private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                log.error("""
                    [ASYNC] Method: {}
                    [ASYNC] Exception Message: {}
                """, method.getName(), ex.getMessage());
            }
        };
    }
}
