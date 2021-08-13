package com.hansung.vinyl.authority.domain;

import com.hansung.vinyl.common.domain.DateTimeAuditor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Access(FIELD)
@SecondaryTable(
        name = "authority_resource",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "authority_id")
)
@Table(uniqueConstraints={ @UniqueConstraint(name = "uk_authority_role", columnNames = "role") })
@Entity
public class Authority extends AbstractAggregateRoot<Authority> implements GrantedAuthority {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private Role role;

    @Column(length = 100)
    private String remark;

    @Embedded
    private AuthorityResources authorityResources;

    @Embedded
    private DateTimeAuditor dateTimeAuditor;

    @Builder
    private Authority(Role role, String remark, AuthorityResources authorityResources) {
        this.role = role;
        this.remark = remark;
        this.authorityResources = authorityResources;
        this.dateTimeAuditor = new DateTimeAuditor();
    }

    public static Authority create(String role, String remark, List<Resource> resources) {
        Authority authority = Authority.builder()
                .role(new Role(role))
                .remark(remark)
                .authorityResources(new AuthorityResources(createAuthorityResources(resources)))
                .build();
        authority.registerEvent(new AuthorityCommandedEvent(authority, Command.CREATE));
        return authority;
    }

    public List<Resource> getResources() {
        return authorityResources.getResources();
    }

    public void update(Authority authority) {
        this.role = authority.role;
        this.remark = authority.remark;
        this.authorityResources.change(authority.getAuthorityResources());
        this.registerEvent(new AuthorityCommandedEvent(this, Command.UPDATE));
    }

    public String getRoleValue() {
        return role.value();
    }

    public Authority delete() {
        registerEvent(new AuthorityCommandedEvent(this, Command.DELETE));
        return this;
    }

    private static List<AuthorityResource> createAuthorityResources(List<Resource> resources) {
        if (Objects.isNull(resources)) {
            return Arrays.asList();
        }
        return resources.stream()
                .map(AuthorityResource::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return Objects.equals(getId(), authority.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String getAuthority() {
        return role.value();
    }

    @Override
    public String toString() {
        return "Authority{" +
                "name='" + role.value() + '\'' +
                '}';
    }
}
