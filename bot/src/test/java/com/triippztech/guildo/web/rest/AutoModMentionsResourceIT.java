package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.AutoModMentions;
import com.triippztech.guildo.repository.AutoModMentionsRepository;
import com.triippztech.guildo.service.moderation.AutoModMentionsService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.moderation.AutoModMentionsQueryService;

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
 * Integration tests for the {@link AutoModMentionsResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class AutoModMentionsResourceIT {

    private static final Integer DEFAULT_MAX_MENTIONS = 1;
    private static final Integer UPDATED_MAX_MENTIONS = 2;
    private static final Integer SMALLER_MAX_MENTIONS = 1 - 1;

    private static final Integer DEFAULT_MAX_MSG_LINES = 1;
    private static final Integer UPDATED_MAX_MSG_LINES = 2;
    private static final Integer SMALLER_MAX_MSG_LINES = 1 - 1;

    private static final Integer DEFAULT_MAX_ROLE_MENTIONS = 1;
    private static final Integer UPDATED_MAX_ROLE_MENTIONS = 2;
    private static final Integer SMALLER_MAX_ROLE_MENTIONS = 1 - 1;

    @Autowired
    private AutoModMentionsRepository autoModMentionsRepository;

    @Autowired
    private AutoModMentionsService autoModMentionsService;

    @Autowired
    private AutoModMentionsQueryService autoModMentionsQueryService;

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

    private MockMvc restAutoModMentionsMockMvc;

    private AutoModMentions autoModMentions;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AutoModMentionsResource autoModMentionsResource = new AutoModMentionsResource(autoModMentionsService, autoModMentionsQueryService);
        this.restAutoModMentionsMockMvc = MockMvcBuilders.standaloneSetup(autoModMentionsResource)
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
    public static AutoModMentions createEntity(EntityManager em) {
        AutoModMentions autoModMentions = new AutoModMentions()
            .maxMentions(DEFAULT_MAX_MENTIONS)
            .maxMsgLines(DEFAULT_MAX_MSG_LINES)
            .maxRoleMentions(DEFAULT_MAX_ROLE_MENTIONS);
        return autoModMentions;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AutoModMentions createUpdatedEntity(EntityManager em) {
        AutoModMentions autoModMentions = new AutoModMentions()
            .maxMentions(UPDATED_MAX_MENTIONS)
            .maxMsgLines(UPDATED_MAX_MSG_LINES)
            .maxRoleMentions(UPDATED_MAX_ROLE_MENTIONS);
        return autoModMentions;
    }

    @BeforeEach
    public void initTest() {
        autoModMentions = createEntity(em);
    }

    @Test
    @Transactional
    public void createAutoModMentions() throws Exception {
        int databaseSizeBeforeCreate = autoModMentionsRepository.findAll().size();

        // Create the AutoModMentions
        restAutoModMentionsMockMvc.perform(post("/api/auto-mod-mentions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModMentions)))
            .andExpect(status().isCreated());

        // Validate the AutoModMentions in the database
        List<AutoModMentions> autoModMentionsList = autoModMentionsRepository.findAll();
        assertThat(autoModMentionsList).hasSize(databaseSizeBeforeCreate + 1);
        AutoModMentions testAutoModMentions = autoModMentionsList.get(autoModMentionsList.size() - 1);
        assertThat(testAutoModMentions.getMaxMentions()).isEqualTo(DEFAULT_MAX_MENTIONS);
        assertThat(testAutoModMentions.getMaxMsgLines()).isEqualTo(DEFAULT_MAX_MSG_LINES);
        assertThat(testAutoModMentions.getMaxRoleMentions()).isEqualTo(DEFAULT_MAX_ROLE_MENTIONS);
    }

    @Test
    @Transactional
    public void createAutoModMentionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = autoModMentionsRepository.findAll().size();

        // Create the AutoModMentions with an existing ID
        autoModMentions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutoModMentionsMockMvc.perform(post("/api/auto-mod-mentions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModMentions)))
            .andExpect(status().isBadRequest());

        // Validate the AutoModMentions in the database
        List<AutoModMentions> autoModMentionsList = autoModMentionsRepository.findAll();
        assertThat(autoModMentionsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkMaxMentionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModMentionsRepository.findAll().size();
        // set the field null
        autoModMentions.setMaxMentions(null);

        // Create the AutoModMentions, which fails.

        restAutoModMentionsMockMvc.perform(post("/api/auto-mod-mentions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModMentions)))
            .andExpect(status().isBadRequest());

        List<AutoModMentions> autoModMentionsList = autoModMentionsRepository.findAll();
        assertThat(autoModMentionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaxMsgLinesIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModMentionsRepository.findAll().size();
        // set the field null
        autoModMentions.setMaxMsgLines(null);

        // Create the AutoModMentions, which fails.

        restAutoModMentionsMockMvc.perform(post("/api/auto-mod-mentions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModMentions)))
            .andExpect(status().isBadRequest());

        List<AutoModMentions> autoModMentionsList = autoModMentionsRepository.findAll();
        assertThat(autoModMentionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaxRoleMentionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModMentionsRepository.findAll().size();
        // set the field null
        autoModMentions.setMaxRoleMentions(null);

        // Create the AutoModMentions, which fails.

        restAutoModMentionsMockMvc.perform(post("/api/auto-mod-mentions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModMentions)))
            .andExpect(status().isBadRequest());

        List<AutoModMentions> autoModMentionsList = autoModMentionsRepository.findAll();
        assertThat(autoModMentionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAutoModMentions() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList
        restAutoModMentionsMockMvc.perform(get("/api/auto-mod-mentions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autoModMentions.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxMentions").value(hasItem(DEFAULT_MAX_MENTIONS)))
            .andExpect(jsonPath("$.[*].maxMsgLines").value(hasItem(DEFAULT_MAX_MSG_LINES)))
            .andExpect(jsonPath("$.[*].maxRoleMentions").value(hasItem(DEFAULT_MAX_ROLE_MENTIONS)));
    }
    
    @Test
    @Transactional
    public void getAutoModMentions() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get the autoModMentions
        restAutoModMentionsMockMvc.perform(get("/api/auto-mod-mentions/{id}", autoModMentions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(autoModMentions.getId().intValue()))
            .andExpect(jsonPath("$.maxMentions").value(DEFAULT_MAX_MENTIONS))
            .andExpect(jsonPath("$.maxMsgLines").value(DEFAULT_MAX_MSG_LINES))
            .andExpect(jsonPath("$.maxRoleMentions").value(DEFAULT_MAX_ROLE_MENTIONS));
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxMentionsIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxMentions equals to DEFAULT_MAX_MENTIONS
        defaultAutoModMentionsShouldBeFound("maxMentions.equals=" + DEFAULT_MAX_MENTIONS);

        // Get all the autoModMentionsList where maxMentions equals to UPDATED_MAX_MENTIONS
        defaultAutoModMentionsShouldNotBeFound("maxMentions.equals=" + UPDATED_MAX_MENTIONS);
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxMentionsIsInShouldWork() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxMentions in DEFAULT_MAX_MENTIONS or UPDATED_MAX_MENTIONS
        defaultAutoModMentionsShouldBeFound("maxMentions.in=" + DEFAULT_MAX_MENTIONS + "," + UPDATED_MAX_MENTIONS);

        // Get all the autoModMentionsList where maxMentions equals to UPDATED_MAX_MENTIONS
        defaultAutoModMentionsShouldNotBeFound("maxMentions.in=" + UPDATED_MAX_MENTIONS);
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxMentionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxMentions is not null
        defaultAutoModMentionsShouldBeFound("maxMentions.specified=true");

        // Get all the autoModMentionsList where maxMentions is null
        defaultAutoModMentionsShouldNotBeFound("maxMentions.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxMentionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxMentions is greater than or equal to DEFAULT_MAX_MENTIONS
        defaultAutoModMentionsShouldBeFound("maxMentions.greaterThanOrEqual=" + DEFAULT_MAX_MENTIONS);

        // Get all the autoModMentionsList where maxMentions is greater than or equal to UPDATED_MAX_MENTIONS
        defaultAutoModMentionsShouldNotBeFound("maxMentions.greaterThanOrEqual=" + UPDATED_MAX_MENTIONS);
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxMentionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxMentions is less than or equal to DEFAULT_MAX_MENTIONS
        defaultAutoModMentionsShouldBeFound("maxMentions.lessThanOrEqual=" + DEFAULT_MAX_MENTIONS);

        // Get all the autoModMentionsList where maxMentions is less than or equal to SMALLER_MAX_MENTIONS
        defaultAutoModMentionsShouldNotBeFound("maxMentions.lessThanOrEqual=" + SMALLER_MAX_MENTIONS);
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxMentionsIsLessThanSomething() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxMentions is less than DEFAULT_MAX_MENTIONS
        defaultAutoModMentionsShouldNotBeFound("maxMentions.lessThan=" + DEFAULT_MAX_MENTIONS);

        // Get all the autoModMentionsList where maxMentions is less than UPDATED_MAX_MENTIONS
        defaultAutoModMentionsShouldBeFound("maxMentions.lessThan=" + UPDATED_MAX_MENTIONS);
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxMentionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxMentions is greater than DEFAULT_MAX_MENTIONS
        defaultAutoModMentionsShouldNotBeFound("maxMentions.greaterThan=" + DEFAULT_MAX_MENTIONS);

        // Get all the autoModMentionsList where maxMentions is greater than SMALLER_MAX_MENTIONS
        defaultAutoModMentionsShouldBeFound("maxMentions.greaterThan=" + SMALLER_MAX_MENTIONS);
    }


    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxMsgLinesIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxMsgLines equals to DEFAULT_MAX_MSG_LINES
        defaultAutoModMentionsShouldBeFound("maxMsgLines.equals=" + DEFAULT_MAX_MSG_LINES);

        // Get all the autoModMentionsList where maxMsgLines equals to UPDATED_MAX_MSG_LINES
        defaultAutoModMentionsShouldNotBeFound("maxMsgLines.equals=" + UPDATED_MAX_MSG_LINES);
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxMsgLinesIsInShouldWork() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxMsgLines in DEFAULT_MAX_MSG_LINES or UPDATED_MAX_MSG_LINES
        defaultAutoModMentionsShouldBeFound("maxMsgLines.in=" + DEFAULT_MAX_MSG_LINES + "," + UPDATED_MAX_MSG_LINES);

        // Get all the autoModMentionsList where maxMsgLines equals to UPDATED_MAX_MSG_LINES
        defaultAutoModMentionsShouldNotBeFound("maxMsgLines.in=" + UPDATED_MAX_MSG_LINES);
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxMsgLinesIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxMsgLines is not null
        defaultAutoModMentionsShouldBeFound("maxMsgLines.specified=true");

        // Get all the autoModMentionsList where maxMsgLines is null
        defaultAutoModMentionsShouldNotBeFound("maxMsgLines.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxMsgLinesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxMsgLines is greater than or equal to DEFAULT_MAX_MSG_LINES
        defaultAutoModMentionsShouldBeFound("maxMsgLines.greaterThanOrEqual=" + DEFAULT_MAX_MSG_LINES);

        // Get all the autoModMentionsList where maxMsgLines is greater than or equal to UPDATED_MAX_MSG_LINES
        defaultAutoModMentionsShouldNotBeFound("maxMsgLines.greaterThanOrEqual=" + UPDATED_MAX_MSG_LINES);
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxMsgLinesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxMsgLines is less than or equal to DEFAULT_MAX_MSG_LINES
        defaultAutoModMentionsShouldBeFound("maxMsgLines.lessThanOrEqual=" + DEFAULT_MAX_MSG_LINES);

        // Get all the autoModMentionsList where maxMsgLines is less than or equal to SMALLER_MAX_MSG_LINES
        defaultAutoModMentionsShouldNotBeFound("maxMsgLines.lessThanOrEqual=" + SMALLER_MAX_MSG_LINES);
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxMsgLinesIsLessThanSomething() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxMsgLines is less than DEFAULT_MAX_MSG_LINES
        defaultAutoModMentionsShouldNotBeFound("maxMsgLines.lessThan=" + DEFAULT_MAX_MSG_LINES);

        // Get all the autoModMentionsList where maxMsgLines is less than UPDATED_MAX_MSG_LINES
        defaultAutoModMentionsShouldBeFound("maxMsgLines.lessThan=" + UPDATED_MAX_MSG_LINES);
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxMsgLinesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxMsgLines is greater than DEFAULT_MAX_MSG_LINES
        defaultAutoModMentionsShouldNotBeFound("maxMsgLines.greaterThan=" + DEFAULT_MAX_MSG_LINES);

        // Get all the autoModMentionsList where maxMsgLines is greater than SMALLER_MAX_MSG_LINES
        defaultAutoModMentionsShouldBeFound("maxMsgLines.greaterThan=" + SMALLER_MAX_MSG_LINES);
    }


    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxRoleMentionsIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxRoleMentions equals to DEFAULT_MAX_ROLE_MENTIONS
        defaultAutoModMentionsShouldBeFound("maxRoleMentions.equals=" + DEFAULT_MAX_ROLE_MENTIONS);

        // Get all the autoModMentionsList where maxRoleMentions equals to UPDATED_MAX_ROLE_MENTIONS
        defaultAutoModMentionsShouldNotBeFound("maxRoleMentions.equals=" + UPDATED_MAX_ROLE_MENTIONS);
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxRoleMentionsIsInShouldWork() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxRoleMentions in DEFAULT_MAX_ROLE_MENTIONS or UPDATED_MAX_ROLE_MENTIONS
        defaultAutoModMentionsShouldBeFound("maxRoleMentions.in=" + DEFAULT_MAX_ROLE_MENTIONS + "," + UPDATED_MAX_ROLE_MENTIONS);

        // Get all the autoModMentionsList where maxRoleMentions equals to UPDATED_MAX_ROLE_MENTIONS
        defaultAutoModMentionsShouldNotBeFound("maxRoleMentions.in=" + UPDATED_MAX_ROLE_MENTIONS);
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxRoleMentionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxRoleMentions is not null
        defaultAutoModMentionsShouldBeFound("maxRoleMentions.specified=true");

        // Get all the autoModMentionsList where maxRoleMentions is null
        defaultAutoModMentionsShouldNotBeFound("maxRoleMentions.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxRoleMentionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxRoleMentions is greater than or equal to DEFAULT_MAX_ROLE_MENTIONS
        defaultAutoModMentionsShouldBeFound("maxRoleMentions.greaterThanOrEqual=" + DEFAULT_MAX_ROLE_MENTIONS);

        // Get all the autoModMentionsList where maxRoleMentions is greater than or equal to UPDATED_MAX_ROLE_MENTIONS
        defaultAutoModMentionsShouldNotBeFound("maxRoleMentions.greaterThanOrEqual=" + UPDATED_MAX_ROLE_MENTIONS);
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxRoleMentionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxRoleMentions is less than or equal to DEFAULT_MAX_ROLE_MENTIONS
        defaultAutoModMentionsShouldBeFound("maxRoleMentions.lessThanOrEqual=" + DEFAULT_MAX_ROLE_MENTIONS);

        // Get all the autoModMentionsList where maxRoleMentions is less than or equal to SMALLER_MAX_ROLE_MENTIONS
        defaultAutoModMentionsShouldNotBeFound("maxRoleMentions.lessThanOrEqual=" + SMALLER_MAX_ROLE_MENTIONS);
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxRoleMentionsIsLessThanSomething() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxRoleMentions is less than DEFAULT_MAX_ROLE_MENTIONS
        defaultAutoModMentionsShouldNotBeFound("maxRoleMentions.lessThan=" + DEFAULT_MAX_ROLE_MENTIONS);

        // Get all the autoModMentionsList where maxRoleMentions is less than UPDATED_MAX_ROLE_MENTIONS
        defaultAutoModMentionsShouldBeFound("maxRoleMentions.lessThan=" + UPDATED_MAX_ROLE_MENTIONS);
    }

    @Test
    @Transactional
    public void getAllAutoModMentionsByMaxRoleMentionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        autoModMentionsRepository.saveAndFlush(autoModMentions);

        // Get all the autoModMentionsList where maxRoleMentions is greater than DEFAULT_MAX_ROLE_MENTIONS
        defaultAutoModMentionsShouldNotBeFound("maxRoleMentions.greaterThan=" + DEFAULT_MAX_ROLE_MENTIONS);

        // Get all the autoModMentionsList where maxRoleMentions is greater than SMALLER_MAX_ROLE_MENTIONS
        defaultAutoModMentionsShouldBeFound("maxRoleMentions.greaterThan=" + SMALLER_MAX_ROLE_MENTIONS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAutoModMentionsShouldBeFound(String filter) throws Exception {
        restAutoModMentionsMockMvc.perform(get("/api/auto-mod-mentions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autoModMentions.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxMentions").value(hasItem(DEFAULT_MAX_MENTIONS)))
            .andExpect(jsonPath("$.[*].maxMsgLines").value(hasItem(DEFAULT_MAX_MSG_LINES)))
            .andExpect(jsonPath("$.[*].maxRoleMentions").value(hasItem(DEFAULT_MAX_ROLE_MENTIONS)));

        // Check, that the count call also returns 1
        restAutoModMentionsMockMvc.perform(get("/api/auto-mod-mentions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAutoModMentionsShouldNotBeFound(String filter) throws Exception {
        restAutoModMentionsMockMvc.perform(get("/api/auto-mod-mentions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAutoModMentionsMockMvc.perform(get("/api/auto-mod-mentions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAutoModMentions() throws Exception {
        // Get the autoModMentions
        restAutoModMentionsMockMvc.perform(get("/api/auto-mod-mentions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAutoModMentions() throws Exception {
        // Initialize the database
        autoModMentionsService.save(autoModMentions);

        int databaseSizeBeforeUpdate = autoModMentionsRepository.findAll().size();

        // Update the autoModMentions
        AutoModMentions updatedAutoModMentions = autoModMentionsRepository.findById(autoModMentions.getId()).get();
        // Disconnect from session so that the updates on updatedAutoModMentions are not directly saved in db
        em.detach(updatedAutoModMentions);
        updatedAutoModMentions
            .maxMentions(UPDATED_MAX_MENTIONS)
            .maxMsgLines(UPDATED_MAX_MSG_LINES)
            .maxRoleMentions(UPDATED_MAX_ROLE_MENTIONS);

        restAutoModMentionsMockMvc.perform(put("/api/auto-mod-mentions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAutoModMentions)))
            .andExpect(status().isOk());

        // Validate the AutoModMentions in the database
        List<AutoModMentions> autoModMentionsList = autoModMentionsRepository.findAll();
        assertThat(autoModMentionsList).hasSize(databaseSizeBeforeUpdate);
        AutoModMentions testAutoModMentions = autoModMentionsList.get(autoModMentionsList.size() - 1);
        assertThat(testAutoModMentions.getMaxMentions()).isEqualTo(UPDATED_MAX_MENTIONS);
        assertThat(testAutoModMentions.getMaxMsgLines()).isEqualTo(UPDATED_MAX_MSG_LINES);
        assertThat(testAutoModMentions.getMaxRoleMentions()).isEqualTo(UPDATED_MAX_ROLE_MENTIONS);
    }

    @Test
    @Transactional
    public void updateNonExistingAutoModMentions() throws Exception {
        int databaseSizeBeforeUpdate = autoModMentionsRepository.findAll().size();

        // Create the AutoModMentions

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutoModMentionsMockMvc.perform(put("/api/auto-mod-mentions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModMentions)))
            .andExpect(status().isBadRequest());

        // Validate the AutoModMentions in the database
        List<AutoModMentions> autoModMentionsList = autoModMentionsRepository.findAll();
        assertThat(autoModMentionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAutoModMentions() throws Exception {
        // Initialize the database
        autoModMentionsService.save(autoModMentions);

        int databaseSizeBeforeDelete = autoModMentionsRepository.findAll().size();

        // Delete the autoModMentions
        restAutoModMentionsMockMvc.perform(delete("/api/auto-mod-mentions/{id}", autoModMentions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AutoModMentions> autoModMentionsList = autoModMentionsRepository.findAll();
        assertThat(autoModMentionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutoModMentions.class);
        AutoModMentions autoModMentions1 = new AutoModMentions();
        autoModMentions1.setId(1L);
        AutoModMentions autoModMentions2 = new AutoModMentions();
        autoModMentions2.setId(autoModMentions1.getId());
        assertThat(autoModMentions1).isEqualTo(autoModMentions2);
        autoModMentions2.setId(2L);
        assertThat(autoModMentions1).isNotEqualTo(autoModMentions2);
        autoModMentions1.setId(null);
        assertThat(autoModMentions1).isNotEqualTo(autoModMentions2);
    }
}
