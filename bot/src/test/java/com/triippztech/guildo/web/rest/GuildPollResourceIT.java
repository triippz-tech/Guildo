package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.GuildPoll;
import com.triippztech.guildo.domain.GuildPollItem;
import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.repository.GuildPollRepository;
import com.triippztech.guildo.service.guild.GuildPollService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.guild.GuildPollQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.triippztech.guildo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link GuildPollResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class GuildPollResourceIT {

    private static final String DEFAULT_POLL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_POLL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_TEXT_CHANNEL_ID = 1L;
    private static final Long UPDATED_TEXT_CHANNEL_ID = 2L;
    private static final Long SMALLER_TEXT_CHANNEL_ID = 1L - 1L;

    private static final LocalDate DEFAULT_FINISH_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FINISH_TIME = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FINISH_TIME = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;
    private static final Long SMALLER_GUILD_ID = 1L - 1L;

    @Autowired
    private GuildPollRepository guildPollRepository;

    @Autowired
    private GuildPollService guildPollService;

    @Autowired
    private GuildPollQueryService guildPollQueryService;

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

    private MockMvc restGuildPollMockMvc;

    private GuildPoll guildPoll;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GuildPollResource guildPollResource = new GuildPollResource(guildPollService, guildPollQueryService);
        this.restGuildPollMockMvc = MockMvcBuilders.standaloneSetup(guildPollResource)
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
    public static GuildPoll createEntity(EntityManager em) {
        GuildPoll guildPoll = new GuildPoll()
            .pollName(DEFAULT_POLL_NAME)
            .description(DEFAULT_DESCRIPTION)
            .textChannelId(DEFAULT_TEXT_CHANNEL_ID)
            .finishTime(DEFAULT_FINISH_TIME)
            .completed(DEFAULT_COMPLETED)
            .guildId(DEFAULT_GUILD_ID);
        return guildPoll;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GuildPoll createUpdatedEntity(EntityManager em) {
        GuildPoll guildPoll = new GuildPoll()
            .pollName(UPDATED_POLL_NAME)
            .description(UPDATED_DESCRIPTION)
            .textChannelId(UPDATED_TEXT_CHANNEL_ID)
            .finishTime(UPDATED_FINISH_TIME)
            .completed(UPDATED_COMPLETED)
            .guildId(UPDATED_GUILD_ID);
        return guildPoll;
    }

    @BeforeEach
    public void initTest() {
        guildPoll = createEntity(em);
    }

    @Test
    @Transactional
    public void createGuildPoll() throws Exception {
        int databaseSizeBeforeCreate = guildPollRepository.findAll().size();

        // Create the GuildPoll
        restGuildPollMockMvc.perform(post("/api/guild-polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildPoll)))
            .andExpect(status().isCreated());

        // Validate the GuildPoll in the database
        List<GuildPoll> guildPollList = guildPollRepository.findAll();
        assertThat(guildPollList).hasSize(databaseSizeBeforeCreate + 1);
        GuildPoll testGuildPoll = guildPollList.get(guildPollList.size() - 1);
        assertThat(testGuildPoll.getPollName()).isEqualTo(DEFAULT_POLL_NAME);
        assertThat(testGuildPoll.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGuildPoll.getTextChannelId()).isEqualTo(DEFAULT_TEXT_CHANNEL_ID);
        assertThat(testGuildPoll.getFinishTime()).isEqualTo(DEFAULT_FINISH_TIME);
        assertThat(testGuildPoll.isCompleted()).isEqualTo(DEFAULT_COMPLETED);
        assertThat(testGuildPoll.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
    }

    @Test
    @Transactional
    public void createGuildPollWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = guildPollRepository.findAll().size();

        // Create the GuildPoll with an existing ID
        guildPoll.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuildPollMockMvc.perform(post("/api/guild-polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildPoll)))
            .andExpect(status().isBadRequest());

        // Validate the GuildPoll in the database
        List<GuildPoll> guildPollList = guildPollRepository.findAll();
        assertThat(guildPollList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPollNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildPollRepository.findAll().size();
        // set the field null
        guildPoll.setPollName(null);

        // Create the GuildPoll, which fails.

        restGuildPollMockMvc.perform(post("/api/guild-polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildPoll)))
            .andExpect(status().isBadRequest());

        List<GuildPoll> guildPollList = guildPollRepository.findAll();
        assertThat(guildPollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildPollRepository.findAll().size();
        // set the field null
        guildPoll.setDescription(null);

        // Create the GuildPoll, which fails.

        restGuildPollMockMvc.perform(post("/api/guild-polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildPoll)))
            .andExpect(status().isBadRequest());

        List<GuildPoll> guildPollList = guildPollRepository.findAll();
        assertThat(guildPollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTextChannelIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildPollRepository.findAll().size();
        // set the field null
        guildPoll.setTextChannelId(null);

        // Create the GuildPoll, which fails.

        restGuildPollMockMvc.perform(post("/api/guild-polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildPoll)))
            .andExpect(status().isBadRequest());

        List<GuildPoll> guildPollList = guildPollRepository.findAll();
        assertThat(guildPollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFinishTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildPollRepository.findAll().size();
        // set the field null
        guildPoll.setFinishTime(null);

        // Create the GuildPoll, which fails.

        restGuildPollMockMvc.perform(post("/api/guild-polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildPoll)))
            .andExpect(status().isBadRequest());

        List<GuildPoll> guildPollList = guildPollRepository.findAll();
        assertThat(guildPollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildPollRepository.findAll().size();
        // set the field null
        guildPoll.setCompleted(null);

        // Create the GuildPoll, which fails.

        restGuildPollMockMvc.perform(post("/api/guild-polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildPoll)))
            .andExpect(status().isBadRequest());

        List<GuildPoll> guildPollList = guildPollRepository.findAll();
        assertThat(guildPollList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGuildPolls() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList
        restGuildPollMockMvc.perform(get("/api/guild-polls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildPoll.getId().intValue())))
            .andExpect(jsonPath("$.[*].pollName").value(hasItem(DEFAULT_POLL_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].textChannelId").value(hasItem(DEFAULT_TEXT_CHANNEL_ID.intValue())))
            .andExpect(jsonPath("$.[*].finishTime").value(hasItem(DEFAULT_FINISH_TIME.toString())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getGuildPoll() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get the guildPoll
        restGuildPollMockMvc.perform(get("/api/guild-polls/{id}", guildPoll.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(guildPoll.getId().intValue()))
            .andExpect(jsonPath("$.pollName").value(DEFAULT_POLL_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.textChannelId").value(DEFAULT_TEXT_CHANNEL_ID.intValue()))
            .andExpect(jsonPath("$.finishTime").value(DEFAULT_FINISH_TIME.toString()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllGuildPollsByPollNameIsEqualToSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where pollName equals to DEFAULT_POLL_NAME
        defaultGuildPollShouldBeFound("pollName.equals=" + DEFAULT_POLL_NAME);

        // Get all the guildPollList where pollName equals to UPDATED_POLL_NAME
        defaultGuildPollShouldNotBeFound("pollName.equals=" + UPDATED_POLL_NAME);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByPollNameIsInShouldWork() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where pollName in DEFAULT_POLL_NAME or UPDATED_POLL_NAME
        defaultGuildPollShouldBeFound("pollName.in=" + DEFAULT_POLL_NAME + "," + UPDATED_POLL_NAME);

        // Get all the guildPollList where pollName equals to UPDATED_POLL_NAME
        defaultGuildPollShouldNotBeFound("pollName.in=" + UPDATED_POLL_NAME);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByPollNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where pollName is not null
        defaultGuildPollShouldBeFound("pollName.specified=true");

        // Get all the guildPollList where pollName is null
        defaultGuildPollShouldNotBeFound("pollName.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildPollsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where description equals to DEFAULT_DESCRIPTION
        defaultGuildPollShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the guildPollList where description equals to UPDATED_DESCRIPTION
        defaultGuildPollShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultGuildPollShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the guildPollList where description equals to UPDATED_DESCRIPTION
        defaultGuildPollShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where description is not null
        defaultGuildPollShouldBeFound("description.specified=true");

        // Get all the guildPollList where description is null
        defaultGuildPollShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildPollsByTextChannelIdIsEqualToSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where textChannelId equals to DEFAULT_TEXT_CHANNEL_ID
        defaultGuildPollShouldBeFound("textChannelId.equals=" + DEFAULT_TEXT_CHANNEL_ID);

        // Get all the guildPollList where textChannelId equals to UPDATED_TEXT_CHANNEL_ID
        defaultGuildPollShouldNotBeFound("textChannelId.equals=" + UPDATED_TEXT_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByTextChannelIdIsInShouldWork() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where textChannelId in DEFAULT_TEXT_CHANNEL_ID or UPDATED_TEXT_CHANNEL_ID
        defaultGuildPollShouldBeFound("textChannelId.in=" + DEFAULT_TEXT_CHANNEL_ID + "," + UPDATED_TEXT_CHANNEL_ID);

        // Get all the guildPollList where textChannelId equals to UPDATED_TEXT_CHANNEL_ID
        defaultGuildPollShouldNotBeFound("textChannelId.in=" + UPDATED_TEXT_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByTextChannelIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where textChannelId is not null
        defaultGuildPollShouldBeFound("textChannelId.specified=true");

        // Get all the guildPollList where textChannelId is null
        defaultGuildPollShouldNotBeFound("textChannelId.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildPollsByTextChannelIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where textChannelId is greater than or equal to DEFAULT_TEXT_CHANNEL_ID
        defaultGuildPollShouldBeFound("textChannelId.greaterThanOrEqual=" + DEFAULT_TEXT_CHANNEL_ID);

        // Get all the guildPollList where textChannelId is greater than or equal to UPDATED_TEXT_CHANNEL_ID
        defaultGuildPollShouldNotBeFound("textChannelId.greaterThanOrEqual=" + UPDATED_TEXT_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByTextChannelIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where textChannelId is less than or equal to DEFAULT_TEXT_CHANNEL_ID
        defaultGuildPollShouldBeFound("textChannelId.lessThanOrEqual=" + DEFAULT_TEXT_CHANNEL_ID);

        // Get all the guildPollList where textChannelId is less than or equal to SMALLER_TEXT_CHANNEL_ID
        defaultGuildPollShouldNotBeFound("textChannelId.lessThanOrEqual=" + SMALLER_TEXT_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByTextChannelIdIsLessThanSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where textChannelId is less than DEFAULT_TEXT_CHANNEL_ID
        defaultGuildPollShouldNotBeFound("textChannelId.lessThan=" + DEFAULT_TEXT_CHANNEL_ID);

        // Get all the guildPollList where textChannelId is less than UPDATED_TEXT_CHANNEL_ID
        defaultGuildPollShouldBeFound("textChannelId.lessThan=" + UPDATED_TEXT_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByTextChannelIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where textChannelId is greater than DEFAULT_TEXT_CHANNEL_ID
        defaultGuildPollShouldNotBeFound("textChannelId.greaterThan=" + DEFAULT_TEXT_CHANNEL_ID);

        // Get all the guildPollList where textChannelId is greater than SMALLER_TEXT_CHANNEL_ID
        defaultGuildPollShouldBeFound("textChannelId.greaterThan=" + SMALLER_TEXT_CHANNEL_ID);
    }


    @Test
    @Transactional
    public void getAllGuildPollsByFinishTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where finishTime equals to DEFAULT_FINISH_TIME
        defaultGuildPollShouldBeFound("finishTime.equals=" + DEFAULT_FINISH_TIME);

        // Get all the guildPollList where finishTime equals to UPDATED_FINISH_TIME
        defaultGuildPollShouldNotBeFound("finishTime.equals=" + UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByFinishTimeIsInShouldWork() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where finishTime in DEFAULT_FINISH_TIME or UPDATED_FINISH_TIME
        defaultGuildPollShouldBeFound("finishTime.in=" + DEFAULT_FINISH_TIME + "," + UPDATED_FINISH_TIME);

        // Get all the guildPollList where finishTime equals to UPDATED_FINISH_TIME
        defaultGuildPollShouldNotBeFound("finishTime.in=" + UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByFinishTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where finishTime is not null
        defaultGuildPollShouldBeFound("finishTime.specified=true");

        // Get all the guildPollList where finishTime is null
        defaultGuildPollShouldNotBeFound("finishTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildPollsByFinishTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where finishTime is greater than or equal to DEFAULT_FINISH_TIME
        defaultGuildPollShouldBeFound("finishTime.greaterThanOrEqual=" + DEFAULT_FINISH_TIME);

        // Get all the guildPollList where finishTime is greater than or equal to UPDATED_FINISH_TIME
        defaultGuildPollShouldNotBeFound("finishTime.greaterThanOrEqual=" + UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByFinishTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where finishTime is less than or equal to DEFAULT_FINISH_TIME
        defaultGuildPollShouldBeFound("finishTime.lessThanOrEqual=" + DEFAULT_FINISH_TIME);

        // Get all the guildPollList where finishTime is less than or equal to SMALLER_FINISH_TIME
        defaultGuildPollShouldNotBeFound("finishTime.lessThanOrEqual=" + SMALLER_FINISH_TIME);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByFinishTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where finishTime is less than DEFAULT_FINISH_TIME
        defaultGuildPollShouldNotBeFound("finishTime.lessThan=" + DEFAULT_FINISH_TIME);

        // Get all the guildPollList where finishTime is less than UPDATED_FINISH_TIME
        defaultGuildPollShouldBeFound("finishTime.lessThan=" + UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByFinishTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where finishTime is greater than DEFAULT_FINISH_TIME
        defaultGuildPollShouldNotBeFound("finishTime.greaterThan=" + DEFAULT_FINISH_TIME);

        // Get all the guildPollList where finishTime is greater than SMALLER_FINISH_TIME
        defaultGuildPollShouldBeFound("finishTime.greaterThan=" + SMALLER_FINISH_TIME);
    }


    @Test
    @Transactional
    public void getAllGuildPollsByCompletedIsEqualToSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where completed equals to DEFAULT_COMPLETED
        defaultGuildPollShouldBeFound("completed.equals=" + DEFAULT_COMPLETED);

        // Get all the guildPollList where completed equals to UPDATED_COMPLETED
        defaultGuildPollShouldNotBeFound("completed.equals=" + UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByCompletedIsInShouldWork() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where completed in DEFAULT_COMPLETED or UPDATED_COMPLETED
        defaultGuildPollShouldBeFound("completed.in=" + DEFAULT_COMPLETED + "," + UPDATED_COMPLETED);

        // Get all the guildPollList where completed equals to UPDATED_COMPLETED
        defaultGuildPollShouldNotBeFound("completed.in=" + UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByCompletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where completed is not null
        defaultGuildPollShouldBeFound("completed.specified=true");

        // Get all the guildPollList where completed is null
        defaultGuildPollShouldNotBeFound("completed.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildPollsByGuildIdIsEqualToSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where guildId equals to DEFAULT_GUILD_ID
        defaultGuildPollShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);

        // Get all the guildPollList where guildId equals to UPDATED_GUILD_ID
        defaultGuildPollShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByGuildIdIsInShouldWork() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
        defaultGuildPollShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);

        // Get all the guildPollList where guildId equals to UPDATED_GUILD_ID
        defaultGuildPollShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByGuildIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where guildId is not null
        defaultGuildPollShouldBeFound("guildId.specified=true");

        // Get all the guildPollList where guildId is null
        defaultGuildPollShouldNotBeFound("guildId.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildPollsByGuildIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where guildId is greater than or equal to DEFAULT_GUILD_ID
        defaultGuildPollShouldBeFound("guildId.greaterThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the guildPollList where guildId is greater than or equal to UPDATED_GUILD_ID
        defaultGuildPollShouldNotBeFound("guildId.greaterThanOrEqual=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByGuildIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where guildId is less than or equal to DEFAULT_GUILD_ID
        defaultGuildPollShouldBeFound("guildId.lessThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the guildPollList where guildId is less than or equal to SMALLER_GUILD_ID
        defaultGuildPollShouldNotBeFound("guildId.lessThanOrEqual=" + SMALLER_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByGuildIdIsLessThanSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where guildId is less than DEFAULT_GUILD_ID
        defaultGuildPollShouldNotBeFound("guildId.lessThan=" + DEFAULT_GUILD_ID);

        // Get all the guildPollList where guildId is less than UPDATED_GUILD_ID
        defaultGuildPollShouldBeFound("guildId.lessThan=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildPollsByGuildIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);

        // Get all the guildPollList where guildId is greater than DEFAULT_GUILD_ID
        defaultGuildPollShouldNotBeFound("guildId.greaterThan=" + DEFAULT_GUILD_ID);

        // Get all the guildPollList where guildId is greater than SMALLER_GUILD_ID
        defaultGuildPollShouldBeFound("guildId.greaterThan=" + SMALLER_GUILD_ID);
    }


    @Test
    @Transactional
    public void getAllGuildPollsByPollItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);
        GuildPollItem pollItems = GuildPollItemResourceIT.createEntity(em);
        em.persist(pollItems);
        em.flush();
        guildPoll.setPollItems(pollItems);
        guildPollRepository.saveAndFlush(guildPoll);
        Long pollItemsId = pollItems.getId();

        // Get all the guildPollList where pollItems equals to pollItemsId
        defaultGuildPollShouldBeFound("pollItemsId.equals=" + pollItemsId);

        // Get all the guildPollList where pollItems equals to pollItemsId + 1
        defaultGuildPollShouldNotBeFound("pollItemsId.equals=" + (pollItemsId + 1));
    }


    @Test
    @Transactional
    public void getAllGuildPollsByPollServerIsEqualToSomething() throws Exception {
        // Initialize the database
        guildPollRepository.saveAndFlush(guildPoll);
        GuildServer pollServer = GuildServerResourceIT.createEntity(em);
        em.persist(pollServer);
        em.flush();
        guildPoll.setPollServer(pollServer);
        guildPollRepository.saveAndFlush(guildPoll);
        Long pollServerId = pollServer.getId();

        // Get all the guildPollList where pollServer equals to pollServerId
        defaultGuildPollShouldBeFound("pollServerId.equals=" + pollServerId);

        // Get all the guildPollList where pollServer equals to pollServerId + 1
        defaultGuildPollShouldNotBeFound("pollServerId.equals=" + (pollServerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGuildPollShouldBeFound(String filter) throws Exception {
        restGuildPollMockMvc.perform(get("/api/guild-polls?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildPoll.getId().intValue())))
            .andExpect(jsonPath("$.[*].pollName").value(hasItem(DEFAULT_POLL_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].textChannelId").value(hasItem(DEFAULT_TEXT_CHANNEL_ID.intValue())))
            .andExpect(jsonPath("$.[*].finishTime").value(hasItem(DEFAULT_FINISH_TIME.toString())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));

        // Check, that the count call also returns 1
        restGuildPollMockMvc.perform(get("/api/guild-polls/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGuildPollShouldNotBeFound(String filter) throws Exception {
        restGuildPollMockMvc.perform(get("/api/guild-polls?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGuildPollMockMvc.perform(get("/api/guild-polls/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGuildPoll() throws Exception {
        // Get the guildPoll
        restGuildPollMockMvc.perform(get("/api/guild-polls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGuildPoll() throws Exception {
        // Initialize the database
        guildPollService.save(guildPoll);

        int databaseSizeBeforeUpdate = guildPollRepository.findAll().size();

        // Update the guildPoll
        GuildPoll updatedGuildPoll = guildPollRepository.findById(guildPoll.getId()).get();
        // Disconnect from session so that the updates on updatedGuildPoll are not directly saved in db
        em.detach(updatedGuildPoll);
        updatedGuildPoll
            .pollName(UPDATED_POLL_NAME)
            .description(UPDATED_DESCRIPTION)
            .textChannelId(UPDATED_TEXT_CHANNEL_ID)
            .finishTime(UPDATED_FINISH_TIME)
            .completed(UPDATED_COMPLETED)
            .guildId(UPDATED_GUILD_ID);

        restGuildPollMockMvc.perform(put("/api/guild-polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGuildPoll)))
            .andExpect(status().isOk());

        // Validate the GuildPoll in the database
        List<GuildPoll> guildPollList = guildPollRepository.findAll();
        assertThat(guildPollList).hasSize(databaseSizeBeforeUpdate);
        GuildPoll testGuildPoll = guildPollList.get(guildPollList.size() - 1);
        assertThat(testGuildPoll.getPollName()).isEqualTo(UPDATED_POLL_NAME);
        assertThat(testGuildPoll.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGuildPoll.getTextChannelId()).isEqualTo(UPDATED_TEXT_CHANNEL_ID);
        assertThat(testGuildPoll.getFinishTime()).isEqualTo(UPDATED_FINISH_TIME);
        assertThat(testGuildPoll.isCompleted()).isEqualTo(UPDATED_COMPLETED);
        assertThat(testGuildPoll.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingGuildPoll() throws Exception {
        int databaseSizeBeforeUpdate = guildPollRepository.findAll().size();

        // Create the GuildPoll

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuildPollMockMvc.perform(put("/api/guild-polls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildPoll)))
            .andExpect(status().isBadRequest());

        // Validate the GuildPoll in the database
        List<GuildPoll> guildPollList = guildPollRepository.findAll();
        assertThat(guildPollList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGuildPoll() throws Exception {
        // Initialize the database
        guildPollService.save(guildPoll);

        int databaseSizeBeforeDelete = guildPollRepository.findAll().size();

        // Delete the guildPoll
        restGuildPollMockMvc.perform(delete("/api/guild-polls/{id}", guildPoll.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GuildPoll> guildPollList = guildPollRepository.findAll();
        assertThat(guildPollList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuildPoll.class);
        GuildPoll guildPoll1 = new GuildPoll();
        guildPoll1.setId(1L);
        GuildPoll guildPoll2 = new GuildPoll();
        guildPoll2.setId(guildPoll1.getId());
        assertThat(guildPoll1).isEqualTo(guildPoll2);
        guildPoll2.setId(2L);
        assertThat(guildPoll1).isNotEqualTo(guildPoll2);
        guildPoll1.setId(null);
        assertThat(guildPoll1).isNotEqualTo(guildPoll2);
    }
}
