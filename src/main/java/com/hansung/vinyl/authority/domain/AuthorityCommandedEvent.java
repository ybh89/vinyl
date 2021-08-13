package com.hansung.vinyl.authority.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AuthorityCommandedEvent {
    private Authority authority;
    private Command command;

    @Override
    public String toString() {
        return "AuthorityCommandedEvent{" +
                "authorityId=" + authority.getId() +
                "authorityName=" + authority.getRoleValue() +
                ", command='" + command + '\'' +
                '}';
    }
}
