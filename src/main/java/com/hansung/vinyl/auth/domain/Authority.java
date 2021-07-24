package com.hansung.vinyl.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class Authority {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    @Column(unique = true)
    private String name;
    private String desc;
    @OneToMany(mappedBy = "authority", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuthorityPath> authorityPaths = new ArrayList<>();

    @Builder
    public Authority(Long id, String name, String desc, List<Path> paths) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        if (Objects.nonNull(paths)) {
            this.authorityPaths = paths.stream()
                    .map(path -> AuthorityPath.builder()
                            .authority(this)
                            .path(path)
                            .build())
                    .collect(Collectors.toList());
        }
    }

    public List<Path> getPaths() {
        return authorityPaths.stream()
                .map(AuthorityPath::getPath)
                .collect(Collectors.toList());
    }

    public void addAuthorityPath(AuthorityPath authorityPath) {
        if (!authorityPaths.contains(authorityPath)) {
            authorityPaths.add(authorityPath);
        }
        authorityPath.setAuthority(this);
    }

    public void update(Authority authority) {
        this.name = authority.name;
        this.desc = authority.desc;
        this.authorityPaths.clear();
        authority.authorityPaths.stream()
                .forEach(this::addAuthorityPath);
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
