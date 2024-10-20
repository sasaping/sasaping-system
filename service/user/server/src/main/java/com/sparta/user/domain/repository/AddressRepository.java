package com.sparta.user.domain.repository;

import com.sparta.user.domain.model.Address;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository {

  Address save(Address address);

  Optional<Address> findById(Long addressId);

  List<Address> findAllByUserId(Long userId);

  List<Address> findAll();

  void delete(Address address);

}
