package com.hansung.vinyl.auth.ui;

import com.hansung.vinyl.auth.application.AccountService;
import com.hansung.vinyl.auth.dto.AccountRequest;
import com.hansung.vinyl.auth.dto.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/accounts")
@RestController
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity join(@RequestBody AccountRequest accountRequest) {
        AccountResponse accountResponse = accountService.join(accountRequest);
        return ResponseEntity.created(URI.create("/accounts" + accountResponse.getId())).build();
    }
}
