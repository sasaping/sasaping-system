package com.sparta.user.domain.repository;

import com.sparta.user.domain.model.Address;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository {

  Address save(Address address);

}
