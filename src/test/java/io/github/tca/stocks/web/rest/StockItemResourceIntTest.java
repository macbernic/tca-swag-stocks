package io.github.tca.stocks.web.rest;

import io.github.tca.stocks.TcaSwagStocksApp;

import io.github.tca.stocks.domain.StockItem;
import io.github.tca.stocks.repository.StockItemRepository;
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

/**
 * Test class for the StockItemResource REST controller.
 *
 * @see StockItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TcaSwagStocksApp.class)
public class StockItemResourceIntTest {

    private static final String DEFAULT_CUSTOM_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_TEXT = "BBBBBBBBBB";

    private static final Float DEFAULT_APPLIED_PRICE = 1F;
    private static final Float UPDATED_APPLIED_PRICE = 2F;

    private static final Boolean DEFAULT_FROM_STOCK = false;
    private static final Boolean UPDATED_FROM_STOCK = true;

    @Autowired
    private StockItemRepository stockItemRepository;

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

    private MockMvc restStockItemMockMvc;

    private StockItem stockItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockItemResource stockItemResource = new StockItemResource(stockItemRepository);
        this.restStockItemMockMvc = MockMvcBuilders.standaloneSetup(stockItemResource)
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
    public static StockItem createEntity(EntityManager em) {
        StockItem stockItem = new StockItem()
            .customText(DEFAULT_CUSTOM_TEXT)
            .appliedPrice(DEFAULT_APPLIED_PRICE)
            .fromStock(DEFAULT_FROM_STOCK);
        return stockItem;
    }

    @Before
    public void initTest() {
        stockItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockItem() throws Exception {
        int databaseSizeBeforeCreate = stockItemRepository.findAll().size();

        // Create the StockItem
        restStockItemMockMvc.perform(post("/api/stock-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockItem)))
            .andExpect(status().isCreated());

        // Validate the StockItem in the database
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeCreate + 1);
        StockItem testStockItem = stockItemList.get(stockItemList.size() - 1);
        assertThat(testStockItem.getCustomText()).isEqualTo(DEFAULT_CUSTOM_TEXT);
        assertThat(testStockItem.getAppliedPrice()).isEqualTo(DEFAULT_APPLIED_PRICE);
        assertThat(testStockItem.isFromStock()).isEqualTo(DEFAULT_FROM_STOCK);
    }

    @Test
    @Transactional
    public void createStockItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockItemRepository.findAll().size();

        // Create the StockItem with an existing ID
        stockItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockItemMockMvc.perform(post("/api/stock-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockItem)))
            .andExpect(status().isBadRequest());

        // Validate the StockItem in the database
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAppliedPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockItemRepository.findAll().size();
        // set the field null
        stockItem.setAppliedPrice(null);

        // Create the StockItem, which fails.

        restStockItemMockMvc.perform(post("/api/stock-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockItem)))
            .andExpect(status().isBadRequest());

        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockItems() throws Exception {
        // Initialize the database
        stockItemRepository.saveAndFlush(stockItem);

        // Get all the stockItemList
        restStockItemMockMvc.perform(get("/api/stock-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].customText").value(hasItem(DEFAULT_CUSTOM_TEXT.toString())))
            .andExpect(jsonPath("$.[*].appliedPrice").value(hasItem(DEFAULT_APPLIED_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].fromStock").value(hasItem(DEFAULT_FROM_STOCK.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getStockItem() throws Exception {
        // Initialize the database
        stockItemRepository.saveAndFlush(stockItem);

        // Get the stockItem
        restStockItemMockMvc.perform(get("/api/stock-items/{id}", stockItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockItem.getId().intValue()))
            .andExpect(jsonPath("$.customText").value(DEFAULT_CUSTOM_TEXT.toString()))
            .andExpect(jsonPath("$.appliedPrice").value(DEFAULT_APPLIED_PRICE.doubleValue()))
            .andExpect(jsonPath("$.fromStock").value(DEFAULT_FROM_STOCK.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStockItem() throws Exception {
        // Get the stockItem
        restStockItemMockMvc.perform(get("/api/stock-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockItem() throws Exception {
        // Initialize the database
        stockItemRepository.saveAndFlush(stockItem);

        int databaseSizeBeforeUpdate = stockItemRepository.findAll().size();

        // Update the stockItem
        StockItem updatedStockItem = stockItemRepository.findById(stockItem.getId()).get();
        // Disconnect from session so that the updates on updatedStockItem are not directly saved in db
        em.detach(updatedStockItem);
        updatedStockItem
            .customText(UPDATED_CUSTOM_TEXT)
            .appliedPrice(UPDATED_APPLIED_PRICE)
            .fromStock(UPDATED_FROM_STOCK);

        restStockItemMockMvc.perform(put("/api/stock-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStockItem)))
            .andExpect(status().isOk());

        // Validate the StockItem in the database
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeUpdate);
        StockItem testStockItem = stockItemList.get(stockItemList.size() - 1);
        assertThat(testStockItem.getCustomText()).isEqualTo(UPDATED_CUSTOM_TEXT);
        assertThat(testStockItem.getAppliedPrice()).isEqualTo(UPDATED_APPLIED_PRICE);
        assertThat(testStockItem.isFromStock()).isEqualTo(UPDATED_FROM_STOCK);
    }

    @Test
    @Transactional
    public void updateNonExistingStockItem() throws Exception {
        int databaseSizeBeforeUpdate = stockItemRepository.findAll().size();

        // Create the StockItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockItemMockMvc.perform(put("/api/stock-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockItem)))
            .andExpect(status().isBadRequest());

        // Validate the StockItem in the database
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStockItem() throws Exception {
        // Initialize the database
        stockItemRepository.saveAndFlush(stockItem);

        int databaseSizeBeforeDelete = stockItemRepository.findAll().size();

        // Delete the stockItem
        restStockItemMockMvc.perform(delete("/api/stock-items/{id}", stockItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockItem> stockItemList = stockItemRepository.findAll();
        assertThat(stockItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockItem.class);
        StockItem stockItem1 = new StockItem();
        stockItem1.setId(1L);
        StockItem stockItem2 = new StockItem();
        stockItem2.setId(stockItem1.getId());
        assertThat(stockItem1).isEqualTo(stockItem2);
        stockItem2.setId(2L);
        assertThat(stockItem1).isNotEqualTo(stockItem2);
        stockItem1.setId(null);
        assertThat(stockItem1).isNotEqualTo(stockItem2);
    }
}
