package com.hansung.vinyl.authority.dto;

import com.hansung.vinyl.authority.domain.HttpMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AuthorityGroupByResourceDto {
    private String path;
    private HttpMethod httpMethod;
    private String role;

    public AuthorityGroupByResourceDto(String path, HttpMethod httpMethod, String role) {
        this.path = path;
        this.httpMethod = httpMethod;
        this.role = role;
    }
}
