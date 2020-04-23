package com.lotfy.logback.loki.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LokiLabels implements Serializable {
    private List<Label> labels;

    public LokiLabels() {
        this.labels = new ArrayList<>();
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void addLabel(Label label) {
        this.labels.add(label);
    }
}
