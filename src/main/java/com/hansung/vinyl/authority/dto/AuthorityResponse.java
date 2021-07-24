package com.hansung.vinyl.authority.dto;

import com.hansung.vinyl.authority.domain.Authority;
import com.hansung.vinyl.authority.domain.Path;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorityResponse {
    private Long id;
    private String name;
    private String desc;
    private List<String> paths;

    public static AuthorityResponse of(Authority authority) {
        List<String> paths = authority.getPaths().stream()
                .map(Path::value)
                .collect(Collectors.toList());
        return new AuthorityResponse(authority.getId(), authority.getName(), authority.getDesc(), paths);
    }
}
