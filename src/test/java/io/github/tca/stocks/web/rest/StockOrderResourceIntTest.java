package io.github.tca.stocks.web.rest;

import io.github.tca.stocks.TcaSwagStocksApp;

import io.github.tca.stocks.domain.StockOrder;
import io.github.tca.stocks.repository.StockOrderRepository;
import io.github.tca.stocks.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static io.github.tca.stocks.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.tca.stocks.domain.enumeration.OrderStatus;
/**
 * Test class for the StockOrderResource REST controller.
 *
 * @see StockOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TcaSwagStocksApp.class)
public class StockOrderResourceIntTest {

    private static final String DEFAULT_ORDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FIRST = false;
    private static final Boolean UPDATED_FIRST = true;

    private static final Float DEFAULT_AMOUNT = 1F;
    private static final Float UPDATED_AMOUNT = 2F;

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.DRAFT;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.SUBMITTED;

    @Autowired
    private StockOrderRepository stockOrderRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restStockOrderMockMvc;

    private StockOrder stockOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockOrderResource stockOrderResource = new StockOrderResource(stockOrderRepository);
        this.restStockOrderMockMvc = MockMvcBuilders.standaloneSetup(stockOrderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockOrder createEntity(EntityManager em) {
        StockOrder stockOrder = new StockOrder()
            .orderId(DEFAULT_ORDER_ID)
            .first(DEFAULT_FIRST)
            .amount(DEFAULT_AMOUNT)
            .status(DEFAULT_STATUS);
        return stockOrder;
    }

    @Before
    public void initTest() {
        stockOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOrder() throws Exception {
        int databaseSizeBeforeCreate = stockOrderRepository.findAll().size();

        // Create the StockOrder
        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrder)))
            .andExpect(status().isCreated());

        // Validate the StockOrder in the database
        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeCreate + 1);
        StockOrder testStockOrder = stockOrderList.get(stockOrderList.size() - 1);
        assertThat(testStockOrder.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testStockOrder.isFirst()).isEqualTo(DEFAULT_FIRST);
        assertThat(testStockOrder.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testStockOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createStockOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOrderRepository.findAll().size();

        // Create the StockOrder with an existing ID
        stockOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrder)))
            .andExpect(status().isBadRequest());

        // Validate the StockOrder in the database
        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkOrderIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOrderRepository.findAll().size();
        // set the field null
        stockOrder.setOrderId(null);

        // Create the StockOrder, which fails.

        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrder)))
            .andExpect(status().isBadRequest());

        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOrderRepository.findAll().size();
        // set the field null
        stockOrder.setFirst(null);

        // Create the StockOrder, which fails.

        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrder)))
            .andExpect(status().isBadRequest());

        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOrders() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get all the stockOrderList
        restStockOrderMockMvc.perform(get("/api/stock-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.toString())))
            .andExpect(jsonPath("$.[*].first").value(hasItem(DEFAULT_FIRST.booleanValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getStockOrder() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get the stockOrder
        restStockOrderMockMvc.perform(get("/api/stock-orders/{id}", stockOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.toString()))
            .andExpect(jsonPath("$.first").value(DEFAULT_FIRST.booleanValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOrder() throws Exception {
        // Get the stockOrder
        restStockOrderMockMvc.perform(get("/api/stock-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOrder() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        int databaseSizeBeforeUpdate = stockOrderRepository.findAll().size();

        // Update the stockOrder
        StockOrder updatedStockOrder = stockOrderRepository.findById(stockOrder.getId()).get();
        // Disconnect from session so that the updates on updatedStockOrder are not directly saved in db
        em.detach(updatedStockOrder);
        updatedStockOrder
            .orderId(UPDATED_ORDER_ID)
            .first(UPDATED_FIRST)
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS);

        restStockOrderMockMvc.perform(put("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStockOrder)))
            .andExpect(status().isOk());

        // Validate the StockOrder in the database
        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeUpdate);
        StockOrder testStockOrder = stockOrderList.get(stockOrderList.size() - 1);
        assertThat(testStockOrder.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testStockOrder.isFirst()).isEqualTo(UPDATED_FIRST);
        assertThat(testStockOrder.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testStockOrder.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOrder() throws Exception {
        int databaseSizeBeforeUpdate = stockOrderRepository.findAll().size();

        // Create the StockOrder

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockOrderMockMvc.perform(put("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrder)))
            .andExpect(status().isBadRequest());

        // Validate the StockOrder in the database
        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStockOrder() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        int databaseSizeBeforeDelete = stockOrderRepository.findAll().size();

        // Delete the stockOrder
        restStockOrderMockMvc.perform(delete("/api/stock-orders/{id}", stockOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOrder.class);
        StockOrder stockOrder1 = new StockOrder();
        stockOrder1.setId(1L);
        StockOrder stockOrder2 = new StockOrder();
        stockOrder2.setId(stockOrder1.getId());
        assertThat(stockOrder1).isEqualTo(stockOrder2);
        stockOrder2.setId(2L);
        assertThat(stockOrder1).isNotEqualTo(stockOrder2);
        stockOrder1.setId(null);
        assertThat(stockOrder1).isNotEqualTo(stockOrder2);
    }
}
