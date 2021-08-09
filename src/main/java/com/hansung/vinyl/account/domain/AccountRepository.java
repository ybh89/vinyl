package com.hansung.vinyl.account.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(Email email);
    Optional<Account> findByEmail(Email email);
}
