package com.sparta.user.application.service;

import com.sparta.user.domain.model.PointHistory;
import com.sparta.user.domain.model.User;
import com.sparta.user.domain.repository.PointHistoryRepository;
import com.sparta.user.domain.repository.UserRepository;
import com.sparta.user.exception.UserErrorCode;
import com.sparta.user.exception.UserException;
import com.sparta.user.presentation.request.PointHistoryRequest.Create;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointHistoryService {

  private final UserRepository userRepository;
  private final PointHistoryRepository pointHistoryRepository;

  @Transactional
  public void createPointHistory(Create request) {
    User user = userRepository
        .findById(request.getUserId())
        .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    switch (request.getType()) {
      case EARN:
      case REFUND:
        handlePointAdd(user, request.getPoint());
        break;
      case USE:
        handlePointUse(user, request.getPoint());
        break;
      default:
        throw new UserException(UserErrorCode.INVALID_POINT_HISTORY_TYPE);
    }

    pointHistoryRepository.save(PointHistory.create(user, request));
  }

  private void handlePointAdd(User user, BigDecimal point) {
    user.updatePoint(user.getPoint().add(point));
    userRepository.save(user);
  }

  private void handlePointUse(User user, BigDecimal point) {
    if (user.getPoint().compareTo(point) < 0) {
      throw new UserException(UserErrorCode.INSUFFICIENT_POINTS);
    }
    user.updatePoint(user.getPoint().subtract(point));
    userRepository.save(user);
  }

}
