package edu.awieclawski.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Setter
@Getter
@SuperBuilder
@Embeddable
@ToString(of = {"email"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Contact {

    @Column(nullable = false)
    private String email;

    private String phone;

}
