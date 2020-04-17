package com.lotfy.logback.loki;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LokiAppenderTest {

    private static final Logger logger = LoggerFactory.getLogger(LokiAppenderTest.class);

    @Test
    public void testLoggerCanCatchAllLevels() {
        logger.info("some info");
        logger.error("an error occurred", new RuntimeException("error occurred"));
        logger.debug("some debug message");
    }

    @Test
    public void testFormatMessage() {
       String msg =  new LokiAppender().formatMsg("connection timed out: /144.217.187.81:9200 at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe$1.run(AbstractNioChannel.java:261) at io.netty.util.concurrent.PromiseTask$RunnableAdapter.call(PromiseTask.java:38) at io.netty.util.concurrent.ScheduledFutureTask.run(ScheduledFutureTask.java:139) at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:163) at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:510) at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:518) at io.netty.util.concurrent.SingleThreadEventExecutor$6.run(SingleThreadEventExecutor.java:1044) at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30) at java.lang.Thread.run(Thread.java:748)");
       String x = "";
    }
}
