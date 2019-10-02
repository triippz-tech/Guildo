package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.domain.GuildServerProfile;
import com.triippztech.guildo.domain.GuildApplicationForm;
import com.triippztech.guildo.domain.GuildServerSettings;
import com.triippztech.guildo.domain.WelcomeMessage;
import com.triippztech.guildo.domain.GuildPoll;
import com.triippztech.guildo.domain.ScheduledAnnouncement;
import com.triippztech.guildo.domain.GuildEvent;
import com.triippztech.guildo.domain.GiveAway;
import com.triippztech.guildo.domain.ModerationLogItem;
import com.triippztech.guildo.domain.ServerLogItem;
import com.triippztech.guildo.domain.TempBan;
import com.triippztech.guildo.domain.Mute;
import com.triippztech.guildo.domain.GuildApplication;
import com.triippztech.guildo.repository.GuildServerRepository;
import com.triippztech.guildo.service.GuildServerService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.dto.GuildServerCriteria;
import com.triippztech.guildo.service.GuildServerQueryService;

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

import com.triippztech.guildo.domain.enumeration.GuildServerLevel;
/**
 * Integration tests for the {@link GuildServerResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class GuildServerResourceIT {

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;
    private static final Long SMALLER_GUILD_ID = 1L - 1L;

    private static final String DEFAULT_GUILD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GUILD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final Long DEFAULT_OWNER = 1L;
    private static final Long UPDATED_OWNER = 2L;
    private static final Long SMALLER_OWNER = 1L - 1L;

    private static final GuildServerLevel DEFAULT_SERVER_LEVEL = GuildServerLevel.STANDARD;
    private static final GuildServerLevel UPDATED_SERVER_LEVEL = GuildServerLevel.PRO;

    @Autowired
    private GuildServerRepository guildServerRepository;

    @Autowired
    private GuildServerService guildServerService;

    @Autowired
    private GuildServerQueryService guildServerQueryService;

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

    private MockMvc restGuildServerMockMvc;

    private GuildServer guildServer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GuildServerResource guildServerResource = new GuildServerResource(guildServerService, guildServerQueryService);
        this.restGuildServerMockMvc = MockMvcBuilders.standaloneSetup(guildServerResource)
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
    public static GuildServer createEntity(EntityManager em) {
        GuildServer guildServer = new GuildServer()
            .guildId(DEFAULT_GUILD_ID)
            .guildName(DEFAULT_GUILD_NAME)
            .icon(DEFAULT_ICON)
            .owner(DEFAULT_OWNER)
            .serverLevel(DEFAULT_SERVER_LEVEL);
        return guildServer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GuildServer createUpdatedEntity(EntityManager em) {
        GuildServer guildServer = new GuildServer()
            .guildId(UPDATED_GUILD_ID)
            .guildName(UPDATED_GUILD_NAME)
            .icon(UPDATED_ICON)
            .owner(UPDATED_OWNER)
            .serverLevel(UPDATED_SERVER_LEVEL);
        return guildServer;
    }

    @BeforeEach
    public void initTest() {
        guildServer = createEntity(em);
    }

    @Test
    @Transactional
    public void createGuildServer() throws Exception {
        int databaseSizeBeforeCreate = guildServerRepository.findAll().size();

        // Create the GuildServer
        restGuildServerMockMvc.perform(post("/api/guild-servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServer)))
            .andExpect(status().isCreated());

        // Validate the GuildServer in the database
        List<GuildServer> guildServerList = guildServerRepository.findAll();
        assertThat(guildServerList).hasSize(databaseSizeBeforeCreate + 1);
        GuildServer testGuildServer = guildServerList.get(guildServerList.size() - 1);
        assertThat(testGuildServer.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
        assertThat(testGuildServer.getGuildName()).isEqualTo(DEFAULT_GUILD_NAME);
        assertThat(testGuildServer.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testGuildServer.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testGuildServer.getServerLevel()).isEqualTo(DEFAULT_SERVER_LEVEL);
    }

    @Test
    @Transactional
    public void createGuildServerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = guildServerRepository.findAll().size();

        // Create the GuildServer with an existing ID
        guildServer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuildServerMockMvc.perform(post("/api/guild-servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServer)))
            .andExpect(status().isBadRequest());

        // Validate the GuildServer in the database
        List<GuildServer> guildServerList = guildServerRepository.findAll();
        assertThat(guildServerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkGuildIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildServerRepository.findAll().size();
        // set the field null
        guildServer.setGuildId(null);

        // Create the GuildServer, which fails.

        restGuildServerMockMvc.perform(post("/api/guild-servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServer)))
            .andExpect(status().isBadRequest());

        List<GuildServer> guildServerList = guildServerRepository.findAll();
        assertThat(guildServerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGuildNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildServerRepository.findAll().size();
        // set the field null
        guildServer.setGuildName(null);

        // Create the GuildServer, which fails.

        restGuildServerMockMvc.perform(post("/api/guild-servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServer)))
            .andExpect(status().isBadRequest());

        List<GuildServer> guildServerList = guildServerRepository.findAll();
        assertThat(guildServerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildServerRepository.findAll().size();
        // set the field null
        guildServer.setOwner(null);

        // Create the GuildServer, which fails.

        restGuildServerMockMvc.perform(post("/api/guild-servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServer)))
            .andExpect(status().isBadRequest());

        List<GuildServer> guildServerList = guildServerRepository.findAll();
        assertThat(guildServerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServerLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildServerRepository.findAll().size();
        // set the field null
        guildServer.setServerLevel(null);

        // Create the GuildServer, which fails.

        restGuildServerMockMvc.perform(post("/api/guild-servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServer)))
            .andExpect(status().isBadRequest());

        List<GuildServer> guildServerList = guildServerRepository.findAll();
        assertThat(guildServerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGuildServers() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList
        restGuildServerMockMvc.perform(get("/api/guild-servers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildServer.getId().intValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
            .andExpect(jsonPath("$.[*].guildName").value(hasItem(DEFAULT_GUILD_NAME.toString())))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON.toString())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.intValue())))
            .andExpect(jsonPath("$.[*].serverLevel").value(hasItem(DEFAULT_SERVER_LEVEL.toString())));
    }
    
    @Test
    @Transactional
    public void getGuildServer() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get the guildServer
        restGuildServerMockMvc.perform(get("/api/guild-servers/{id}", guildServer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(guildServer.getId().intValue()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
            .andExpect(jsonPath("$.guildName").value(DEFAULT_GUILD_NAME.toString()))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON.toString()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER.intValue()))
            .andExpect(jsonPath("$.serverLevel").value(DEFAULT_SERVER_LEVEL.toString()));
    }

    @Test
    @Transactional
    public void getAllGuildServersByGuildIdIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where guildId equals to DEFAULT_GUILD_ID
        defaultGuildServerShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);

        // Get all the guildServerList where guildId equals to UPDATED_GUILD_ID
        defaultGuildServerShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildServersByGuildIdIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
        defaultGuildServerShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);

        // Get all the guildServerList where guildId equals to UPDATED_GUILD_ID
        defaultGuildServerShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildServersByGuildIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where guildId is not null
        defaultGuildServerShouldBeFound("guildId.specified=true");

        // Get all the guildServerList where guildId is null
        defaultGuildServerShouldNotBeFound("guildId.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServersByGuildIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where guildId is greater than or equal to DEFAULT_GUILD_ID
        defaultGuildServerShouldBeFound("guildId.greaterThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the guildServerList where guildId is greater than or equal to UPDATED_GUILD_ID
        defaultGuildServerShouldNotBeFound("guildId.greaterThanOrEqual=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildServersByGuildIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where guildId is less than or equal to DEFAULT_GUILD_ID
        defaultGuildServerShouldBeFound("guildId.lessThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the guildServerList where guildId is less than or equal to SMALLER_GUILD_ID
        defaultGuildServerShouldNotBeFound("guildId.lessThanOrEqual=" + SMALLER_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildServersByGuildIdIsLessThanSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where guildId is less than DEFAULT_GUILD_ID
        defaultGuildServerShouldNotBeFound("guildId.lessThan=" + DEFAULT_GUILD_ID);

        // Get all the guildServerList where guildId is less than UPDATED_GUILD_ID
        defaultGuildServerShouldBeFound("guildId.lessThan=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildServersByGuildIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where guildId is greater than DEFAULT_GUILD_ID
        defaultGuildServerShouldNotBeFound("guildId.greaterThan=" + DEFAULT_GUILD_ID);

        // Get all the guildServerList where guildId is greater than SMALLER_GUILD_ID
        defaultGuildServerShouldBeFound("guildId.greaterThan=" + SMALLER_GUILD_ID);
    }


    @Test
    @Transactional
    public void getAllGuildServersByGuildNameIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where guildName equals to DEFAULT_GUILD_NAME
        defaultGuildServerShouldBeFound("guildName.equals=" + DEFAULT_GUILD_NAME);

        // Get all the guildServerList where guildName equals to UPDATED_GUILD_NAME
        defaultGuildServerShouldNotBeFound("guildName.equals=" + UPDATED_GUILD_NAME);
    }

    @Test
    @Transactional
    public void getAllGuildServersByGuildNameIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where guildName in DEFAULT_GUILD_NAME or UPDATED_GUILD_NAME
        defaultGuildServerShouldBeFound("guildName.in=" + DEFAULT_GUILD_NAME + "," + UPDATED_GUILD_NAME);

        // Get all the guildServerList where guildName equals to UPDATED_GUILD_NAME
        defaultGuildServerShouldNotBeFound("guildName.in=" + UPDATED_GUILD_NAME);
    }

    @Test
    @Transactional
    public void getAllGuildServersByGuildNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where guildName is not null
        defaultGuildServerShouldBeFound("guildName.specified=true");

        // Get all the guildServerList where guildName is null
        defaultGuildServerShouldNotBeFound("guildName.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServersByIconIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where icon equals to DEFAULT_ICON
        defaultGuildServerShouldBeFound("icon.equals=" + DEFAULT_ICON);

        // Get all the guildServerList where icon equals to UPDATED_ICON
        defaultGuildServerShouldNotBeFound("icon.equals=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    public void getAllGuildServersByIconIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where icon in DEFAULT_ICON or UPDATED_ICON
        defaultGuildServerShouldBeFound("icon.in=" + DEFAULT_ICON + "," + UPDATED_ICON);

        // Get all the guildServerList where icon equals to UPDATED_ICON
        defaultGuildServerShouldNotBeFound("icon.in=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    public void getAllGuildServersByIconIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where icon is not null
        defaultGuildServerShouldBeFound("icon.specified=true");

        // Get all the guildServerList where icon is null
        defaultGuildServerShouldNotBeFound("icon.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServersByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where owner equals to DEFAULT_OWNER
        defaultGuildServerShouldBeFound("owner.equals=" + DEFAULT_OWNER);

        // Get all the guildServerList where owner equals to UPDATED_OWNER
        defaultGuildServerShouldNotBeFound("owner.equals=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    public void getAllGuildServersByOwnerIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where owner in DEFAULT_OWNER or UPDATED_OWNER
        defaultGuildServerShouldBeFound("owner.in=" + DEFAULT_OWNER + "," + UPDATED_OWNER);

        // Get all the guildServerList where owner equals to UPDATED_OWNER
        defaultGuildServerShouldNotBeFound("owner.in=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    public void getAllGuildServersByOwnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where owner is not null
        defaultGuildServerShouldBeFound("owner.specified=true");

        // Get all the guildServerList where owner is null
        defaultGuildServerShouldNotBeFound("owner.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServersByOwnerIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where owner is greater than or equal to DEFAULT_OWNER
        defaultGuildServerShouldBeFound("owner.greaterThanOrEqual=" + DEFAULT_OWNER);

        // Get all the guildServerList where owner is greater than or equal to UPDATED_OWNER
        defaultGuildServerShouldNotBeFound("owner.greaterThanOrEqual=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    public void getAllGuildServersByOwnerIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where owner is less than or equal to DEFAULT_OWNER
        defaultGuildServerShouldBeFound("owner.lessThanOrEqual=" + DEFAULT_OWNER);

        // Get all the guildServerList where owner is less than or equal to SMALLER_OWNER
        defaultGuildServerShouldNotBeFound("owner.lessThanOrEqual=" + SMALLER_OWNER);
    }

    @Test
    @Transactional
    public void getAllGuildServersByOwnerIsLessThanSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where owner is less than DEFAULT_OWNER
        defaultGuildServerShouldNotBeFound("owner.lessThan=" + DEFAULT_OWNER);

        // Get all the guildServerList where owner is less than UPDATED_OWNER
        defaultGuildServerShouldBeFound("owner.lessThan=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    public void getAllGuildServersByOwnerIsGreaterThanSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where owner is greater than DEFAULT_OWNER
        defaultGuildServerShouldNotBeFound("owner.greaterThan=" + DEFAULT_OWNER);

        // Get all the guildServerList where owner is greater than SMALLER_OWNER
        defaultGuildServerShouldBeFound("owner.greaterThan=" + SMALLER_OWNER);
    }


    @Test
    @Transactional
    public void getAllGuildServersByServerLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where serverLevel equals to DEFAULT_SERVER_LEVEL
        defaultGuildServerShouldBeFound("serverLevel.equals=" + DEFAULT_SERVER_LEVEL);

        // Get all the guildServerList where serverLevel equals to UPDATED_SERVER_LEVEL
        defaultGuildServerShouldNotBeFound("serverLevel.equals=" + UPDATED_SERVER_LEVEL);
    }

    @Test
    @Transactional
    public void getAllGuildServersByServerLevelIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where serverLevel in DEFAULT_SERVER_LEVEL or UPDATED_SERVER_LEVEL
        defaultGuildServerShouldBeFound("serverLevel.in=" + DEFAULT_SERVER_LEVEL + "," + UPDATED_SERVER_LEVEL);

        // Get all the guildServerList where serverLevel equals to UPDATED_SERVER_LEVEL
        defaultGuildServerShouldNotBeFound("serverLevel.in=" + UPDATED_SERVER_LEVEL);
    }

    @Test
    @Transactional
    public void getAllGuildServersByServerLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);

        // Get all the guildServerList where serverLevel is not null
        defaultGuildServerShouldBeFound("serverLevel.specified=true");

        // Get all the guildServerList where serverLevel is null
        defaultGuildServerShouldNotBeFound("serverLevel.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServersByGuildProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);
        GuildServerProfile guildProfile = GuildServerProfileResourceIT.createEntity(em);
        em.persist(guildProfile);
        em.flush();
        guildServer.setGuildProfile(guildProfile);
        guildServerRepository.saveAndFlush(guildServer);
        Long guildProfileId = guildProfile.getId();

        // Get all the guildServerList where guildProfile equals to guildProfileId
        defaultGuildServerShouldBeFound("guildProfileId.equals=" + guildProfileId);

        // Get all the guildServerList where guildProfile equals to guildProfileId + 1
        defaultGuildServerShouldNotBeFound("guildProfileId.equals=" + (guildProfileId + 1));
    }


    @Test
    @Transactional
    public void getAllGuildServersByApplicationFormIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);
        GuildApplicationForm applicationForm = GuildApplicationFormResourceIT.createEntity(em);
        em.persist(applicationForm);
        em.flush();
        guildServer.setApplicationForm(applicationForm);
        guildServerRepository.saveAndFlush(guildServer);
        Long applicationFormId = applicationForm.getId();

        // Get all the guildServerList where applicationForm equals to applicationFormId
        defaultGuildServerShouldBeFound("applicationFormId.equals=" + applicationFormId);

        // Get all the guildServerList where applicationForm equals to applicationFormId + 1
        defaultGuildServerShouldNotBeFound("applicationFormId.equals=" + (applicationFormId + 1));
    }


    @Test
    @Transactional
    public void getAllGuildServersByGuildSettingsIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);
        GuildServerSettings guildSettings = GuildServerSettingsResourceIT.createEntity(em);
        em.persist(guildSettings);
        em.flush();
        guildServer.setGuildSettings(guildSettings);
        guildServerRepository.saveAndFlush(guildServer);
        Long guildSettingsId = guildSettings.getId();

        // Get all the guildServerList where guildSettings equals to guildSettingsId
        defaultGuildServerShouldBeFound("guildSettingsId.equals=" + guildSettingsId);

        // Get all the guildServerList where guildSettings equals to guildSettingsId + 1
        defaultGuildServerShouldNotBeFound("guildSettingsId.equals=" + (guildSettingsId + 1));
    }


    @Test
    @Transactional
    public void getAllGuildServersByWelcomeMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);
        WelcomeMessage welcomeMessage = WelcomeMessageResourceIT.createEntity(em);
        em.persist(welcomeMessage);
        em.flush();
        guildServer.setWelcomeMessage(welcomeMessage);
        guildServerRepository.saveAndFlush(guildServer);
        Long welcomeMessageId = welcomeMessage.getId();

        // Get all the guildServerList where welcomeMessage equals to welcomeMessageId
        defaultGuildServerShouldBeFound("welcomeMessageId.equals=" + welcomeMessageId);

        // Get all the guildServerList where welcomeMessage equals to welcomeMessageId + 1
        defaultGuildServerShouldNotBeFound("welcomeMessageId.equals=" + (welcomeMessageId + 1));
    }


    @Test
    @Transactional
    public void getAllGuildServersByServerPollsIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);
        GuildPoll serverPolls = GuildPollResourceIT.createEntity(em);
        em.persist(serverPolls);
        em.flush();
        guildServer.addServerPolls(serverPolls);
        guildServerRepository.saveAndFlush(guildServer);
        Long serverPollsId = serverPolls.getId();

        // Get all the guildServerList where serverPolls equals to serverPollsId
        defaultGuildServerShouldBeFound("serverPollsId.equals=" + serverPollsId);

        // Get all the guildServerList where serverPolls equals to serverPollsId + 1
        defaultGuildServerShouldNotBeFound("serverPollsId.equals=" + (serverPollsId + 1));
    }


    @Test
    @Transactional
    public void getAllGuildServersByGuildAnnoucementsIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);
        ScheduledAnnouncement guildAnnoucements = ScheduledAnnouncementResourceIT.createEntity(em);
        em.persist(guildAnnoucements);
        em.flush();
        guildServer.addGuildAnnoucements(guildAnnoucements);
        guildServerRepository.saveAndFlush(guildServer);
        Long guildAnnoucementsId = guildAnnoucements.getId();

        // Get all the guildServerList where guildAnnoucements equals to guildAnnoucementsId
        defaultGuildServerShouldBeFound("guildAnnoucementsId.equals=" + guildAnnoucementsId);

        // Get all the guildServerList where guildAnnoucements equals to guildAnnoucementsId + 1
        defaultGuildServerShouldNotBeFound("guildAnnoucementsId.equals=" + (guildAnnoucementsId + 1));
    }


    @Test
    @Transactional
    public void getAllGuildServersByGuildEventsIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);
        GuildEvent guildEvents = GuildEventResourceIT.createEntity(em);
        em.persist(guildEvents);
        em.flush();
        guildServer.addGuildEvents(guildEvents);
        guildServerRepository.saveAndFlush(guildServer);
        Long guildEventsId = guildEvents.getId();

        // Get all the guildServerList where guildEvents equals to guildEventsId
        defaultGuildServerShouldBeFound("guildEventsId.equals=" + guildEventsId);

        // Get all the guildServerList where guildEvents equals to guildEventsId + 1
        defaultGuildServerShouldNotBeFound("guildEventsId.equals=" + (guildEventsId + 1));
    }


    @Test
    @Transactional
    public void getAllGuildServersByGiveAwaysIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);
        GiveAway giveAways = GiveAwayResourceIT.createEntity(em);
        em.persist(giveAways);
        em.flush();
        guildServer.addGiveAways(giveAways);
        guildServerRepository.saveAndFlush(guildServer);
        Long giveAwaysId = giveAways.getId();

        // Get all the guildServerList where giveAways equals to giveAwaysId
        defaultGuildServerShouldBeFound("giveAwaysId.equals=" + giveAwaysId);

        // Get all the guildServerList where giveAways equals to giveAwaysId + 1
        defaultGuildServerShouldNotBeFound("giveAwaysId.equals=" + (giveAwaysId + 1));
    }


    @Test
    @Transactional
    public void getAllGuildServersByModLogItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);
        ModerationLogItem modLogItems = ModerationLogItemResourceIT.createEntity(em);
        em.persist(modLogItems);
        em.flush();
        guildServer.addModLogItems(modLogItems);
        guildServerRepository.saveAndFlush(guildServer);
        Long modLogItemsId = modLogItems.getId();

        // Get all the guildServerList where modLogItems equals to modLogItemsId
        defaultGuildServerShouldBeFound("modLogItemsId.equals=" + modLogItemsId);

        // Get all the guildServerList where modLogItems equals to modLogItemsId + 1
        defaultGuildServerShouldNotBeFound("modLogItemsId.equals=" + (modLogItemsId + 1));
    }


    @Test
    @Transactional
    public void getAllGuildServersByServerLogItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);
        ServerLogItem serverLogItems = ServerLogItemResourceIT.createEntity(em);
        em.persist(serverLogItems);
        em.flush();
        guildServer.addServerLogItems(serverLogItems);
        guildServerRepository.saveAndFlush(guildServer);
        Long serverLogItemsId = serverLogItems.getId();

        // Get all the guildServerList where serverLogItems equals to serverLogItemsId
        defaultGuildServerShouldBeFound("serverLogItemsId.equals=" + serverLogItemsId);

        // Get all the guildServerList where serverLogItems equals to serverLogItemsId + 1
        defaultGuildServerShouldNotBeFound("serverLogItemsId.equals=" + (serverLogItemsId + 1));
    }


    @Test
    @Transactional
    public void getAllGuildServersByGuildTempBansIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);
        TempBan guildTempBans = TempBanResourceIT.createEntity(em);
        em.persist(guildTempBans);
        em.flush();
        guildServer.addGuildTempBans(guildTempBans);
        guildServerRepository.saveAndFlush(guildServer);
        Long guildTempBansId = guildTempBans.getId();

        // Get all the guildServerList where guildTempBans equals to guildTempBansId
        defaultGuildServerShouldBeFound("guildTempBansId.equals=" + guildTempBansId);

        // Get all the guildServerList where guildTempBans equals to guildTempBansId + 1
        defaultGuildServerShouldNotBeFound("guildTempBansId.equals=" + (guildTempBansId + 1));
    }


    @Test
    @Transactional
    public void getAllGuildServersByMutedUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);
        Mute mutedUsers = MuteResourceIT.createEntity(em);
        em.persist(mutedUsers);
        em.flush();
        guildServer.addMutedUsers(mutedUsers);
        guildServerRepository.saveAndFlush(guildServer);
        Long mutedUsersId = mutedUsers.getId();

        // Get all the guildServerList where mutedUsers equals to mutedUsersId
        defaultGuildServerShouldBeFound("mutedUsersId.equals=" + mutedUsersId);

        // Get all the guildServerList where mutedUsers equals to mutedUsersId + 1
        defaultGuildServerShouldNotBeFound("mutedUsersId.equals=" + (mutedUsersId + 1));
    }


    @Test
    @Transactional
    public void getAllGuildServersByGuildApplicationsIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerRepository.saveAndFlush(guildServer);
        GuildApplication guildApplications = GuildApplicationResourceIT.createEntity(em);
        em.persist(guildApplications);
        em.flush();
        guildServer.addGuildApplications(guildApplications);
        guildServerRepository.saveAndFlush(guildServer);
        Long guildApplicationsId = guildApplications.getId();

        // Get all the guildServerList where guildApplications equals to guildApplicationsId
        defaultGuildServerShouldBeFound("guildApplicationsId.equals=" + guildApplicationsId);

        // Get all the guildServerList where guildApplications equals to guildApplicationsId + 1
        defaultGuildServerShouldNotBeFound("guildApplicationsId.equals=" + (guildApplicationsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGuildServerShouldBeFound(String filter) throws Exception {
        restGuildServerMockMvc.perform(get("/api/guild-servers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildServer.getId().intValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
            .andExpect(jsonPath("$.[*].guildName").value(hasItem(DEFAULT_GUILD_NAME)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.intValue())))
            .andExpect(jsonPath("$.[*].serverLevel").value(hasItem(DEFAULT_SERVER_LEVEL.toString())));

        // Check, that the count call also returns 1
        restGuildServerMockMvc.perform(get("/api/guild-servers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGuildServerShouldNotBeFound(String filter) throws Exception {
        restGuildServerMockMvc.perform(get("/api/guild-servers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGuildServerMockMvc.perform(get("/api/guild-servers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGuildServer() throws Exception {
        // Get the guildServer
        restGuildServerMockMvc.perform(get("/api/guild-servers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGuildServer() throws Exception {
        // Initialize the database
        guildServerService.save(guildServer);

        int databaseSizeBeforeUpdate = guildServerRepository.findAll().size();

        // Update the guildServer
        GuildServer updatedGuildServer = guildServerRepository.findById(guildServer.getId()).get();
        // Disconnect from session so that the updates on updatedGuildServer are not directly saved in db
        em.detach(updatedGuildServer);
        updatedGuildServer
            .guildId(UPDATED_GUILD_ID)
            .guildName(UPDATED_GUILD_NAME)
            .icon(UPDATED_ICON)
            .owner(UPDATED_OWNER)
            .serverLevel(UPDATED_SERVER_LEVEL);

        restGuildServerMockMvc.perform(put("/api/guild-servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGuildServer)))
            .andExpect(status().isOk());

        // Validate the GuildServer in the database
        List<GuildServer> guildServerList = guildServerRepository.findAll();
        assertThat(guildServerList).hasSize(databaseSizeBeforeUpdate);
        GuildServer testGuildServer = guildServerList.get(guildServerList.size() - 1);
        assertThat(testGuildServer.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
        assertThat(testGuildServer.getGuildName()).isEqualTo(UPDATED_GUILD_NAME);
        assertThat(testGuildServer.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testGuildServer.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testGuildServer.getServerLevel()).isEqualTo(UPDATED_SERVER_LEVEL);
    }

    @Test
    @Transactional
    public void updateNonExistingGuildServer() throws Exception {
        int databaseSizeBeforeUpdate = guildServerRepository.findAll().size();

        // Create the GuildServer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuildServerMockMvc.perform(put("/api/guild-servers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServer)))
            .andExpect(status().isBadRequest());

        // Validate the GuildServer in the database
        List<GuildServer> guildServerList = guildServerRepository.findAll();
        assertThat(guildServerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGuildServer() throws Exception {
        // Initialize the database
        guildServerService.save(guildServer);

        int databaseSizeBeforeDelete = guildServerRepository.findAll().size();

        // Delete the guildServer
        restGuildServerMockMvc.perform(delete("/api/guild-servers/{id}", guildServer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GuildServer> guildServerList = guildServerRepository.findAll();
        assertThat(guildServerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuildServer.class);
        GuildServer guildServer1 = new GuildServer();
        guildServer1.setId(1L);
        GuildServer guildServer2 = new GuildServer();
        guildServer2.setId(guildServer1.getId());
        assertThat(guildServer1).isEqualTo(guildServer2);
        guildServer2.setId(2L);
        assertThat(guildServer1).isNotEqualTo(guildServer2);
        guildServer1.setId(null);
        assertThat(guildServer1).isNotEqualTo(guildServer2);
    }
}
