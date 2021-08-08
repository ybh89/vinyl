package com.hansung.vinyl.member.ui;

import com.hansung.vinyl.account.domain.AuthenticationPrincipal;
import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.member.application.MemberService;
import com.hansung.vinyl.member.dto.MemberResponse;
import com.hansung.vinyl.news.dto.NewsListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> me(@AuthenticationPrincipal User user) {
        MemberResponse memberResponse = memberService.me(user);
        return ResponseEntity.ok(memberResponse);
    }

    @GetMapping("/subscribes")
    public ResponseEntity<List<NewsListResponse>> subscribes(@AuthenticationPrincipal User user) {
        List<NewsListResponse> newsListResponses = memberService.subscribes(user);
        return ResponseEntity.ok(newsListResponses);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> member(@PathVariable Long memberId) {
        MemberResponse memberResponse = memberService.member(memberId);
        return ResponseEntity.ok(memberResponse);
    }

    @GetMapping
    public ResponseEntity<Page<MemberResponse>> list(@PageableDefault(size = 20, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MemberResponse> memberResponses = memberService.list(pageable);
        return ResponseEntity.ok(memberResponses);
    }
}
