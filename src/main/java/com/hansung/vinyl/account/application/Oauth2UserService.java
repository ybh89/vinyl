package com.hansung.vinyl.account.application;

import com.hansung.vinyl.account.domain.*;
import com.hansung.vinyl.account.dto.OauthUserJoinRequest;
import com.hansung.vinyl.authority.domain.Authority;
import com.hansung.vinyl.authority.domain.AuthorityRepository;
import com.hansung.vinyl.authority.domain.DefaultRole;
import com.hansung.vinyl.authority.domain.Role;
import com.hansung.vinyl.common.exception.data.NoSuchDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class Oauth2UserService extends DefaultOAuth2UserService {
    private final PasswordService passwordService;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Optional<Account> optionalAccount = accountRepository.findByEmail(Email.of(oAuth2User
                .getAttribute("email")));

        // 최초 로그인시 회원가입 진행
        if (optionalAccount.isEmpty()) {
            OauthUserJoinRequest oauthUserJoinRequest = buildOauthUserJoinRequest(userRequest, oAuth2User);
            List<Authority> authorities = Arrays.asList(findUserAuthority());
            Account account = joinOauthUser(oauthUserJoinRequest, authorities);
            return new User(account, authorities, oAuth2User.getAttributes());
        }

        Account account = optionalAccount.get();
        List<Authority> authorities = authorityRepository.findAllDistinctByIdIn(account.getAuthorityIds());
        return new User(account, authorities, oAuth2User.getAttributes());
    }

    private OauthUserJoinRequest buildOauthUserJoinRequest(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        return OauthUserJoinRequest.builder()
                .email(oAuth2User.getAttribute("email"))
                .password(passwordService.createRandomPassword())
                .name(oAuth2User.getName())
                .provider(userRequest.getClientRegistration().getClientId())
                .providerId(oAuth2User.getAttribute("sub"))
                .build();
    }

    public Account joinOauthUser(OauthUserJoinRequest oauthUserJoinRequest, List<Authority> authorities) {
        AccountInfo accountInfo = buildAccountInfo(oauthUserJoinRequest, authorities);
        MemberInfo memberInfo = buildMemberInfo(oauthUserJoinRequest);
        Account account = Account.create(accountInfo, memberInfo);
        return accountRepository.save(account);
    }

    private MemberInfo buildMemberInfo(OauthUserJoinRequest oauthUserJoinRequest) {
        return MemberInfo.builder()
                .name(oauthUserJoinRequest.getName())
                .build();
    }

    private AccountInfo buildAccountInfo(OauthUserJoinRequest oauthUserJoinRequest, List<Authority> authorities) {
        return AccountInfo.builder()
                .email(oauthUserJoinRequest.getEmail())
                .encryptedPassword(passwordEncoder.encode(oauthUserJoinRequest.getPassword()))
                .authorities(authorities)
                .provider(oauthUserJoinRequest.getProvider())
                .providerId(oauthUserJoinRequest.getProviderId())
                .build();
    }

    private Authority findUserAuthority() {
        return authorityRepository.findByRole(Role.of(DefaultRole.ROLE_USER.name()))
                .orElseThrow(() -> new NoSuchDataException("role", DefaultRole.ROLE_USER, getClass().getName()));
    }
}
