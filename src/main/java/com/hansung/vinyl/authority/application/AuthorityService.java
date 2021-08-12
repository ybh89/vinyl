package com.hansung.vinyl.authority.application;

import com.hansung.vinyl.authority.domain.*;
import com.hansung.vinyl.authority.dto.AuthorityRequest;
import com.hansung.vinyl.authority.dto.AuthorityResponse;
import com.hansung.vinyl.authority.dto.ResourceRoleDto;
import com.hansung.vinyl.common.exception.data.DuplicateDataException;
import com.hansung.vinyl.common.exception.data.NoSuchDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthorityService {
    private final AuthorityRepository authorityRepository;
    private final ApplicationEventPublisher publisher;

    public AuthorityResponse create(AuthorityRequest authorityRequest) {
        Authority authority = buildAuthority(authorityRequest);
        Authority savedAuthority = authorityRepository.save(authority);
        return AuthorityResponse.of(savedAuthority);
    }

    @Transactional(readOnly = true)
    public List<AuthorityResponse> list() {
        List<Authority> authorities = authorityRepository.findAllDistinct();
        return authorities.stream()
                .map(AuthorityResponse::of)
                .collect(Collectors.toList());
    }

    public AuthorityResponse update(Long authorityId, AuthorityRequest authorityRequest) {
        validateUpdatable(authorityId, authorityRequest);
        Authority authority = findAuthorityById(authorityId);
        Authority updateAuthority = buildAuthority(authorityRequest);
        authority.update(updateAuthority, publisher);
        return AuthorityResponse.of(authority);
    }

    private void validateUpdatable(Long authorityId, AuthorityRequest authorityRequest) {
        if (authorityRepository.existsByIdNotAndRoleEquals(authorityId, Role.of(authorityRequest.getName()))) {
            throw new DuplicateDataException("name", authorityRequest.getName(), getClass().getName());
        }
    }

    public void delete(Long authorityId) {
        Authority authority = findAuthorityById(authorityId);
        authority.publishEvent(publisher, new AuthorityCommandedEvent(authority, "delete"));
        authorityRepository.delete(authority);
    }

    private Authority findAuthorityById(Long authorityId) {
        return authorityRepository.findDistinctById(authorityId)
                .orElseThrow(() -> new NoSuchDataException("authorityId", String.valueOf(authorityId),
                        getClass().getName()));
    }

    private Authority buildAuthority(AuthorityRequest authorityRequest) {
        Authority authority = Authority.builder()
                .role(authorityRequest.getName())
                .remark(authorityRequest.getRemark())
                .resources(createResources(authorityRequest))
                .publisher(publisher)
                .build();
        return authority;
    }

    private List<Resource> createResources(AuthorityRequest authorityRequest) {
        return authorityRequest.getResources().stream()
                .map(resourceRequest -> new Resource(resourceRequest.getPath(), resourceRequest.getHttpMethod()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> findAuthorityPathMap() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Authority> authorities = authorityRepository.findAll();
        List<ResourceRoleDto> resourceRoleDtoList = new ArrayList<>();

        flatten(authorities, resourceRoleDtoList);
        Map<Resource, List<ResourceRoleDto>> resourceRoleMap = groupByResource(resourceRoleDtoList);

        for (Resource resource : resourceRoleMap.keySet()) {
            List<ConfigAttribute> configAttributes = new ArrayList<>();
            addConfigAttribute(resourceRoleMap, resource, configAttributes);
            result.put(new AntPathRequestMatcher(resource.getPathValue(), resource.getHttpMethod().name()), configAttributes);
        }
        return result;
    }

    private void addConfigAttribute(Map<Resource, List<ResourceRoleDto>> resourceRoleMap, Resource resource, List<ConfigAttribute> configAttributes) {
        List<ResourceRoleDto> resourceRoleDtos = resourceRoleMap.get(resource);
        resourceRoleDtos.forEach(resourceRoleDto ->
                configAttributes.add(new SecurityConfig(resourceRoleDto.getRole().value())));
    }

    private Map<Resource, List<ResourceRoleDto>> groupByResource(List<ResourceRoleDto> resourceRoleDtoList) {
        return resourceRoleDtoList.stream()
                .collect(Collectors.groupingBy(ResourceRoleDto::getResource));
    }

    private void flatten(List<Authority> authorities, List<ResourceRoleDto> resourceRoleDtoList) {
        authorities.forEach(authority -> {
            List<Resource> resources = authority.getAuthorityResources().getResources();
            resources.forEach(resource ->
                resourceRoleDtoList.add(new ResourceRoleDto(resource, authority.getRole())));
        });
    }
}
