package com.hansung.vinyl.account.application;

import com.hansung.vinyl.account.domain.*;
import com.hansung.vinyl.account.dto.AccountAuthorityRequest;
import com.hansung.vinyl.account.dto.JoinRequest;
import com.hansung.vinyl.account.dto.JoinResponse;
import com.hansung.vinyl.account.dto.VerifyEmailResponse;
import com.hansung.vinyl.authority.domain.Authority;
import com.hansung.vinyl.authority.domain.AuthorityRepository;
import com.hansung.vinyl.authority.domain.DefaultRole;
import com.hansung.vinyl.authority.domain.Role;
import com.hansung.vinyl.common.exception.AuthorizationException;
import com.hansung.vinyl.common.exception.data.DuplicateDataException;
import com.hansung.vinyl.common.exception.data.NoSuchDataException;
import com.hansung.vinyl.identification.application.IdentificationService;
import com.hansung.vinyl.identification.dto.IdentificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
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
    private final MessageSource messageSource;
    private final IdentificationService identificationService;

    public JoinResponse join(JoinRequest joinRequest) {
        validateEmail(joinRequest.getEmail());
        Account account = createAccount(joinRequest, Arrays.asList(findUserAuthority()));
        Account savedAccount = accountRepository.save(account);
        return JoinResponse.of(savedAccount);
    }

    private Authority findUserAuthority() {
        return authorityRepository.findByRole(Role.of(DefaultRole.ROLE_USER.name()))
                .orElseThrow(() -> new NoSuchDataException("role", DefaultRole.ROLE_USER, getClass().getName()));
    }

    private void validateEmail(String email) {
        validateDuplicatedEmail(email);
        IdentificationResponse result = identificationService.result(email);
        if (!result.isApproved()) {
            throw new AuthorizationException("????????? ?????? ????????? ???????????? ???????????????.");
        }
    }

    private Account createAccount(JoinRequest joinRequest, List<Authority> authorities) {
        AccountInfo accountInfo = AccountInfo.builder()
                .email(joinRequest.getEmail())
                .encryptedPassword(passwordEncoder.encode(joinRequest.getPassword()))
                .authorities(authorities)
                .build();

        MemberInfo memberInfo = MemberInfo.builder()
                .name(joinRequest.getName())
                .phone(joinRequest.getPhone())
                .gender(joinRequest.getGender())
                .fcmToken(joinRequest.getFcmToken())
                .build();

        return Account.create(accountInfo, memberInfo);
    }

    @Transactional(readOnly = true)
    public List<JoinResponse> list() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(JoinResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = findAccountByEmail(Email.of(username));
        List<Authority> authorities = findAuthoritiesById(account.getAuthorityIds());
        return new User(account, authorities);
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long accountId) {
        Account account = findAccountById(accountId);
        List<Authority> authorities = findAuthoritiesById(account.getAuthorityIds());
        return new User(account, authorities);
    }

    public Account findAccountByEmail(Email email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchDataException("email", email, getClass().getName()));
    }

    public void delete(User user) {
        Account account = findAccountById(user.getAccountId());
        account.delete();
    }

    private Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchDataException("accountId", accountId, getClass().getName()));
    }

    private void validateDuplicatedEmail(String email) {
        if (accountRepository.existsByEmail(Email.of(email))) {
            throw new DuplicateDataException("email", email, getClass().getName());
        }
    }

    private List<Authority> findAuthoritiesById(List<Long> ids) {
        if (Objects.isNull(ids)) {
            return Arrays.asList();
        }
        return authorityRepository.findAllDistinctByIdIn(ids);
    }

    public void updateAuthorities(Long accountId, AccountAuthorityRequest accountAuthorityRequest) {
        Account account = findAccountById(accountId);
        List<Authority> authorities = findAuthoritiesById(accountAuthorityRequest.getAuthorityIds());
        account.changeAuthorities(authorities);
    }

    public void updateRefreshToken(Long accountId, RefreshToken refreshToken) {
        Account account = findAccountById(accountId);
        account.updateRefreshToken(refreshToken);
    }

    public VerifyEmailResponse verifyEmail(String sEmail) {
        Email email = Email.of(sEmail);
        VerifyEmailResponse verifyEmailResponse = new VerifyEmailResponse(email.value(), false,
                messageSource.getMessage("account.email.available", null, null));
        try {
            validateDuplicatedEmail(email.value());
        } catch (DuplicateDataException exception) {
            verifyEmailResponse.setDuplicated(true);
            verifyEmailResponse.setMessage(messageSource.getMessage("account.email.duplicated", null, null));
        }
        return verifyEmailResponse;
    }
}
