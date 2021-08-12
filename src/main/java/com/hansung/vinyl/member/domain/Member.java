package com.hansung.vinyl.member.domain;

import com.hansung.vinyl.account.domain.Email;
import com.hansung.vinyl.common.domain.BaseDateTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@SecondaryTable(
        name = "subscribe",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "member_id")
)
@Entity
public class Member extends BaseDateTimeEntity implements Persistable<Long> {
    @Id
    private Long id;

    @Column(unique = true, nullable = false, updatable = false, length = 50)
    private Email email;

    @Embedded
    private Name name;

    @Embedded
    private Phone phone;

    private Gender gender;

    @Embedded
    private Subscribes subscribes;

    @Builder
    public Member(Long accountId, String email, String name, String phone, Gender gender) {
        this.id = accountId;
        this.email = new Email(email);
        this.name = new Name(name);
        this.phone = new Phone(phone);
        this.gender = gender;
    }

    public void subscribe(Long newsId) {
        subscribes.subscribe(newsId);
    }

    public void unsubscribe(Long newsId) {
        subscribes.unsubscribe(newsId);
    }

    public List<Long> getSubscribes() {
        return subscribes.getSubscribes();
    }

    public String getEmailValue() {
        return email.value();
    }

    public String getNameValue() {
        return name.value();
    }

    public String getPhoneValue() {
        if (Objects.isNull(phone)) {
            return null;
        }
        return phone.value();
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email=" + email +
                ", name=" + name +
                ", phone=" + phone +
                ", gender=" + gender +
                ", subscribes=" + subscribes +
                '}';
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(getCreatedAt());
    }
}
