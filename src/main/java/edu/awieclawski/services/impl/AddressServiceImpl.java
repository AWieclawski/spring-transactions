package edu.awieclawski.services.impl;

import edu.awieclawski.dtos.AddressDto;
import edu.awieclawski.entities.Address;
import edu.awieclawski.entities.User;
import edu.awieclawski.exceptions.EntityNotFoundException;
import edu.awieclawski.mappers.AddressMapper;
import edu.awieclawski.repositories.AddressRepository;
import edu.awieclawski.services.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class AddressServiceImpl extends TransactionalBase<Address> implements AddressService {

    public AddressServiceImpl(AddressRepository addressRepository) {
        super(addressRepository);
    }

    @Override
    public AddressRepository getBaseRepository() {
        return ((AddressRepository) baseRepository);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressDto saveAddress(AddressDto addressDto) {
        Address address = AddressMapper.toEntity(addressDto);
        assignTransactionToEntity(address);
        Address saved = saveNewAddress(address);
        return AddressMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDto getAddress(Long id) {
        Address found = getBaseRepository().findById(id).orElseThrow(() -> new EntityNotFoundException("Address not found! id=" + id));
        return AddressMapper.toDto(found);
    }

    @Override
    public void assignAddressToUser(User user) {
        AtomicReference<Address> address = new AtomicReference<>(user.getAddress());
        String verKey = address.get().buildVerificationKey();
        getBaseRepository().findByVerificationKey(verKey).stream()
                .findFirst().ifPresentOrElse(address::set, () -> saveNewAddress(address.get()));
        user.setAddress(address.get());
    }


    private Address saveNewAddress(Address address) {
        try {
            return saveNewEntity(address);
        } catch (SQLException e) {
            log.error("Address save failed! {} ", e.getMessage());
        }
        return null;
    }

}
