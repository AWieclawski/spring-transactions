package edu.awieclawski.mappers;

import edu.awieclawski.dtos.AddressDto;
import edu.awieclawski.entities.Address;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressMapper {

    public static void update(AddressDto source, Address target) {
        target.setCity(source.getCity());
        target.setCountry(source.getCountry());
        target.setPostalCode(source.getPostalCode());
        target.setStreetLocal(source.getStreetLocal());
    }

    public static Address toEntity(AddressDto dto) {
        return Address.builder()
                .city(dto.getCity())
                .country(dto.getCountry())
                .postalCode(dto.getPostalCode())
                .streetLocal(dto.getStreetLocal())
                .build();
    }

    public static AddressDto toDto(Address entity) {
        return entity != null
                ? AddressDto.builder()
                .city(entity.getCity())
                .country(entity.getCountry())
                .postalCode(entity.getPostalCode())
                .streetLocal(entity.getStreetLocal())
                .build()
                : null;
    }

}
