package edu.awieclawski.repositories;

import edu.awieclawski.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByCountryAndCity(String country, String city);

    List<Address> findByVerificationKey(String verificationKey);

}
