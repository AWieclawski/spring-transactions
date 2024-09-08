package edu.awieclawski.services;

import edu.awieclawski.dtos.AddressDto;
import edu.awieclawski.entities.User;

public interface AddressService {

    AddressDto saveAddress(AddressDto address);

    AddressDto getAddress(Long id);

    void assignAddressToUser(User user);

}
