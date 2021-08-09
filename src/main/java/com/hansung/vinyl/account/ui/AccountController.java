package com.hansung.vinyl.account.ui;

import com.hansung.vinyl.account.application.AccountService;
import com.hansung.vinyl.account.domain.AuthenticationPrincipal;
import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.account.dto.AccountAuthorityRequest;
import com.hansung.vinyl.account.dto.JoinRequest;
import com.hansung.vinyl.account.dto.JoinResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/accounts")
@RestController
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<JoinResponse> join(@Validated @RequestBody JoinRequest joinRequest) {
        JoinResponse joinResponse = accountService.join(joinRequest);
        return ResponseEntity.created(URI.create("/accounts/" + joinResponse.getId())).body(joinResponse);
    }

    @DeleteMapping
    public ResponseEntity delete(@AuthenticationPrincipal User user) {
        accountService.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{accountId}/authorities")
    public ResponseEntity updateAuthorities(@PathVariable Long accountId,
                                                          @RequestBody AccountAuthorityRequest accountAuthorityRequest) {
        accountService.updateAuthorities(accountId, accountAuthorityRequest);
        return ResponseEntity.noContent().build();
    }
}
