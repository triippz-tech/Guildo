package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.ScheduledAnnouncement;
import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.repository.ScheduledAnnouncementRepository;
import com.triippztech.guildo.service.guild.ScheduledAnnouncementService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.guild.ScheduledAnnouncementQueryService;

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
 * Integration tests for the {@link ScheduledAnnouncementResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class ScheduledAnnouncementResourceIT {

    private static final String DEFAULT_ANNOUCEMENT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_ANNOUCEMENT_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ANNOUCEMENT_IMG_URL = "AAAAAAAAAA";
    private static final String UPDATED_ANNOUCEMENT_IMG_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ANNOUCEMENT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ANNOUCEMENT_MESSAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_ANNOUCEMENT_FIRE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ANNOUCEMENT_FIRE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_ANNOUCEMENT_FIRE = Instant.ofEpochMilli(-1L);

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;
    private static final Long SMALLER_GUILD_ID = 1L - 1L;

    @Autowired
    private ScheduledAnnouncementRepository scheduledAnnouncementRepository;

    @Autowired
    private ScheduledAnnouncementService scheduledAnnouncementService;

    @Autowired
    private ScheduledAnnouncementQueryService scheduledAnnouncementQueryService;

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

    private MockMvc restScheduledAnnouncementMockMvc;

    private ScheduledAnnouncement scheduledAnnouncement;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ScheduledAnnouncementResource scheduledAnnouncementResource = new ScheduledAnnouncementResource(scheduledAnnouncementService, scheduledAnnouncementQueryService);
        this.restScheduledAnnouncementMockMvc = MockMvcBuilders.standaloneSetup(scheduledAnnouncementResource)
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
    public static ScheduledAnnouncement createEntity(EntityManager em) {
        ScheduledAnnouncement scheduledAnnouncement = new ScheduledAnnouncement()
            .annoucementTitle(DEFAULT_ANNOUCEMENT_TITLE)
            .annoucementImgUrl(DEFAULT_ANNOUCEMENT_IMG_URL)
            .annoucementMessage(DEFAULT_ANNOUCEMENT_MESSAGE)
            .annoucementFire(DEFAULT_ANNOUCEMENT_FIRE)
            .guildId(DEFAULT_GUILD_ID);
        return scheduledAnnouncement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduledAnnouncement createUpdatedEntity(EntityManager em) {
        ScheduledAnnouncement scheduledAnnouncement = new ScheduledAnnouncement()
            .annoucementTitle(UPDATED_ANNOUCEMENT_TITLE)
            .annoucementImgUrl(UPDATED_ANNOUCEMENT_IMG_URL)
            .annoucementMessage(UPDATED_ANNOUCEMENT_MESSAGE)
            .annoucementFire(UPDATED_ANNOUCEMENT_FIRE)
            .guildId(UPDATED_GUILD_ID);
        return scheduledAnnouncement;
    }

    @BeforeEach
    public void initTest() {
        scheduledAnnouncement = createEntity(em);
    }

    @Test
    @Transactional
    public void createScheduledAnnouncement() throws Exception {
        int databaseSizeBeforeCreate = scheduledAnnouncementRepository.findAll().size();

        // Create the ScheduledAnnouncement
        restScheduledAnnouncementMockMvc.perform(post("/api/scheduled-announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduledAnnouncement)))
            .andExpect(status().isCreated());

        // Validate the ScheduledAnnouncement in the database
        List<ScheduledAnnouncement> scheduledAnnouncementList = scheduledAnnouncementRepository.findAll();
        assertThat(scheduledAnnouncementList).hasSize(databaseSizeBeforeCreate + 1);
        ScheduledAnnouncement testScheduledAnnouncement = scheduledAnnouncementList.get(scheduledAnnouncementList.size() - 1);
        assertThat(testScheduledAnnouncement.getAnnoucementTitle()).isEqualTo(DEFAULT_ANNOUCEMENT_TITLE);
        assertThat(testScheduledAnnouncement.getAnnoucementImgUrl()).isEqualTo(DEFAULT_ANNOUCEMENT_IMG_URL);
        assertThat(testScheduledAnnouncement.getAnnoucementMessage()).isEqualTo(DEFAULT_ANNOUCEMENT_MESSAGE);
        assertThat(testScheduledAnnouncement.getAnnoucementFire()).isEqualTo(DEFAULT_ANNOUCEMENT_FIRE);
        assertThat(testScheduledAnnouncement.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
    }

    @Test
    @Transactional
    public void createScheduledAnnouncementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scheduledAnnouncementRepository.findAll().size();

        // Create the ScheduledAnnouncement with an existing ID
        scheduledAnnouncement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduledAnnouncementMockMvc.perform(post("/api/scheduled-announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduledAnnouncement)))
            .andExpect(status().isBadRequest());

        // Validate the ScheduledAnnouncement in the database
        List<ScheduledAnnouncement> scheduledAnnouncementList = scheduledAnnouncementRepository.findAll();
        assertThat(scheduledAnnouncementList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkAnnoucementTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduledAnnouncementRepository.findAll().size();
        // set the field null
        scheduledAnnouncement.setAnnoucementTitle(null);

        // Create the ScheduledAnnouncement, which fails.

        restScheduledAnnouncementMockMvc.perform(post("/api/scheduled-announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduledAnnouncement)))
            .andExpect(status().isBadRequest());

        List<ScheduledAnnouncement> scheduledAnnouncementList = scheduledAnnouncementRepository.findAll();
        assertThat(scheduledAnnouncementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnnoucementImgUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduledAnnouncementRepository.findAll().size();
        // set the field null
        scheduledAnnouncement.setAnnoucementImgUrl(null);

        // Create the ScheduledAnnouncement, which fails.

        restScheduledAnnouncementMockMvc.perform(post("/api/scheduled-announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduledAnnouncement)))
            .andExpect(status().isBadRequest());

        List<ScheduledAnnouncement> scheduledAnnouncementList = scheduledAnnouncementRepository.findAll();
        assertThat(scheduledAnnouncementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnnoucementFireIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduledAnnouncementRepository.findAll().size();
        // set the field null
        scheduledAnnouncement.setAnnoucementFire(null);

        // Create the ScheduledAnnouncement, which fails.

        restScheduledAnnouncementMockMvc.perform(post("/api/scheduled-announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduledAnnouncement)))
            .andExpect(status().isBadRequest());

        List<ScheduledAnnouncement> scheduledAnnouncementList = scheduledAnnouncementRepository.findAll();
        assertThat(scheduledAnnouncementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGuildIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduledAnnouncementRepository.findAll().size();
        // set the field null
        scheduledAnnouncement.setGuildId(null);

        // Create the ScheduledAnnouncement, which fails.

        restScheduledAnnouncementMockMvc.perform(post("/api/scheduled-announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduledAnnouncement)))
            .andExpect(status().isBadRequest());

        List<ScheduledAnnouncement> scheduledAnnouncementList = scheduledAnnouncementRepository.findAll();
        assertThat(scheduledAnnouncementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncements() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList
        restScheduledAnnouncementMockMvc.perform(get("/api/scheduled-announcements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduledAnnouncement.getId().intValue())))
            .andExpect(jsonPath("$.[*].annoucementTitle").value(hasItem(DEFAULT_ANNOUCEMENT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].annoucementImgUrl").value(hasItem(DEFAULT_ANNOUCEMENT_IMG_URL.toString())))
            .andExpect(jsonPath("$.[*].annoucementMessage").value(hasItem(DEFAULT_ANNOUCEMENT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].annoucementFire").value(hasItem(DEFAULT_ANNOUCEMENT_FIRE.toString())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getScheduledAnnouncement() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get the scheduledAnnouncement
        restScheduledAnnouncementMockMvc.perform(get("/api/scheduled-announcements/{id}", scheduledAnnouncement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(scheduledAnnouncement.getId().intValue()))
            .andExpect(jsonPath("$.annoucementTitle").value(DEFAULT_ANNOUCEMENT_TITLE.toString()))
            .andExpect(jsonPath("$.annoucementImgUrl").value(DEFAULT_ANNOUCEMENT_IMG_URL.toString()))
            .andExpect(jsonPath("$.annoucementMessage").value(DEFAULT_ANNOUCEMENT_MESSAGE.toString()))
            .andExpect(jsonPath("$.annoucementFire").value(DEFAULT_ANNOUCEMENT_FIRE.toString()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByAnnoucementTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where annoucementTitle equals to DEFAULT_ANNOUCEMENT_TITLE
        defaultScheduledAnnouncementShouldBeFound("annoucementTitle.equals=" + DEFAULT_ANNOUCEMENT_TITLE);

        // Get all the scheduledAnnouncementList where annoucementTitle equals to UPDATED_ANNOUCEMENT_TITLE
        defaultScheduledAnnouncementShouldNotBeFound("annoucementTitle.equals=" + UPDATED_ANNOUCEMENT_TITLE);
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByAnnoucementTitleIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where annoucementTitle in DEFAULT_ANNOUCEMENT_TITLE or UPDATED_ANNOUCEMENT_TITLE
        defaultScheduledAnnouncementShouldBeFound("annoucementTitle.in=" + DEFAULT_ANNOUCEMENT_TITLE + "," + UPDATED_ANNOUCEMENT_TITLE);

        // Get all the scheduledAnnouncementList where annoucementTitle equals to UPDATED_ANNOUCEMENT_TITLE
        defaultScheduledAnnouncementShouldNotBeFound("annoucementTitle.in=" + UPDATED_ANNOUCEMENT_TITLE);
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByAnnoucementTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where annoucementTitle is not null
        defaultScheduledAnnouncementShouldBeFound("annoucementTitle.specified=true");

        // Get all the scheduledAnnouncementList where annoucementTitle is null
        defaultScheduledAnnouncementShouldNotBeFound("annoucementTitle.specified=false");
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByAnnoucementImgUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where annoucementImgUrl equals to DEFAULT_ANNOUCEMENT_IMG_URL
        defaultScheduledAnnouncementShouldBeFound("annoucementImgUrl.equals=" + DEFAULT_ANNOUCEMENT_IMG_URL);

        // Get all the scheduledAnnouncementList where annoucementImgUrl equals to UPDATED_ANNOUCEMENT_IMG_URL
        defaultScheduledAnnouncementShouldNotBeFound("annoucementImgUrl.equals=" + UPDATED_ANNOUCEMENT_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByAnnoucementImgUrlIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where annoucementImgUrl in DEFAULT_ANNOUCEMENT_IMG_URL or UPDATED_ANNOUCEMENT_IMG_URL
        defaultScheduledAnnouncementShouldBeFound("annoucementImgUrl.in=" + DEFAULT_ANNOUCEMENT_IMG_URL + "," + UPDATED_ANNOUCEMENT_IMG_URL);

        // Get all the scheduledAnnouncementList where annoucementImgUrl equals to UPDATED_ANNOUCEMENT_IMG_URL
        defaultScheduledAnnouncementShouldNotBeFound("annoucementImgUrl.in=" + UPDATED_ANNOUCEMENT_IMG_URL);
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByAnnoucementImgUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where annoucementImgUrl is not null
        defaultScheduledAnnouncementShouldBeFound("annoucementImgUrl.specified=true");

        // Get all the scheduledAnnouncementList where annoucementImgUrl is null
        defaultScheduledAnnouncementShouldNotBeFound("annoucementImgUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByAnnoucementFireIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where annoucementFire equals to DEFAULT_ANNOUCEMENT_FIRE
        defaultScheduledAnnouncementShouldBeFound("annoucementFire.equals=" + DEFAULT_ANNOUCEMENT_FIRE);

        // Get all the scheduledAnnouncementList where annoucementFire equals to UPDATED_ANNOUCEMENT_FIRE
        defaultScheduledAnnouncementShouldNotBeFound("annoucementFire.equals=" + UPDATED_ANNOUCEMENT_FIRE);
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByAnnoucementFireIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where annoucementFire in DEFAULT_ANNOUCEMENT_FIRE or UPDATED_ANNOUCEMENT_FIRE
        defaultScheduledAnnouncementShouldBeFound("annoucementFire.in=" + DEFAULT_ANNOUCEMENT_FIRE + "," + UPDATED_ANNOUCEMENT_FIRE);

        // Get all the scheduledAnnouncementList where annoucementFire equals to UPDATED_ANNOUCEMENT_FIRE
        defaultScheduledAnnouncementShouldNotBeFound("annoucementFire.in=" + UPDATED_ANNOUCEMENT_FIRE);
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByAnnoucementFireIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where annoucementFire is not null
        defaultScheduledAnnouncementShouldBeFound("annoucementFire.specified=true");

        // Get all the scheduledAnnouncementList where annoucementFire is null
        defaultScheduledAnnouncementShouldNotBeFound("annoucementFire.specified=false");
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByGuildIdIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where guildId equals to DEFAULT_GUILD_ID
        defaultScheduledAnnouncementShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);

        // Get all the scheduledAnnouncementList where guildId equals to UPDATED_GUILD_ID
        defaultScheduledAnnouncementShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByGuildIdIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
        defaultScheduledAnnouncementShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);

        // Get all the scheduledAnnouncementList where guildId equals to UPDATED_GUILD_ID
        defaultScheduledAnnouncementShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByGuildIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where guildId is not null
        defaultScheduledAnnouncementShouldBeFound("guildId.specified=true");

        // Get all the scheduledAnnouncementList where guildId is null
        defaultScheduledAnnouncementShouldNotBeFound("guildId.specified=false");
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByGuildIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where guildId is greater than or equal to DEFAULT_GUILD_ID
        defaultScheduledAnnouncementShouldBeFound("guildId.greaterThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the scheduledAnnouncementList where guildId is greater than or equal to UPDATED_GUILD_ID
        defaultScheduledAnnouncementShouldNotBeFound("guildId.greaterThanOrEqual=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByGuildIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where guildId is less than or equal to DEFAULT_GUILD_ID
        defaultScheduledAnnouncementShouldBeFound("guildId.lessThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the scheduledAnnouncementList where guildId is less than or equal to SMALLER_GUILD_ID
        defaultScheduledAnnouncementShouldNotBeFound("guildId.lessThanOrEqual=" + SMALLER_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByGuildIdIsLessThanSomething() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where guildId is less than DEFAULT_GUILD_ID
        defaultScheduledAnnouncementShouldNotBeFound("guildId.lessThan=" + DEFAULT_GUILD_ID);

        // Get all the scheduledAnnouncementList where guildId is less than UPDATED_GUILD_ID
        defaultScheduledAnnouncementShouldBeFound("guildId.lessThan=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByGuildIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);

        // Get all the scheduledAnnouncementList where guildId is greater than DEFAULT_GUILD_ID
        defaultScheduledAnnouncementShouldNotBeFound("guildId.greaterThan=" + DEFAULT_GUILD_ID);

        // Get all the scheduledAnnouncementList where guildId is greater than SMALLER_GUILD_ID
        defaultScheduledAnnouncementShouldBeFound("guildId.greaterThan=" + SMALLER_GUILD_ID);
    }


    @Test
    @Transactional
    public void getAllScheduledAnnouncementsByAnnouceGuildIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);
        GuildServer annouceGuild = GuildServerResourceIT.createEntity(em);
        em.persist(annouceGuild);
        em.flush();
        scheduledAnnouncement.setAnnouceGuild(annouceGuild);
        scheduledAnnouncementRepository.saveAndFlush(scheduledAnnouncement);
        Long annouceGuildId = annouceGuild.getId();

        // Get all the scheduledAnnouncementList where annouceGuild equals to annouceGuildId
        defaultScheduledAnnouncementShouldBeFound("annouceGuildId.equals=" + annouceGuildId);

        // Get all the scheduledAnnouncementList where annouceGuild equals to annouceGuildId + 1
        defaultScheduledAnnouncementShouldNotBeFound("annouceGuildId.equals=" + (annouceGuildId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultScheduledAnnouncementShouldBeFound(String filter) throws Exception {
        restScheduledAnnouncementMockMvc.perform(get("/api/scheduled-announcements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduledAnnouncement.getId().intValue())))
            .andExpect(jsonPath("$.[*].annoucementTitle").value(hasItem(DEFAULT_ANNOUCEMENT_TITLE)))
            .andExpect(jsonPath("$.[*].annoucementImgUrl").value(hasItem(DEFAULT_ANNOUCEMENT_IMG_URL)))
            .andExpect(jsonPath("$.[*].annoucementMessage").value(hasItem(DEFAULT_ANNOUCEMENT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].annoucementFire").value(hasItem(DEFAULT_ANNOUCEMENT_FIRE.toString())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));

        // Check, that the count call also returns 1
        restScheduledAnnouncementMockMvc.perform(get("/api/scheduled-announcements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultScheduledAnnouncementShouldNotBeFound(String filter) throws Exception {
        restScheduledAnnouncementMockMvc.perform(get("/api/scheduled-announcements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restScheduledAnnouncementMockMvc.perform(get("/api/scheduled-announcements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingScheduledAnnouncement() throws Exception {
        // Get the scheduledAnnouncement
        restScheduledAnnouncementMockMvc.perform(get("/api/scheduled-announcements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScheduledAnnouncement() throws Exception {
        // Initialize the database
        scheduledAnnouncementService.save(scheduledAnnouncement);

        int databaseSizeBeforeUpdate = scheduledAnnouncementRepository.findAll().size();

        // Update the scheduledAnnouncement
        ScheduledAnnouncement updatedScheduledAnnouncement = scheduledAnnouncementRepository.findById(scheduledAnnouncement.getId()).get();
        // Disconnect from session so that the updates on updatedScheduledAnnouncement are not directly saved in db
        em.detach(updatedScheduledAnnouncement);
        updatedScheduledAnnouncement
            .annoucementTitle(UPDATED_ANNOUCEMENT_TITLE)
            .annoucementImgUrl(UPDATED_ANNOUCEMENT_IMG_URL)
            .annoucementMessage(UPDATED_ANNOUCEMENT_MESSAGE)
            .annoucementFire(UPDATED_ANNOUCEMENT_FIRE)
            .guildId(UPDATED_GUILD_ID);

        restScheduledAnnouncementMockMvc.perform(put("/api/scheduled-announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedScheduledAnnouncement)))
            .andExpect(status().isOk());

        // Validate the ScheduledAnnouncement in the database
        List<ScheduledAnnouncement> scheduledAnnouncementList = scheduledAnnouncementRepository.findAll();
        assertThat(scheduledAnnouncementList).hasSize(databaseSizeBeforeUpdate);
        ScheduledAnnouncement testScheduledAnnouncement = scheduledAnnouncementList.get(scheduledAnnouncementList.size() - 1);
        assertThat(testScheduledAnnouncement.getAnnoucementTitle()).isEqualTo(UPDATED_ANNOUCEMENT_TITLE);
        assertThat(testScheduledAnnouncement.getAnnoucementImgUrl()).isEqualTo(UPDATED_ANNOUCEMENT_IMG_URL);
        assertThat(testScheduledAnnouncement.getAnnoucementMessage()).isEqualTo(UPDATED_ANNOUCEMENT_MESSAGE);
        assertThat(testScheduledAnnouncement.getAnnoucementFire()).isEqualTo(UPDATED_ANNOUCEMENT_FIRE);
        assertThat(testScheduledAnnouncement.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingScheduledAnnouncement() throws Exception {
        int databaseSizeBeforeUpdate = scheduledAnnouncementRepository.findAll().size();

        // Create the ScheduledAnnouncement

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduledAnnouncementMockMvc.perform(put("/api/scheduled-announcements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduledAnnouncement)))
            .andExpect(status().isBadRequest());

        // Validate the ScheduledAnnouncement in the database
        List<ScheduledAnnouncement> scheduledAnnouncementList = scheduledAnnouncementRepository.findAll();
        assertThat(scheduledAnnouncementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteScheduledAnnouncement() throws Exception {
        // Initialize the database
        scheduledAnnouncementService.save(scheduledAnnouncement);

        int databaseSizeBeforeDelete = scheduledAnnouncementRepository.findAll().size();

        // Delete the scheduledAnnouncement
        restScheduledAnnouncementMockMvc.perform(delete("/api/scheduled-announcements/{id}", scheduledAnnouncement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ScheduledAnnouncement> scheduledAnnouncementList = scheduledAnnouncementRepository.findAll();
        assertThat(scheduledAnnouncementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduledAnnouncement.class);
        ScheduledAnnouncement scheduledAnnouncement1 = new ScheduledAnnouncement();
        scheduledAnnouncement1.setId(1L);
        ScheduledAnnouncement scheduledAnnouncement2 = new ScheduledAnnouncement();
        scheduledAnnouncement2.setId(scheduledAnnouncement1.getId());
        assertThat(scheduledAnnouncement1).isEqualTo(scheduledAnnouncement2);
        scheduledAnnouncement2.setId(2L);
        assertThat(scheduledAnnouncement1).isNotEqualTo(scheduledAnnouncement2);
        scheduledAnnouncement1.setId(null);
        assertThat(scheduledAnnouncement1).isNotEqualTo(scheduledAnnouncement2);
    }
}
