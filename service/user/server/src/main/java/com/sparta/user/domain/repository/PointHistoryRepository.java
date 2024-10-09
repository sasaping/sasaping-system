package com.sparta.user.domain.repository;

import com.sparta.user.domain.model.PointHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository {

  PointHistory save(PointHistory pointHistory);

}
