package edu.awieclawski.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String login;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private AddressDto address; // UserDto allowed to update Address

}
