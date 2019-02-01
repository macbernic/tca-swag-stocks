package io.github.tca.stocks.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A StockItem.
 */
@Entity
@Table(name = "stock_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockItem implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "custom_text")
    private String customText;

    @NotNull
    @Column(name = "applied_price", nullable = false)
    private Float appliedPrice;

    @Column(name = "from_stock")
    private Boolean fromStock;

    @ManyToOne
    @JsonIgnoreProperties("stockItems")
    private Product product;

    @ManyToOne
    @JsonIgnoreProperties("stockItems")
    private ProductSku productSku;

    @ManyToOne
    @JsonIgnoreProperties("items")
    private StockOrder memberOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomText() {
        return customText;
    }

    public StockItem customText(String customText) {
        this.customText = customText;
        return this;
    }

    public void setCustomText(String customText) {
        this.customText = customText;
    }

    public Float getAppliedPrice() {
        return appliedPrice;
    }

    public StockItem appliedPrice(Float appliedPrice) {
        this.appliedPrice = appliedPrice;
        return this;
    }

    public void setAppliedPrice(Float appliedPrice) {
        this.appliedPrice = appliedPrice;
    }

    public Boolean isFromStock() {
        return fromStock;
    }

    public StockItem fromStock(Boolean fromStock) {
        this.fromStock = fromStock;
        return this;
    }

    public void setFromStock(Boolean fromStock) {
        this.fromStock = fromStock;
    }

    public Product getProduct() {
        return product;
    }

    public StockItem product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductSku getProductSku() {
        return productSku;
    }

    public StockItem productSku(ProductSku productSku) {
        this.productSku = productSku;
        return this;
    }

    public void setProductSku(ProductSku productSku) {
        this.productSku = productSku;
    }

    public StockOrder getMemberOrder() {
        return memberOrder;
    }

    public StockItem memberOrder(StockOrder stockOrder) {
        this.memberOrder = stockOrder;
        return this;
    }

    public void setMemberOrder(StockOrder stockOrder) {
        this.memberOrder = stockOrder;
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
        StockItem stockItem = (StockItem) o;
        if (stockItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockItem{" +
            "id=" + getId() +
            ", customText='" + getCustomText() + "'" +
            ", appliedPrice=" + getAppliedPrice() +
            ", fromStock='" + isFromStock() + "'" +
            "}";
    }
}
