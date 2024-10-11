package com.sparta.user.domain.repository;

import com.sparta.user.domain.model.PointHistory;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository {

  PointHistory save(PointHistory pointHistory);

  List<PointHistory> findAllByUserId(Long userId);

}
