package com.hansung.vinyl.authority.application;

import com.hansung.vinyl.account.domain.Account;
import com.hansung.vinyl.account.domain.AccountCreatedEvent;
import com.hansung.vinyl.account.domain.AccountRepository;
import com.hansung.vinyl.account.domain.Join;
import com.hansung.vinyl.authority.domain.*;
import com.hansung.vinyl.member.domain.Gender;
import com.hansung.vinyl.member.domain.Member;
import com.hansung.vinyl.member.domain.MemberRepository;
import com.hansung.vinyl.security.infrastructure.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class InitialDataService {
    private final DefaultDataService defaultDataService;
    private final UrlFilterInvocationSecurityMetadataSource metadataSource;

    @PostConstruct
    public void init() {
        log.info("[InitialDataService] initial data start");
        defaultDataService.initSuperAccount();
        defaultDataService.initAuthority();
        metadataSource.reload(new AuthorityCommandedEvent());
        log.info("[InitialDataService] initial data end");
    }

    @RequiredArgsConstructor
    @Component
    static class DefaultDataService {
        @Value("${vinyl.init-data.roles:}")
        private List<String> roles;
        @Value("${vinyl.init-data.super.role}")
        private String superRole;
        @Value("${vinyl.init-data.super.email}")
        private String superEmail;
        @Value("${vinyl.init-data.super.password}")
        private String superPassword;
        @Value("${vinyl.init-data.super.path}")
        private String superPath;

        private final AuthorityRepository authorityRepository;
        private final AccountRepository accountRepository;
        private final MemberRepository memberRepository;
        private final ApplicationEventPublisher publisher;

        @Transactional
        public void initAuthority() {
            roles.stream()
                .filter(roleName -> !authorityRepository.existsByName(roleName))
                .forEach(roleName -> {
                    Authority authority = Authority.builder()
                            .name(roleName)
                            .publisher(publisher)
                            .build();
                    authorityRepository.save(authority);
                });
        }

        @Transactional
        public void initSuperAccount() {
            List<Resource> resources = new ArrayList<>();
            for (HttpMethod httpMethod : HttpMethod.values()) {
                resources.add(new Resource(superPath, httpMethod));
            }

            Authority authority = Authority.builder()
                    .name(superRole)
                    .resources(resources)
                    .publisher(publisher)
                    .build();

            authorityRepository.save(authority);

            if (!accountRepository.existsByEmail(superEmail)) {
                Account account = Account.builder()
                        .email(superEmail)
                        .password(superPassword)
                        .authorities(Arrays.asList(authority))
                        .build();

                Account savedAccount = accountRepository.save(account);

                Member member = Member.builder()
                        .accountId(savedAccount.getId())
                        .email(savedAccount.getEmail())
                        .name("super")
                        .gender(Gender.MALE)
                        .build();

                memberRepository.save(member);
            }
        }
    }
}
