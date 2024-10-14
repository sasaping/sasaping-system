package com.sparta.user.domain.repository;

import com.sparta.user.domain.model.PointHistory;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository {

  PointHistory save(PointHistory pointHistory);

  Optional<PointHistory> findById(Long pointHistoryId);

  void rollback(PointHistory pointHistory);

}
