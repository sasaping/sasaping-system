package com.sparta.user.infrastructure.repository;

import com.sparta.user.domain.model.Tier;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTierRepository extends JpaRepository<Tier, Long> {

  Optional<Tier> findByName(String name);

}
