package io.github.tca.stocks.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import io.github.tca.stocks.domain.enumeration.GenderType;

import io.github.tca.stocks.domain.enumeration.ProductSize;

/**
 * A ProductSku.
 */
@Entity
@Table(name = "product_sku")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductSku implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private GenderType gender;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_size", nullable = false)
    private ProductSize size;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GenderType getGender() {
        return gender;
    }

    public ProductSku gender(GenderType gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public ProductSize getSize() {
        return size;
    }

    public ProductSku size(ProductSize size) {
        this.size = size;
        return this;
    }

    public void setSize(ProductSize size) {
        this.size = size;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductSku productSku = (ProductSku) o;
        if (productSku.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productSku.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductSku{" +
            "id=" + getId() +
            ", gender='" + getGender() + "'" +
            ", size='" + getSize() + "'" +
            "}";
    }
}
