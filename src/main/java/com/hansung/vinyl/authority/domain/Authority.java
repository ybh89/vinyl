package com.hansung.vinyl.authority.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Table(uniqueConstraints={ @UniqueConstraint(name = "uk_authority_name", columnNames = "name") })
@Entity
public class Authority implements GrantedAuthority {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 100)
    private String remark;

    @OneToMany(mappedBy = "authority", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuthorityResource> authorityResources = new ArrayList<>();

    @Builder
    public Authority(Long id, String name, String remark, List<Resource> resources, ApplicationEventPublisher publisher) {
        this.id = id;
        this.name = name;
        this.remark = remark;
        if (Objects.nonNull(resources)) {
            this.authorityResources = createAuthorityResources(resources);
        }
        publishEvent(publisher, new AuthorityCommandedEvent(this, "create"));
    }

    public List<Resource> getResources() {
        return authorityResources.stream()
                .map(AuthorityResource::getResource)
                .collect(Collectors.toList());
    }

    public void update(Authority authority, ApplicationEventPublisher publisher) {
        this.name = authority.name;
        this.remark = authority.remark;
        this.authorityResources.clear();
        authority.authorityResources.forEach(this::addAuthorityResource);
        publishEvent(publisher, new AuthorityCommandedEvent(this, "update"));
    }

    public void publishEvent(ApplicationEventPublisher publisher, Object event) {
        if (Objects.nonNull(publisher)) {
            publisher.publishEvent(event);
        }
    }

    private void addAuthorityResource(AuthorityResource authorityResource) {
        if (!authorityResources.contains(authorityResource)) {
            authorityResources.add(authorityResource);
        }
        authorityResource.setAuthority(this);
    }

    private List<AuthorityResource> createAuthorityResources(List<Resource> resources) {
        AtomicInteger seq = new AtomicInteger(1);
        return resources.stream()
                .map(resource ->  AuthorityResource.builder()
                            .authority(this)
                            .resource(resource)
                            .seq(seq.getAndIncrement())
                            .build())
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
        return name;
    }
}
