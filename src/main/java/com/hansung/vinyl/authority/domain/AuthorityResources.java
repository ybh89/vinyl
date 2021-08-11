package com.hansung.vinyl.authority.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Access(FIELD)
@Embeddable
public class AuthorityResources {
    @ElementCollection
    @CollectionTable(name = "authority_resource", joinColumns = @JoinColumn(name = "authority_id"))
    @OrderColumn(name = "resource_seq")
    private List<AuthorityResource> authorityResources = new ArrayList<>();

    public AuthorityResources(List<AuthorityResource> authorityResources) {
        this.authorityResources = authorityResources;
    }

    public List<Resource> getResources() {
        return authorityResources.stream()
                .map(AuthorityResource::getResource)
                .collect(Collectors.toList());
    }

    public void change(AuthorityResources authorityResources) {
        this.authorityResources.clear();
        this.authorityResources.addAll(authorityResources.getAuthorityResources());
    }

    public List<AuthorityResource> getAuthorityResources() {
        return authorityResources;
    }
}
