package com.hansung.vinyl.member.ui;

import com.hansung.vinyl.account.domain.AuthenticationPrincipal;
import com.hansung.vinyl.account.domain.LoginMember;
import com.hansung.vinyl.member.application.MemberService;
import com.hansung.vinyl.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> me(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse memberResponse = memberService.me(loginMember);
        return ResponseEntity.ok(memberResponse);
    }

}
