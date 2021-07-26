package com.hansung.vinyl.authority.dto;

import com.hansung.vinyl.authority.domain.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResourceRequest {
    private String path;
    private HttpMethod httpMethod;
}
