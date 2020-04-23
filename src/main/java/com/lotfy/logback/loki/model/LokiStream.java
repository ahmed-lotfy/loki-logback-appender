package com.lotfy.logback.loki.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LokiStream implements Serializable {
    private Map<String, String> stream = new HashMap<>();
    private List<String[]> values = new ArrayList<>();

    public Map<String, String> getStream() {
        return stream;
    }

    public void setStream(Map<String, String> stream) {
        this.stream = stream;
    }

    public List<String[]> getValues() {
        return values;
    }

    public void setValues(List<String[]> values) {
        this.values = values;
    }


}
