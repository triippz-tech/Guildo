package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.GuildPollItem;
import com.triippztech.guildo.repository.GuildPollItemRepository;
import com.triippztech.guildo.service.GuildPollItemService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.dto.GuildPollItemCriteria;
import com.triippztech.guildo.service.GuildPollItemQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.triippztech.guildo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link GuildPollItemResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class GuildPollItemResourceIT {

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_VOTES = 1;
    private static final Integer UPDATED_VOTES = 2;
    private static final Integer SMALLER_VOTES = 1 - 1;

    @Autowired
    private GuildPollItemRepository guildPollItemRepository;

    @Autowired
    private GuildPollItemService guildPollItemService;

    @Autowired
    private GuildPollItemQueryService guildPollItemQueryService;

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

    private MockMvc restGuildPollItemMockMvc;

    private GuildPollItem guildPollItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GuildPollItemResource guildPollItemResource = new GuildPollItemResource(guildPollItemService, guildPollItemQueryService);
        this.restGuildPollItemMockMvc = MockMvcBuilders.standaloneSetup(guildPollItemResource)
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
    public static GuildPollItem createEntity(EntityManager em) {
        GuildPollItem guildPollItem = new GuildPollItem()
            .itemName(DEFAULT_ITEM_NAME)
            .votes(DEFAULT_VOTES);
        return guildPollItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GuildPollItem createUpdatedEntity(EntityManager em) {
        GuildPollItem guildPollItem = new GuildPollItem()
            .itemName(UPDATED_ITEM_NAME)
            .votes(UPDATED_VOTES);
        return guildPollItem;
    }

    @BeforeEach
    public void initTest() {
        guildPollItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createGuildPollItem() throws Exception {
        int databaseSizeBeforeCreate = guildPollItemRepository.findAll().size();

        // Create the GuildPollItem
        restGuildPollItemMockMvc.perform(post("/api/guild-poll-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildPollItem)))
            .andExpect(status().isCreated());

        // Validate the GuildPollItem in the database
        List<GuildPollItem> guildPollItemList = guildPollItemRepository.findAll();
        assertThat(guildPollItemList).hasSize(databaseSizeBeforeCreate + 1);
        GuildPollItem testGuildPollItem = guildPollItemList.get(guildPollItemList.size() - 1);
        assertThat(testGuildPollItem.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
        assertThat(testGuildPollItem.getVotes()).isEqualTo(DEFAULT_VOTES);
    }

    @Test
    @Transactional
    public void createGuildPollItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = guildPollItemRepository.findAll().size();

        // Create the GuildPollItem with an existing ID
        guildPollItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuildPollItemMockMvc.perform(post("/api/guild-poll-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildPollItem)))
            .andExpect(status().isBadRequest());

        // Validate the GuildPollItem in the database
        List<GuildPollItem> guildPollItemList = guildPollItemRepository.findAll();
        assertThat(guildPollItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkItemNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildPollItemRepository.findAll().size();
        // set the field null
        guildPollItem.setItemName(null);

        // Create the GuildPollItem, which fails.

        restGuildPollItemMockMvc.perform(post("/api/guild-poll-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildPollItem)))
            .andExpect(status().isBadRequest());

        List<GuildPollItem> guildPollItemList = guildPollItemRepository.findAll();
        assertThat(guildPollItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVotesIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildPollItemRepository.findAll().size();
        // set the field null
        guildPollItem.setVotes(null);

        // Create the GuildPollItem, which fails.

        restGuildPollItemMockMvc.perform(post("/api/guild-poll-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildPollItem)))
            .andExpect(status().isBadRequest());

        List<GuildPollItem> guildPollItemList = guildPollItemRepository.findAll();
        assertThat(guildPollItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGuildPollItems() throws Exception {
        // Initialize the database
        guildPollItemRepository.saveAndFlush(guildPollItem);

        // Get all the guildPollItemList
        restGuildPollItemMockMvc.perform(get("/api/guild-poll-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildPollItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME.toString())))
            .andExpect(jsonPath("$.[*].votes").value(hasItem(DEFAULT_VOTES)));
    }
    
    @Test
    @Transactional
    public void getGuildPollItem() throws Exception {
        // Initialize the database
        guildPollItemRepository.saveAndFlush(guildPollItem);

        // Get the guildPollItem
        restGuildPollItemMockMvc.perform(get("/api/guild-poll-items/{id}", guildPollItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(guildPollItem.getId().intValue()))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME.toString()))
            .andExpect(jsonPath("$.votes").value(DEFAULT_VOTES));
    }

    @Test
    @Transactional
    public void getAllGuildPollItemsByItemNameIsEqualToSomething() throws Exception {
        // Initialize the database
        guildPollItemRepository.saveAndFlush(guildPollItem);

        // Get all the guildPollItemList where itemName equals to DEFAULT_ITEM_NAME
        defaultGuildPollItemShouldBeFound("itemName.equals=" + DEFAULT_ITEM_NAME);

        // Get all the guildPollItemList where itemName equals to UPDATED_ITEM_NAME
        defaultGuildPollItemShouldNotBeFound("itemName.equals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllGuildPollItemsByItemNameIsInShouldWork() throws Exception {
        // Initialize the database
        guildPollItemRepository.saveAndFlush(guildPollItem);

        // Get all the guildPollItemList where itemName in DEFAULT_ITEM_NAME or UPDATED_ITEM_NAME
        defaultGuildPollItemShouldBeFound("itemName.in=" + DEFAULT_ITEM_NAME + "," + UPDATED_ITEM_NAME);

        // Get all the guildPollItemList where itemName equals to UPDATED_ITEM_NAME
        defaultGuildPollItemShouldNotBeFound("itemName.in=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllGuildPollItemsByItemNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildPollItemRepository.saveAndFlush(guildPollItem);

        // Get all the guildPollItemList where itemName is not null
        defaultGuildPollItemShouldBeFound("itemName.specified=true");

        // Get all the guildPollItemList where itemName is null
        defaultGuildPollItemShouldNotBeFound("itemName.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildPollItemsByVotesIsEqualToSomething() throws Exception {
        // Initialize the database
        guildPollItemRepository.saveAndFlush(guildPollItem);

        // Get all the guildPollItemList where votes equals to DEFAULT_VOTES
        defaultGuildPollItemShouldBeFound("votes.equals=" + DEFAULT_VOTES);

        // Get all the guildPollItemList where votes equals to UPDATED_VOTES
        defaultGuildPollItemShouldNotBeFound("votes.equals=" + UPDATED_VOTES);
    }

    @Test
    @Transactional
    public void getAllGuildPollItemsByVotesIsInShouldWork() throws Exception {
        // Initialize the database
        guildPollItemRepository.saveAndFlush(guildPollItem);

        // Get all the guildPollItemList where votes in DEFAULT_VOTES or UPDATED_VOTES
        defaultGuildPollItemShouldBeFound("votes.in=" + DEFAULT_VOTES + "," + UPDATED_VOTES);

        // Get all the guildPollItemList where votes equals to UPDATED_VOTES
        defaultGuildPollItemShouldNotBeFound("votes.in=" + UPDATED_VOTES);
    }

    @Test
    @Transactional
    public void getAllGuildPollItemsByVotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildPollItemRepository.saveAndFlush(guildPollItem);

        // Get all the guildPollItemList where votes is not null
        defaultGuildPollItemShouldBeFound("votes.specified=true");

        // Get all the guildPollItemList where votes is null
        defaultGuildPollItemShouldNotBeFound("votes.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildPollItemsByVotesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildPollItemRepository.saveAndFlush(guildPollItem);

        // Get all the guildPollItemList where votes is greater than or equal to DEFAULT_VOTES
        defaultGuildPollItemShouldBeFound("votes.greaterThanOrEqual=" + DEFAULT_VOTES);

        // Get all the guildPollItemList where votes is greater than or equal to UPDATED_VOTES
        defaultGuildPollItemShouldNotBeFound("votes.greaterThanOrEqual=" + UPDATED_VOTES);
    }

    @Test
    @Transactional
    public void getAllGuildPollItemsByVotesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildPollItemRepository.saveAndFlush(guildPollItem);

        // Get all the guildPollItemList where votes is less than or equal to DEFAULT_VOTES
        defaultGuildPollItemShouldBeFound("votes.lessThanOrEqual=" + DEFAULT_VOTES);

        // Get all the guildPollItemList where votes is less than or equal to SMALLER_VOTES
        defaultGuildPollItemShouldNotBeFound("votes.lessThanOrEqual=" + SMALLER_VOTES);
    }

    @Test
    @Transactional
    public void getAllGuildPollItemsByVotesIsLessThanSomething() throws Exception {
        // Initialize the database
        guildPollItemRepository.saveAndFlush(guildPollItem);

        // Get all the guildPollItemList where votes is less than DEFAULT_VOTES
        defaultGuildPollItemShouldNotBeFound("votes.lessThan=" + DEFAULT_VOTES);

        // Get all the guildPollItemList where votes is less than UPDATED_VOTES
        defaultGuildPollItemShouldBeFound("votes.lessThan=" + UPDATED_VOTES);
    }

    @Test
    @Transactional
    public void getAllGuildPollItemsByVotesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        guildPollItemRepository.saveAndFlush(guildPollItem);

        // Get all the guildPollItemList where votes is greater than DEFAULT_VOTES
        defaultGuildPollItemShouldNotBeFound("votes.greaterThan=" + DEFAULT_VOTES);

        // Get all the guildPollItemList where votes is greater than SMALLER_VOTES
        defaultGuildPollItemShouldBeFound("votes.greaterThan=" + SMALLER_VOTES);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGuildPollItemShouldBeFound(String filter) throws Exception {
        restGuildPollItemMockMvc.perform(get("/api/guild-poll-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildPollItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].votes").value(hasItem(DEFAULT_VOTES)));

        // Check, that the count call also returns 1
        restGuildPollItemMockMvc.perform(get("/api/guild-poll-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGuildPollItemShouldNotBeFound(String filter) throws Exception {
        restGuildPollItemMockMvc.perform(get("/api/guild-poll-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGuildPollItemMockMvc.perform(get("/api/guild-poll-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGuildPollItem() throws Exception {
        // Get the guildPollItem
        restGuildPollItemMockMvc.perform(get("/api/guild-poll-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGuildPollItem() throws Exception {
        // Initialize the database
        guildPollItemService.save(guildPollItem);

        int databaseSizeBeforeUpdate = guildPollItemRepository.findAll().size();

        // Update the guildPollItem
        GuildPollItem updatedGuildPollItem = guildPollItemRepository.findById(guildPollItem.getId()).get();
        // Disconnect from session so that the updates on updatedGuildPollItem are not directly saved in db
        em.detach(updatedGuildPollItem);
        updatedGuildPollItem
            .itemName(UPDATED_ITEM_NAME)
            .votes(UPDATED_VOTES);

        restGuildPollItemMockMvc.perform(put("/api/guild-poll-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGuildPollItem)))
            .andExpect(status().isOk());

        // Validate the GuildPollItem in the database
        List<GuildPollItem> guildPollItemList = guildPollItemRepository.findAll();
        assertThat(guildPollItemList).hasSize(databaseSizeBeforeUpdate);
        GuildPollItem testGuildPollItem = guildPollItemList.get(guildPollItemList.size() - 1);
        assertThat(testGuildPollItem.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
        assertThat(testGuildPollItem.getVotes()).isEqualTo(UPDATED_VOTES);
    }

    @Test
    @Transactional
    public void updateNonExistingGuildPollItem() throws Exception {
        int databaseSizeBeforeUpdate = guildPollItemRepository.findAll().size();

        // Create the GuildPollItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuildPollItemMockMvc.perform(put("/api/guild-poll-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildPollItem)))
            .andExpect(status().isBadRequest());

        // Validate the GuildPollItem in the database
        List<GuildPollItem> guildPollItemList = guildPollItemRepository.findAll();
        assertThat(guildPollItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGuildPollItem() throws Exception {
        // Initialize the database
        guildPollItemService.save(guildPollItem);

        int databaseSizeBeforeDelete = guildPollItemRepository.findAll().size();

        // Delete the guildPollItem
        restGuildPollItemMockMvc.perform(delete("/api/guild-poll-items/{id}", guildPollItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GuildPollItem> guildPollItemList = guildPollItemRepository.findAll();
        assertThat(guildPollItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuildPollItem.class);
        GuildPollItem guildPollItem1 = new GuildPollItem();
        guildPollItem1.setId(1L);
        GuildPollItem guildPollItem2 = new GuildPollItem();
        guildPollItem2.setId(guildPollItem1.getId());
        assertThat(guildPollItem1).isEqualTo(guildPollItem2);
        guildPollItem2.setId(2L);
        assertThat(guildPollItem1).isNotEqualTo(guildPollItem2);
        guildPollItem1.setId(null);
        assertThat(guildPollItem1).isNotEqualTo(guildPollItem2);
    }
}
