package com.sparta.user.infrastructure.repository;

import com.sparta.user.domain.model.PointHistory;
import com.sparta.user.domain.repository.PointHistoryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

  private final JpaPointHistoryRepository jpaPointHistoryRepository;

  @Override
  public PointHistory save(PointHistory pointHistory) {
    return jpaPointHistoryRepository.save(pointHistory);
  }

  @Override
  public Optional<PointHistory> findById(Long pointHistoryId) {
    return jpaPointHistoryRepository.findById(pointHistoryId);
  }

  @Override
  public void rollback(PointHistory pointHistory) {
    jpaPointHistoryRepository.delete(pointHistory);
  }

}
