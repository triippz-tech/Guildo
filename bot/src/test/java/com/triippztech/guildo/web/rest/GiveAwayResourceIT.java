package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.GiveAway;
import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.repository.GiveAwayRepository;
import com.triippztech.guildo.service.guild.GiveAwayService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.guild.GiveAwayQueryService;

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

/**
 * Integration tests for the {@link GiveAwayResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class GiveAwayResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Long DEFAULT_MESSAGE_ID = 1L;
    private static final Long UPDATED_MESSAGE_ID = 2L;
    private static final Long SMALLER_MESSAGE_ID = 1L - 1L;

    private static final Long DEFAULT_TEXT_CHANNEL_ID = 1L;
    private static final Long UPDATED_TEXT_CHANNEL_ID = 2L;
    private static final Long SMALLER_TEXT_CHANNEL_ID = 1L - 1L;

    private static final Instant DEFAULT_FINISH = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FINISH = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_FINISH = Instant.ofEpochMilli(-1L);

    private static final Boolean DEFAULT_EXPIRED = false;
    private static final Boolean UPDATED_EXPIRED = true;

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;
    private static final Long SMALLER_GUILD_ID = 1L - 1L;

    @Autowired
    private GiveAwayRepository giveAwayRepository;

    @Autowired
    private GiveAwayService giveAwayService;

    @Autowired
    private GiveAwayQueryService giveAwayQueryService;

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

    private MockMvc restGiveAwayMockMvc;

    private GiveAway giveAway;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GiveAwayResource giveAwayResource = new GiveAwayResource(giveAwayService, giveAwayQueryService);
        this.restGiveAwayMockMvc = MockMvcBuilders.standaloneSetup(giveAwayResource)
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
    public static GiveAway createEntity(EntityManager em) {
        GiveAway giveAway = new GiveAway()
            .name(DEFAULT_NAME)
            .image(DEFAULT_IMAGE)
            .message(DEFAULT_MESSAGE)
            .messageId(DEFAULT_MESSAGE_ID)
            .textChannelId(DEFAULT_TEXT_CHANNEL_ID)
            .finish(DEFAULT_FINISH)
            .expired(DEFAULT_EXPIRED)
            .guildId(DEFAULT_GUILD_ID);
        return giveAway;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GiveAway createUpdatedEntity(EntityManager em) {
        GiveAway giveAway = new GiveAway()
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .message(UPDATED_MESSAGE)
            .messageId(UPDATED_MESSAGE_ID)
            .textChannelId(UPDATED_TEXT_CHANNEL_ID)
            .finish(UPDATED_FINISH)
            .expired(UPDATED_EXPIRED)
            .guildId(UPDATED_GUILD_ID);
        return giveAway;
    }

    @BeforeEach
    public void initTest() {
        giveAway = createEntity(em);
    }

    @Test
    @Transactional
    public void createGiveAway() throws Exception {
        int databaseSizeBeforeCreate = giveAwayRepository.findAll().size();

        // Create the GiveAway
        restGiveAwayMockMvc.perform(post("/api/give-aways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
            .andExpect(status().isCreated());

        // Validate the GiveAway in the database
        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
        assertThat(giveAwayList).hasSize(databaseSizeBeforeCreate + 1);
        GiveAway testGiveAway = giveAwayList.get(giveAwayList.size() - 1);
        assertThat(testGiveAway.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGiveAway.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testGiveAway.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testGiveAway.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
        assertThat(testGiveAway.getTextChannelId()).isEqualTo(DEFAULT_TEXT_CHANNEL_ID);
        assertThat(testGiveAway.getFinish()).isEqualTo(DEFAULT_FINISH);
        assertThat(testGiveAway.isExpired()).isEqualTo(DEFAULT_EXPIRED);
        assertThat(testGiveAway.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
    }

    @Test
    @Transactional
    public void createGiveAwayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = giveAwayRepository.findAll().size();

        // Create the GiveAway with an existing ID
        giveAway.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGiveAwayMockMvc.perform(post("/api/give-aways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
            .andExpect(status().isBadRequest());

        // Validate the GiveAway in the database
        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
        assertThat(giveAwayList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = giveAwayRepository.findAll().size();
        // set the field null
        giveAway.setName(null);

        // Create the GiveAway, which fails.

        restGiveAwayMockMvc.perform(post("/api/give-aways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
            .andExpect(status().isBadRequest());

        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
        assertThat(giveAwayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = giveAwayRepository.findAll().size();
        // set the field null
        giveAway.setMessageId(null);

        // Create the GiveAway, which fails.

        restGiveAwayMockMvc.perform(post("/api/give-aways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
            .andExpect(status().isBadRequest());

        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
        assertThat(giveAwayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTextChannelIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = giveAwayRepository.findAll().size();
        // set the field null
        giveAway.setTextChannelId(null);

        // Create the GiveAway, which fails.

        restGiveAwayMockMvc.perform(post("/api/give-aways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
            .andExpect(status().isBadRequest());

        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
        assertThat(giveAwayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFinishIsRequired() throws Exception {
        int databaseSizeBeforeTest = giveAwayRepository.findAll().size();
        // set the field null
        giveAway.setFinish(null);

        // Create the GiveAway, which fails.

        restGiveAwayMockMvc.perform(post("/api/give-aways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
            .andExpect(status().isBadRequest());

        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
        assertThat(giveAwayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpiredIsRequired() throws Exception {
        int databaseSizeBeforeTest = giveAwayRepository.findAll().size();
        // set the field null
        giveAway.setExpired(null);

        // Create the GiveAway, which fails.

        restGiveAwayMockMvc.perform(post("/api/give-aways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
            .andExpect(status().isBadRequest());

        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
        assertThat(giveAwayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGiveAways() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList
        restGiveAwayMockMvc.perform(get("/api/give-aways?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(giveAway.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID.intValue())))
            .andExpect(jsonPath("$.[*].textChannelId").value(hasItem(DEFAULT_TEXT_CHANNEL_ID.intValue())))
            .andExpect(jsonPath("$.[*].finish").value(hasItem(DEFAULT_FINISH.toString())))
            .andExpect(jsonPath("$.[*].expired").value(hasItem(DEFAULT_EXPIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getGiveAway() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get the giveAway
        restGiveAwayMockMvc.perform(get("/api/give-aways/{id}", giveAway.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(giveAway.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.messageId").value(DEFAULT_MESSAGE_ID.intValue()))
            .andExpect(jsonPath("$.textChannelId").value(DEFAULT_TEXT_CHANNEL_ID.intValue()))
            .andExpect(jsonPath("$.finish").value(DEFAULT_FINISH.toString()))
            .andExpect(jsonPath("$.expired").value(DEFAULT_EXPIRED.booleanValue()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where name equals to DEFAULT_NAME
        defaultGiveAwayShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the giveAwayList where name equals to UPDATED_NAME
        defaultGiveAwayShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByNameIsInShouldWork() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where name in DEFAULT_NAME or UPDATED_NAME
        defaultGiveAwayShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the giveAwayList where name equals to UPDATED_NAME
        defaultGiveAwayShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where name is not null
        defaultGiveAwayShouldBeFound("name.specified=true");

        // Get all the giveAwayList where name is null
        defaultGiveAwayShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByImageIsEqualToSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where image equals to DEFAULT_IMAGE
        defaultGiveAwayShouldBeFound("image.equals=" + DEFAULT_IMAGE);

        // Get all the giveAwayList where image equals to UPDATED_IMAGE
        defaultGiveAwayShouldNotBeFound("image.equals=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByImageIsInShouldWork() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where image in DEFAULT_IMAGE or UPDATED_IMAGE
        defaultGiveAwayShouldBeFound("image.in=" + DEFAULT_IMAGE + "," + UPDATED_IMAGE);

        // Get all the giveAwayList where image equals to UPDATED_IMAGE
        defaultGiveAwayShouldNotBeFound("image.in=" + UPDATED_IMAGE);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where image is not null
        defaultGiveAwayShouldBeFound("image.specified=true");

        // Get all the giveAwayList where image is null
        defaultGiveAwayShouldNotBeFound("image.specified=false");
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByMessageIdIsEqualToSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where messageId equals to DEFAULT_MESSAGE_ID
        defaultGiveAwayShouldBeFound("messageId.equals=" + DEFAULT_MESSAGE_ID);

        // Get all the giveAwayList where messageId equals to UPDATED_MESSAGE_ID
        defaultGiveAwayShouldNotBeFound("messageId.equals=" + UPDATED_MESSAGE_ID);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByMessageIdIsInShouldWork() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where messageId in DEFAULT_MESSAGE_ID or UPDATED_MESSAGE_ID
        defaultGiveAwayShouldBeFound("messageId.in=" + DEFAULT_MESSAGE_ID + "," + UPDATED_MESSAGE_ID);

        // Get all the giveAwayList where messageId equals to UPDATED_MESSAGE_ID
        defaultGiveAwayShouldNotBeFound("messageId.in=" + UPDATED_MESSAGE_ID);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByMessageIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where messageId is not null
        defaultGiveAwayShouldBeFound("messageId.specified=true");

        // Get all the giveAwayList where messageId is null
        defaultGiveAwayShouldNotBeFound("messageId.specified=false");
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByMessageIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where messageId is greater than or equal to DEFAULT_MESSAGE_ID
        defaultGiveAwayShouldBeFound("messageId.greaterThanOrEqual=" + DEFAULT_MESSAGE_ID);

        // Get all the giveAwayList where messageId is greater than or equal to UPDATED_MESSAGE_ID
        defaultGiveAwayShouldNotBeFound("messageId.greaterThanOrEqual=" + UPDATED_MESSAGE_ID);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByMessageIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where messageId is less than or equal to DEFAULT_MESSAGE_ID
        defaultGiveAwayShouldBeFound("messageId.lessThanOrEqual=" + DEFAULT_MESSAGE_ID);

        // Get all the giveAwayList where messageId is less than or equal to SMALLER_MESSAGE_ID
        defaultGiveAwayShouldNotBeFound("messageId.lessThanOrEqual=" + SMALLER_MESSAGE_ID);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByMessageIdIsLessThanSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where messageId is less than DEFAULT_MESSAGE_ID
        defaultGiveAwayShouldNotBeFound("messageId.lessThan=" + DEFAULT_MESSAGE_ID);

        // Get all the giveAwayList where messageId is less than UPDATED_MESSAGE_ID
        defaultGiveAwayShouldBeFound("messageId.lessThan=" + UPDATED_MESSAGE_ID);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByMessageIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where messageId is greater than DEFAULT_MESSAGE_ID
        defaultGiveAwayShouldNotBeFound("messageId.greaterThan=" + DEFAULT_MESSAGE_ID);

        // Get all the giveAwayList where messageId is greater than SMALLER_MESSAGE_ID
        defaultGiveAwayShouldBeFound("messageId.greaterThan=" + SMALLER_MESSAGE_ID);
    }


    @Test
    @Transactional
    public void getAllGiveAwaysByTextChannelIdIsEqualToSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where textChannelId equals to DEFAULT_TEXT_CHANNEL_ID
        defaultGiveAwayShouldBeFound("textChannelId.equals=" + DEFAULT_TEXT_CHANNEL_ID);

        // Get all the giveAwayList where textChannelId equals to UPDATED_TEXT_CHANNEL_ID
        defaultGiveAwayShouldNotBeFound("textChannelId.equals=" + UPDATED_TEXT_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByTextChannelIdIsInShouldWork() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where textChannelId in DEFAULT_TEXT_CHANNEL_ID or UPDATED_TEXT_CHANNEL_ID
        defaultGiveAwayShouldBeFound("textChannelId.in=" + DEFAULT_TEXT_CHANNEL_ID + "," + UPDATED_TEXT_CHANNEL_ID);

        // Get all the giveAwayList where textChannelId equals to UPDATED_TEXT_CHANNEL_ID
        defaultGiveAwayShouldNotBeFound("textChannelId.in=" + UPDATED_TEXT_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByTextChannelIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where textChannelId is not null
        defaultGiveAwayShouldBeFound("textChannelId.specified=true");

        // Get all the giveAwayList where textChannelId is null
        defaultGiveAwayShouldNotBeFound("textChannelId.specified=false");
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByTextChannelIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where textChannelId is greater than or equal to DEFAULT_TEXT_CHANNEL_ID
        defaultGiveAwayShouldBeFound("textChannelId.greaterThanOrEqual=" + DEFAULT_TEXT_CHANNEL_ID);

        // Get all the giveAwayList where textChannelId is greater than or equal to UPDATED_TEXT_CHANNEL_ID
        defaultGiveAwayShouldNotBeFound("textChannelId.greaterThanOrEqual=" + UPDATED_TEXT_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByTextChannelIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where textChannelId is less than or equal to DEFAULT_TEXT_CHANNEL_ID
        defaultGiveAwayShouldBeFound("textChannelId.lessThanOrEqual=" + DEFAULT_TEXT_CHANNEL_ID);

        // Get all the giveAwayList where textChannelId is less than or equal to SMALLER_TEXT_CHANNEL_ID
        defaultGiveAwayShouldNotBeFound("textChannelId.lessThanOrEqual=" + SMALLER_TEXT_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByTextChannelIdIsLessThanSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where textChannelId is less than DEFAULT_TEXT_CHANNEL_ID
        defaultGiveAwayShouldNotBeFound("textChannelId.lessThan=" + DEFAULT_TEXT_CHANNEL_ID);

        // Get all the giveAwayList where textChannelId is less than UPDATED_TEXT_CHANNEL_ID
        defaultGiveAwayShouldBeFound("textChannelId.lessThan=" + UPDATED_TEXT_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByTextChannelIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where textChannelId is greater than DEFAULT_TEXT_CHANNEL_ID
        defaultGiveAwayShouldNotBeFound("textChannelId.greaterThan=" + DEFAULT_TEXT_CHANNEL_ID);

        // Get all the giveAwayList where textChannelId is greater than SMALLER_TEXT_CHANNEL_ID
        defaultGiveAwayShouldBeFound("textChannelId.greaterThan=" + SMALLER_TEXT_CHANNEL_ID);
    }


    @Test
    @Transactional
    public void getAllGiveAwaysByFinishIsEqualToSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where finish equals to DEFAULT_FINISH
        defaultGiveAwayShouldBeFound("finish.equals=" + DEFAULT_FINISH);

        // Get all the giveAwayList where finish equals to UPDATED_FINISH
        defaultGiveAwayShouldNotBeFound("finish.equals=" + UPDATED_FINISH);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByFinishIsInShouldWork() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where finish in DEFAULT_FINISH or UPDATED_FINISH
        defaultGiveAwayShouldBeFound("finish.in=" + DEFAULT_FINISH + "," + UPDATED_FINISH);

        // Get all the giveAwayList where finish equals to UPDATED_FINISH
        defaultGiveAwayShouldNotBeFound("finish.in=" + UPDATED_FINISH);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByFinishIsNullOrNotNull() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where finish is not null
        defaultGiveAwayShouldBeFound("finish.specified=true");

        // Get all the giveAwayList where finish is null
        defaultGiveAwayShouldNotBeFound("finish.specified=false");
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByExpiredIsEqualToSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where expired equals to DEFAULT_EXPIRED
        defaultGiveAwayShouldBeFound("expired.equals=" + DEFAULT_EXPIRED);

        // Get all the giveAwayList where expired equals to UPDATED_EXPIRED
        defaultGiveAwayShouldNotBeFound("expired.equals=" + UPDATED_EXPIRED);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByExpiredIsInShouldWork() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where expired in DEFAULT_EXPIRED or UPDATED_EXPIRED
        defaultGiveAwayShouldBeFound("expired.in=" + DEFAULT_EXPIRED + "," + UPDATED_EXPIRED);

        // Get all the giveAwayList where expired equals to UPDATED_EXPIRED
        defaultGiveAwayShouldNotBeFound("expired.in=" + UPDATED_EXPIRED);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByExpiredIsNullOrNotNull() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where expired is not null
        defaultGiveAwayShouldBeFound("expired.specified=true");

        // Get all the giveAwayList where expired is null
        defaultGiveAwayShouldNotBeFound("expired.specified=false");
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByGuildIdIsEqualToSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where guildId equals to DEFAULT_GUILD_ID
        defaultGiveAwayShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);

        // Get all the giveAwayList where guildId equals to UPDATED_GUILD_ID
        defaultGiveAwayShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByGuildIdIsInShouldWork() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
        defaultGiveAwayShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);

        // Get all the giveAwayList where guildId equals to UPDATED_GUILD_ID
        defaultGiveAwayShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByGuildIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where guildId is not null
        defaultGiveAwayShouldBeFound("guildId.specified=true");

        // Get all the giveAwayList where guildId is null
        defaultGiveAwayShouldNotBeFound("guildId.specified=false");
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByGuildIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where guildId is greater than or equal to DEFAULT_GUILD_ID
        defaultGiveAwayShouldBeFound("guildId.greaterThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the giveAwayList where guildId is greater than or equal to UPDATED_GUILD_ID
        defaultGiveAwayShouldNotBeFound("guildId.greaterThanOrEqual=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByGuildIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where guildId is less than or equal to DEFAULT_GUILD_ID
        defaultGiveAwayShouldBeFound("guildId.lessThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the giveAwayList where guildId is less than or equal to SMALLER_GUILD_ID
        defaultGiveAwayShouldNotBeFound("guildId.lessThanOrEqual=" + SMALLER_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByGuildIdIsLessThanSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where guildId is less than DEFAULT_GUILD_ID
        defaultGiveAwayShouldNotBeFound("guildId.lessThan=" + DEFAULT_GUILD_ID);

        // Get all the giveAwayList where guildId is less than UPDATED_GUILD_ID
        defaultGiveAwayShouldBeFound("guildId.lessThan=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGiveAwaysByGuildIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);

        // Get all the giveAwayList where guildId is greater than DEFAULT_GUILD_ID
        defaultGiveAwayShouldNotBeFound("guildId.greaterThan=" + DEFAULT_GUILD_ID);

        // Get all the giveAwayList where guildId is greater than SMALLER_GUILD_ID
        defaultGiveAwayShouldBeFound("guildId.greaterThan=" + SMALLER_GUILD_ID);
    }


    @Test
    @Transactional
    public void getAllGiveAwaysByWinnerIsEqualToSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);
        DiscordUser winner = DiscordUserResourceIT.createEntity(em);
        em.persist(winner);
        em.flush();
        giveAway.setWinner(winner);
        giveAwayRepository.saveAndFlush(giveAway);
        Long winnerId = winner.getId();

        // Get all the giveAwayList where winner equals to winnerId
        defaultGiveAwayShouldBeFound("winnerId.equals=" + winnerId);

        // Get all the giveAwayList where winner equals to winnerId + 1
        defaultGiveAwayShouldNotBeFound("winnerId.equals=" + (winnerId + 1));
    }


    @Test
    @Transactional
    public void getAllGiveAwaysByGuildGiveAwayIsEqualToSomething() throws Exception {
        // Initialize the database
        giveAwayRepository.saveAndFlush(giveAway);
        GuildServer guildGiveAway = GuildServerResourceIT.createEntity(em);
        em.persist(guildGiveAway);
        em.flush();
        giveAway.setGuildGiveAway(guildGiveAway);
        giveAwayRepository.saveAndFlush(giveAway);
        Long guildGiveAwayId = guildGiveAway.getId();

        // Get all the giveAwayList where guildGiveAway equals to guildGiveAwayId
        defaultGiveAwayShouldBeFound("guildGiveAwayId.equals=" + guildGiveAwayId);

        // Get all the giveAwayList where guildGiveAway equals to guildGiveAwayId + 1
        defaultGiveAwayShouldNotBeFound("guildGiveAwayId.equals=" + (guildGiveAwayId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGiveAwayShouldBeFound(String filter) throws Exception {
        restGiveAwayMockMvc.perform(get("/api/give-aways?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(giveAway.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID.intValue())))
            .andExpect(jsonPath("$.[*].textChannelId").value(hasItem(DEFAULT_TEXT_CHANNEL_ID.intValue())))
            .andExpect(jsonPath("$.[*].finish").value(hasItem(DEFAULT_FINISH.toString())))
            .andExpect(jsonPath("$.[*].expired").value(hasItem(DEFAULT_EXPIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));

        // Check, that the count call also returns 1
        restGiveAwayMockMvc.perform(get("/api/give-aways/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGiveAwayShouldNotBeFound(String filter) throws Exception {
        restGiveAwayMockMvc.perform(get("/api/give-aways?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGiveAwayMockMvc.perform(get("/api/give-aways/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGiveAway() throws Exception {
        // Get the giveAway
        restGiveAwayMockMvc.perform(get("/api/give-aways/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGiveAway() throws Exception {
        // Initialize the database
        giveAwayService.save(giveAway);

        int databaseSizeBeforeUpdate = giveAwayRepository.findAll().size();

        // Update the giveAway
        GiveAway updatedGiveAway = giveAwayRepository.findById(giveAway.getId()).get();
        // Disconnect from session so that the updates on updatedGiveAway are not directly saved in db
        em.detach(updatedGiveAway);
        updatedGiveAway
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .message(UPDATED_MESSAGE)
            .messageId(UPDATED_MESSAGE_ID)
            .textChannelId(UPDATED_TEXT_CHANNEL_ID)
            .finish(UPDATED_FINISH)
            .expired(UPDATED_EXPIRED)
            .guildId(UPDATED_GUILD_ID);

        restGiveAwayMockMvc.perform(put("/api/give-aways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGiveAway)))
            .andExpect(status().isOk());

        // Validate the GiveAway in the database
        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
        assertThat(giveAwayList).hasSize(databaseSizeBeforeUpdate);
        GiveAway testGiveAway = giveAwayList.get(giveAwayList.size() - 1);
        assertThat(testGiveAway.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGiveAway.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testGiveAway.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testGiveAway.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
        assertThat(testGiveAway.getTextChannelId()).isEqualTo(UPDATED_TEXT_CHANNEL_ID);
        assertThat(testGiveAway.getFinish()).isEqualTo(UPDATED_FINISH);
        assertThat(testGiveAway.isExpired()).isEqualTo(UPDATED_EXPIRED);
        assertThat(testGiveAway.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingGiveAway() throws Exception {
        int databaseSizeBeforeUpdate = giveAwayRepository.findAll().size();

        // Create the GiveAway

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGiveAwayMockMvc.perform(put("/api/give-aways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(giveAway)))
            .andExpect(status().isBadRequest());

        // Validate the GiveAway in the database
        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
        assertThat(giveAwayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGiveAway() throws Exception {
        // Initialize the database
        giveAwayService.save(giveAway);

        int databaseSizeBeforeDelete = giveAwayRepository.findAll().size();

        // Delete the giveAway
        restGiveAwayMockMvc.perform(delete("/api/give-aways/{id}", giveAway.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GiveAway> giveAwayList = giveAwayRepository.findAll();
        assertThat(giveAwayList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GiveAway.class);
        GiveAway giveAway1 = new GiveAway();
        giveAway1.setId(1L);
        GiveAway giveAway2 = new GiveAway();
        giveAway2.setId(giveAway1.getId());
        assertThat(giveAway1).isEqualTo(giveAway2);
        giveAway2.setId(2L);
        assertThat(giveAway1).isNotEqualTo(giveAway2);
        giveAway1.setId(null);
        assertThat(giveAway1).isNotEqualTo(giveAway2);
    }
}
