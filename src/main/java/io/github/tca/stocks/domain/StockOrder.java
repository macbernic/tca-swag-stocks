package io.github.tca.stocks.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import io.github.tca.stocks.domain.enumeration.OrderStatus;

/**
 * A StockOrder.
 */
@Entity
@Table(name = "stock_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 6)
    @Column(name = "order_id", nullable = false)
    private String orderId;

    @NotNull
    @Column(name = "first", nullable = false)
    private Boolean first;

    @Column(name = "amount")
    private Float amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @OneToMany(mappedBy = "memberOrder")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<StockItem> items = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public StockOrder orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Boolean isFirst() {
        return first;
    }

    public StockOrder first(Boolean first) {
        this.first = first;
        return this;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Float getAmount() {
        return amount;
    }

    public StockOrder amount(Float amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public StockOrder status(OrderStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Set<StockItem> getItems() {
        return items;
    }

    public StockOrder items(Set<StockItem> stockItems) {
        this.items = stockItems;
        return this;
    }

    public StockOrder addItem(StockItem stockItem) {
        this.items.add(stockItem);
        stockItem.setMemberOrder(this);
        return this;
    }

    public StockOrder removeItem(StockItem stockItem) {
        this.items.remove(stockItem);
        stockItem.setMemberOrder(null);
        return this;
    }

    public void setItems(Set<StockItem> stockItems) {
        this.items = stockItems;
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
        StockOrder stockOrder = (StockOrder) o;
        if (stockOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockOrder{" +
            "id=" + getId() +
            ", orderId='" + getOrderId() + "'" +
            ", first='" + isFirst() + "'" +
            ", amount=" + getAmount() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
