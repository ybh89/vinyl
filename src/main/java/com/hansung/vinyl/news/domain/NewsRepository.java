package com.hansung.vinyl.news.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {
    Slice<News> findAllByDeletedFalse(Pageable pageable);

    Optional<News> findByIdAndDeletedFalse(Long newsId);
}
