package com.hansung.vinyl.authority.application;

import com.hansung.vinyl.account.domain.*;
import com.hansung.vinyl.authority.domain.*;
import com.hansung.vinyl.common.exception.data.NoSuchDataException;
import com.hansung.vinyl.member.domain.Gender;
import com.hansung.vinyl.member.domain.Member;
import com.hansung.vinyl.member.domain.MemberRepository;
import com.hansung.vinyl.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger appLogger = LoggerFactory.getLogger("app");

    private final DefaultDataService defaultDataService;
    private final UrlFilterInvocationSecurityMetadataSource metadataSource;

    @PostConstruct
    public void init() {
        appLogger.info("[InitialDataService] initial data start");
        defaultDataService.initAuthority();
        defaultDataService.initSuperAccount();
        metadataSource.reload(new AuthorityCommandedEvent());
        appLogger.info("[InitialDataService] initial data end");
    }

    @RequiredArgsConstructor
    @Component
    static class DefaultDataService {
        @Value("${vinyl.init-data.super.email}")
        private String superEmail;
        @Value("${vinyl.init-data.super.password}")
        private String superPassword;
        @Value("${vinyl.init-data.super.path}")
        private String superPath;

        private final AuthorityRepository authorityRepository;
        private final AccountRepository accountRepository;
        private final MemberRepository memberRepository;

        @Transactional
        public void initAuthority() {
            Arrays.stream(DefaultRole.values())
                    .filter(role -> !authorityRepository.existsByRole(Role.of(role.name())))
                    .forEach(role -> {
                        Authority authority = Authority.create(role.name(), "", null);
                        authorityRepository.save(authority);
                    });
        }

        @Transactional
        public void initSuperAccount() {
            List<Resource> resources = new ArrayList<>();
            for (HttpMethod httpMethod : HttpMethod.values()) {
                resources.add(new Resource(superPath, httpMethod));
            }

            Authority superAuthority = authorityRepository.findByRole(Role.of(DefaultRole.ROLE_SUPER.name()))
                    .orElseThrow(() -> new NoSuchDataException("role", DefaultRole.ROLE_SUPER, getClass().getName()));

            Authority updateAuthority = Authority.create(DefaultRole.ROLE_SUPER.name(), "", resources);
            superAuthority.update(updateAuthority);

            if (!accountRepository.existsByEmail(Email.of(superEmail))) {
                AccountInfo accountInfo = AccountInfo.builder()
                        .email(superEmail)
                        .encryptedPassword(superPassword)
                        .authorities(Arrays.asList(superAuthority))
                        .build();
                MemberInfo memberInfo = MemberInfo.builder()
                        .name("super")
                        .fcmToken("fcm-token")
                        .gender(Gender.MALE)
                        .build();
                Account account = Account.create(accountInfo, memberInfo);
                Account savedAccount = accountRepository.save(account);

                Member member = Member.create(savedAccount.getId(), savedAccount.getEmailValue(), "super",
                        null, Gender.MALE);
                memberRepository.save(member);
            }
        }
    }
}
