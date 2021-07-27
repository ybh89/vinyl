package com.hansung.vinyl.authority.application;

import com.hansung.vinyl.authority.domain.*;
import com.hansung.vinyl.authority.dto.AuthorityRequest;
import com.hansung.vinyl.authority.dto.AuthorityResponse;
import com.hansung.vinyl.common.exception.DuplicateDataException;
import com.hansung.vinyl.common.exception.NoSuchDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthorityService {
    private final AuthorityRepository authorityRepository;
    private final AccountAuthorityRepository accountAuthorityRepository;
    private final AuthorityResourceRepository authorityResourceRepository;
    private final ApplicationEventPublisher publisher;

    public AuthorityResponse create(AuthorityRequest authorityRequest) {
        Authority authority = createAuthority(authorityRequest);
        Authority savedAuthority = authorityRepository.save(authority);
        return AuthorityResponse.of(savedAuthority);
    }

    @Transactional(readOnly = true)
    public List<AuthorityResponse> list() {
        List<Authority> authorities = authorityRepository.findAll();
        return authorities.stream()
                .map(AuthorityResponse::of)
                .collect(Collectors.toList());
    }

    public AuthorityResponse update(Long authorityId, AuthorityRequest authorityRequest) {
        validateSameName(authorityId, authorityRequest);

        Authority authority = findAuthorityById(authorityId);
        Authority updateAuthority = createAuthority(authorityRequest);
        authority.clearAuthorityResources();
        authorityResourceRepository.flush();

        authority.update(updateAuthority, publisher);
        return AuthorityResponse.of(authority);
    }

    private List<Resource> createResources(AuthorityRequest authorityRequest) {
        return authorityRequest.getResources().stream()
                .map(resourceRequest -> new Resource(resourceRequest.getPath(), resourceRequest.getHttpMethod()))
                .collect(Collectors.toList());
    }

    private void validateSameName(Long authorityId, AuthorityRequest authorityRequest) {
        if (authorityRepository.existsByIdNotAndNameEquals(authorityId, authorityRequest.getName())) {
            throw new DuplicateDataException("name", authorityRequest.getName(), getClass().getName());
        }
    }

    public void delete(Long authorityId) {
        if (accountAuthorityRepository.existsByAuthorityId(authorityId)) {
            throw new IllegalArgumentException("권한에 매핑된 계정이 존재합니다.");
        }
        Authority authority = findAuthorityById(authorityId);
        authority.publishEvent(publisher, new AuthorityCommandedEvent(authority, "delete"));
        authorityRepository.delete(authority);
    }

    private Authority findAuthorityById(Long authorityId) {
        return authorityRepository.findById(authorityId)
                .orElseThrow(() -> new NoSuchDataException("authority_id", String.valueOf(authorityId),
                        getClass().getName()));
    }

    private Authority createAuthority(AuthorityRequest authorityRequest) {
        Authority authority = Authority.builder()
                .name(authorityRequest.getName())
                .remark(authorityRequest.getRemark())
                .resources(createResources(authorityRequest))
                .publisher(publisher)
                .build();
        return authority;
    }

    @Transactional(readOnly = true)
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> findAuthorityPathMap() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<AuthorityResource> authorityResources = authorityResourceRepository.findAll();
        Map<Resource, List<AuthorityResource>> pathAuthorityMap = authorityResources.stream()
                .collect(Collectors.groupingBy(AuthorityResource::getResource));

        for (Resource resource : pathAuthorityMap.keySet()) {
            List<ConfigAttribute> configAttributes = new ArrayList<>();
            List<AuthorityResource> authorities = pathAuthorityMap.get(resource);
            authorities.sort(Comparator.comparingInt(AuthorityResource::getSeq));// seq 오름차순 정렬
            authorities.forEach(authorityResource ->
                    configAttributes.add(new SecurityConfig(authorityResource.getAuthority().getName())));
            result.put(new AntPathRequestMatcher(resource.getPath(), resource.getHttpMethod().name()), configAttributes);
        }
        return result;
    }
}
