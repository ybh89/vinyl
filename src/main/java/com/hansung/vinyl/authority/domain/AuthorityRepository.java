package com.hansung.vinyl.authority.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    boolean existsByIdNotAndNameEquals(Long authorityId, String name);
    boolean existsByName(String name);
}
