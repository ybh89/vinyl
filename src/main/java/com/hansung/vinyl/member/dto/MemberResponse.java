package com.hansung.vinyl.member.dto;

import com.hansung.vinyl.member.domain.Gender;
import com.hansung.vinyl.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberResponse {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private Gender gender;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MemberResponse of(Member member) {
        if (Objects.isNull(member)) {
            return new MemberResponse();
        }
        return new MemberResponse(member.getId(), member.getEmailValue(), member.getNameValue(), member.getPhoneValue(),
                member.getGender(), member.getCreatedAt(), member.getUpdatedAt());
    }
}
