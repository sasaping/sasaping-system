package com.sparta.user.infrastructure.repository;

import com.sparta.user.domain.model.Tier;
import com.sparta.user.domain.repository.TierRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TierRepositoryImpl implements TierRepository {

  private final JpaTierRepository jpaTierRepository;

  @Override
  public Tier save(Tier tier) {
    return jpaTierRepository.save(tier);
  }

  @Override
  public Optional<Tier> findByName(String name) {
    return jpaTierRepository.findByName(name);
  }

  @Override
  public List<Tier> findAll() {
    return jpaTierRepository.findAll();
  }

  @Override
  public Optional<Tier> findById(Long tierId) {
    return jpaTierRepository.findById(tierId);
  }

  @Override
  public void delete(Tier tier) {
    jpaTierRepository.delete(tier);
  }

}
