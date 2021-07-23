package com.hansung.vinyl.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorityRequest {
    private String name;
    private String desc;
    private List<String> paths = new ArrayList<>();
}