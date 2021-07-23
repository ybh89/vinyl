package com.hansung.vinyl.auth.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Path {
    @Column
    private String path;

    public Path(String path) {
        this.path = path;
    }

    public String value() {
        return path;
    }
}
