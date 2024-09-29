package edu.awieclawski.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(of = {"city", "country"}, callSuper = true)
@Table(name = Address.TABLE_NAME)
public class Address extends BaseEntity {
    public static final String TABLE_NAME = "addresses";

    private String city;

    private String country;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "street_local")
    private String streetLocal;

    @OneToMany(mappedBy = "address")
    private List<User> users;

    @Override
    List<String> getVerificationFields() {
        return List.of("country", "city", "postalCode", "streetLocal");
    }
}
