package com.jiocoders.java.jiofamily.security;

public enum AuthScope {

    SUPER_ADMIN("97531"),
    ADMIN("13579"),
    USER("24680");

    final String value;

    AuthScope(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }

}
