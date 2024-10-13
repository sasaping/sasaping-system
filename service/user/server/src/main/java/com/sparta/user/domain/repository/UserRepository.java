package com.sparta.user.domain.repository;

import com.sparta.user.domain.model.User;
import java.util.Collection;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

  User save(User user);

  Optional<User> findByUsername(String username);

  Optional<User> findById(Long userId);

  Collection<User> findAll();

}
