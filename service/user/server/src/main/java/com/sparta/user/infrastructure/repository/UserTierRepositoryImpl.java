package com.sparta.user.infrastructure.repository;

import com.sparta.user.domain.model.UserTier;
import com.sparta.user.domain.repository.UserTierRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserTierRepositoryImpl implements UserTierRepository {

  private final JpaUserTierRepository jpaUserTierRepository;

  @Override
  public UserTier save(UserTier userTier) {
    return jpaUserTierRepository.save(userTier);
  }

  @Override
  public Optional<UserTier> findByUserId(Long userId) {
    return jpaUserTierRepository.findByUserId(userId);
  }

  @Override
  public Page<UserTier> findAll(Pageable pageable) {
    return jpaUserTierRepository.findAll(pageable);
  }

}
