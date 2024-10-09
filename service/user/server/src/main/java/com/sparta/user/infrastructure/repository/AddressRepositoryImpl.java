package com.sparta.user.infrastructure.repository;

import com.sparta.user.domain.model.Address;
import com.sparta.user.domain.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AddressRepositoryImpl implements AddressRepository {

  private final JpaAddressRepository jpaAddressRepository;

  @Override
  public Address save(Address address) {
    return jpaAddressRepository.save(address);
  }

}
