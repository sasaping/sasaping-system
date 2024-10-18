package com.sparta.user.application.service;

import static com.sparta.user.exception.UserErrorCode.USER_NOT_FOUND;

import com.sparta.user.application.dto.PointResponse;
import com.sparta.user.domain.model.PointHistory;
import com.sparta.user.domain.model.User;
import com.sparta.user.domain.model.vo.PointHistoryType;
import com.sparta.user.domain.repository.PointHistoryRepository;
import com.sparta.user.domain.repository.UserRepository;
import com.sparta.user.exception.UserErrorCode;
import com.sparta.user.exception.UserException;
import com.sparta.user.user_dto.infrastructure.PointHistoryDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointHistoryService {

  private final UserRepository userRepository;
  private final PointHistoryRepository pointHistoryRepository;

  @Transactional
  public Long createPointHistory(PointHistoryDto request) {
    User user = userRepository
        .findById(request.getUserId())
        .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

    switch (PointHistoryType.from(request.getType())) {
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
    return pointHistoryRepository.save(PointHistory.create(user, request)).getId();
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

  public List<PointResponse.Get> getPointHistoryByUserid(Long userId) {
    User user = userRepository
        .findById(userId)
        .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    return pointHistoryRepository
        .findAllByUserId(user.getId())
        .stream()
        .map(PointResponse.Get::of)
        .collect(Collectors.toList());
  }

  @Transactional
  public void rollbackPointHistory(Long pointHistoryId) {
    PointHistory pointHistory = pointHistoryRepository.findById(pointHistoryId).orElseThrow(
        () -> new UserException(UserErrorCode.POINT_HISTORY_NOT_FOUND)
    );
    handlePointAdd(pointHistory.getUser(), pointHistory.getPoint());
    pointHistoryRepository.rollback(pointHistory);
  }

}
