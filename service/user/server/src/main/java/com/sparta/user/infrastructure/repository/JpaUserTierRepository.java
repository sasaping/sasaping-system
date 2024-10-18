package com.sparta.user.infrastructure.repository;

import com.sparta.user.domain.model.UserTier;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserTierRepository extends JpaRepository<UserTier, Long> {

  Optional<UserTier> findByUserId(Long userId);

}
