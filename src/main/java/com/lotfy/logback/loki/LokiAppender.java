package com.lotfy.logback.loki;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotfy.logback.loki.conf.Settings;
import com.lotfy.logback.loki.model.LokiStream;
import com.lotfy.logback.loki.model.LokiStreamWrapper;
import com.lotfy.logback.loki.util.RestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LokiAppender extends AppenderBase<ILoggingEvent> {

    private String lokiUrl;
    private String label;
    private Map<String, ILoggingEvent> map = new ConcurrentHashMap();


    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        LokiStreamWrapper streamWrapper = null;
        if (iLoggingEvent.getLevel() == Level.ERROR) {
            streamWrapper = buildLogPush(iLoggingEvent.getLevel().toString(), iLoggingEvent.getLoggerName(), iLoggingEvent.getThreadName(), iLoggingEvent.getMessage(), iLoggingEvent.getThrowableProxy());
        } else {
            streamWrapper = buildLogPush(iLoggingEvent.getLevel().toString(), iLoggingEvent.getLoggerName(), iLoggingEvent.getThreadName(), iLoggingEvent.getMessage(), null);
        }

        StringBuilder url = new StringBuilder();
        url.append(lokiUrl).append(Settings.getLokiUrl());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        try {
            ResponseEntity response = RestUtils.post(url.toString(), headers, new ObjectMapper().writeValueAsString(streamWrapper));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

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

    private LokiStreamWrapper buildLogPush(String level, String loggerName, String threadName, String message, IThrowableProxy throwableProxy) {
        LokiStreamWrapper streamWrapper = new LokiStreamWrapper();
        List<LokiStream> streams = new ArrayList<>();
        LokiStream stream = new LokiStream();
        LokiStream.Stream stream1 = new LokiStream.Stream();
        stream1.setApp(getLabel());
        List<Map<String, String>> map = new ArrayList<>();

        stream.setStream(stream1);
        String[] v1 = null;
        if (!level.equalsIgnoreCase("error")) {
            v1 = new String[]{String.valueOf(System.currentTimeMillis() * 1000000),
                    formatFirstLine(level.toUpperCase(), loggerName, threadName, formatMsg(message))};
        } else {
            v1 = new String[]{String.valueOf(System.currentTimeMillis() * 1000000),
                    formatFirstLine(level.toUpperCase(), loggerName, threadName, formatMsg(message)) + formatStackTrace(throwableProxy)};
        }

        List<String[]> values = new ArrayList<>();
        values.add(v1);

        stream.setValues(values);

        streams.add(stream);

        streamWrapper.setStreams(streams);
        return streamWrapper;

    }

    public String formatMsg(String msg) {
        String[] stack = msg.split(" ");
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < stack.length; i++) {
            if (stack[i].equals("at")) {
                ret.append("\n\t\t").append(stack[i]);
            } else {
                ret.append(" ").append(stack[i]);
            }
        }
        return ret.toString();
    }

    private String formatFirstLine(String level, String loggerName, String threadName, String message) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String time = format.format(new Date(System.currentTimeMillis()));

        return new StringBuilder().append(time)
                .append("\t")
                .append(level.toUpperCase())
                .append("\t")
                .append(loggerName)
                .append("\t")
                .append("[" + threadName + "]")
                .append("\t")
                .append(message).toString();
    }

    private String formatStackTrace(IThrowableProxy iThrowableProxy) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < iThrowableProxy.getStackTraceElementProxyArray().length; i++) {
            builder.append("\n\t\t").append(iThrowableProxy.getStackTraceElementProxyArray()[i].toString());
        }
        return builder.toString();
    }
}
