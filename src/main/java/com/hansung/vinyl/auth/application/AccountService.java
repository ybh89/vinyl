package com.hansung.vinyl.auth.application;

import com.hansung.vinyl.auth.domain.Account;
import com.hansung.vinyl.auth.domain.AccountRepository;
import com.hansung.vinyl.auth.dto.AccountRequest;
import com.hansung.vinyl.auth.dto.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountResponse join(AccountRequest accountRequest) {
        if (accountRepository.existsByEmail(accountRequest.getEmail())) {
            throw new IllegalArgumentException("해당 아이디로 가입된 계정이 이미 존재합니다.");
        }

        Account account = Account.builder()
                .email(accountRequest.getEmail())
                .password(accountRequest.getPassword())
                .build();

        Account savedAccount = accountRepository.save(account);

        return AccountResponse.of(savedAccount);
    }
}
