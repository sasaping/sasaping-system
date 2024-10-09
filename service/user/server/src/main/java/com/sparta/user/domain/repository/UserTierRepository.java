package com.sparta.user.domain.repository;

import com.sparta.user.domain.model.UserTier;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTierRepository {

  UserTier save(UserTier userTier);

}
