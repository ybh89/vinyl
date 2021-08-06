package com.hansung.vinyl.authority.dto;

import com.hansung.vinyl.authority.domain.Authority;
import com.hansung.vinyl.authority.domain.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorityResponse {
    private Long id;
    private String name;
    private String remark;
    private List<ResourceResponse> resources;

    public static AuthorityResponse of(Authority authority) {
        List<ResourceResponse> resourceResponses = authority.getResources().stream()
                .map(ResourceResponse::of)
                .collect(Collectors.toList());
        return new AuthorityResponse(authority.getId(), authority.getName(), authority.getRemark(), resourceResponses);
    }
}
