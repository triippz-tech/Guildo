package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.AutoModeration;
import com.triippztech.guildo.domain.AutoModIgnore;
import com.triippztech.guildo.domain.AutoModMentions;
import com.triippztech.guildo.domain.AutoModAntiDup;
import com.triippztech.guildo.domain.AutoModAutoRaid;
import com.triippztech.guildo.repository.AutoModerationRepository;
import com.triippztech.guildo.service.moderation.AutoModerationService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.moderation.AutoModerationQueryService;

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
 * Integration tests for the {@link AutoModerationResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class AutoModerationResourceIT {

    private static final Integer DEFAULT_INVITE_STRIKES = 1;
    private static final Integer UPDATED_INVITE_STRIKES = 2;
    private static final Integer SMALLER_INVITE_STRIKES = 1 - 1;

    private static final Integer DEFAULT_COPY_PASTA_STRIKES = 1;
    private static final Integer UPDATED_COPY_PASTA_STRIKES = 2;
    private static final Integer SMALLER_COPY_PASTA_STRIKES = 1 - 1;

    private static final Integer DEFAULT_EVERYONE_MENTION_STRIKES = 1;
    private static final Integer UPDATED_EVERYONE_MENTION_STRIKES = 2;
    private static final Integer SMALLER_EVERYONE_MENTION_STRIKES = 1 - 1;

    private static final Integer DEFAULT_REFERRAL_STRIKES = 1;
    private static final Integer UPDATED_REFERRAL_STRIKES = 2;
    private static final Integer SMALLER_REFERRAL_STRIKES = 1 - 1;

    private static final Integer DEFAULT_DUPLICATE_STRIKES = 1;
    private static final Integer UPDATED_DUPLICATE_STRIKES = 2;
    private static final Integer SMALLER_DUPLICATE_STRIKES = 1 - 1;

    private static final Boolean DEFAULT_RESOLVE_URLS = false;
    private static final Boolean UPDATED_RESOLVE_URLS = true;

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private AutoModerationRepository autoModerationRepository;

    @Autowired
    private AutoModerationService autoModerationService;

    @Autowired
    private AutoModerationQueryService autoModerationQueryService;

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

    private MockMvc restAutoModerationMockMvc;

    private AutoModeration autoModeration;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AutoModerationResource autoModerationResource = new AutoModerationResource(autoModerationService, autoModerationQueryService);
        this.restAutoModerationMockMvc = MockMvcBuilders.standaloneSetup(autoModerationResource)
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
    public static AutoModeration createEntity(EntityManager em) {
        AutoModeration autoModeration = new AutoModeration()
            .inviteStrikes(DEFAULT_INVITE_STRIKES)
            .copyPastaStrikes(DEFAULT_COPY_PASTA_STRIKES)
            .everyoneMentionStrikes(DEFAULT_EVERYONE_MENTION_STRIKES)
            .referralStrikes(DEFAULT_REFERRAL_STRIKES)
            .duplicateStrikes(DEFAULT_DUPLICATE_STRIKES)
            .resolveUrls(DEFAULT_RESOLVE_URLS)
            .enabled(DEFAULT_ENABLED);
        return autoModeration;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AutoModeration createUpdatedEntity(EntityManager em) {
        AutoModeration autoModeration = new AutoModeration()
            .inviteStrikes(UPDATED_INVITE_STRIKES)
            .copyPastaStrikes(UPDATED_COPY_PASTA_STRIKES)
            .everyoneMentionStrikes(UPDATED_EVERYONE_MENTION_STRIKES)
            .referralStrikes(UPDATED_REFERRAL_STRIKES)
            .duplicateStrikes(UPDATED_DUPLICATE_STRIKES)
            .resolveUrls(UPDATED_RESOLVE_URLS)
            .enabled(UPDATED_ENABLED);
        return autoModeration;
    }

    @BeforeEach
    public void initTest() {
        autoModeration = createEntity(em);
    }

    @Test
    @Transactional
    public void createAutoModeration() throws Exception {
        int databaseSizeBeforeCreate = autoModerationRepository.findAll().size();

        // Create the AutoModeration
        restAutoModerationMockMvc.perform(post("/api/auto-moderations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModeration)))
            .andExpect(status().isCreated());

        // Validate the AutoModeration in the database
        List<AutoModeration> autoModerationList = autoModerationRepository.findAll();
        assertThat(autoModerationList).hasSize(databaseSizeBeforeCreate + 1);
        AutoModeration testAutoModeration = autoModerationList.get(autoModerationList.size() - 1);
        assertThat(testAutoModeration.getInviteStrikes()).isEqualTo(DEFAULT_INVITE_STRIKES);
        assertThat(testAutoModeration.getCopyPastaStrikes()).isEqualTo(DEFAULT_COPY_PASTA_STRIKES);
        assertThat(testAutoModeration.getEveryoneMentionStrikes()).isEqualTo(DEFAULT_EVERYONE_MENTION_STRIKES);
        assertThat(testAutoModeration.getReferralStrikes()).isEqualTo(DEFAULT_REFERRAL_STRIKES);
        assertThat(testAutoModeration.getDuplicateStrikes()).isEqualTo(DEFAULT_DUPLICATE_STRIKES);
        assertThat(testAutoModeration.isResolveUrls()).isEqualTo(DEFAULT_RESOLVE_URLS);
        assertThat(testAutoModeration.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createAutoModerationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = autoModerationRepository.findAll().size();

        // Create the AutoModeration with an existing ID
        autoModeration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutoModerationMockMvc.perform(post("/api/auto-moderations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModeration)))
            .andExpect(status().isBadRequest());

        // Validate the AutoModeration in the database
        List<AutoModeration> autoModerationList = autoModerationRepository.findAll();
        assertThat(autoModerationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkInviteStrikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModerationRepository.findAll().size();
        // set the field null
        autoModeration.setInviteStrikes(null);

        // Create the AutoModeration, which fails.

        restAutoModerationMockMvc.perform(post("/api/auto-moderations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModeration)))
            .andExpect(status().isBadRequest());

        List<AutoModeration> autoModerationList = autoModerationRepository.findAll();
        assertThat(autoModerationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCopyPastaStrikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModerationRepository.findAll().size();
        // set the field null
        autoModeration.setCopyPastaStrikes(null);

        // Create the AutoModeration, which fails.

        restAutoModerationMockMvc.perform(post("/api/auto-moderations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModeration)))
            .andExpect(status().isBadRequest());

        List<AutoModeration> autoModerationList = autoModerationRepository.findAll();
        assertThat(autoModerationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEveryoneMentionStrikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModerationRepository.findAll().size();
        // set the field null
        autoModeration.setEveryoneMentionStrikes(null);

        // Create the AutoModeration, which fails.

        restAutoModerationMockMvc.perform(post("/api/auto-moderations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModeration)))
            .andExpect(status().isBadRequest());

        List<AutoModeration> autoModerationList = autoModerationRepository.findAll();
        assertThat(autoModerationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReferralStrikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModerationRepository.findAll().size();
        // set the field null
        autoModeration.setReferralStrikes(null);

        // Create the AutoModeration, which fails.

        restAutoModerationMockMvc.perform(post("/api/auto-moderations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModeration)))
            .andExpect(status().isBadRequest());

        List<AutoModeration> autoModerationList = autoModerationRepository.findAll();
        assertThat(autoModerationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDuplicateStrikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModerationRepository.findAll().size();
        // set the field null
        autoModeration.setDuplicateStrikes(null);

        // Create the AutoModeration, which fails.

        restAutoModerationMockMvc.perform(post("/api/auto-moderations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModeration)))
            .andExpect(status().isBadRequest());

        List<AutoModeration> autoModerationList = autoModerationRepository.findAll();
        assertThat(autoModerationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResolveUrlsIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModerationRepository.findAll().size();
        // set the field null
        autoModeration.setResolveUrls(null);

        // Create the AutoModeration, which fails.

        restAutoModerationMockMvc.perform(post("/api/auto-moderations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModeration)))
            .andExpect(status().isBadRequest());

        List<AutoModeration> autoModerationList = autoModerationRepository.findAll();
        assertThat(autoModerationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModerationRepository.findAll().size();
        // set the field null
        autoModeration.setEnabled(null);

        // Create the AutoModeration, which fails.

        restAutoModerationMockMvc.perform(post("/api/auto-moderations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModeration)))
            .andExpect(status().isBadRequest());

        List<AutoModeration> autoModerationList = autoModerationRepository.findAll();
        assertThat(autoModerationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAutoModerations() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList
        restAutoModerationMockMvc.perform(get("/api/auto-moderations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autoModeration.getId().intValue())))
            .andExpect(jsonPath("$.[*].inviteStrikes").value(hasItem(DEFAULT_INVITE_STRIKES)))
            .andExpect(jsonPath("$.[*].copyPastaStrikes").value(hasItem(DEFAULT_COPY_PASTA_STRIKES)))
            .andExpect(jsonPath("$.[*].everyoneMentionStrikes").value(hasItem(DEFAULT_EVERYONE_MENTION_STRIKES)))
            .andExpect(jsonPath("$.[*].referralStrikes").value(hasItem(DEFAULT_REFERRAL_STRIKES)))
            .andExpect(jsonPath("$.[*].duplicateStrikes").value(hasItem(DEFAULT_DUPLICATE_STRIKES)))
            .andExpect(jsonPath("$.[*].resolveUrls").value(hasItem(DEFAULT_RESOLVE_URLS.booleanValue())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getAutoModeration() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get the autoModeration
        restAutoModerationMockMvc.perform(get("/api/auto-moderations/{id}", autoModeration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(autoModeration.getId().intValue()))
            .andExpect(jsonPath("$.inviteStrikes").value(DEFAULT_INVITE_STRIKES))
            .andExpect(jsonPath("$.copyPastaStrikes").value(DEFAULT_COPY_PASTA_STRIKES))
            .andExpect(jsonPath("$.everyoneMentionStrikes").value(DEFAULT_EVERYONE_MENTION_STRIKES))
            .andExpect(jsonPath("$.referralStrikes").value(DEFAULT_REFERRAL_STRIKES))
            .andExpect(jsonPath("$.duplicateStrikes").value(DEFAULT_DUPLICATE_STRIKES))
            .andExpect(jsonPath("$.resolveUrls").value(DEFAULT_RESOLVE_URLS.booleanValue()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByInviteStrikesIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where inviteStrikes equals to DEFAULT_INVITE_STRIKES
        defaultAutoModerationShouldBeFound("inviteStrikes.equals=" + DEFAULT_INVITE_STRIKES);

        // Get all the autoModerationList where inviteStrikes equals to UPDATED_INVITE_STRIKES
        defaultAutoModerationShouldNotBeFound("inviteStrikes.equals=" + UPDATED_INVITE_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByInviteStrikesIsInShouldWork() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where inviteStrikes in DEFAULT_INVITE_STRIKES or UPDATED_INVITE_STRIKES
        defaultAutoModerationShouldBeFound("inviteStrikes.in=" + DEFAULT_INVITE_STRIKES + "," + UPDATED_INVITE_STRIKES);

        // Get all the autoModerationList where inviteStrikes equals to UPDATED_INVITE_STRIKES
        defaultAutoModerationShouldNotBeFound("inviteStrikes.in=" + UPDATED_INVITE_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByInviteStrikesIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where inviteStrikes is not null
        defaultAutoModerationShouldBeFound("inviteStrikes.specified=true");

        // Get all the autoModerationList where inviteStrikes is null
        defaultAutoModerationShouldNotBeFound("inviteStrikes.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByInviteStrikesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where inviteStrikes is greater than or equal to DEFAULT_INVITE_STRIKES
        defaultAutoModerationShouldBeFound("inviteStrikes.greaterThanOrEqual=" + DEFAULT_INVITE_STRIKES);

        // Get all the autoModerationList where inviteStrikes is greater than or equal to UPDATED_INVITE_STRIKES
        defaultAutoModerationShouldNotBeFound("inviteStrikes.greaterThanOrEqual=" + UPDATED_INVITE_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByInviteStrikesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where inviteStrikes is less than or equal to DEFAULT_INVITE_STRIKES
        defaultAutoModerationShouldBeFound("inviteStrikes.lessThanOrEqual=" + DEFAULT_INVITE_STRIKES);

        // Get all the autoModerationList where inviteStrikes is less than or equal to SMALLER_INVITE_STRIKES
        defaultAutoModerationShouldNotBeFound("inviteStrikes.lessThanOrEqual=" + SMALLER_INVITE_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByInviteStrikesIsLessThanSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where inviteStrikes is less than DEFAULT_INVITE_STRIKES
        defaultAutoModerationShouldNotBeFound("inviteStrikes.lessThan=" + DEFAULT_INVITE_STRIKES);

        // Get all the autoModerationList where inviteStrikes is less than UPDATED_INVITE_STRIKES
        defaultAutoModerationShouldBeFound("inviteStrikes.lessThan=" + UPDATED_INVITE_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByInviteStrikesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where inviteStrikes is greater than DEFAULT_INVITE_STRIKES
        defaultAutoModerationShouldNotBeFound("inviteStrikes.greaterThan=" + DEFAULT_INVITE_STRIKES);

        // Get all the autoModerationList where inviteStrikes is greater than SMALLER_INVITE_STRIKES
        defaultAutoModerationShouldBeFound("inviteStrikes.greaterThan=" + SMALLER_INVITE_STRIKES);
    }


    @Test
    @Transactional
    public void getAllAutoModerationsByCopyPastaStrikesIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where copyPastaStrikes equals to DEFAULT_COPY_PASTA_STRIKES
        defaultAutoModerationShouldBeFound("copyPastaStrikes.equals=" + DEFAULT_COPY_PASTA_STRIKES);

        // Get all the autoModerationList where copyPastaStrikes equals to UPDATED_COPY_PASTA_STRIKES
        defaultAutoModerationShouldNotBeFound("copyPastaStrikes.equals=" + UPDATED_COPY_PASTA_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByCopyPastaStrikesIsInShouldWork() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where copyPastaStrikes in DEFAULT_COPY_PASTA_STRIKES or UPDATED_COPY_PASTA_STRIKES
        defaultAutoModerationShouldBeFound("copyPastaStrikes.in=" + DEFAULT_COPY_PASTA_STRIKES + "," + UPDATED_COPY_PASTA_STRIKES);

        // Get all the autoModerationList where copyPastaStrikes equals to UPDATED_COPY_PASTA_STRIKES
        defaultAutoModerationShouldNotBeFound("copyPastaStrikes.in=" + UPDATED_COPY_PASTA_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByCopyPastaStrikesIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where copyPastaStrikes is not null
        defaultAutoModerationShouldBeFound("copyPastaStrikes.specified=true");

        // Get all the autoModerationList where copyPastaStrikes is null
        defaultAutoModerationShouldNotBeFound("copyPastaStrikes.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByCopyPastaStrikesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where copyPastaStrikes is greater than or equal to DEFAULT_COPY_PASTA_STRIKES
        defaultAutoModerationShouldBeFound("copyPastaStrikes.greaterThanOrEqual=" + DEFAULT_COPY_PASTA_STRIKES);

        // Get all the autoModerationList where copyPastaStrikes is greater than or equal to UPDATED_COPY_PASTA_STRIKES
        defaultAutoModerationShouldNotBeFound("copyPastaStrikes.greaterThanOrEqual=" + UPDATED_COPY_PASTA_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByCopyPastaStrikesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where copyPastaStrikes is less than or equal to DEFAULT_COPY_PASTA_STRIKES
        defaultAutoModerationShouldBeFound("copyPastaStrikes.lessThanOrEqual=" + DEFAULT_COPY_PASTA_STRIKES);

        // Get all the autoModerationList where copyPastaStrikes is less than or equal to SMALLER_COPY_PASTA_STRIKES
        defaultAutoModerationShouldNotBeFound("copyPastaStrikes.lessThanOrEqual=" + SMALLER_COPY_PASTA_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByCopyPastaStrikesIsLessThanSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where copyPastaStrikes is less than DEFAULT_COPY_PASTA_STRIKES
        defaultAutoModerationShouldNotBeFound("copyPastaStrikes.lessThan=" + DEFAULT_COPY_PASTA_STRIKES);

        // Get all the autoModerationList where copyPastaStrikes is less than UPDATED_COPY_PASTA_STRIKES
        defaultAutoModerationShouldBeFound("copyPastaStrikes.lessThan=" + UPDATED_COPY_PASTA_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByCopyPastaStrikesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where copyPastaStrikes is greater than DEFAULT_COPY_PASTA_STRIKES
        defaultAutoModerationShouldNotBeFound("copyPastaStrikes.greaterThan=" + DEFAULT_COPY_PASTA_STRIKES);

        // Get all the autoModerationList where copyPastaStrikes is greater than SMALLER_COPY_PASTA_STRIKES
        defaultAutoModerationShouldBeFound("copyPastaStrikes.greaterThan=" + SMALLER_COPY_PASTA_STRIKES);
    }


    @Test
    @Transactional
    public void getAllAutoModerationsByEveryoneMentionStrikesIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where everyoneMentionStrikes equals to DEFAULT_EVERYONE_MENTION_STRIKES
        defaultAutoModerationShouldBeFound("everyoneMentionStrikes.equals=" + DEFAULT_EVERYONE_MENTION_STRIKES);

        // Get all the autoModerationList where everyoneMentionStrikes equals to UPDATED_EVERYONE_MENTION_STRIKES
        defaultAutoModerationShouldNotBeFound("everyoneMentionStrikes.equals=" + UPDATED_EVERYONE_MENTION_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByEveryoneMentionStrikesIsInShouldWork() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where everyoneMentionStrikes in DEFAULT_EVERYONE_MENTION_STRIKES or UPDATED_EVERYONE_MENTION_STRIKES
        defaultAutoModerationShouldBeFound("everyoneMentionStrikes.in=" + DEFAULT_EVERYONE_MENTION_STRIKES + "," + UPDATED_EVERYONE_MENTION_STRIKES);

        // Get all the autoModerationList where everyoneMentionStrikes equals to UPDATED_EVERYONE_MENTION_STRIKES
        defaultAutoModerationShouldNotBeFound("everyoneMentionStrikes.in=" + UPDATED_EVERYONE_MENTION_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByEveryoneMentionStrikesIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where everyoneMentionStrikes is not null
        defaultAutoModerationShouldBeFound("everyoneMentionStrikes.specified=true");

        // Get all the autoModerationList where everyoneMentionStrikes is null
        defaultAutoModerationShouldNotBeFound("everyoneMentionStrikes.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByEveryoneMentionStrikesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where everyoneMentionStrikes is greater than or equal to DEFAULT_EVERYONE_MENTION_STRIKES
        defaultAutoModerationShouldBeFound("everyoneMentionStrikes.greaterThanOrEqual=" + DEFAULT_EVERYONE_MENTION_STRIKES);

        // Get all the autoModerationList where everyoneMentionStrikes is greater than or equal to UPDATED_EVERYONE_MENTION_STRIKES
        defaultAutoModerationShouldNotBeFound("everyoneMentionStrikes.greaterThanOrEqual=" + UPDATED_EVERYONE_MENTION_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByEveryoneMentionStrikesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where everyoneMentionStrikes is less than or equal to DEFAULT_EVERYONE_MENTION_STRIKES
        defaultAutoModerationShouldBeFound("everyoneMentionStrikes.lessThanOrEqual=" + DEFAULT_EVERYONE_MENTION_STRIKES);

        // Get all the autoModerationList where everyoneMentionStrikes is less than or equal to SMALLER_EVERYONE_MENTION_STRIKES
        defaultAutoModerationShouldNotBeFound("everyoneMentionStrikes.lessThanOrEqual=" + SMALLER_EVERYONE_MENTION_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByEveryoneMentionStrikesIsLessThanSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where everyoneMentionStrikes is less than DEFAULT_EVERYONE_MENTION_STRIKES
        defaultAutoModerationShouldNotBeFound("everyoneMentionStrikes.lessThan=" + DEFAULT_EVERYONE_MENTION_STRIKES);

        // Get all the autoModerationList where everyoneMentionStrikes is less than UPDATED_EVERYONE_MENTION_STRIKES
        defaultAutoModerationShouldBeFound("everyoneMentionStrikes.lessThan=" + UPDATED_EVERYONE_MENTION_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByEveryoneMentionStrikesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where everyoneMentionStrikes is greater than DEFAULT_EVERYONE_MENTION_STRIKES
        defaultAutoModerationShouldNotBeFound("everyoneMentionStrikes.greaterThan=" + DEFAULT_EVERYONE_MENTION_STRIKES);

        // Get all the autoModerationList where everyoneMentionStrikes is greater than SMALLER_EVERYONE_MENTION_STRIKES
        defaultAutoModerationShouldBeFound("everyoneMentionStrikes.greaterThan=" + SMALLER_EVERYONE_MENTION_STRIKES);
    }


    @Test
    @Transactional
    public void getAllAutoModerationsByReferralStrikesIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where referralStrikes equals to DEFAULT_REFERRAL_STRIKES
        defaultAutoModerationShouldBeFound("referralStrikes.equals=" + DEFAULT_REFERRAL_STRIKES);

        // Get all the autoModerationList where referralStrikes equals to UPDATED_REFERRAL_STRIKES
        defaultAutoModerationShouldNotBeFound("referralStrikes.equals=" + UPDATED_REFERRAL_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByReferralStrikesIsInShouldWork() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where referralStrikes in DEFAULT_REFERRAL_STRIKES or UPDATED_REFERRAL_STRIKES
        defaultAutoModerationShouldBeFound("referralStrikes.in=" + DEFAULT_REFERRAL_STRIKES + "," + UPDATED_REFERRAL_STRIKES);

        // Get all the autoModerationList where referralStrikes equals to UPDATED_REFERRAL_STRIKES
        defaultAutoModerationShouldNotBeFound("referralStrikes.in=" + UPDATED_REFERRAL_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByReferralStrikesIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where referralStrikes is not null
        defaultAutoModerationShouldBeFound("referralStrikes.specified=true");

        // Get all the autoModerationList where referralStrikes is null
        defaultAutoModerationShouldNotBeFound("referralStrikes.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByReferralStrikesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where referralStrikes is greater than or equal to DEFAULT_REFERRAL_STRIKES
        defaultAutoModerationShouldBeFound("referralStrikes.greaterThanOrEqual=" + DEFAULT_REFERRAL_STRIKES);

        // Get all the autoModerationList where referralStrikes is greater than or equal to UPDATED_REFERRAL_STRIKES
        defaultAutoModerationShouldNotBeFound("referralStrikes.greaterThanOrEqual=" + UPDATED_REFERRAL_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByReferralStrikesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where referralStrikes is less than or equal to DEFAULT_REFERRAL_STRIKES
        defaultAutoModerationShouldBeFound("referralStrikes.lessThanOrEqual=" + DEFAULT_REFERRAL_STRIKES);

        // Get all the autoModerationList where referralStrikes is less than or equal to SMALLER_REFERRAL_STRIKES
        defaultAutoModerationShouldNotBeFound("referralStrikes.lessThanOrEqual=" + SMALLER_REFERRAL_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByReferralStrikesIsLessThanSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where referralStrikes is less than DEFAULT_REFERRAL_STRIKES
        defaultAutoModerationShouldNotBeFound("referralStrikes.lessThan=" + DEFAULT_REFERRAL_STRIKES);

        // Get all the autoModerationList where referralStrikes is less than UPDATED_REFERRAL_STRIKES
        defaultAutoModerationShouldBeFound("referralStrikes.lessThan=" + UPDATED_REFERRAL_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByReferralStrikesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where referralStrikes is greater than DEFAULT_REFERRAL_STRIKES
        defaultAutoModerationShouldNotBeFound("referralStrikes.greaterThan=" + DEFAULT_REFERRAL_STRIKES);

        // Get all the autoModerationList where referralStrikes is greater than SMALLER_REFERRAL_STRIKES
        defaultAutoModerationShouldBeFound("referralStrikes.greaterThan=" + SMALLER_REFERRAL_STRIKES);
    }


    @Test
    @Transactional
    public void getAllAutoModerationsByDuplicateStrikesIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where duplicateStrikes equals to DEFAULT_DUPLICATE_STRIKES
        defaultAutoModerationShouldBeFound("duplicateStrikes.equals=" + DEFAULT_DUPLICATE_STRIKES);

        // Get all the autoModerationList where duplicateStrikes equals to UPDATED_DUPLICATE_STRIKES
        defaultAutoModerationShouldNotBeFound("duplicateStrikes.equals=" + UPDATED_DUPLICATE_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByDuplicateStrikesIsInShouldWork() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where duplicateStrikes in DEFAULT_DUPLICATE_STRIKES or UPDATED_DUPLICATE_STRIKES
        defaultAutoModerationShouldBeFound("duplicateStrikes.in=" + DEFAULT_DUPLICATE_STRIKES + "," + UPDATED_DUPLICATE_STRIKES);

        // Get all the autoModerationList where duplicateStrikes equals to UPDATED_DUPLICATE_STRIKES
        defaultAutoModerationShouldNotBeFound("duplicateStrikes.in=" + UPDATED_DUPLICATE_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByDuplicateStrikesIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where duplicateStrikes is not null
        defaultAutoModerationShouldBeFound("duplicateStrikes.specified=true");

        // Get all the autoModerationList where duplicateStrikes is null
        defaultAutoModerationShouldNotBeFound("duplicateStrikes.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByDuplicateStrikesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where duplicateStrikes is greater than or equal to DEFAULT_DUPLICATE_STRIKES
        defaultAutoModerationShouldBeFound("duplicateStrikes.greaterThanOrEqual=" + DEFAULT_DUPLICATE_STRIKES);

        // Get all the autoModerationList where duplicateStrikes is greater than or equal to UPDATED_DUPLICATE_STRIKES
        defaultAutoModerationShouldNotBeFound("duplicateStrikes.greaterThanOrEqual=" + UPDATED_DUPLICATE_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByDuplicateStrikesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where duplicateStrikes is less than or equal to DEFAULT_DUPLICATE_STRIKES
        defaultAutoModerationShouldBeFound("duplicateStrikes.lessThanOrEqual=" + DEFAULT_DUPLICATE_STRIKES);

        // Get all the autoModerationList where duplicateStrikes is less than or equal to SMALLER_DUPLICATE_STRIKES
        defaultAutoModerationShouldNotBeFound("duplicateStrikes.lessThanOrEqual=" + SMALLER_DUPLICATE_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByDuplicateStrikesIsLessThanSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where duplicateStrikes is less than DEFAULT_DUPLICATE_STRIKES
        defaultAutoModerationShouldNotBeFound("duplicateStrikes.lessThan=" + DEFAULT_DUPLICATE_STRIKES);

        // Get all the autoModerationList where duplicateStrikes is less than UPDATED_DUPLICATE_STRIKES
        defaultAutoModerationShouldBeFound("duplicateStrikes.lessThan=" + UPDATED_DUPLICATE_STRIKES);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByDuplicateStrikesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where duplicateStrikes is greater than DEFAULT_DUPLICATE_STRIKES
        defaultAutoModerationShouldNotBeFound("duplicateStrikes.greaterThan=" + DEFAULT_DUPLICATE_STRIKES);

        // Get all the autoModerationList where duplicateStrikes is greater than SMALLER_DUPLICATE_STRIKES
        defaultAutoModerationShouldBeFound("duplicateStrikes.greaterThan=" + SMALLER_DUPLICATE_STRIKES);
    }


    @Test
    @Transactional
    public void getAllAutoModerationsByResolveUrlsIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where resolveUrls equals to DEFAULT_RESOLVE_URLS
        defaultAutoModerationShouldBeFound("resolveUrls.equals=" + DEFAULT_RESOLVE_URLS);

        // Get all the autoModerationList where resolveUrls equals to UPDATED_RESOLVE_URLS
        defaultAutoModerationShouldNotBeFound("resolveUrls.equals=" + UPDATED_RESOLVE_URLS);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByResolveUrlsIsInShouldWork() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where resolveUrls in DEFAULT_RESOLVE_URLS or UPDATED_RESOLVE_URLS
        defaultAutoModerationShouldBeFound("resolveUrls.in=" + DEFAULT_RESOLVE_URLS + "," + UPDATED_RESOLVE_URLS);

        // Get all the autoModerationList where resolveUrls equals to UPDATED_RESOLVE_URLS
        defaultAutoModerationShouldNotBeFound("resolveUrls.in=" + UPDATED_RESOLVE_URLS);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByResolveUrlsIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where resolveUrls is not null
        defaultAutoModerationShouldBeFound("resolveUrls.specified=true");

        // Get all the autoModerationList where resolveUrls is null
        defaultAutoModerationShouldNotBeFound("resolveUrls.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where enabled equals to DEFAULT_ENABLED
        defaultAutoModerationShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the autoModerationList where enabled equals to UPDATED_ENABLED
        defaultAutoModerationShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultAutoModerationShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the autoModerationList where enabled equals to UPDATED_ENABLED
        defaultAutoModerationShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);

        // Get all the autoModerationList where enabled is not null
        defaultAutoModerationShouldBeFound("enabled.specified=true");

        // Get all the autoModerationList where enabled is null
        defaultAutoModerationShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModerationsByIgnoreConfigIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);
        AutoModIgnore ignoreConfig = AutoModIgnoreResourceIT.createEntity(em);
        em.persist(ignoreConfig);
        em.flush();
        autoModeration.setIgnoreConfig(ignoreConfig);
        autoModerationRepository.saveAndFlush(autoModeration);
        Long ignoreConfigId = ignoreConfig.getId();

        // Get all the autoModerationList where ignoreConfig equals to ignoreConfigId
        defaultAutoModerationShouldBeFound("ignoreConfigId.equals=" + ignoreConfigId);

        // Get all the autoModerationList where ignoreConfig equals to ignoreConfigId + 1
        defaultAutoModerationShouldNotBeFound("ignoreConfigId.equals=" + (ignoreConfigId + 1));
    }


    @Test
    @Transactional
    public void getAllAutoModerationsByMentionConfigIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);
        AutoModMentions mentionConfig = AutoModMentionsResourceIT.createEntity(em);
        em.persist(mentionConfig);
        em.flush();
        autoModeration.setMentionConfig(mentionConfig);
        autoModerationRepository.saveAndFlush(autoModeration);
        Long mentionConfigId = mentionConfig.getId();

        // Get all the autoModerationList where mentionConfig equals to mentionConfigId
        defaultAutoModerationShouldBeFound("mentionConfigId.equals=" + mentionConfigId);

        // Get all the autoModerationList where mentionConfig equals to mentionConfigId + 1
        defaultAutoModerationShouldNotBeFound("mentionConfigId.equals=" + (mentionConfigId + 1));
    }


    @Test
    @Transactional
    public void getAllAutoModerationsByAntiDupConfigIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);
        AutoModAntiDup antiDupConfig = AutoModAntiDupResourceIT.createEntity(em);
        em.persist(antiDupConfig);
        em.flush();
        autoModeration.setAntiDupConfig(antiDupConfig);
        autoModerationRepository.saveAndFlush(autoModeration);
        Long antiDupConfigId = antiDupConfig.getId();

        // Get all the autoModerationList where antiDupConfig equals to antiDupConfigId
        defaultAutoModerationShouldBeFound("antiDupConfigId.equals=" + antiDupConfigId);

        // Get all the autoModerationList where antiDupConfig equals to antiDupConfigId + 1
        defaultAutoModerationShouldNotBeFound("antiDupConfigId.equals=" + (antiDupConfigId + 1));
    }


    @Test
    @Transactional
    public void getAllAutoModerationsByAutoRaidConfigIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModerationRepository.saveAndFlush(autoModeration);
        AutoModAutoRaid autoRaidConfig = AutoModAutoRaidResourceIT.createEntity(em);
        em.persist(autoRaidConfig);
        em.flush();
        autoModeration.setAutoRaidConfig(autoRaidConfig);
        autoModerationRepository.saveAndFlush(autoModeration);
        Long autoRaidConfigId = autoRaidConfig.getId();

        // Get all the autoModerationList where autoRaidConfig equals to autoRaidConfigId
        defaultAutoModerationShouldBeFound("autoRaidConfigId.equals=" + autoRaidConfigId);

        // Get all the autoModerationList where autoRaidConfig equals to autoRaidConfigId + 1
        defaultAutoModerationShouldNotBeFound("autoRaidConfigId.equals=" + (autoRaidConfigId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAutoModerationShouldBeFound(String filter) throws Exception {
        restAutoModerationMockMvc.perform(get("/api/auto-moderations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autoModeration.getId().intValue())))
            .andExpect(jsonPath("$.[*].inviteStrikes").value(hasItem(DEFAULT_INVITE_STRIKES)))
            .andExpect(jsonPath("$.[*].copyPastaStrikes").value(hasItem(DEFAULT_COPY_PASTA_STRIKES)))
            .andExpect(jsonPath("$.[*].everyoneMentionStrikes").value(hasItem(DEFAULT_EVERYONE_MENTION_STRIKES)))
            .andExpect(jsonPath("$.[*].referralStrikes").value(hasItem(DEFAULT_REFERRAL_STRIKES)))
            .andExpect(jsonPath("$.[*].duplicateStrikes").value(hasItem(DEFAULT_DUPLICATE_STRIKES)))
            .andExpect(jsonPath("$.[*].resolveUrls").value(hasItem(DEFAULT_RESOLVE_URLS.booleanValue())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restAutoModerationMockMvc.perform(get("/api/auto-moderations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAutoModerationShouldNotBeFound(String filter) throws Exception {
        restAutoModerationMockMvc.perform(get("/api/auto-moderations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAutoModerationMockMvc.perform(get("/api/auto-moderations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAutoModeration() throws Exception {
        // Get the autoModeration
        restAutoModerationMockMvc.perform(get("/api/auto-moderations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAutoModeration() throws Exception {
        // Initialize the database
        autoModerationService.save(autoModeration);

        int databaseSizeBeforeUpdate = autoModerationRepository.findAll().size();

        // Update the autoModeration
        AutoModeration updatedAutoModeration = autoModerationRepository.findById(autoModeration.getId()).get();
        // Disconnect from session so that the updates on updatedAutoModeration are not directly saved in db
        em.detach(updatedAutoModeration);
        updatedAutoModeration
            .inviteStrikes(UPDATED_INVITE_STRIKES)
            .copyPastaStrikes(UPDATED_COPY_PASTA_STRIKES)
            .everyoneMentionStrikes(UPDATED_EVERYONE_MENTION_STRIKES)
            .referralStrikes(UPDATED_REFERRAL_STRIKES)
            .duplicateStrikes(UPDATED_DUPLICATE_STRIKES)
            .resolveUrls(UPDATED_RESOLVE_URLS)
            .enabled(UPDATED_ENABLED);

        restAutoModerationMockMvc.perform(put("/api/auto-moderations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAutoModeration)))
            .andExpect(status().isOk());

        // Validate the AutoModeration in the database
        List<AutoModeration> autoModerationList = autoModerationRepository.findAll();
        assertThat(autoModerationList).hasSize(databaseSizeBeforeUpdate);
        AutoModeration testAutoModeration = autoModerationList.get(autoModerationList.size() - 1);
        assertThat(testAutoModeration.getInviteStrikes()).isEqualTo(UPDATED_INVITE_STRIKES);
        assertThat(testAutoModeration.getCopyPastaStrikes()).isEqualTo(UPDATED_COPY_PASTA_STRIKES);
        assertThat(testAutoModeration.getEveryoneMentionStrikes()).isEqualTo(UPDATED_EVERYONE_MENTION_STRIKES);
        assertThat(testAutoModeration.getReferralStrikes()).isEqualTo(UPDATED_REFERRAL_STRIKES);
        assertThat(testAutoModeration.getDuplicateStrikes()).isEqualTo(UPDATED_DUPLICATE_STRIKES);
        assertThat(testAutoModeration.isResolveUrls()).isEqualTo(UPDATED_RESOLVE_URLS);
        assertThat(testAutoModeration.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingAutoModeration() throws Exception {
        int databaseSizeBeforeUpdate = autoModerationRepository.findAll().size();

        // Create the AutoModeration

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutoModerationMockMvc.perform(put("/api/auto-moderations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModeration)))
            .andExpect(status().isBadRequest());

        // Validate the AutoModeration in the database
        List<AutoModeration> autoModerationList = autoModerationRepository.findAll();
        assertThat(autoModerationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAutoModeration() throws Exception {
        // Initialize the database
        autoModerationService.save(autoModeration);

        int databaseSizeBeforeDelete = autoModerationRepository.findAll().size();

        // Delete the autoModeration
        restAutoModerationMockMvc.perform(delete("/api/auto-moderations/{id}", autoModeration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AutoModeration> autoModerationList = autoModerationRepository.findAll();
        assertThat(autoModerationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutoModeration.class);
        AutoModeration autoModeration1 = new AutoModeration();
        autoModeration1.setId(1L);
        AutoModeration autoModeration2 = new AutoModeration();
        autoModeration2.setId(autoModeration1.getId());
        assertThat(autoModeration1).isEqualTo(autoModeration2);
        autoModeration2.setId(2L);
        assertThat(autoModeration1).isNotEqualTo(autoModeration2);
        autoModeration1.setId(null);
        assertThat(autoModeration1).isNotEqualTo(autoModeration2);
    }
}
