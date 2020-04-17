package com.lotfy.logback.loki.conf;

public class Settings {

    private static final String LokiUrl = "/loki/api/v1/push";
    private String label;

    public static String getLokiUrl() {
        return LokiUrl;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
