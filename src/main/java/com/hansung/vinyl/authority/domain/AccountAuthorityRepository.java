package com.hansung.vinyl.authority.domain;

import com.hansung.vinyl.account.domain.AccountAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountAuthorityRepository extends JpaRepository<AccountAuthority, Long> {
    boolean existsByAuthorityId(Long authorityId);
}
