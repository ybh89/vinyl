package com.hansung.vinyl.member.application;

import com.hansung.vinyl.account.domain.AccountCreatedEvent;
import com.hansung.vinyl.account.domain.LoginMember;
import com.hansung.vinyl.account.domain.User;
import com.hansung.vinyl.common.exception.NoSuchDataException;
import com.hansung.vinyl.member.domain.Gender;
import com.hansung.vinyl.member.domain.Member;
import com.hansung.vinyl.member.domain.MemberRepository;
import com.hansung.vinyl.member.dto.FavoritesRequest;
import com.hansung.vinyl.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;


@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @TransactionalEventListener
    public void create(AccountCreatedEvent accountCreatedEvent) {
        System.out.println(accountCreatedEvent);
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
        /**
         * TODO
         * 상품 디비 조회 해야함.
         */
        return MemberResponse.of(member);
    }

    private Member findMemberById(Long accountId) {
        return memberRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchDataException("accountId", String.valueOf(accountId),
                        getClass().getName()));
    }

    public void add(LoginMember loginMember, FavoritesRequest favoritesRequest) {
        Member member = findMemberById(loginMember.getId());
        /**
         * TODO
         * 상품 도메인 먼저 개발 후.. 상품 도메인 조회하여 객체를 add..
         */
    }
}
