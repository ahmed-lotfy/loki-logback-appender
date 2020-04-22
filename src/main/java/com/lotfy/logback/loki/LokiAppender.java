package com.lotfy.logback.loki;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotfy.logback.loki.conf.Settings;
import com.lotfy.logback.loki.model.LokiStream;
import com.lotfy.logback.loki.model.LokiStreamWrapper;
import com.lotfy.logback.loki.util.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LokiAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private static final Logger logger = LoggerFactory.getLogger(LokiAppender.class);

    private String lokiUrl;
    private String label;
    private boolean enabled;


    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        if (enabled) {
            LokiStreamWrapper streamWrapper;
            if (iLoggingEvent.getLevel() == Level.ERROR) {
                streamWrapper = buildLogPush(iLoggingEvent.getLevel().toString(), iLoggingEvent.getLoggerName(), iLoggingEvent.getThreadName(), iLoggingEvent.getMessage(), iLoggingEvent.getThrowableProxy());
            } else {
                streamWrapper = buildLogPush(iLoggingEvent.getLevel().toString(), iLoggingEvent.getLoggerName(), iLoggingEvent.getThreadName(), iLoggingEvent.getMessage(), null);
            }

            StringBuilder url = new StringBuilder();
            url.append(lokiUrl).append(Settings.getLokiUrl());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add(HttpHeaders.ACCEPT_CHARSET, "utf-8");
            try {
                RestUtils.post(url.toString(), headers, new ObjectMapper().writeValueAsString(streamWrapper));
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private LokiStreamWrapper buildLogPush(String level, String loggerName, String threadName, String message, IThrowableProxy throwableProxy) {
        LokiStreamWrapper streamWrapper = new LokiStreamWrapper();
        List<LokiStream> streams = new ArrayList<>();
        LokiStream stream = new LokiStream();
        LokiStream.Stream stream1 = new LokiStream.Stream();
        stream1.setApp(getLabel());
        stream.setStream(stream1);
        String[] v1;
        if (!level.equalsIgnoreCase("error")) {
            v1 = new String[]{String.valueOf(System.currentTimeMillis() * 1000000),
                    formatFirstLine(level.toUpperCase(), loggerName, threadName, message)};
        } else {
            v1 = new String[]{String.valueOf(System.currentTimeMillis() * 1000000),
                    formatFirstLine(level.toUpperCase(), loggerName, threadName, message) + formatStackTrace(throwableProxy)};
        }
        List<String[]> values = new ArrayList<>();
        values.add(v1);
        stream.setValues(values);
        streams.add(stream);
        streamWrapper.setStreams(streams);
        return streamWrapper;

    }

    private String formatFirstLine(String level, String loggerName, String threadName, String message) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String time = format.format(new Date(System.currentTimeMillis()));

        return time +
                " " +
                level.toUpperCase() +
                " " +
                loggerName +
                " " +
                "[" + threadName + "]" +
                " " +
                message;
    }

    private String formatStackTrace(IThrowableProxy iThrowableProxy) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < iThrowableProxy.getStackTraceElementProxyArray().length; i++) {
            builder.append("\n\t\t").append(iThrowableProxy.getStackTraceElementProxyArray()[i].toString());
        }
        return builder.toString();
    }

    public String getLokiUrl() {
        return lokiUrl;
    }

    public void setLokiUrl(String lokiUrl) {
        this.lokiUrl = lokiUrl;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
