package com.hansung.vinyl.authority.dto;

import com.hansung.vinyl.authority.domain.HttpMethod;
import com.hansung.vinyl.authority.domain.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResourceResponse {
    private String path;
    private HttpMethod httpMethod;

    public static ResourceResponse of(Resource resource) {
        return new ResourceResponse(resource.getPath(), resource.getHttpMethod());
    }
}
