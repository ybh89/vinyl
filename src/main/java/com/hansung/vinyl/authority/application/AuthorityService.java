package com.hansung.vinyl.authority.application;

import com.hansung.vinyl.authority.domain.*;
import com.hansung.vinyl.authority.dto.AuthorityRequest;
import com.hansung.vinyl.authority.dto.AuthorityResponse;
import com.hansung.vinyl.common.exception.NoSuchDataException;
import com.hansung.vinyl.security.infrastructure.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import lombok.RequiredArgsConstructor;
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
    private final AuthorityPathRepository authorityPathRepository;
    private final UrlFilterInvocationSecurityMetadataSource metadataSource;

    public AuthorityResponse create(AuthorityRequest authorityRequest) {
        Authority authority = createAuthority(authorityRequest);
        Authority savedAuthority = authorityRepository.save(authority);
        metadataSource.reload();
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
        Authority authority = findAuthorityById(authorityId);
        Authority updateAuthority = createAuthority(authorityRequest);
        authority.update(updateAuthority);
        metadataSource.reload();
        return AuthorityResponse.of(authority);
    }

    public void delete(Long authorityId) {
        if (accountAuthorityRepository.existsByAuthorityId(authorityId)) {
            throw new IllegalArgumentException("권한에 매핑된 계정이 존재합니다.");
        }
        authorityRepository.deleteById(authorityId);
        metadataSource.reload();
    }

    private Authority findAuthorityById(Long authorityId) {
        return authorityRepository.findById(authorityId)
                .orElseThrow(() -> new NoSuchDataException("authority_id", String.valueOf(authorityId),
                        getClass().getName()));
    }

    private Authority createAuthority(AuthorityRequest authorityRequest) {
        List<Path> paths = authorityRequest.getPaths().stream()
                .map(Path::new)
                .collect(Collectors.toList());

        Authority authority = Authority.builder()
                .name(authorityRequest.getName())
                .remark(authorityRequest.getRemark())
                .paths(paths)
                .build();
        return authority;
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> findAuthorityPathMap() {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<AuthorityPath> authorityPaths = authorityPathRepository.findAll();
        Map<Path, List<AuthorityPath>> pathAuthorityMap = authorityPaths.stream()
                .collect(Collectors.groupingBy(AuthorityPath::getPath));

        for (Path path : pathAuthorityMap.keySet()) {
            List<ConfigAttribute> configAttributes = new ArrayList<>();
            List<AuthorityPath> authorities = pathAuthorityMap.get(path);
            authorities.sort(Comparator.comparingInt(AuthorityPath::getSeq));// seq 오름차순 정렬
            authorities.forEach(authorityPath ->
                    configAttributes.add(new SecurityConfig(authorityPath.getAuthority().getName())));
            result.put(new AntPathRequestMatcher(path.value()), configAttributes);
        }
        return result;
    }
}
