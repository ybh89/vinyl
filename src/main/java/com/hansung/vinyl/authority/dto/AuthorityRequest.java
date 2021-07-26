package com.hansung.vinyl.authority.dto;

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
    private String remark;
    private List<ResourceRequest> resources = new ArrayList<>();
}
