package com.hansung.vinyl.member.application;

import com.hansung.vinyl.account.domain.AccountCreatedEvent;
import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.common.exception.data.NoSuchDataException;
import com.hansung.vinyl.member.domain.Member;
import com.hansung.vinyl.member.domain.MemberRepository;
import com.hansung.vinyl.member.dto.MemberResponse;
import com.hansung.vinyl.news.application.NewsService;
import com.hansung.vinyl.news.dto.NewsListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final NewsService newsService;


    @Async
    @EventListener
    public void create(AccountCreatedEvent accountCreatedEvent) {
        System.out.println("AccountCreatedEvent = " + accountCreatedEvent);

        Member member = Member.builder()
                .accountId(accountCreatedEvent.getAccountId())
                .email(accountCreatedEvent.getEmail())
                .name(accountCreatedEvent.getName())
                .phone(accountCreatedEvent.getPhone())
                .gender(accountCreatedEvent.getGender())
                .build();
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse me(User user) {
        Member member = findMemberById(user.getAccountId());
        return MemberResponse.of(member);
    }

    private Member findMemberById(Long accountId) {
        return memberRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchDataException("accountId", String.valueOf(accountId),
                        getClass().getName()));
    }

    @Transactional(readOnly = true)
    public MemberResponse member(Long memberId) {
        Member member = findMemberById(memberId);
        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public List<NewsListResponse> subscribes(User user) {
        Member member = findMemberById(user.getAccountId());
        List<Long> newsIds = member.getSubscribes();
        return newsService.list(newsIds);
    }

    @Transactional(readOnly = true)
    public Page<MemberResponse> list(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        return members.map(MemberResponse::of);
    }
}
