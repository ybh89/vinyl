package com.hansung.vinyl.identification.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IdentificationTokenRepository extends JpaRepository<IdentificationToken, UUID> {
    Optional<IdentificationToken> findByEmail(Email email);
}
