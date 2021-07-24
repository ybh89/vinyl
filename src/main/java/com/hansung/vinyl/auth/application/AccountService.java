package com.hansung.vinyl.auth.application;

import com.hansung.vinyl.auth.domain.*;
import com.hansung.vinyl.auth.dto.AccountAuthorityRequest;
import com.hansung.vinyl.auth.dto.AccountRequest;
import com.hansung.vinyl.auth.dto.AccountResponse;
import com.hansung.vinyl.auth.exception.JwtValidateException;
import com.hansung.vinyl.auth.infrastructure.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AccountResponse join(AccountRequest accountRequest) {
        validateEmail(accountRequest.getEmail());
        List<Authority> authorities = authorityRepository.findAllById(accountRequest.getAuthorityIds());
        Account account = Account.builder()
                .email(accountRequest.getEmail())
                .password(passwordEncoder.encode(accountRequest.getPassword()))
                .authorities(authorities)
                .build();

        Account savedAccount = accountRepository.save(account);
        return AccountResponse.of(savedAccount);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> list() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(AccountResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AccountResponse findById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        return AccountResponse.of(account);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = findAccountByEmail(username);
        return User.builder()
                .accountId(account.getId())
                .username(account.getEmail())
                .password(account.getPassword())
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .authorities(new ArrayList<>())
                .build();
    }

    public Account findAccountByEmail(String username) {
        return accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. email=" + username));
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtProvider.validateToken(credentials)) {
            throw new JwtValidateException();
        }

        String email = jwtProvider.getPayload(credentials);
        Account account = findAccountByEmail(email);
        return new LoginMember(account.getId(), account.getEmail());
    }

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

    public AccountResponse updateAuthorities(Long accountId, AccountAuthorityRequest accountAuthorityRequest) {

        return null;
    }
}
