package com.hansung.vinyl.auth.ui;

import com.hansung.vinyl.auth.application.AuthorityService;
import com.hansung.vinyl.auth.dto.AuthorityRequest;
import com.hansung.vinyl.auth.dto.AuthorityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/authorities")
@RestController
public class AuthorityController {
    private final AuthorityService authorityService;

    @PostMapping
    public ResponseEntity<AuthorityResponse> create(@RequestBody AuthorityRequest authorityRequest) {
        AuthorityResponse authorityResponse = authorityService.create(authorityRequest);
        return ResponseEntity.created(URI.create("/authorities/" + authorityResponse.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<AuthorityResponse>> list() {
        List<AuthorityResponse> authorityResponses = authorityService.list();
        return ResponseEntity.ok(authorityResponses);
    }
}
