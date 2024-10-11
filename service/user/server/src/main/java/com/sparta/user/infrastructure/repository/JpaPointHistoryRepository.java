package com.sparta.user.infrastructure.repository;

import com.sparta.user.domain.model.PointHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPointHistoryRepository extends JpaRepository<PointHistory, Long> {

  List<PointHistory> findAllByUserId(Long userId);

}
