package com.sparta.user.domain.repository;

import com.sparta.user.domain.model.PointHistory;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository {

  PointHistory save(PointHistory pointHistory);

  Page<PointHistory> findAllByUserId(Long userId, Pageable pageable);

  Optional<PointHistory> findById(Long pointHistoryId);

  void rollback(PointHistory pointHistory);

  Page<PointHistory> findAll(Pageable pageable);

  void delete(PointHistory pointHistory);

}
