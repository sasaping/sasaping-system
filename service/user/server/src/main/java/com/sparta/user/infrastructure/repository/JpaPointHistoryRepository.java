package com.sparta.user.infrastructure.repository;

import com.sparta.user.domain.model.PointHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPointHistoryRepository extends JpaRepository<PointHistory, Long> {

  Page<PointHistory> findAllByUserId(Long userId, Pageable pageable);

}
