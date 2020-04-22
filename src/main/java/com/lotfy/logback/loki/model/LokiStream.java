package com.lotfy.logback.loki.model;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.List;

public class LokiStream implements Serializable {
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

    public static class Stream implements Serializable {
        private String app;

        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("app", app)
                    .toString();
        }
    }
}
