package edu.awieclawski.mappers;

import edu.awieclawski.dtos.UserDto;
import edu.awieclawski.entities.Contact;
import edu.awieclawski.entities.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static void update(UserDto source, User target) {
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        assignContact(source, target);
    }

    public static User toEntity(UserDto dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .login(dto.getLogin())
                .contact(toContact(dto))
                .address(AddressMapper.toEntity(dto.getAddress()))
                .build();
    }

    public static UserDto toDto(User entity) {
        Contact contact = entity != null ? entity.getContact() : null;
        if (contact != null ) { // must be some contact
            return UserDto.builder()
                    .firstName(entity.getFirstName())
                    .lastName(entity.getLastName())
                    .login(entity.getLogin())
                    .address(AddressMapper.toDto(entity.getAddress()))
                    .email(entity.getContact() != null ? entity.getContact().getEmail() : null)
                    .phone(entity.getContact() != null ? entity.getContact().getPhone() : null)
                    .build();
        }
        return null;
    }

    public static void assignContact(UserDto source, User target) {
        if (target.getContact() == null) {
            target.setContact(toContact(source));
        } else {
            target.getContact().setEmail(source.getEmail());
            target.getContact().setPhone(source.getPhone());
        }
    }

    public static Contact toContact(UserDto dto) {
        return Contact.builder()
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
    }
}
