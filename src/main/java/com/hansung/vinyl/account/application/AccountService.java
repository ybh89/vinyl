package com.hansung.vinyl.account.application;

import com.hansung.vinyl.account.domain.*;
import com.hansung.vinyl.account.dto.AccountAuthorityRequest;
import com.hansung.vinyl.account.dto.JoinRequest;
import com.hansung.vinyl.account.dto.JoinResponse;
import com.hansung.vinyl.authority.domain.Authority;
import com.hansung.vinyl.authority.domain.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;

    public JoinResponse join(JoinRequest joinRequest) {
        validateEmail(joinRequest.getEmail());
        List<Authority> authorities = findAuthoritiesById(joinRequest.getAuthorityIds());
        Join join = joinRequest.toJoin();

        Account account = Account.builder()
                .email(joinRequest.getEmail())
                .password(passwordEncoder.encode(joinRequest.getPassword()))
                .authorities(authorities)
                .join(join)
                .build();

        Account savedAccount = accountRepository.save(account);
        return JoinResponse.of(savedAccount);
    }

    @Transactional(readOnly = true)
    public List<JoinResponse> list() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(JoinResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public JoinResponse findById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        return JoinResponse.of(account);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = findAccountByEmail(username);
        return loadUser(account);
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long accountId) {
        Account account = findAccountById(accountId);
        return loadUser(account);
    }

    private UserDetails loadUser(Account account) {
        List<Long> authorityIds = account.getAccountAuthorities().stream()
                .map(accountAuthority -> accountAuthority.getAuthorityId())
                .collect(Collectors.toList());
        List<Authority> authorities = authorityRepository.findAllById(authorityIds);

        return User.builder()
                .accountId(account.getId())
                .username(account.getEmail())
                .password(account.getPassword())
                .refreshToken(account.getRefreshToken())
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .authorities(authorities)
                .build();
    }

    public Account findAccountByEmail(String username) {
        return accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. email=" + username));
    }

    /*public LoginMember findMemberByToken(String credentials) {
        if (!jwtProvider.validateToken(credentials)) {
            throw new JwtValidateException();
        }

        String email = jwtProvider.getPayload(credentials);
        Account account = findAccountByEmail(email);
        return new LoginMember(account.getId(), account.getEmail());
    }*/

    public void delete(Long accountId) {
        Account account = findAccountById(accountId);
        account.delete();
    }

    private Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. id=" + accountId));
    }

    private void validateEmail(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("해당 아이디로 가입된 계정이 이미 존재합니다.");
        }
    }

    private List<Authority> findAuthoritiesById(List<Long> ids) {
        if (Objects.isNull(ids)) {
            return Arrays.asList();
        }
        return authorityRepository.findAllById(ids);
    }

    public JoinResponse updateAuthorities(Long accountId, AccountAuthorityRequest accountAuthorityRequest) {
        Account account = findAccountById(accountId);
        List<Authority> authorities = findAuthoritiesById(accountAuthorityRequest.getAuthorityIds());
        account.changeAuthorities(authorities);
        return JoinResponse.of(account);
    }

    public void updateRefreshToken(Long accountId, String refreshToken) {
        Account account = findAccountById(accountId);
        account.updateRefreshToken(refreshToken);
    }
}
