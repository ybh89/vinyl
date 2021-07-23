package com.hansung.vinyl.auth.application;

import com.hansung.vinyl.auth.domain.Authority;
import com.hansung.vinyl.auth.domain.AuthorityRepository;
import com.hansung.vinyl.auth.domain.Path;
import com.hansung.vinyl.auth.dto.AuthorityRequest;
import com.hansung.vinyl.auth.dto.AuthorityResponse;
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

    public AuthorityResponse create(AuthorityRequest authorityRequest) {
        List<Path> paths = authorityRequest.getPaths().stream()
                .map(Path::new)
                .collect(Collectors.toList());

        Authority authority = Authority.builder()
                .name(authorityRequest.getName())
                .desc(authorityRequest.getDesc())
                .paths(paths)
                .build();

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
}
