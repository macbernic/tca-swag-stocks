package io.github.tca.stocks.web.rest;
import io.github.tca.stocks.domain.StockOrder;
import io.github.tca.stocks.repository.StockOrderRepository;
import io.github.tca.stocks.web.rest.errors.BadRequestAlertException;
import io.github.tca.stocks.web.rest.util.HeaderUtil;
import io.github.tca.stocks.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing StockOrder.
 */
@RestController
@RequestMapping("/api")
public class StockOrderResource {

    private final Logger log = LoggerFactory.getLogger(StockOrderResource.class);

    private static final String ENTITY_NAME = "stockOrder";

    private final StockOrderRepository stockOrderRepository;

    public StockOrderResource(StockOrderRepository stockOrderRepository) {
        this.stockOrderRepository = stockOrderRepository;
    }

    /**
     * POST  /stock-orders : Create a new stockOrder.
     *
     * @param stockOrder the stockOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOrder, or with status 400 (Bad Request) if the stockOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-orders")
    public ResponseEntity<StockOrder> createStockOrder(@Valid @RequestBody StockOrder stockOrder) throws URISyntaxException {
        log.debug("REST request to save StockOrder : {}", stockOrder);
        if (stockOrder.getId() != null) {
            throw new BadRequestAlertException("A new stockOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockOrder result = stockOrderRepository.save(stockOrder);
        return ResponseEntity.created(new URI("/api/stock-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-orders : Updates an existing stockOrder.
     *
     * @param stockOrder the stockOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOrder,
     * or with status 400 (Bad Request) if the stockOrder is not valid,
     * or with status 500 (Internal Server Error) if the stockOrder couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-orders")
    public ResponseEntity<StockOrder> updateStockOrder(@Valid @RequestBody StockOrder stockOrder) throws URISyntaxException {
        log.debug("REST request to update StockOrder : {}", stockOrder);
        if (stockOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockOrder result = stockOrderRepository.save(stockOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-orders : get all the stockOrders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockOrders in body
     */
    @GetMapping("/stock-orders")
    public ResponseEntity<List<StockOrder>> getAllStockOrders(Pageable pageable) {
        log.debug("REST request to get a page of StockOrders");
        Page<StockOrder> page = stockOrderRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-orders");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /stock-orders/:id : get the "id" stockOrder.
     *
     * @param id the id of the stockOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOrder, or with status 404 (Not Found)
     */
    @GetMapping("/stock-orders/{id}")
    public ResponseEntity<StockOrder> getStockOrder(@PathVariable Long id) {
        log.debug("REST request to get StockOrder : {}", id);
        Optional<StockOrder> stockOrder = stockOrderRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(stockOrder);
    }

    /**
     * DELETE  /stock-orders/:id : delete the "id" stockOrder.
     *
     * @param id the id of the stockOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-orders/{id}")
    public ResponseEntity<Void> deleteStockOrder(@PathVariable Long id) {
        log.debug("REST request to delete StockOrder : {}", id);
        stockOrderRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
