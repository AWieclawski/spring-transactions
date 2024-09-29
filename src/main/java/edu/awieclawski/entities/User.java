package edu.awieclawski.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@SuperBuilder
@EqualsAndHashCode(callSuper = true, of = {"firstName", "lastName"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true, of = {"firstName", "lastName"})
@Table(name = User.TABLE_NAME)
public class User extends BaseEntity {

    public static final String TABLE_NAME = "users";

    @Column(name = "login", unique = true)
    private String login;

    @Setter
    @Column(name = "first_name")
    private String firstName;

    @Setter
    @Column(name = "last_name")
    private String lastName;

    @Setter
    @Embedded
    private Contact contact;

    @Setter
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @Override
    List<String> getVerificationFields() {
        return List.of("firstName", "lastName", "login");
    }
}


