package com.hansung.vinyl.authority.domain;

public enum HttpMethod {
    POST, GET, DELETE, PUT, PATCH;

    public boolean equalsIgnoreCase(String method) {
        return this.toString().equalsIgnoreCase(method);
    }
}
