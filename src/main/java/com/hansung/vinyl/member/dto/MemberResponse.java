package com.hansung.vinyl.member.dto;

import com.hansung.vinyl.member.domain.Gender;
import com.hansung.vinyl.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberResponse {
    private Long accountId;
    private String email;
    private String name;
    private String phone;
    private Gender gender;
    private List<Long> favorites = new ArrayList<>();

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getName(), member.getPhone(), member.getGender(), member.getFavorites());
    }
}
