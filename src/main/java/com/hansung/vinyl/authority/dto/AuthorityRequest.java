package com.hansung.vinyl.authority.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorityRequest {
    private String name;
    private String remark;
    private List<ResourceRequest> resources;
}
