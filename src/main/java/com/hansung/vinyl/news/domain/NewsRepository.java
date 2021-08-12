package com.hansung.vinyl.news.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {
    Slice<News> findAllByPostDeletedFalse(Pageable pageable);
    Optional<News> findByIdAndPostDeletedFalse(Long newsId);
    List<News> findAllByCatalogReleaseDateBetweenAndPostDeletedFalse(LocalDateTime targetDate, LocalDateTime nextDate);
}
