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
    public void testRussianChar() {
        logger.info("Статья 1 Все люди рождаются свободными и равными в своем достоинстве и правах. Они наделены разумом и совестью и должны поступать в отношении друг друга в духе братства");
    }
}
