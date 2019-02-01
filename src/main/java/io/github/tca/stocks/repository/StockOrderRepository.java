package io.github.tca.stocks.repository;

import io.github.tca.stocks.domain.StockOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StockOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockOrderRepository extends JpaRepository<StockOrder, Long> {

}
