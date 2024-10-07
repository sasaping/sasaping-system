package com.sparta.user.application.service;

import com.sparta.user.application.dto.TierResponse;
import com.sparta.user.domain.model.Tier;
import com.sparta.user.domain.repository.TierRepository;
import com.sparta.user.exception.UserErrorCode;
import com.sparta.user.exception.UserException;
import com.sparta.user.presentation.request.TierRequest;
import com.sparta.user.presentation.request.TierRequest.Create;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TierService {

  private final TierRepository tierRepository;

  @Transactional
  public void createTier(Create request) {
    tierRepository
        .findByName(request.getName())
        .ifPresent(
            tier -> {
              throw new UserException(UserErrorCode.TIER_CONFLICT);
            });
    tierRepository.save(Tier.create(request));
  }

  public List<TierResponse.Get> getTierList() {
    return tierRepository.findAll().stream().map(TierResponse.Get::of).toList();
  }

  @Transactional
  public void updateTier(Long tierId, TierRequest.Update request) {
    Tier tier = tierRepository
        .findById(tierId)
        .orElseThrow(() -> new UserException(UserErrorCode.TIER_NOT_FOUND));
    tier.update(request);
  }

  @Transactional
  public void deleteTier(Long tierId) {
    Tier tier = tierRepository
        .findById(tierId)
        .orElseThrow(() -> new UserException(UserErrorCode.TIER_NOT_FOUND));
    tierRepository.delete(tier);
  }

}
