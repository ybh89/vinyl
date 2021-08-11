package com.hansung.vinyl.authority.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
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
public class Authority implements GrantedAuthority {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Embedded
    private Role role;

    @Column(length = 100)
    private String remark;

    @Embedded
    private AuthorityResources authorityResources;

    @Builder
    public Authority(Long id, String role, String remark, List<Resource> resources, ApplicationEventPublisher publisher) {
        this.id = id;
        this.role = new Role(role);
        this.remark = remark;
        if (Objects.nonNull(resources)) {
            authorityResources = new AuthorityResources(createAuthorityResources(resources));
        }
        publishEvent(publisher, new AuthorityCommandedEvent(this, "create"));
    }

    public List<Resource> getResources() {
        return authorityResources.getResources();
    }

    public void update(Authority authority, ApplicationEventPublisher publisher) {
        this.role = authority.role;
        this.remark = authority.remark;
        this.authorityResources.change(authority.getAuthorityResources());
        publishEvent(publisher, new AuthorityCommandedEvent(this, "update"));
    }

    public void publishEvent(ApplicationEventPublisher publisher, Object event) {
        if (Objects.nonNull(publisher)) {
            publisher.publishEvent(event);
        }
    }

    private List<AuthorityResource> createAuthorityResources(List<Resource> resources) {
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

    public String getRoleValue() {
        return role.value();
    }
}
