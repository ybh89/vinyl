package com.hansung.vinyl.authority.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    boolean existsByIdNotAndRoleEquals(Long authorityId, Role role);
    boolean existsByRole(Role role);
    List<Authority> findAllDistinctByIdIn(List<Long> ids);
    @Query("select distinct auth from Authority auth")
    List<Authority> findAllDistinct();
    Optional<Authority> findDistinctById(Long authorityId);
    Optional<Authority> findByRole(Role role);
}
