package com.sparta.user.infrastructure.repository;

import com.sparta.user.domain.model.PointHistory;
import com.sparta.user.domain.repository.PointHistoryRepository;
import java.util.List;
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
  public List<PointHistory> findAllByUserId(Long userId) {
    return jpaPointHistoryRepository.findAllByUserId(userId);
  }

}
