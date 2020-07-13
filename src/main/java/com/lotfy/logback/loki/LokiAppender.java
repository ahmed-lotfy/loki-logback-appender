package com.lotfy.logback.loki;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotfy.logback.loki.model.Label;
import com.lotfy.logback.loki.model.LokiLabels;
import com.lotfy.logback.loki.model.LokiStream;
import com.lotfy.logback.loki.model.LokiStreamWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LokiAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private static final Logger logger = LoggerFactory.getLogger(LokiAppender.class);

    private String lokiUrl;
    private LokiLabels labels;
    private boolean enabled;
    private URL lokiURL;

    private static final int TIMEOUT = 3 * 1000; // 3s
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        if (enabled) {
            LokiStreamWrapper streamWrapper;
            if (iLoggingEvent.getLevel() == Level.ERROR) {
                streamWrapper = buildLogPush(iLoggingEvent.getLevel().toString(), iLoggingEvent.getLoggerName(), iLoggingEvent.getThreadName(), iLoggingEvent.getFormattedMessage(), iLoggingEvent.getThrowableProxy());
            } else {
                streamWrapper = buildLogPush(iLoggingEvent.getLevel().toString(), iLoggingEvent.getLoggerName(), iLoggingEvent.getThreadName(), iLoggingEvent.getFormattedMessage(), null);
            }
            try {
                lokiURL = new URL(lokiUrl + "/loki/api/v1/push");
                HttpURLConnection conn = (HttpURLConnection) lokiURL.openConnection();
                conn.addRequestProperty("Content-Type", "application/json");
                conn.addRequestProperty("Accept-Charset", "utf-8");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setReadTimeout(TIMEOUT);
                conn.setConnectTimeout(TIMEOUT);
                objectMapper.writeValue(conn.getOutputStream(), streamWrapper);
                conn.connect();
                logger.info(String.valueOf(conn.getResponseCode()), conn.getResponseMessage());
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private LokiStreamWrapper buildLogPush(String level, String loggerName, String threadName, String message, IThrowableProxy throwableProxy) {
        LokiStreamWrapper streamWrapper = new LokiStreamWrapper();
        LokiStream stream = new LokiStream();
        for (Label label : labels.getLabels()) {
            stream.getStream().put(label.getKey(), label.getValue());
        }
        stream.getStream().put("level", level);
        stream.getStream().put("logger", loggerName);
        stream.getStream().put("thread", threadName);
        Instant now = Instant.now();
        stream.getValues().add(new String[]{String.valueOf(now.toEpochMilli() * 1000000), formatValue(now.atZone(ZoneId.systemDefault()).format(FORMAT), level, loggerName, threadName, message, throwableProxy)});
        streamWrapper.addStream(stream);
        return streamWrapper;

    }

    private String formatValue(String nanos, String level, String loggerName, String threadName, String message, IThrowableProxy iThrowableProxy) {
        StringBuilder builder = new StringBuilder();
        builder.append(nanos);
        builder.append(" [");
        builder.append(threadName);
        builder.append("] ");
        builder.append(level.toUpperCase());
        builder.append(" ");
        builder.append(loggerName);
        builder.append(" - ");
        builder.append(message);
        if (iThrowableProxy != null) {
            for (int i = 0; i < iThrowableProxy.getStackTraceElementProxyArray().length; i++) {
                builder.append("\n\t\t").append(iThrowableProxy.getStackTraceElementProxyArray()[i].toString());
            }
        }
        return builder.toString();
    }

    public String getLokiUrl() {
        return lokiUrl;
    }

    public void setLokiUrl(String lokiUrl) {
        this.lokiUrl = lokiUrl;
    }

    public LokiLabels getLabels() {
        return labels;
    }

    public void setLabels(LokiLabels labels) {
        this.labels = labels;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
