package com.sparta.user.domain.repository;

import com.sparta.user.domain.model.Tier;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface TierRepository {

  Tier save(Tier tier);

  Optional<Tier> findByName(String name);

}
