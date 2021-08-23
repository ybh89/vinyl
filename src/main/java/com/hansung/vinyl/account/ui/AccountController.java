package com.hansung.vinyl.account.ui;

import com.hansung.vinyl.account.application.AccountService;
import com.hansung.vinyl.account.domain.AuthenticationPrincipal;
import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.account.dto.AccountAuthorityRequest;
import com.hansung.vinyl.account.dto.JoinRequest;
import com.hansung.vinyl.account.dto.JoinResponse;
import com.hansung.vinyl.account.dto.VerifyEmailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/v1/accounts")
    public ResponseEntity<JoinResponse> join(@Validated @RequestBody JoinRequest joinRequest) {
        JoinResponse joinResponse = accountService.join(joinRequest);
        return ResponseEntity.created(URI.create("v1/accounts/" + joinResponse.getId())).body(joinResponse);
    }

    @DeleteMapping("/v1/accounts")
    public ResponseEntity delete(@AuthenticationPrincipal User user) {
        accountService.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/v1/accounts/{accountId}/authorities")
    public ResponseEntity updateAuthorities(@PathVariable Long accountId,
                                                          @RequestBody AccountAuthorityRequest accountAuthorityRequest) {
        accountService.updateAuthorities(accountId, accountAuthorityRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/v1/accounts/verify-email")
    public ResponseEntity<VerifyEmailResponse> verifyEmail(String email) {
        VerifyEmailResponse verifyEmailResponse = accountService.verifyEmail(email);
        return ResponseEntity.ok(verifyEmailResponse);
    }
}
