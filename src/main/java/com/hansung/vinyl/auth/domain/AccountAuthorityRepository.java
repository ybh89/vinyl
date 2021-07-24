package com.hansung.vinyl.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountAuthorityRepository extends JpaRepository<AccountAuthority, Long> {
    boolean existsByAuthorityId(Long authorityId);
}
