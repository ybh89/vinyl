package com.hansung.vinyl.auth.application;

import com.hansung.vinyl.auth.domain.*;
import com.hansung.vinyl.auth.dto.AuthorityRequest;
import com.hansung.vinyl.auth.dto.AuthorityResponse;
import com.hansung.vinyl.common.exception.NoSuchDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthorityService {
    private final AuthorityRepository authorityRepository;
    private final AccountAuthorityRepository accountAuthorityRepository;

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
        Authority authority = findAuthorityById(authorityId);
        Authority updateAuthority = createAuthority(authorityRequest);
        authority.update(updateAuthority);
        return AuthorityResponse.of(authority);
    }

    public void delete(Long authorityId) {
        if (accountAuthorityRepository.existsByAuthorityId(authorityId)) {
            throw new IllegalArgumentException("권한에 매핑된 계정이 존재합니다.");
        }
        authorityRepository.deleteById(authorityId);
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
                .desc(authorityRequest.getDesc())
                .paths(paths)
                .build();
        return authority;
    }
}
