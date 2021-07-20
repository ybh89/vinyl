package com.hansung.vinyl.auth.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
public class Account {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String email;
    private String password;

    protected Account() {
    }

    @Builder
    public Account(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
