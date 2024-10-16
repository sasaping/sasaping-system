package com.sparta.user.infrastructure.repository;

import com.sparta.user.domain.model.User;
import com.sparta.user.domain.repository.UserRepository;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

  private final JpaUserRepository jpaUserRepository;

  @Override
  public User save(User user) {
    return jpaUserRepository.save(user);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return jpaUserRepository.findByUsername(username);
  }

  @Override
  public Optional<User> findById(Long userId) {
    return jpaUserRepository.findById(userId);
  }

  @Override
  public Collection<User> findAllByIsDeletedFalse() {
    return jpaUserRepository.findAllByIsDeletedFalse();
  }

}
