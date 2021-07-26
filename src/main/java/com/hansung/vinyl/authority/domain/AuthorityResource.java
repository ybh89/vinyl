package com.hansung.vinyl.authority.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(uniqueConstraints={ @UniqueConstraint(name = "uk_authority_path", columnNames = { "authority_id", "path", "http_method" }) })
@Entity
public class AuthorityResource {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "authority_id", foreignKey = @ForeignKey(name = "fk_path_authority"))
    @ManyToOne
    private Authority authority;

    @Embedded
    private Resource resource;

    private int seq;

    @Builder
    public AuthorityResource(Long id, Authority authority, Resource resource, int seq) {
        this.id = id;
        this.authority = authority;
        this.resource = resource;
        this.seq = seq;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }
}
