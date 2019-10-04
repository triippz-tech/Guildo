package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.GuildServerSettings;
import com.triippztech.guildo.domain.AutoModeration;
import com.triippztech.guildo.domain.Punishment;
import com.triippztech.guildo.repository.GuildServerSettingsRepository;
import com.triippztech.guildo.service.server.GuildServerSettingsService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.server.GuildServerSettingsQueryService;

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
 * Integration tests for the {@link GuildServerSettingsResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class GuildServerSettingsResourceIT {

    private static final String DEFAULT_PREFIX = "AAAAAAAAAA";
    private static final String UPDATED_PREFIX = "BBBBBBBBBB";

    private static final String DEFAULT_TIMEZONE = "AAAAAAAAAA";
    private static final String UPDATED_TIMEZONE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RAID_MODE_ENABLED = false;
    private static final Boolean UPDATED_RAID_MODE_ENABLED = true;

    private static final String DEFAULT_RAID_MODE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_RAID_MODE_REASON = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAX_STRIKES = 1;
    private static final Integer UPDATED_MAX_STRIKES = 2;
    private static final Integer SMALLER_MAX_STRIKES = 1 - 1;

    private static final Boolean DEFAULT_ACCEPTING_APPLICATIONS = false;
    private static final Boolean UPDATED_ACCEPTING_APPLICATIONS = true;

    @Autowired
    private GuildServerSettingsRepository guildServerSettingsRepository;

    @Autowired
    private GuildServerSettingsService guildServerSettingsService;

    @Autowired
    private GuildServerSettingsQueryService guildServerSettingsQueryService;

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

    private MockMvc restGuildServerSettingsMockMvc;

    private GuildServerSettings guildServerSettings;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GuildServerSettingsResource guildServerSettingsResource = new GuildServerSettingsResource(guildServerSettingsService, guildServerSettingsQueryService);
        this.restGuildServerSettingsMockMvc = MockMvcBuilders.standaloneSetup(guildServerSettingsResource)
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
    public static GuildServerSettings createEntity(EntityManager em) {
        GuildServerSettings guildServerSettings = new GuildServerSettings()
            .prefix(DEFAULT_PREFIX)
            .timezone(DEFAULT_TIMEZONE)
            .raidModeEnabled(DEFAULT_RAID_MODE_ENABLED)
            .raidModeReason(DEFAULT_RAID_MODE_REASON)
            .maxStrikes(DEFAULT_MAX_STRIKES)
            .acceptingApplications(DEFAULT_ACCEPTING_APPLICATIONS);
        return guildServerSettings;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GuildServerSettings createUpdatedEntity(EntityManager em) {
        GuildServerSettings guildServerSettings = new GuildServerSettings()
            .prefix(UPDATED_PREFIX)
            .timezone(UPDATED_TIMEZONE)
            .raidModeEnabled(UPDATED_RAID_MODE_ENABLED)
            .raidModeReason(UPDATED_RAID_MODE_REASON)
            .maxStrikes(UPDATED_MAX_STRIKES)
            .acceptingApplications(UPDATED_ACCEPTING_APPLICATIONS);
        return guildServerSettings;
    }

    @BeforeEach
    public void initTest() {
        guildServerSettings = createEntity(em);
    }

    @Test
    @Transactional
    public void createGuildServerSettings() throws Exception {
        int databaseSizeBeforeCreate = guildServerSettingsRepository.findAll().size();

        // Create the GuildServerSettings
        restGuildServerSettingsMockMvc.perform(post("/api/guild-server-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerSettings)))
            .andExpect(status().isCreated());

        // Validate the GuildServerSettings in the database
        List<GuildServerSettings> guildServerSettingsList = guildServerSettingsRepository.findAll();
        assertThat(guildServerSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        GuildServerSettings testGuildServerSettings = guildServerSettingsList.get(guildServerSettingsList.size() - 1);
        assertThat(testGuildServerSettings.getPrefix()).isEqualTo(DEFAULT_PREFIX);
        assertThat(testGuildServerSettings.getTimezone()).isEqualTo(DEFAULT_TIMEZONE);
        assertThat(testGuildServerSettings.isRaidModeEnabled()).isEqualTo(DEFAULT_RAID_MODE_ENABLED);
        assertThat(testGuildServerSettings.getRaidModeReason()).isEqualTo(DEFAULT_RAID_MODE_REASON);
        assertThat(testGuildServerSettings.getMaxStrikes()).isEqualTo(DEFAULT_MAX_STRIKES);
        assertThat(testGuildServerSettings.isAcceptingApplications()).isEqualTo(DEFAULT_ACCEPTING_APPLICATIONS);
    }

    @Test
    @Transactional
    public void createGuildServerSettingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = guildServerSettingsRepository.findAll().size();

        // Create the GuildServerSettings with an existing ID
        guildServerSettings.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuildServerSettingsMockMvc.perform(post("/api/guild-server-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerSettings)))
            .andExpect(status().isBadRequest());

        // Validate the GuildServerSettings in the database
        List<GuildServerSettings> guildServerSettingsList = guildServerSettingsRepository.findAll();
        assertThat(guildServerSettingsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkRaidModeEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildServerSettingsRepository.findAll().size();
        // set the field null
        guildServerSettings.setRaidModeEnabled(null);

        // Create the GuildServerSettings, which fails.

        restGuildServerSettingsMockMvc.perform(post("/api/guild-server-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerSettings)))
            .andExpect(status().isBadRequest());

        List<GuildServerSettings> guildServerSettingsList = guildServerSettingsRepository.findAll();
        assertThat(guildServerSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRaidModeReasonIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildServerSettingsRepository.findAll().size();
        // set the field null
        guildServerSettings.setRaidModeReason(null);

        // Create the GuildServerSettings, which fails.

        restGuildServerSettingsMockMvc.perform(post("/api/guild-server-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerSettings)))
            .andExpect(status().isBadRequest());

        List<GuildServerSettings> guildServerSettingsList = guildServerSettingsRepository.findAll();
        assertThat(guildServerSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaxStrikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildServerSettingsRepository.findAll().size();
        // set the field null
        guildServerSettings.setMaxStrikes(null);

        // Create the GuildServerSettings, which fails.

        restGuildServerSettingsMockMvc.perform(post("/api/guild-server-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerSettings)))
            .andExpect(status().isBadRequest());

        List<GuildServerSettings> guildServerSettingsList = guildServerSettingsRepository.findAll();
        assertThat(guildServerSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAcceptingApplicationsIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildServerSettingsRepository.findAll().size();
        // set the field null
        guildServerSettings.setAcceptingApplications(null);

        // Create the GuildServerSettings, which fails.

        restGuildServerSettingsMockMvc.perform(post("/api/guild-server-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerSettings)))
            .andExpect(status().isBadRequest());

        List<GuildServerSettings> guildServerSettingsList = guildServerSettingsRepository.findAll();
        assertThat(guildServerSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettings() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList
        restGuildServerSettingsMockMvc.perform(get("/api/guild-server-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildServerSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX.toString())))
            .andExpect(jsonPath("$.[*].timezone").value(hasItem(DEFAULT_TIMEZONE.toString())))
            .andExpect(jsonPath("$.[*].raidModeEnabled").value(hasItem(DEFAULT_RAID_MODE_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].raidModeReason").value(hasItem(DEFAULT_RAID_MODE_REASON.toString())))
            .andExpect(jsonPath("$.[*].maxStrikes").value(hasItem(DEFAULT_MAX_STRIKES)))
            .andExpect(jsonPath("$.[*].acceptingApplications").value(hasItem(DEFAULT_ACCEPTING_APPLICATIONS.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getGuildServerSettings() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get the guildServerSettings
        restGuildServerSettingsMockMvc.perform(get("/api/guild-server-settings/{id}", guildServerSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(guildServerSettings.getId().intValue()))
            .andExpect(jsonPath("$.prefix").value(DEFAULT_PREFIX.toString()))
            .andExpect(jsonPath("$.timezone").value(DEFAULT_TIMEZONE.toString()))
            .andExpect(jsonPath("$.raidModeEnabled").value(DEFAULT_RAID_MODE_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.raidModeReason").value(DEFAULT_RAID_MODE_REASON.toString()))
            .andExpect(jsonPath("$.maxStrikes").value(DEFAULT_MAX_STRIKES))
            .andExpect(jsonPath("$.acceptingApplications").value(DEFAULT_ACCEPTING_APPLICATIONS.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByPrefixIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where prefix equals to DEFAULT_PREFIX
        defaultGuildServerSettingsShouldBeFound("prefix.equals=" + DEFAULT_PREFIX);

        // Get all the guildServerSettingsList where prefix equals to UPDATED_PREFIX
        defaultGuildServerSettingsShouldNotBeFound("prefix.equals=" + UPDATED_PREFIX);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByPrefixIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where prefix in DEFAULT_PREFIX or UPDATED_PREFIX
        defaultGuildServerSettingsShouldBeFound("prefix.in=" + DEFAULT_PREFIX + "," + UPDATED_PREFIX);

        // Get all the guildServerSettingsList where prefix equals to UPDATED_PREFIX
        defaultGuildServerSettingsShouldNotBeFound("prefix.in=" + UPDATED_PREFIX);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByPrefixIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where prefix is not null
        defaultGuildServerSettingsShouldBeFound("prefix.specified=true");

        // Get all the guildServerSettingsList where prefix is null
        defaultGuildServerSettingsShouldNotBeFound("prefix.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByTimezoneIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where timezone equals to DEFAULT_TIMEZONE
        defaultGuildServerSettingsShouldBeFound("timezone.equals=" + DEFAULT_TIMEZONE);

        // Get all the guildServerSettingsList where timezone equals to UPDATED_TIMEZONE
        defaultGuildServerSettingsShouldNotBeFound("timezone.equals=" + UPDATED_TIMEZONE);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByTimezoneIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where timezone in DEFAULT_TIMEZONE or UPDATED_TIMEZONE
        defaultGuildServerSettingsShouldBeFound("timezone.in=" + DEFAULT_TIMEZONE + "," + UPDATED_TIMEZONE);

        // Get all the guildServerSettingsList where timezone equals to UPDATED_TIMEZONE
        defaultGuildServerSettingsShouldNotBeFound("timezone.in=" + UPDATED_TIMEZONE);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByTimezoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where timezone is not null
        defaultGuildServerSettingsShouldBeFound("timezone.specified=true");

        // Get all the guildServerSettingsList where timezone is null
        defaultGuildServerSettingsShouldNotBeFound("timezone.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByRaidModeEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where raidModeEnabled equals to DEFAULT_RAID_MODE_ENABLED
        defaultGuildServerSettingsShouldBeFound("raidModeEnabled.equals=" + DEFAULT_RAID_MODE_ENABLED);

        // Get all the guildServerSettingsList where raidModeEnabled equals to UPDATED_RAID_MODE_ENABLED
        defaultGuildServerSettingsShouldNotBeFound("raidModeEnabled.equals=" + UPDATED_RAID_MODE_ENABLED);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByRaidModeEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where raidModeEnabled in DEFAULT_RAID_MODE_ENABLED or UPDATED_RAID_MODE_ENABLED
        defaultGuildServerSettingsShouldBeFound("raidModeEnabled.in=" + DEFAULT_RAID_MODE_ENABLED + "," + UPDATED_RAID_MODE_ENABLED);

        // Get all the guildServerSettingsList where raidModeEnabled equals to UPDATED_RAID_MODE_ENABLED
        defaultGuildServerSettingsShouldNotBeFound("raidModeEnabled.in=" + UPDATED_RAID_MODE_ENABLED);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByRaidModeEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where raidModeEnabled is not null
        defaultGuildServerSettingsShouldBeFound("raidModeEnabled.specified=true");

        // Get all the guildServerSettingsList where raidModeEnabled is null
        defaultGuildServerSettingsShouldNotBeFound("raidModeEnabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByRaidModeReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where raidModeReason equals to DEFAULT_RAID_MODE_REASON
        defaultGuildServerSettingsShouldBeFound("raidModeReason.equals=" + DEFAULT_RAID_MODE_REASON);

        // Get all the guildServerSettingsList where raidModeReason equals to UPDATED_RAID_MODE_REASON
        defaultGuildServerSettingsShouldNotBeFound("raidModeReason.equals=" + UPDATED_RAID_MODE_REASON);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByRaidModeReasonIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where raidModeReason in DEFAULT_RAID_MODE_REASON or UPDATED_RAID_MODE_REASON
        defaultGuildServerSettingsShouldBeFound("raidModeReason.in=" + DEFAULT_RAID_MODE_REASON + "," + UPDATED_RAID_MODE_REASON);

        // Get all the guildServerSettingsList where raidModeReason equals to UPDATED_RAID_MODE_REASON
        defaultGuildServerSettingsShouldNotBeFound("raidModeReason.in=" + UPDATED_RAID_MODE_REASON);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByRaidModeReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where raidModeReason is not null
        defaultGuildServerSettingsShouldBeFound("raidModeReason.specified=true");

        // Get all the guildServerSettingsList where raidModeReason is null
        defaultGuildServerSettingsShouldNotBeFound("raidModeReason.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByMaxStrikesIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where maxStrikes equals to DEFAULT_MAX_STRIKES
        defaultGuildServerSettingsShouldBeFound("maxStrikes.equals=" + DEFAULT_MAX_STRIKES);

        // Get all the guildServerSettingsList where maxStrikes equals to UPDATED_MAX_STRIKES
        defaultGuildServerSettingsShouldNotBeFound("maxStrikes.equals=" + UPDATED_MAX_STRIKES);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByMaxStrikesIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where maxStrikes in DEFAULT_MAX_STRIKES or UPDATED_MAX_STRIKES
        defaultGuildServerSettingsShouldBeFound("maxStrikes.in=" + DEFAULT_MAX_STRIKES + "," + UPDATED_MAX_STRIKES);

        // Get all the guildServerSettingsList where maxStrikes equals to UPDATED_MAX_STRIKES
        defaultGuildServerSettingsShouldNotBeFound("maxStrikes.in=" + UPDATED_MAX_STRIKES);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByMaxStrikesIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where maxStrikes is not null
        defaultGuildServerSettingsShouldBeFound("maxStrikes.specified=true");

        // Get all the guildServerSettingsList where maxStrikes is null
        defaultGuildServerSettingsShouldNotBeFound("maxStrikes.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByMaxStrikesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where maxStrikes is greater than or equal to DEFAULT_MAX_STRIKES
        defaultGuildServerSettingsShouldBeFound("maxStrikes.greaterThanOrEqual=" + DEFAULT_MAX_STRIKES);

        // Get all the guildServerSettingsList where maxStrikes is greater than or equal to UPDATED_MAX_STRIKES
        defaultGuildServerSettingsShouldNotBeFound("maxStrikes.greaterThanOrEqual=" + UPDATED_MAX_STRIKES);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByMaxStrikesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where maxStrikes is less than or equal to DEFAULT_MAX_STRIKES
        defaultGuildServerSettingsShouldBeFound("maxStrikes.lessThanOrEqual=" + DEFAULT_MAX_STRIKES);

        // Get all the guildServerSettingsList where maxStrikes is less than or equal to SMALLER_MAX_STRIKES
        defaultGuildServerSettingsShouldNotBeFound("maxStrikes.lessThanOrEqual=" + SMALLER_MAX_STRIKES);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByMaxStrikesIsLessThanSomething() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where maxStrikes is less than DEFAULT_MAX_STRIKES
        defaultGuildServerSettingsShouldNotBeFound("maxStrikes.lessThan=" + DEFAULT_MAX_STRIKES);

        // Get all the guildServerSettingsList where maxStrikes is less than UPDATED_MAX_STRIKES
        defaultGuildServerSettingsShouldBeFound("maxStrikes.lessThan=" + UPDATED_MAX_STRIKES);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByMaxStrikesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where maxStrikes is greater than DEFAULT_MAX_STRIKES
        defaultGuildServerSettingsShouldNotBeFound("maxStrikes.greaterThan=" + DEFAULT_MAX_STRIKES);

        // Get all the guildServerSettingsList where maxStrikes is greater than SMALLER_MAX_STRIKES
        defaultGuildServerSettingsShouldBeFound("maxStrikes.greaterThan=" + SMALLER_MAX_STRIKES);
    }


    @Test
    @Transactional
    public void getAllGuildServerSettingsByAcceptingApplicationsIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where acceptingApplications equals to DEFAULT_ACCEPTING_APPLICATIONS
        defaultGuildServerSettingsShouldBeFound("acceptingApplications.equals=" + DEFAULT_ACCEPTING_APPLICATIONS);

        // Get all the guildServerSettingsList where acceptingApplications equals to UPDATED_ACCEPTING_APPLICATIONS
        defaultGuildServerSettingsShouldNotBeFound("acceptingApplications.equals=" + UPDATED_ACCEPTING_APPLICATIONS);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByAcceptingApplicationsIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where acceptingApplications in DEFAULT_ACCEPTING_APPLICATIONS or UPDATED_ACCEPTING_APPLICATIONS
        defaultGuildServerSettingsShouldBeFound("acceptingApplications.in=" + DEFAULT_ACCEPTING_APPLICATIONS + "," + UPDATED_ACCEPTING_APPLICATIONS);

        // Get all the guildServerSettingsList where acceptingApplications equals to UPDATED_ACCEPTING_APPLICATIONS
        defaultGuildServerSettingsShouldNotBeFound("acceptingApplications.in=" + UPDATED_ACCEPTING_APPLICATIONS);
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByAcceptingApplicationsIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);

        // Get all the guildServerSettingsList where acceptingApplications is not null
        defaultGuildServerSettingsShouldBeFound("acceptingApplications.specified=true");

        // Get all the guildServerSettingsList where acceptingApplications is null
        defaultGuildServerSettingsShouldNotBeFound("acceptingApplications.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServerSettingsByAutoModConfigIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);
        AutoModeration autoModConfig = AutoModerationResourceIT.createEntity(em);
        em.persist(autoModConfig);
        em.flush();
        guildServerSettings.setAutoModConfig(autoModConfig);
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);
        Long autoModConfigId = autoModConfig.getId();

        // Get all the guildServerSettingsList where autoModConfig equals to autoModConfigId
        defaultGuildServerSettingsShouldBeFound("autoModConfigId.equals=" + autoModConfigId);

        // Get all the guildServerSettingsList where autoModConfig equals to autoModConfigId + 1
        defaultGuildServerSettingsShouldNotBeFound("autoModConfigId.equals=" + (autoModConfigId + 1));
    }


    @Test
    @Transactional
    public void getAllGuildServerSettingsByPunishmentConfigIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);
        Punishment punishmentConfig = PunishmentResourceIT.createEntity(em);
        em.persist(punishmentConfig);
        em.flush();
        guildServerSettings.setPunishmentConfig(punishmentConfig);
        guildServerSettingsRepository.saveAndFlush(guildServerSettings);
        Long punishmentConfigId = punishmentConfig.getId();

        // Get all the guildServerSettingsList where punishmentConfig equals to punishmentConfigId
        defaultGuildServerSettingsShouldBeFound("punishmentConfigId.equals=" + punishmentConfigId);

        // Get all the guildServerSettingsList where punishmentConfig equals to punishmentConfigId + 1
        defaultGuildServerSettingsShouldNotBeFound("punishmentConfigId.equals=" + (punishmentConfigId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGuildServerSettingsShouldBeFound(String filter) throws Exception {
        restGuildServerSettingsMockMvc.perform(get("/api/guild-server-settings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildServerSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX)))
            .andExpect(jsonPath("$.[*].timezone").value(hasItem(DEFAULT_TIMEZONE)))
            .andExpect(jsonPath("$.[*].raidModeEnabled").value(hasItem(DEFAULT_RAID_MODE_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].raidModeReason").value(hasItem(DEFAULT_RAID_MODE_REASON)))
            .andExpect(jsonPath("$.[*].maxStrikes").value(hasItem(DEFAULT_MAX_STRIKES)))
            .andExpect(jsonPath("$.[*].acceptingApplications").value(hasItem(DEFAULT_ACCEPTING_APPLICATIONS.booleanValue())));

        // Check, that the count call also returns 1
        restGuildServerSettingsMockMvc.perform(get("/api/guild-server-settings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGuildServerSettingsShouldNotBeFound(String filter) throws Exception {
        restGuildServerSettingsMockMvc.perform(get("/api/guild-server-settings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGuildServerSettingsMockMvc.perform(get("/api/guild-server-settings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGuildServerSettings() throws Exception {
        // Get the guildServerSettings
        restGuildServerSettingsMockMvc.perform(get("/api/guild-server-settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGuildServerSettings() throws Exception {
        // Initialize the database
        guildServerSettingsService.save(guildServerSettings);

        int databaseSizeBeforeUpdate = guildServerSettingsRepository.findAll().size();

        // Update the guildServerSettings
        GuildServerSettings updatedGuildServerSettings = guildServerSettingsRepository.findById(guildServerSettings.getId()).get();
        // Disconnect from session so that the updates on updatedGuildServerSettings are not directly saved in db
        em.detach(updatedGuildServerSettings);
        updatedGuildServerSettings
            .prefix(UPDATED_PREFIX)
            .timezone(UPDATED_TIMEZONE)
            .raidModeEnabled(UPDATED_RAID_MODE_ENABLED)
            .raidModeReason(UPDATED_RAID_MODE_REASON)
            .maxStrikes(UPDATED_MAX_STRIKES)
            .acceptingApplications(UPDATED_ACCEPTING_APPLICATIONS);

        restGuildServerSettingsMockMvc.perform(put("/api/guild-server-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGuildServerSettings)))
            .andExpect(status().isOk());

        // Validate the GuildServerSettings in the database
        List<GuildServerSettings> guildServerSettingsList = guildServerSettingsRepository.findAll();
        assertThat(guildServerSettingsList).hasSize(databaseSizeBeforeUpdate);
        GuildServerSettings testGuildServerSettings = guildServerSettingsList.get(guildServerSettingsList.size() - 1);
        assertThat(testGuildServerSettings.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testGuildServerSettings.getTimezone()).isEqualTo(UPDATED_TIMEZONE);
        assertThat(testGuildServerSettings.isRaidModeEnabled()).isEqualTo(UPDATED_RAID_MODE_ENABLED);
        assertThat(testGuildServerSettings.getRaidModeReason()).isEqualTo(UPDATED_RAID_MODE_REASON);
        assertThat(testGuildServerSettings.getMaxStrikes()).isEqualTo(UPDATED_MAX_STRIKES);
        assertThat(testGuildServerSettings.isAcceptingApplications()).isEqualTo(UPDATED_ACCEPTING_APPLICATIONS);
    }

    @Test
    @Transactional
    public void updateNonExistingGuildServerSettings() throws Exception {
        int databaseSizeBeforeUpdate = guildServerSettingsRepository.findAll().size();

        // Create the GuildServerSettings

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuildServerSettingsMockMvc.perform(put("/api/guild-server-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerSettings)))
            .andExpect(status().isBadRequest());

        // Validate the GuildServerSettings in the database
        List<GuildServerSettings> guildServerSettingsList = guildServerSettingsRepository.findAll();
        assertThat(guildServerSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGuildServerSettings() throws Exception {
        // Initialize the database
        guildServerSettingsService.save(guildServerSettings);

        int databaseSizeBeforeDelete = guildServerSettingsRepository.findAll().size();

        // Delete the guildServerSettings
        restGuildServerSettingsMockMvc.perform(delete("/api/guild-server-settings/{id}", guildServerSettings.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GuildServerSettings> guildServerSettingsList = guildServerSettingsRepository.findAll();
        assertThat(guildServerSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuildServerSettings.class);
        GuildServerSettings guildServerSettings1 = new GuildServerSettings();
        guildServerSettings1.setId(1L);
        GuildServerSettings guildServerSettings2 = new GuildServerSettings();
        guildServerSettings2.setId(guildServerSettings1.getId());
        assertThat(guildServerSettings1).isEqualTo(guildServerSettings2);
        guildServerSettings2.setId(2L);
        assertThat(guildServerSettings1).isNotEqualTo(guildServerSettings2);
        guildServerSettings1.setId(null);
        assertThat(guildServerSettings1).isNotEqualTo(guildServerSettings2);
    }
}
