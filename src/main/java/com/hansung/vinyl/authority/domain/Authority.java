package com.hansung.vinyl.authority.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

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
public class Authority {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 100)
    private String remark;

    @OneToMany(mappedBy = "authority", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuthorityPath> authorityPaths = new ArrayList<>();

    @Builder
    public Authority(Long id, String name, String remark, List<Path> paths, ApplicationEventPublisher publisher) {
        this.id = id;
        this.name = name;
        this.remark = remark;
        if (Objects.nonNull(paths)) {
            this.authorityPaths = createAuthorityResources(paths);
        }
        publishEvent(publisher, new AuthorityCommandedEvent());
    }

    public List<Path> getPaths() {
        return authorityPaths.stream()
                .map(AuthorityPath::getPath)
                .collect(Collectors.toList());
    }

    public void update(Authority authority, ApplicationEventPublisher publisher) {
        this.name = authority.name;
        this.remark = authority.remark;
        this.authorityPaths.clear();
        authority.authorityPaths.forEach(this::addAuthorityPath);
        publishEvent(publisher, new AuthorityCommandedEvent());
    }

    public void publishEvent(ApplicationEventPublisher publisher, Object event) {
        publisher.publishEvent(event);
    }

    private void addAuthorityPath(AuthorityPath authorityPath) {
        if (!authorityPaths.contains(authorityPath)) {
            authorityPaths.add(authorityPath);
        }
        authorityPath.setAuthority(this);
    }

    private List<AuthorityPath> createAuthorityResources(List<Path> paths) {
        AtomicInteger seq = new AtomicInteger(1);
        return paths.stream()
                .map(path ->  AuthorityPath.builder()
                            .authority(this)
                            .path(path)
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
}
