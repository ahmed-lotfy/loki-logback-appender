package com.lotfy.logback.loki.model;

import java.util.List;

public class LokiStream {
    private Stream stream;
    private List<String[]> values;

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }

    public List<String[]> getValues() {
        return values;
    }

    public void setValues(List<String[]> values) {
        this.values = values;
    }

    public static class Stream {
        private String app;

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }
    }
}
