package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.ModerationLogItem;
import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.repository.ModerationLogItemRepository;
import com.triippztech.guildo.service.moderation.ModerationLogItemService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.moderation.ModerationLogItemQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.triippztech.guildo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.triippztech.guildo.domain.enumeration.PunishmentType;
/**
 * Integration tests for the {@link ModerationLogItemResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class ModerationLogItemResourceIT {

    private static final Long DEFAULT_CHANNEL_ID = 1L;
    private static final Long UPDATED_CHANNEL_ID = 2L;
    private static final Long SMALLER_CHANNEL_ID = 1L - 1L;

    private static final String DEFAULT_CHANNEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_ISSUED_BY_ID = 1L;
    private static final Long UPDATED_ISSUED_BY_ID = 2L;
    private static final Long SMALLER_ISSUED_BY_ID = 1L - 1L;

    private static final String DEFAULT_ISSUED_BY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ISSUED_BY_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_ISSUED_TO_ID = 1L;
    private static final Long UPDATED_ISSUED_TO_ID = 2L;
    private static final Long SMALLER_ISSUED_TO_ID = 1L - 1L;

    private static final String DEFAULT_ISSUED_TO_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ISSUED_TO_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_TIME = Instant.ofEpochMilli(-1L);

    private static final PunishmentType DEFAULT_MODERATION_ACTION = PunishmentType.NONE;
    private static final PunishmentType UPDATED_MODERATION_ACTION = PunishmentType.KICK;

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;
    private static final Long SMALLER_GUILD_ID = 1L - 1L;

    @Autowired
    private ModerationLogItemRepository moderationLogItemRepository;

    @Autowired
    private ModerationLogItemService moderationLogItemService;

    @Autowired
    private ModerationLogItemQueryService moderationLogItemQueryService;

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

    private MockMvc restModerationLogItemMockMvc;

    private ModerationLogItem moderationLogItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ModerationLogItemResource moderationLogItemResource = new ModerationLogItemResource(moderationLogItemService, moderationLogItemQueryService);
        this.restModerationLogItemMockMvc = MockMvcBuilders.standaloneSetup(moderationLogItemResource)
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
    public static ModerationLogItem createEntity(EntityManager em) {
        ModerationLogItem moderationLogItem = new ModerationLogItem()
            .channelId(DEFAULT_CHANNEL_ID)
            .channelName(DEFAULT_CHANNEL_NAME)
            .issuedById(DEFAULT_ISSUED_BY_ID)
            .issuedByName(DEFAULT_ISSUED_BY_NAME)
            .issuedToId(DEFAULT_ISSUED_TO_ID)
            .issuedToName(DEFAULT_ISSUED_TO_NAME)
            .reason(DEFAULT_REASON)
            .time(DEFAULT_TIME)
            .moderationAction(DEFAULT_MODERATION_ACTION)
            .guildId(DEFAULT_GUILD_ID);
        return moderationLogItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModerationLogItem createUpdatedEntity(EntityManager em) {
        ModerationLogItem moderationLogItem = new ModerationLogItem()
            .channelId(UPDATED_CHANNEL_ID)
            .channelName(UPDATED_CHANNEL_NAME)
            .issuedById(UPDATED_ISSUED_BY_ID)
            .issuedByName(UPDATED_ISSUED_BY_NAME)
            .issuedToId(UPDATED_ISSUED_TO_ID)
            .issuedToName(UPDATED_ISSUED_TO_NAME)
            .reason(UPDATED_REASON)
            .time(UPDATED_TIME)
            .moderationAction(UPDATED_MODERATION_ACTION)
            .guildId(UPDATED_GUILD_ID);
        return moderationLogItem;
    }

    @BeforeEach
    public void initTest() {
        moderationLogItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createModerationLogItem() throws Exception {
        int databaseSizeBeforeCreate = moderationLogItemRepository.findAll().size();

        // Create the ModerationLogItem
        restModerationLogItemMockMvc.perform(post("/api/moderation-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moderationLogItem)))
            .andExpect(status().isCreated());

        // Validate the ModerationLogItem in the database
        List<ModerationLogItem> moderationLogItemList = moderationLogItemRepository.findAll();
        assertThat(moderationLogItemList).hasSize(databaseSizeBeforeCreate + 1);
        ModerationLogItem testModerationLogItem = moderationLogItemList.get(moderationLogItemList.size() - 1);
        assertThat(testModerationLogItem.getChannelId()).isEqualTo(DEFAULT_CHANNEL_ID);
        assertThat(testModerationLogItem.getChannelName()).isEqualTo(DEFAULT_CHANNEL_NAME);
        assertThat(testModerationLogItem.getIssuedById()).isEqualTo(DEFAULT_ISSUED_BY_ID);
        assertThat(testModerationLogItem.getIssuedByName()).isEqualTo(DEFAULT_ISSUED_BY_NAME);
        assertThat(testModerationLogItem.getIssuedToId()).isEqualTo(DEFAULT_ISSUED_TO_ID);
        assertThat(testModerationLogItem.getIssuedToName()).isEqualTo(DEFAULT_ISSUED_TO_NAME);
        assertThat(testModerationLogItem.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testModerationLogItem.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testModerationLogItem.getModerationAction()).isEqualTo(DEFAULT_MODERATION_ACTION);
        assertThat(testModerationLogItem.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
    }

    @Test
    @Transactional
    public void createModerationLogItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = moderationLogItemRepository.findAll().size();

        // Create the ModerationLogItem with an existing ID
        moderationLogItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModerationLogItemMockMvc.perform(post("/api/moderation-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moderationLogItem)))
            .andExpect(status().isBadRequest());

        // Validate the ModerationLogItem in the database
        List<ModerationLogItem> moderationLogItemList = moderationLogItemRepository.findAll();
        assertThat(moderationLogItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkChannelIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = moderationLogItemRepository.findAll().size();
        // set the field null
        moderationLogItem.setChannelId(null);

        // Create the ModerationLogItem, which fails.

        restModerationLogItemMockMvc.perform(post("/api/moderation-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moderationLogItem)))
            .andExpect(status().isBadRequest());

        List<ModerationLogItem> moderationLogItemList = moderationLogItemRepository.findAll();
        assertThat(moderationLogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChannelNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = moderationLogItemRepository.findAll().size();
        // set the field null
        moderationLogItem.setChannelName(null);

        // Create the ModerationLogItem, which fails.

        restModerationLogItemMockMvc.perform(post("/api/moderation-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moderationLogItem)))
            .andExpect(status().isBadRequest());

        List<ModerationLogItem> moderationLogItemList = moderationLogItemRepository.findAll();
        assertThat(moderationLogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIssuedByIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = moderationLogItemRepository.findAll().size();
        // set the field null
        moderationLogItem.setIssuedById(null);

        // Create the ModerationLogItem, which fails.

        restModerationLogItemMockMvc.perform(post("/api/moderation-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moderationLogItem)))
            .andExpect(status().isBadRequest());

        List<ModerationLogItem> moderationLogItemList = moderationLogItemRepository.findAll();
        assertThat(moderationLogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIssuedByNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = moderationLogItemRepository.findAll().size();
        // set the field null
        moderationLogItem.setIssuedByName(null);

        // Create the ModerationLogItem, which fails.

        restModerationLogItemMockMvc.perform(post("/api/moderation-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moderationLogItem)))
            .andExpect(status().isBadRequest());

        List<ModerationLogItem> moderationLogItemList = moderationLogItemRepository.findAll();
        assertThat(moderationLogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIssuedToIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = moderationLogItemRepository.findAll().size();
        // set the field null
        moderationLogItem.setIssuedToId(null);

        // Create the ModerationLogItem, which fails.

        restModerationLogItemMockMvc.perform(post("/api/moderation-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moderationLogItem)))
            .andExpect(status().isBadRequest());

        List<ModerationLogItem> moderationLogItemList = moderationLogItemRepository.findAll();
        assertThat(moderationLogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIssuedToNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = moderationLogItemRepository.findAll().size();
        // set the field null
        moderationLogItem.setIssuedToName(null);

        // Create the ModerationLogItem, which fails.

        restModerationLogItemMockMvc.perform(post("/api/moderation-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moderationLogItem)))
            .andExpect(status().isBadRequest());

        List<ModerationLogItem> moderationLogItemList = moderationLogItemRepository.findAll();
        assertThat(moderationLogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReasonIsRequired() throws Exception {
        int databaseSizeBeforeTest = moderationLogItemRepository.findAll().size();
        // set the field null
        moderationLogItem.setReason(null);

        // Create the ModerationLogItem, which fails.

        restModerationLogItemMockMvc.perform(post("/api/moderation-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moderationLogItem)))
            .andExpect(status().isBadRequest());

        List<ModerationLogItem> moderationLogItemList = moderationLogItemRepository.findAll();
        assertThat(moderationLogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = moderationLogItemRepository.findAll().size();
        // set the field null
        moderationLogItem.setTime(null);

        // Create the ModerationLogItem, which fails.

        restModerationLogItemMockMvc.perform(post("/api/moderation-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moderationLogItem)))
            .andExpect(status().isBadRequest());

        List<ModerationLogItem> moderationLogItemList = moderationLogItemRepository.findAll();
        assertThat(moderationLogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllModerationLogItems() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList
        restModerationLogItemMockMvc.perform(get("/api/moderation-log-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moderationLogItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].channelId").value(hasItem(DEFAULT_CHANNEL_ID.intValue())))
            .andExpect(jsonPath("$.[*].channelName").value(hasItem(DEFAULT_CHANNEL_NAME.toString())))
            .andExpect(jsonPath("$.[*].issuedById").value(hasItem(DEFAULT_ISSUED_BY_ID.intValue())))
            .andExpect(jsonPath("$.[*].issuedByName").value(hasItem(DEFAULT_ISSUED_BY_NAME.toString())))
            .andExpect(jsonPath("$.[*].issuedToId").value(hasItem(DEFAULT_ISSUED_TO_ID.intValue())))
            .andExpect(jsonPath("$.[*].issuedToName").value(hasItem(DEFAULT_ISSUED_TO_NAME.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].moderationAction").value(hasItem(DEFAULT_MODERATION_ACTION.toString())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getModerationLogItem() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get the moderationLogItem
        restModerationLogItemMockMvc.perform(get("/api/moderation-log-items/{id}", moderationLogItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(moderationLogItem.getId().intValue()))
            .andExpect(jsonPath("$.channelId").value(DEFAULT_CHANNEL_ID.intValue()))
            .andExpect(jsonPath("$.channelName").value(DEFAULT_CHANNEL_NAME.toString()))
            .andExpect(jsonPath("$.issuedById").value(DEFAULT_ISSUED_BY_ID.intValue()))
            .andExpect(jsonPath("$.issuedByName").value(DEFAULT_ISSUED_BY_NAME.toString()))
            .andExpect(jsonPath("$.issuedToId").value(DEFAULT_ISSUED_TO_ID.intValue()))
            .andExpect(jsonPath("$.issuedToName").value(DEFAULT_ISSUED_TO_NAME.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.moderationAction").value(DEFAULT_MODERATION_ACTION.toString()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByChannelIdIsEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where channelId equals to DEFAULT_CHANNEL_ID
        defaultModerationLogItemShouldBeFound("channelId.equals=" + DEFAULT_CHANNEL_ID);

        // Get all the moderationLogItemList where channelId equals to UPDATED_CHANNEL_ID
        defaultModerationLogItemShouldNotBeFound("channelId.equals=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByChannelIdIsInShouldWork() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where channelId in DEFAULT_CHANNEL_ID or UPDATED_CHANNEL_ID
        defaultModerationLogItemShouldBeFound("channelId.in=" + DEFAULT_CHANNEL_ID + "," + UPDATED_CHANNEL_ID);

        // Get all the moderationLogItemList where channelId equals to UPDATED_CHANNEL_ID
        defaultModerationLogItemShouldNotBeFound("channelId.in=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByChannelIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where channelId is not null
        defaultModerationLogItemShouldBeFound("channelId.specified=true");

        // Get all the moderationLogItemList where channelId is null
        defaultModerationLogItemShouldNotBeFound("channelId.specified=false");
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByChannelIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where channelId is greater than or equal to DEFAULT_CHANNEL_ID
        defaultModerationLogItemShouldBeFound("channelId.greaterThanOrEqual=" + DEFAULT_CHANNEL_ID);

        // Get all the moderationLogItemList where channelId is greater than or equal to UPDATED_CHANNEL_ID
        defaultModerationLogItemShouldNotBeFound("channelId.greaterThanOrEqual=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByChannelIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where channelId is less than or equal to DEFAULT_CHANNEL_ID
        defaultModerationLogItemShouldBeFound("channelId.lessThanOrEqual=" + DEFAULT_CHANNEL_ID);

        // Get all the moderationLogItemList where channelId is less than or equal to SMALLER_CHANNEL_ID
        defaultModerationLogItemShouldNotBeFound("channelId.lessThanOrEqual=" + SMALLER_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByChannelIdIsLessThanSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where channelId is less than DEFAULT_CHANNEL_ID
        defaultModerationLogItemShouldNotBeFound("channelId.lessThan=" + DEFAULT_CHANNEL_ID);

        // Get all the moderationLogItemList where channelId is less than UPDATED_CHANNEL_ID
        defaultModerationLogItemShouldBeFound("channelId.lessThan=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByChannelIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where channelId is greater than DEFAULT_CHANNEL_ID
        defaultModerationLogItemShouldNotBeFound("channelId.greaterThan=" + DEFAULT_CHANNEL_ID);

        // Get all the moderationLogItemList where channelId is greater than SMALLER_CHANNEL_ID
        defaultModerationLogItemShouldBeFound("channelId.greaterThan=" + SMALLER_CHANNEL_ID);
    }


    @Test
    @Transactional
    public void getAllModerationLogItemsByChannelNameIsEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where channelName equals to DEFAULT_CHANNEL_NAME
        defaultModerationLogItemShouldBeFound("channelName.equals=" + DEFAULT_CHANNEL_NAME);

        // Get all the moderationLogItemList where channelName equals to UPDATED_CHANNEL_NAME
        defaultModerationLogItemShouldNotBeFound("channelName.equals=" + UPDATED_CHANNEL_NAME);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByChannelNameIsInShouldWork() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where channelName in DEFAULT_CHANNEL_NAME or UPDATED_CHANNEL_NAME
        defaultModerationLogItemShouldBeFound("channelName.in=" + DEFAULT_CHANNEL_NAME + "," + UPDATED_CHANNEL_NAME);

        // Get all the moderationLogItemList where channelName equals to UPDATED_CHANNEL_NAME
        defaultModerationLogItemShouldNotBeFound("channelName.in=" + UPDATED_CHANNEL_NAME);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByChannelNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where channelName is not null
        defaultModerationLogItemShouldBeFound("channelName.specified=true");

        // Get all the moderationLogItemList where channelName is null
        defaultModerationLogItemShouldNotBeFound("channelName.specified=false");
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedByIdIsEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedById equals to DEFAULT_ISSUED_BY_ID
        defaultModerationLogItemShouldBeFound("issuedById.equals=" + DEFAULT_ISSUED_BY_ID);

        // Get all the moderationLogItemList where issuedById equals to UPDATED_ISSUED_BY_ID
        defaultModerationLogItemShouldNotBeFound("issuedById.equals=" + UPDATED_ISSUED_BY_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedByIdIsInShouldWork() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedById in DEFAULT_ISSUED_BY_ID or UPDATED_ISSUED_BY_ID
        defaultModerationLogItemShouldBeFound("issuedById.in=" + DEFAULT_ISSUED_BY_ID + "," + UPDATED_ISSUED_BY_ID);

        // Get all the moderationLogItemList where issuedById equals to UPDATED_ISSUED_BY_ID
        defaultModerationLogItemShouldNotBeFound("issuedById.in=" + UPDATED_ISSUED_BY_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedByIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedById is not null
        defaultModerationLogItemShouldBeFound("issuedById.specified=true");

        // Get all the moderationLogItemList where issuedById is null
        defaultModerationLogItemShouldNotBeFound("issuedById.specified=false");
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedByIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedById is greater than or equal to DEFAULT_ISSUED_BY_ID
        defaultModerationLogItemShouldBeFound("issuedById.greaterThanOrEqual=" + DEFAULT_ISSUED_BY_ID);

        // Get all the moderationLogItemList where issuedById is greater than or equal to UPDATED_ISSUED_BY_ID
        defaultModerationLogItemShouldNotBeFound("issuedById.greaterThanOrEqual=" + UPDATED_ISSUED_BY_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedByIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedById is less than or equal to DEFAULT_ISSUED_BY_ID
        defaultModerationLogItemShouldBeFound("issuedById.lessThanOrEqual=" + DEFAULT_ISSUED_BY_ID);

        // Get all the moderationLogItemList where issuedById is less than or equal to SMALLER_ISSUED_BY_ID
        defaultModerationLogItemShouldNotBeFound("issuedById.lessThanOrEqual=" + SMALLER_ISSUED_BY_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedByIdIsLessThanSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedById is less than DEFAULT_ISSUED_BY_ID
        defaultModerationLogItemShouldNotBeFound("issuedById.lessThan=" + DEFAULT_ISSUED_BY_ID);

        // Get all the moderationLogItemList where issuedById is less than UPDATED_ISSUED_BY_ID
        defaultModerationLogItemShouldBeFound("issuedById.lessThan=" + UPDATED_ISSUED_BY_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedByIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedById is greater than DEFAULT_ISSUED_BY_ID
        defaultModerationLogItemShouldNotBeFound("issuedById.greaterThan=" + DEFAULT_ISSUED_BY_ID);

        // Get all the moderationLogItemList where issuedById is greater than SMALLER_ISSUED_BY_ID
        defaultModerationLogItemShouldBeFound("issuedById.greaterThan=" + SMALLER_ISSUED_BY_ID);
    }


    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedByName equals to DEFAULT_ISSUED_BY_NAME
        defaultModerationLogItemShouldBeFound("issuedByName.equals=" + DEFAULT_ISSUED_BY_NAME);

        // Get all the moderationLogItemList where issuedByName equals to UPDATED_ISSUED_BY_NAME
        defaultModerationLogItemShouldNotBeFound("issuedByName.equals=" + UPDATED_ISSUED_BY_NAME);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedByNameIsInShouldWork() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedByName in DEFAULT_ISSUED_BY_NAME or UPDATED_ISSUED_BY_NAME
        defaultModerationLogItemShouldBeFound("issuedByName.in=" + DEFAULT_ISSUED_BY_NAME + "," + UPDATED_ISSUED_BY_NAME);

        // Get all the moderationLogItemList where issuedByName equals to UPDATED_ISSUED_BY_NAME
        defaultModerationLogItemShouldNotBeFound("issuedByName.in=" + UPDATED_ISSUED_BY_NAME);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedByName is not null
        defaultModerationLogItemShouldBeFound("issuedByName.specified=true");

        // Get all the moderationLogItemList where issuedByName is null
        defaultModerationLogItemShouldNotBeFound("issuedByName.specified=false");
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedToIdIsEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedToId equals to DEFAULT_ISSUED_TO_ID
        defaultModerationLogItemShouldBeFound("issuedToId.equals=" + DEFAULT_ISSUED_TO_ID);

        // Get all the moderationLogItemList where issuedToId equals to UPDATED_ISSUED_TO_ID
        defaultModerationLogItemShouldNotBeFound("issuedToId.equals=" + UPDATED_ISSUED_TO_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedToIdIsInShouldWork() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedToId in DEFAULT_ISSUED_TO_ID or UPDATED_ISSUED_TO_ID
        defaultModerationLogItemShouldBeFound("issuedToId.in=" + DEFAULT_ISSUED_TO_ID + "," + UPDATED_ISSUED_TO_ID);

        // Get all the moderationLogItemList where issuedToId equals to UPDATED_ISSUED_TO_ID
        defaultModerationLogItemShouldNotBeFound("issuedToId.in=" + UPDATED_ISSUED_TO_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedToIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedToId is not null
        defaultModerationLogItemShouldBeFound("issuedToId.specified=true");

        // Get all the moderationLogItemList where issuedToId is null
        defaultModerationLogItemShouldNotBeFound("issuedToId.specified=false");
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedToIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedToId is greater than or equal to DEFAULT_ISSUED_TO_ID
        defaultModerationLogItemShouldBeFound("issuedToId.greaterThanOrEqual=" + DEFAULT_ISSUED_TO_ID);

        // Get all the moderationLogItemList where issuedToId is greater than or equal to UPDATED_ISSUED_TO_ID
        defaultModerationLogItemShouldNotBeFound("issuedToId.greaterThanOrEqual=" + UPDATED_ISSUED_TO_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedToIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedToId is less than or equal to DEFAULT_ISSUED_TO_ID
        defaultModerationLogItemShouldBeFound("issuedToId.lessThanOrEqual=" + DEFAULT_ISSUED_TO_ID);

        // Get all the moderationLogItemList where issuedToId is less than or equal to SMALLER_ISSUED_TO_ID
        defaultModerationLogItemShouldNotBeFound("issuedToId.lessThanOrEqual=" + SMALLER_ISSUED_TO_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedToIdIsLessThanSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedToId is less than DEFAULT_ISSUED_TO_ID
        defaultModerationLogItemShouldNotBeFound("issuedToId.lessThan=" + DEFAULT_ISSUED_TO_ID);

        // Get all the moderationLogItemList where issuedToId is less than UPDATED_ISSUED_TO_ID
        defaultModerationLogItemShouldBeFound("issuedToId.lessThan=" + UPDATED_ISSUED_TO_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedToIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedToId is greater than DEFAULT_ISSUED_TO_ID
        defaultModerationLogItemShouldNotBeFound("issuedToId.greaterThan=" + DEFAULT_ISSUED_TO_ID);

        // Get all the moderationLogItemList where issuedToId is greater than SMALLER_ISSUED_TO_ID
        defaultModerationLogItemShouldBeFound("issuedToId.greaterThan=" + SMALLER_ISSUED_TO_ID);
    }


    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedToNameIsEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedToName equals to DEFAULT_ISSUED_TO_NAME
        defaultModerationLogItemShouldBeFound("issuedToName.equals=" + DEFAULT_ISSUED_TO_NAME);

        // Get all the moderationLogItemList where issuedToName equals to UPDATED_ISSUED_TO_NAME
        defaultModerationLogItemShouldNotBeFound("issuedToName.equals=" + UPDATED_ISSUED_TO_NAME);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedToNameIsInShouldWork() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedToName in DEFAULT_ISSUED_TO_NAME or UPDATED_ISSUED_TO_NAME
        defaultModerationLogItemShouldBeFound("issuedToName.in=" + DEFAULT_ISSUED_TO_NAME + "," + UPDATED_ISSUED_TO_NAME);

        // Get all the moderationLogItemList where issuedToName equals to UPDATED_ISSUED_TO_NAME
        defaultModerationLogItemShouldNotBeFound("issuedToName.in=" + UPDATED_ISSUED_TO_NAME);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByIssuedToNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where issuedToName is not null
        defaultModerationLogItemShouldBeFound("issuedToName.specified=true");

        // Get all the moderationLogItemList where issuedToName is null
        defaultModerationLogItemShouldNotBeFound("issuedToName.specified=false");
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where reason equals to DEFAULT_REASON
        defaultModerationLogItemShouldBeFound("reason.equals=" + DEFAULT_REASON);

        // Get all the moderationLogItemList where reason equals to UPDATED_REASON
        defaultModerationLogItemShouldNotBeFound("reason.equals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByReasonIsInShouldWork() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where reason in DEFAULT_REASON or UPDATED_REASON
        defaultModerationLogItemShouldBeFound("reason.in=" + DEFAULT_REASON + "," + UPDATED_REASON);

        // Get all the moderationLogItemList where reason equals to UPDATED_REASON
        defaultModerationLogItemShouldNotBeFound("reason.in=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where reason is not null
        defaultModerationLogItemShouldBeFound("reason.specified=true");

        // Get all the moderationLogItemList where reason is null
        defaultModerationLogItemShouldNotBeFound("reason.specified=false");
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where time equals to DEFAULT_TIME
        defaultModerationLogItemShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the moderationLogItemList where time equals to UPDATED_TIME
        defaultModerationLogItemShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where time in DEFAULT_TIME or UPDATED_TIME
        defaultModerationLogItemShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the moderationLogItemList where time equals to UPDATED_TIME
        defaultModerationLogItemShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where time is not null
        defaultModerationLogItemShouldBeFound("time.specified=true");

        // Get all the moderationLogItemList where time is null
        defaultModerationLogItemShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByModerationActionIsEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where moderationAction equals to DEFAULT_MODERATION_ACTION
        defaultModerationLogItemShouldBeFound("moderationAction.equals=" + DEFAULT_MODERATION_ACTION);

        // Get all the moderationLogItemList where moderationAction equals to UPDATED_MODERATION_ACTION
        defaultModerationLogItemShouldNotBeFound("moderationAction.equals=" + UPDATED_MODERATION_ACTION);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByModerationActionIsInShouldWork() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where moderationAction in DEFAULT_MODERATION_ACTION or UPDATED_MODERATION_ACTION
        defaultModerationLogItemShouldBeFound("moderationAction.in=" + DEFAULT_MODERATION_ACTION + "," + UPDATED_MODERATION_ACTION);

        // Get all the moderationLogItemList where moderationAction equals to UPDATED_MODERATION_ACTION
        defaultModerationLogItemShouldNotBeFound("moderationAction.in=" + UPDATED_MODERATION_ACTION);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByModerationActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where moderationAction is not null
        defaultModerationLogItemShouldBeFound("moderationAction.specified=true");

        // Get all the moderationLogItemList where moderationAction is null
        defaultModerationLogItemShouldNotBeFound("moderationAction.specified=false");
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByGuildIdIsEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where guildId equals to DEFAULT_GUILD_ID
        defaultModerationLogItemShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);

        // Get all the moderationLogItemList where guildId equals to UPDATED_GUILD_ID
        defaultModerationLogItemShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByGuildIdIsInShouldWork() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
        defaultModerationLogItemShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);

        // Get all the moderationLogItemList where guildId equals to UPDATED_GUILD_ID
        defaultModerationLogItemShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByGuildIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where guildId is not null
        defaultModerationLogItemShouldBeFound("guildId.specified=true");

        // Get all the moderationLogItemList where guildId is null
        defaultModerationLogItemShouldNotBeFound("guildId.specified=false");
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByGuildIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where guildId is greater than or equal to DEFAULT_GUILD_ID
        defaultModerationLogItemShouldBeFound("guildId.greaterThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the moderationLogItemList where guildId is greater than or equal to UPDATED_GUILD_ID
        defaultModerationLogItemShouldNotBeFound("guildId.greaterThanOrEqual=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByGuildIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where guildId is less than or equal to DEFAULT_GUILD_ID
        defaultModerationLogItemShouldBeFound("guildId.lessThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the moderationLogItemList where guildId is less than or equal to SMALLER_GUILD_ID
        defaultModerationLogItemShouldNotBeFound("guildId.lessThanOrEqual=" + SMALLER_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByGuildIdIsLessThanSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where guildId is less than DEFAULT_GUILD_ID
        defaultModerationLogItemShouldNotBeFound("guildId.lessThan=" + DEFAULT_GUILD_ID);

        // Get all the moderationLogItemList where guildId is less than UPDATED_GUILD_ID
        defaultModerationLogItemShouldBeFound("guildId.lessThan=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllModerationLogItemsByGuildIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);

        // Get all the moderationLogItemList where guildId is greater than DEFAULT_GUILD_ID
        defaultModerationLogItemShouldNotBeFound("guildId.greaterThan=" + DEFAULT_GUILD_ID);

        // Get all the moderationLogItemList where guildId is greater than SMALLER_GUILD_ID
        defaultModerationLogItemShouldBeFound("guildId.greaterThan=" + SMALLER_GUILD_ID);
    }


    @Test
    @Transactional
    public void getAllModerationLogItemsByModItemGuildServerIsEqualToSomething() throws Exception {
        // Initialize the database
        moderationLogItemRepository.saveAndFlush(moderationLogItem);
        GuildServer modItemGuildServer = GuildServerResourceIT.createEntity(em);
        em.persist(modItemGuildServer);
        em.flush();
        moderationLogItem.setModItemGuildServer(modItemGuildServer);
        moderationLogItemRepository.saveAndFlush(moderationLogItem);
        Long modItemGuildServerId = modItemGuildServer.getId();

        // Get all the moderationLogItemList where modItemGuildServer equals to modItemGuildServerId
        defaultModerationLogItemShouldBeFound("modItemGuildServerId.equals=" + modItemGuildServerId);

        // Get all the moderationLogItemList where modItemGuildServer equals to modItemGuildServerId + 1
        defaultModerationLogItemShouldNotBeFound("modItemGuildServerId.equals=" + (modItemGuildServerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultModerationLogItemShouldBeFound(String filter) throws Exception {
        restModerationLogItemMockMvc.perform(get("/api/moderation-log-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moderationLogItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].channelId").value(hasItem(DEFAULT_CHANNEL_ID.intValue())))
            .andExpect(jsonPath("$.[*].channelName").value(hasItem(DEFAULT_CHANNEL_NAME)))
            .andExpect(jsonPath("$.[*].issuedById").value(hasItem(DEFAULT_ISSUED_BY_ID.intValue())))
            .andExpect(jsonPath("$.[*].issuedByName").value(hasItem(DEFAULT_ISSUED_BY_NAME)))
            .andExpect(jsonPath("$.[*].issuedToId").value(hasItem(DEFAULT_ISSUED_TO_ID.intValue())))
            .andExpect(jsonPath("$.[*].issuedToName").value(hasItem(DEFAULT_ISSUED_TO_NAME)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].moderationAction").value(hasItem(DEFAULT_MODERATION_ACTION.toString())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));

        // Check, that the count call also returns 1
        restModerationLogItemMockMvc.perform(get("/api/moderation-log-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultModerationLogItemShouldNotBeFound(String filter) throws Exception {
        restModerationLogItemMockMvc.perform(get("/api/moderation-log-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restModerationLogItemMockMvc.perform(get("/api/moderation-log-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingModerationLogItem() throws Exception {
        // Get the moderationLogItem
        restModerationLogItemMockMvc.perform(get("/api/moderation-log-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModerationLogItem() throws Exception {
        // Initialize the database
        moderationLogItemService.save(moderationLogItem);

        int databaseSizeBeforeUpdate = moderationLogItemRepository.findAll().size();

        // Update the moderationLogItem
        ModerationLogItem updatedModerationLogItem = moderationLogItemRepository.findById(moderationLogItem.getId()).get();
        // Disconnect from session so that the updates on updatedModerationLogItem are not directly saved in db
        em.detach(updatedModerationLogItem);
        updatedModerationLogItem
            .channelId(UPDATED_CHANNEL_ID)
            .channelName(UPDATED_CHANNEL_NAME)
            .issuedById(UPDATED_ISSUED_BY_ID)
            .issuedByName(UPDATED_ISSUED_BY_NAME)
            .issuedToId(UPDATED_ISSUED_TO_ID)
            .issuedToName(UPDATED_ISSUED_TO_NAME)
            .reason(UPDATED_REASON)
            .time(UPDATED_TIME)
            .moderationAction(UPDATED_MODERATION_ACTION)
            .guildId(UPDATED_GUILD_ID);

        restModerationLogItemMockMvc.perform(put("/api/moderation-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedModerationLogItem)))
            .andExpect(status().isOk());

        // Validate the ModerationLogItem in the database
        List<ModerationLogItem> moderationLogItemList = moderationLogItemRepository.findAll();
        assertThat(moderationLogItemList).hasSize(databaseSizeBeforeUpdate);
        ModerationLogItem testModerationLogItem = moderationLogItemList.get(moderationLogItemList.size() - 1);
        assertThat(testModerationLogItem.getChannelId()).isEqualTo(UPDATED_CHANNEL_ID);
        assertThat(testModerationLogItem.getChannelName()).isEqualTo(UPDATED_CHANNEL_NAME);
        assertThat(testModerationLogItem.getIssuedById()).isEqualTo(UPDATED_ISSUED_BY_ID);
        assertThat(testModerationLogItem.getIssuedByName()).isEqualTo(UPDATED_ISSUED_BY_NAME);
        assertThat(testModerationLogItem.getIssuedToId()).isEqualTo(UPDATED_ISSUED_TO_ID);
        assertThat(testModerationLogItem.getIssuedToName()).isEqualTo(UPDATED_ISSUED_TO_NAME);
        assertThat(testModerationLogItem.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testModerationLogItem.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testModerationLogItem.getModerationAction()).isEqualTo(UPDATED_MODERATION_ACTION);
        assertThat(testModerationLogItem.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingModerationLogItem() throws Exception {
        int databaseSizeBeforeUpdate = moderationLogItemRepository.findAll().size();

        // Create the ModerationLogItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModerationLogItemMockMvc.perform(put("/api/moderation-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(moderationLogItem)))
            .andExpect(status().isBadRequest());

        // Validate the ModerationLogItem in the database
        List<ModerationLogItem> moderationLogItemList = moderationLogItemRepository.findAll();
        assertThat(moderationLogItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteModerationLogItem() throws Exception {
        // Initialize the database
        moderationLogItemService.save(moderationLogItem);

        int databaseSizeBeforeDelete = moderationLogItemRepository.findAll().size();

        // Delete the moderationLogItem
        restModerationLogItemMockMvc.perform(delete("/api/moderation-log-items/{id}", moderationLogItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ModerationLogItem> moderationLogItemList = moderationLogItemRepository.findAll();
        assertThat(moderationLogItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModerationLogItem.class);
        ModerationLogItem moderationLogItem1 = new ModerationLogItem();
        moderationLogItem1.setId(1L);
        ModerationLogItem moderationLogItem2 = new ModerationLogItem();
        moderationLogItem2.setId(moderationLogItem1.getId());
        assertThat(moderationLogItem1).isEqualTo(moderationLogItem2);
        moderationLogItem2.setId(2L);
        assertThat(moderationLogItem1).isNotEqualTo(moderationLogItem2);
        moderationLogItem1.setId(null);
        assertThat(moderationLogItem1).isNotEqualTo(moderationLogItem2);
    }
}
