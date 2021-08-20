package com.hansung.vinyl.authority.ui;

import com.hansung.vinyl.authority.application.AuthorityService;
import com.hansung.vinyl.authority.dto.AuthorityRequest;
import com.hansung.vinyl.authority.dto.AuthorityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthorityController {
    private final AuthorityService authorityService;

    @PostMapping("/v1/authorities")
    public ResponseEntity<AuthorityResponse> create(@RequestBody AuthorityRequest authorityRequest) {
        AuthorityResponse authorityResponse = authorityService.create(authorityRequest);
        return ResponseEntity.created(URI.create("v1/authorities/" + authorityResponse.getId())).body(authorityResponse);
    }

    @GetMapping("/v1/authorities")
    public ResponseEntity<List<AuthorityResponse>> list() {
        List<AuthorityResponse> authorityResponses = authorityService.list();
        return ResponseEntity.ok(authorityResponses);
    }

    @PutMapping("/v1/authorities/{authorityId}")
    public ResponseEntity<AuthorityResponse> update(@PathVariable Long authorityId,
                                                    @RequestBody AuthorityRequest authorityRequest) {
        AuthorityResponse authorityResponse = authorityService.update(authorityId, authorityRequest);
        return ResponseEntity.ok(authorityResponse);
    }

    @DeleteMapping("/v1/authorities/{authorityId}")
    public ResponseEntity delete(@PathVariable Long authorityId) {
        authorityService.delete(authorityId);
        return ResponseEntity.noContent().build();
    }
}
