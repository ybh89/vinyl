package com.hansung.vinyl.auth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(uniqueConstraints={ @UniqueConstraint(columnNames = { "authority_id", "path" }) })
@Entity
public class AuthorityPath {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    @JoinColumn(name = "authority_id")
    @ManyToOne
    private Authority authority;
    @Embedded
    private Path path;

    @Builder
    public AuthorityPath(Long id, Authority authority, Path path) {
        this.id = id;
        this.authority = authority;
        this.path = path;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }
}
