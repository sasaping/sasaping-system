package com.sparta.user.domain.repository;

import com.sparta.user.domain.model.PointHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository {

  PointHistory save(PointHistory pointHistory);

  List<PointHistory> findAllByUserId(Long userId);

  Optional<PointHistory> findById(Long pointHistoryId);

  void rollback(PointHistory pointHistory);

}
