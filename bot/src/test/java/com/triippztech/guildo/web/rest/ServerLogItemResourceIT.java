package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.ServerLogItem;
import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.repository.ServerLogItemRepository;
import com.triippztech.guildo.service.ServerLogItemService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.dto.ServerLogItemCriteria;
import com.triippztech.guildo.service.ServerLogItemQueryService;

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

import com.triippztech.guildo.domain.enumeration.Activity;
/**
 * Integration tests for the {@link ServerLogItemResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class ServerLogItemResourceIT {

    private static final Activity DEFAULT_ACTIVITY = Activity.SERVER_JOIN;
    private static final Activity UPDATED_ACTIVITY = Activity.SERVER_QUIT;

    private static final Long DEFAULT_CHANNEL_ID = 1L;
    private static final Long UPDATED_CHANNEL_ID = 2L;
    private static final Long SMALLER_CHANNEL_ID = 1L - 1L;

    private static final String DEFAULT_CHANNEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_TIME = Instant.ofEpochMilli(-1L);

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;
    private static final Long SMALLER_GUILD_ID = 1L - 1L;

    @Autowired
    private ServerLogItemRepository serverLogItemRepository;

    @Autowired
    private ServerLogItemService serverLogItemService;

    @Autowired
    private ServerLogItemQueryService serverLogItemQueryService;

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

    private MockMvc restServerLogItemMockMvc;

    private ServerLogItem serverLogItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServerLogItemResource serverLogItemResource = new ServerLogItemResource(serverLogItemService, serverLogItemQueryService);
        this.restServerLogItemMockMvc = MockMvcBuilders.standaloneSetup(serverLogItemResource)
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
    public static ServerLogItem createEntity(EntityManager em) {
        ServerLogItem serverLogItem = new ServerLogItem()
            .activity(DEFAULT_ACTIVITY)
            .channelId(DEFAULT_CHANNEL_ID)
            .channelName(DEFAULT_CHANNEL_NAME)
            .time(DEFAULT_TIME)
            .userId(DEFAULT_USER_ID)
            .userName(DEFAULT_USER_NAME)
            .guildId(DEFAULT_GUILD_ID);
        return serverLogItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServerLogItem createUpdatedEntity(EntityManager em) {
        ServerLogItem serverLogItem = new ServerLogItem()
            .activity(UPDATED_ACTIVITY)
            .channelId(UPDATED_CHANNEL_ID)
            .channelName(UPDATED_CHANNEL_NAME)
            .time(UPDATED_TIME)
            .userId(UPDATED_USER_ID)
            .userName(UPDATED_USER_NAME)
            .guildId(UPDATED_GUILD_ID);
        return serverLogItem;
    }

    @BeforeEach
    public void initTest() {
        serverLogItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createServerLogItem() throws Exception {
        int databaseSizeBeforeCreate = serverLogItemRepository.findAll().size();

        // Create the ServerLogItem
        restServerLogItemMockMvc.perform(post("/api/server-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverLogItem)))
            .andExpect(status().isCreated());

        // Validate the ServerLogItem in the database
        List<ServerLogItem> serverLogItemList = serverLogItemRepository.findAll();
        assertThat(serverLogItemList).hasSize(databaseSizeBeforeCreate + 1);
        ServerLogItem testServerLogItem = serverLogItemList.get(serverLogItemList.size() - 1);
        assertThat(testServerLogItem.getActivity()).isEqualTo(DEFAULT_ACTIVITY);
        assertThat(testServerLogItem.getChannelId()).isEqualTo(DEFAULT_CHANNEL_ID);
        assertThat(testServerLogItem.getChannelName()).isEqualTo(DEFAULT_CHANNEL_NAME);
        assertThat(testServerLogItem.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testServerLogItem.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testServerLogItem.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testServerLogItem.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
    }

    @Test
    @Transactional
    public void createServerLogItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serverLogItemRepository.findAll().size();

        // Create the ServerLogItem with an existing ID
        serverLogItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServerLogItemMockMvc.perform(post("/api/server-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverLogItem)))
            .andExpect(status().isBadRequest());

        // Validate the ServerLogItem in the database
        List<ServerLogItem> serverLogItemList = serverLogItemRepository.findAll();
        assertThat(serverLogItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkActivityIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverLogItemRepository.findAll().size();
        // set the field null
        serverLogItem.setActivity(null);

        // Create the ServerLogItem, which fails.

        restServerLogItemMockMvc.perform(post("/api/server-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverLogItem)))
            .andExpect(status().isBadRequest());

        List<ServerLogItem> serverLogItemList = serverLogItemRepository.findAll();
        assertThat(serverLogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChannelIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverLogItemRepository.findAll().size();
        // set the field null
        serverLogItem.setChannelId(null);

        // Create the ServerLogItem, which fails.

        restServerLogItemMockMvc.perform(post("/api/server-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverLogItem)))
            .andExpect(status().isBadRequest());

        List<ServerLogItem> serverLogItemList = serverLogItemRepository.findAll();
        assertThat(serverLogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChannelNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverLogItemRepository.findAll().size();
        // set the field null
        serverLogItem.setChannelName(null);

        // Create the ServerLogItem, which fails.

        restServerLogItemMockMvc.perform(post("/api/server-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverLogItem)))
            .andExpect(status().isBadRequest());

        List<ServerLogItem> serverLogItemList = serverLogItemRepository.findAll();
        assertThat(serverLogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverLogItemRepository.findAll().size();
        // set the field null
        serverLogItem.setTime(null);

        // Create the ServerLogItem, which fails.

        restServerLogItemMockMvc.perform(post("/api/server-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverLogItem)))
            .andExpect(status().isBadRequest());

        List<ServerLogItem> serverLogItemList = serverLogItemRepository.findAll();
        assertThat(serverLogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverLogItemRepository.findAll().size();
        // set the field null
        serverLogItem.setUserId(null);

        // Create the ServerLogItem, which fails.

        restServerLogItemMockMvc.perform(post("/api/server-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverLogItem)))
            .andExpect(status().isBadRequest());

        List<ServerLogItem> serverLogItemList = serverLogItemRepository.findAll();
        assertThat(serverLogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverLogItemRepository.findAll().size();
        // set the field null
        serverLogItem.setUserName(null);

        // Create the ServerLogItem, which fails.

        restServerLogItemMockMvc.perform(post("/api/server-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverLogItem)))
            .andExpect(status().isBadRequest());

        List<ServerLogItem> serverLogItemList = serverLogItemRepository.findAll();
        assertThat(serverLogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGuildIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverLogItemRepository.findAll().size();
        // set the field null
        serverLogItem.setGuildId(null);

        // Create the ServerLogItem, which fails.

        restServerLogItemMockMvc.perform(post("/api/server-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverLogItem)))
            .andExpect(status().isBadRequest());

        List<ServerLogItem> serverLogItemList = serverLogItemRepository.findAll();
        assertThat(serverLogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServerLogItems() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList
        restServerLogItemMockMvc.perform(get("/api/server-log-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serverLogItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].activity").value(hasItem(DEFAULT_ACTIVITY.toString())))
            .andExpect(jsonPath("$.[*].channelId").value(hasItem(DEFAULT_CHANNEL_ID.intValue())))
            .andExpect(jsonPath("$.[*].channelName").value(hasItem(DEFAULT_CHANNEL_NAME.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getServerLogItem() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get the serverLogItem
        restServerLogItemMockMvc.perform(get("/api/server-log-items/{id}", serverLogItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serverLogItem.getId().intValue()))
            .andExpect(jsonPath("$.activity").value(DEFAULT_ACTIVITY.toString()))
            .andExpect(jsonPath("$.channelId").value(DEFAULT_CHANNEL_ID.intValue()))
            .andExpect(jsonPath("$.channelName").value(DEFAULT_CHANNEL_NAME.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByActivityIsEqualToSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where activity equals to DEFAULT_ACTIVITY
        defaultServerLogItemShouldBeFound("activity.equals=" + DEFAULT_ACTIVITY);

        // Get all the serverLogItemList where activity equals to UPDATED_ACTIVITY
        defaultServerLogItemShouldNotBeFound("activity.equals=" + UPDATED_ACTIVITY);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByActivityIsInShouldWork() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where activity in DEFAULT_ACTIVITY or UPDATED_ACTIVITY
        defaultServerLogItemShouldBeFound("activity.in=" + DEFAULT_ACTIVITY + "," + UPDATED_ACTIVITY);

        // Get all the serverLogItemList where activity equals to UPDATED_ACTIVITY
        defaultServerLogItemShouldNotBeFound("activity.in=" + UPDATED_ACTIVITY);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByActivityIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where activity is not null
        defaultServerLogItemShouldBeFound("activity.specified=true");

        // Get all the serverLogItemList where activity is null
        defaultServerLogItemShouldNotBeFound("activity.specified=false");
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByChannelIdIsEqualToSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where channelId equals to DEFAULT_CHANNEL_ID
        defaultServerLogItemShouldBeFound("channelId.equals=" + DEFAULT_CHANNEL_ID);

        // Get all the serverLogItemList where channelId equals to UPDATED_CHANNEL_ID
        defaultServerLogItemShouldNotBeFound("channelId.equals=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByChannelIdIsInShouldWork() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where channelId in DEFAULT_CHANNEL_ID or UPDATED_CHANNEL_ID
        defaultServerLogItemShouldBeFound("channelId.in=" + DEFAULT_CHANNEL_ID + "," + UPDATED_CHANNEL_ID);

        // Get all the serverLogItemList where channelId equals to UPDATED_CHANNEL_ID
        defaultServerLogItemShouldNotBeFound("channelId.in=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByChannelIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where channelId is not null
        defaultServerLogItemShouldBeFound("channelId.specified=true");

        // Get all the serverLogItemList where channelId is null
        defaultServerLogItemShouldNotBeFound("channelId.specified=false");
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByChannelIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where channelId is greater than or equal to DEFAULT_CHANNEL_ID
        defaultServerLogItemShouldBeFound("channelId.greaterThanOrEqual=" + DEFAULT_CHANNEL_ID);

        // Get all the serverLogItemList where channelId is greater than or equal to UPDATED_CHANNEL_ID
        defaultServerLogItemShouldNotBeFound("channelId.greaterThanOrEqual=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByChannelIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where channelId is less than or equal to DEFAULT_CHANNEL_ID
        defaultServerLogItemShouldBeFound("channelId.lessThanOrEqual=" + DEFAULT_CHANNEL_ID);

        // Get all the serverLogItemList where channelId is less than or equal to SMALLER_CHANNEL_ID
        defaultServerLogItemShouldNotBeFound("channelId.lessThanOrEqual=" + SMALLER_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByChannelIdIsLessThanSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where channelId is less than DEFAULT_CHANNEL_ID
        defaultServerLogItemShouldNotBeFound("channelId.lessThan=" + DEFAULT_CHANNEL_ID);

        // Get all the serverLogItemList where channelId is less than UPDATED_CHANNEL_ID
        defaultServerLogItemShouldBeFound("channelId.lessThan=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByChannelIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where channelId is greater than DEFAULT_CHANNEL_ID
        defaultServerLogItemShouldNotBeFound("channelId.greaterThan=" + DEFAULT_CHANNEL_ID);

        // Get all the serverLogItemList where channelId is greater than SMALLER_CHANNEL_ID
        defaultServerLogItemShouldBeFound("channelId.greaterThan=" + SMALLER_CHANNEL_ID);
    }


    @Test
    @Transactional
    public void getAllServerLogItemsByChannelNameIsEqualToSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where channelName equals to DEFAULT_CHANNEL_NAME
        defaultServerLogItemShouldBeFound("channelName.equals=" + DEFAULT_CHANNEL_NAME);

        // Get all the serverLogItemList where channelName equals to UPDATED_CHANNEL_NAME
        defaultServerLogItemShouldNotBeFound("channelName.equals=" + UPDATED_CHANNEL_NAME);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByChannelNameIsInShouldWork() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where channelName in DEFAULT_CHANNEL_NAME or UPDATED_CHANNEL_NAME
        defaultServerLogItemShouldBeFound("channelName.in=" + DEFAULT_CHANNEL_NAME + "," + UPDATED_CHANNEL_NAME);

        // Get all the serverLogItemList where channelName equals to UPDATED_CHANNEL_NAME
        defaultServerLogItemShouldNotBeFound("channelName.in=" + UPDATED_CHANNEL_NAME);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByChannelNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where channelName is not null
        defaultServerLogItemShouldBeFound("channelName.specified=true");

        // Get all the serverLogItemList where channelName is null
        defaultServerLogItemShouldNotBeFound("channelName.specified=false");
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where time equals to DEFAULT_TIME
        defaultServerLogItemShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the serverLogItemList where time equals to UPDATED_TIME
        defaultServerLogItemShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where time in DEFAULT_TIME or UPDATED_TIME
        defaultServerLogItemShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the serverLogItemList where time equals to UPDATED_TIME
        defaultServerLogItemShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where time is not null
        defaultServerLogItemShouldBeFound("time.specified=true");

        // Get all the serverLogItemList where time is null
        defaultServerLogItemShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where userId equals to DEFAULT_USER_ID
        defaultServerLogItemShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the serverLogItemList where userId equals to UPDATED_USER_ID
        defaultServerLogItemShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultServerLogItemShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the serverLogItemList where userId equals to UPDATED_USER_ID
        defaultServerLogItemShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where userId is not null
        defaultServerLogItemShouldBeFound("userId.specified=true");

        // Get all the serverLogItemList where userId is null
        defaultServerLogItemShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where userId is greater than or equal to DEFAULT_USER_ID
        defaultServerLogItemShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the serverLogItemList where userId is greater than or equal to UPDATED_USER_ID
        defaultServerLogItemShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where userId is less than or equal to DEFAULT_USER_ID
        defaultServerLogItemShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the serverLogItemList where userId is less than or equal to SMALLER_USER_ID
        defaultServerLogItemShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where userId is less than DEFAULT_USER_ID
        defaultServerLogItemShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the serverLogItemList where userId is less than UPDATED_USER_ID
        defaultServerLogItemShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where userId is greater than DEFAULT_USER_ID
        defaultServerLogItemShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the serverLogItemList where userId is greater than SMALLER_USER_ID
        defaultServerLogItemShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }


    @Test
    @Transactional
    public void getAllServerLogItemsByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where userName equals to DEFAULT_USER_NAME
        defaultServerLogItemShouldBeFound("userName.equals=" + DEFAULT_USER_NAME);

        // Get all the serverLogItemList where userName equals to UPDATED_USER_NAME
        defaultServerLogItemShouldNotBeFound("userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where userName in DEFAULT_USER_NAME or UPDATED_USER_NAME
        defaultServerLogItemShouldBeFound("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME);

        // Get all the serverLogItemList where userName equals to UPDATED_USER_NAME
        defaultServerLogItemShouldNotBeFound("userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where userName is not null
        defaultServerLogItemShouldBeFound("userName.specified=true");

        // Get all the serverLogItemList where userName is null
        defaultServerLogItemShouldNotBeFound("userName.specified=false");
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByGuildIdIsEqualToSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where guildId equals to DEFAULT_GUILD_ID
        defaultServerLogItemShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);

        // Get all the serverLogItemList where guildId equals to UPDATED_GUILD_ID
        defaultServerLogItemShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByGuildIdIsInShouldWork() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
        defaultServerLogItemShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);

        // Get all the serverLogItemList where guildId equals to UPDATED_GUILD_ID
        defaultServerLogItemShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByGuildIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where guildId is not null
        defaultServerLogItemShouldBeFound("guildId.specified=true");

        // Get all the serverLogItemList where guildId is null
        defaultServerLogItemShouldNotBeFound("guildId.specified=false");
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByGuildIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where guildId is greater than or equal to DEFAULT_GUILD_ID
        defaultServerLogItemShouldBeFound("guildId.greaterThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the serverLogItemList where guildId is greater than or equal to UPDATED_GUILD_ID
        defaultServerLogItemShouldNotBeFound("guildId.greaterThanOrEqual=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByGuildIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where guildId is less than or equal to DEFAULT_GUILD_ID
        defaultServerLogItemShouldBeFound("guildId.lessThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the serverLogItemList where guildId is less than or equal to SMALLER_GUILD_ID
        defaultServerLogItemShouldNotBeFound("guildId.lessThanOrEqual=" + SMALLER_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByGuildIdIsLessThanSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where guildId is less than DEFAULT_GUILD_ID
        defaultServerLogItemShouldNotBeFound("guildId.lessThan=" + DEFAULT_GUILD_ID);

        // Get all the serverLogItemList where guildId is less than UPDATED_GUILD_ID
        defaultServerLogItemShouldBeFound("guildId.lessThan=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllServerLogItemsByGuildIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);

        // Get all the serverLogItemList where guildId is greater than DEFAULT_GUILD_ID
        defaultServerLogItemShouldNotBeFound("guildId.greaterThan=" + DEFAULT_GUILD_ID);

        // Get all the serverLogItemList where guildId is greater than SMALLER_GUILD_ID
        defaultServerLogItemShouldBeFound("guildId.greaterThan=" + SMALLER_GUILD_ID);
    }


    @Test
    @Transactional
    public void getAllServerLogItemsByServerItemGuildServerIsEqualToSomething() throws Exception {
        // Initialize the database
        serverLogItemRepository.saveAndFlush(serverLogItem);
        GuildServer serverItemGuildServer = GuildServerResourceIT.createEntity(em);
        em.persist(serverItemGuildServer);
        em.flush();
        serverLogItem.setServerItemGuildServer(serverItemGuildServer);
        serverLogItemRepository.saveAndFlush(serverLogItem);
        Long serverItemGuildServerId = serverItemGuildServer.getId();

        // Get all the serverLogItemList where serverItemGuildServer equals to serverItemGuildServerId
        defaultServerLogItemShouldBeFound("serverItemGuildServerId.equals=" + serverItemGuildServerId);

        // Get all the serverLogItemList where serverItemGuildServer equals to serverItemGuildServerId + 1
        defaultServerLogItemShouldNotBeFound("serverItemGuildServerId.equals=" + (serverItemGuildServerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServerLogItemShouldBeFound(String filter) throws Exception {
        restServerLogItemMockMvc.perform(get("/api/server-log-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serverLogItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].activity").value(hasItem(DEFAULT_ACTIVITY.toString())))
            .andExpect(jsonPath("$.[*].channelId").value(hasItem(DEFAULT_CHANNEL_ID.intValue())))
            .andExpect(jsonPath("$.[*].channelName").value(hasItem(DEFAULT_CHANNEL_NAME)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));

        // Check, that the count call also returns 1
        restServerLogItemMockMvc.perform(get("/api/server-log-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServerLogItemShouldNotBeFound(String filter) throws Exception {
        restServerLogItemMockMvc.perform(get("/api/server-log-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServerLogItemMockMvc.perform(get("/api/server-log-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingServerLogItem() throws Exception {
        // Get the serverLogItem
        restServerLogItemMockMvc.perform(get("/api/server-log-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServerLogItem() throws Exception {
        // Initialize the database
        serverLogItemService.save(serverLogItem);

        int databaseSizeBeforeUpdate = serverLogItemRepository.findAll().size();

        // Update the serverLogItem
        ServerLogItem updatedServerLogItem = serverLogItemRepository.findById(serverLogItem.getId()).get();
        // Disconnect from session so that the updates on updatedServerLogItem are not directly saved in db
        em.detach(updatedServerLogItem);
        updatedServerLogItem
            .activity(UPDATED_ACTIVITY)
            .channelId(UPDATED_CHANNEL_ID)
            .channelName(UPDATED_CHANNEL_NAME)
            .time(UPDATED_TIME)
            .userId(UPDATED_USER_ID)
            .userName(UPDATED_USER_NAME)
            .guildId(UPDATED_GUILD_ID);

        restServerLogItemMockMvc.perform(put("/api/server-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServerLogItem)))
            .andExpect(status().isOk());

        // Validate the ServerLogItem in the database
        List<ServerLogItem> serverLogItemList = serverLogItemRepository.findAll();
        assertThat(serverLogItemList).hasSize(databaseSizeBeforeUpdate);
        ServerLogItem testServerLogItem = serverLogItemList.get(serverLogItemList.size() - 1);
        assertThat(testServerLogItem.getActivity()).isEqualTo(UPDATED_ACTIVITY);
        assertThat(testServerLogItem.getChannelId()).isEqualTo(UPDATED_CHANNEL_ID);
        assertThat(testServerLogItem.getChannelName()).isEqualTo(UPDATED_CHANNEL_NAME);
        assertThat(testServerLogItem.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testServerLogItem.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testServerLogItem.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testServerLogItem.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingServerLogItem() throws Exception {
        int databaseSizeBeforeUpdate = serverLogItemRepository.findAll().size();

        // Create the ServerLogItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServerLogItemMockMvc.perform(put("/api/server-log-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverLogItem)))
            .andExpect(status().isBadRequest());

        // Validate the ServerLogItem in the database
        List<ServerLogItem> serverLogItemList = serverLogItemRepository.findAll();
        assertThat(serverLogItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServerLogItem() throws Exception {
        // Initialize the database
        serverLogItemService.save(serverLogItem);

        int databaseSizeBeforeDelete = serverLogItemRepository.findAll().size();

        // Delete the serverLogItem
        restServerLogItemMockMvc.perform(delete("/api/server-log-items/{id}", serverLogItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServerLogItem> serverLogItemList = serverLogItemRepository.findAll();
        assertThat(serverLogItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServerLogItem.class);
        ServerLogItem serverLogItem1 = new ServerLogItem();
        serverLogItem1.setId(1L);
        ServerLogItem serverLogItem2 = new ServerLogItem();
        serverLogItem2.setId(serverLogItem1.getId());
        assertThat(serverLogItem1).isEqualTo(serverLogItem2);
        serverLogItem2.setId(2L);
        assertThat(serverLogItem1).isNotEqualTo(serverLogItem2);
        serverLogItem1.setId(null);
        assertThat(serverLogItem1).isNotEqualTo(serverLogItem2);
    }
}
