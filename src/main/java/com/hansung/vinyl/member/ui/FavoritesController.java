package com.hansung.vinyl.member.ui;

import com.hansung.vinyl.account.domain.AuthenticationPrincipal;
import com.hansung.vinyl.account.domain.LoginMember;
import com.hansung.vinyl.member.application.MemberService;
import com.hansung.vinyl.member.dto.FavoritesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/favorites")
@RestController
public class FavoritesController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity add(@AuthenticationPrincipal LoginMember loginMember,
                              @RequestBody FavoritesRequest favoritesRequest) {
        memberService.add(loginMember, favoritesRequest);
        return ResponseEntity.ok().build();
    }
}
