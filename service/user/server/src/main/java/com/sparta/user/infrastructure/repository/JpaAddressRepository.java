package com.sparta.user.infrastructure.repository;

import com.sparta.user.domain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAddressRepository extends JpaRepository<Address, Long> {

}
