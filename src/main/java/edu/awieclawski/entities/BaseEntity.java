package edu.awieclawski.entities;


import edu.awieclawski.utils.ReflectionUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
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

    @Column(name = "verification_key")
    protected String verificationKey;

    @Version
    private Long version;

    @PrePersist
    private void create() {
        this.createdAt = this.createdAt == null ? Instant.now() : this.createdAt;
        this.verificationKey = buildVerificationKey();
    }

    @PreUpdate
    private void update() {
        this.updatedAt = Instant.now();
        this.verificationKey = buildVerificationKey();
    }

    public String buildVerificationKey() {
        StringBuilder result = new StringBuilder();
        if (CollectionUtils.isNotEmpty(getVerificationFields())) {
            for (String fieldName : getVerificationFields()) {
                result.append(ReflectionUtils.getCleanFieldValue(this, fieldName));
            }
        }
        return result.length() > 0 ? result.toString() : null;
    }

    protected Instant getActualizationDate() {
        return this.updatedAt != null ? this.updatedAt : this.createdAt;
    }

    abstract List<String> getVerificationFields();
}
