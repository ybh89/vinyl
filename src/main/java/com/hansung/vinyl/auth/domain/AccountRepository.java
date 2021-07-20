package com.hansung.vinyl.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
}
