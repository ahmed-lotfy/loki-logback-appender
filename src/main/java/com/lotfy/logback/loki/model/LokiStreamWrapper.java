package com.lotfy.logback.loki.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LokiStreamWrapper implements Serializable {
    private List<LokiStream> streams = new ArrayList<>();

    public List<LokiStream> getStreams() {
        return streams;
    }

    public void setStreams(List<LokiStream> streams) {
        this.streams = streams;
    }

    public void addStream(LokiStream stream) {
        this.streams.add(stream);
    }

}
