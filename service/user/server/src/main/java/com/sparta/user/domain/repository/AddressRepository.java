package com.sparta.user.domain.repository;

import com.sparta.user.domain.model.Address;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository {

  Address save(Address address);

  Optional<Address> findById(Long addressId);

}
