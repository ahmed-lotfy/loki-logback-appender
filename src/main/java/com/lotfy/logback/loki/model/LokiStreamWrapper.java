package com.lotfy.logback.loki.model;

import java.util.ArrayList;
import java.util.List;

public class LokiStreamWrapper {
    private List<LokiStream> streams = new ArrayList<>();

    public List<LokiStream> getStreams() {
        return streams;
    }

    public void setStreams(List<LokiStream> streams) {
        this.streams = streams;
    }
}
