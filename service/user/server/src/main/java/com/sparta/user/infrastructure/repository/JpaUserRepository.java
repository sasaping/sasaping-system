package com.sparta.user.infrastructure.repository;

import com.sparta.user.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);
}
