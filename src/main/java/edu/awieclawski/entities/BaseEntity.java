package edu.awieclawski.entities;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

/**
 * Common Entity @Id field requires common sequence table `hibernate_sequence`.
 * Any individual identification rule not allowed for all inheriting entities.
 */
@Getter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id"})
public abstract class BaseEntity implements Serializable {

    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "updated_at")
    protected Instant updatedAt;

    @Column(name = "created_at")
    protected Instant createdAt;

    @Setter
    @Column(name = "transaction_name")
    protected String transactionName;

    @Version
    private Long version;

    @PrePersist
    private void create() {
        this.createdAt = this.createdAt == null ? Instant.now() : this.createdAt;
    }

    @PreUpdate
    private void update() {
        this.updatedAt = Instant.now();
    }

    protected Instant getActualizationDate() {
        return this.updatedAt != null ? this.updatedAt : this.createdAt;
    }

    abstract Set<String> getVerificationFields();
}
