package edu.awieclawski.entities;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.experimental.SuperBuilder;

@Setter
@Getter
@Entity
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@Table(name = User.TABLE_NAME)
public class User extends BaseEntity {
    public static final String TABLE_NAME = "users";
    private String firstName;
    private String lastName;
}


