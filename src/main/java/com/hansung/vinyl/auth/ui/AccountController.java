package com.hansung.vinyl.auth.ui;

import com.hansung.vinyl.auth.application.AccountService;
import com.hansung.vinyl.auth.dto.AccountRequest;
import com.hansung.vinyl.auth.dto.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/accounts")
@RestController
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> join(@RequestBody AccountRequest accountRequest) {
        AccountResponse accountResponse = accountService.join(accountRequest);
        return ResponseEntity.created(URI.create("/accounts/" + accountResponse.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> list() {
        List<AccountResponse> accountResponses = accountService.list();
        return ResponseEntity.ok(accountResponses);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> findById(@PathVariable Long accountId) {
        AccountResponse accountResponse = accountService.findById(accountId);
        return ResponseEntity.ok(accountResponse);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity delete(@PathVariable Long accountId) {
        accountService.delete(accountId);
        return ResponseEntity.noContent().build();
    }
}
