package com.hansung.vinyl.auth.application;

import com.hansung.vinyl.auth.domain.Account;
import com.hansung.vinyl.auth.domain.AccountRepository;
import com.hansung.vinyl.auth.dto.AccountRequest;
import com.hansung.vinyl.auth.dto.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountResponse join(AccountRequest accountRequest) {
        validateEmail(accountRequest.getEmail());
        Account account = Account.builder()
                .email(accountRequest.getEmail())
                .password(accountRequest.getPassword())
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

    private void validateEmail(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("해당 아이디로 가입된 계정이 이미 존재합니다.");
        }
    }
}
