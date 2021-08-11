package com.hansung.vinyl.authority.dto;

import com.hansung.vinyl.authority.domain.HttpMethod;
import com.hansung.vinyl.authority.domain.Path;
import com.hansung.vinyl.authority.domain.Resource;
import com.hansung.vinyl.authority.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResourceRoleDto {
    private Resource resource;
    private Role role;
}
