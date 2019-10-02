package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.Punishment;
import com.triippztech.guildo.repository.PunishmentRepository;
import com.triippztech.guildo.service.PunishmentService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.dto.PunishmentCriteria;
import com.triippztech.guildo.service.PunishmentQueryService;

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
 * Integration tests for the {@link PunishmentResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class PunishmentResourceIT {

    private static final Integer DEFAULT_MAX_STRIKES = 1;
    private static final Integer UPDATED_MAX_STRIKES = 2;
    private static final Integer SMALLER_MAX_STRIKES = 1 - 1;

    private static final PunishmentType DEFAULT_ACTION = PunishmentType.NONE;
    private static final PunishmentType UPDATED_ACTION = PunishmentType.KICK;

    private static final Instant DEFAULT_PUNISHMENT_DURATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUNISHMENT_DURATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_PUNISHMENT_DURATION = Instant.ofEpochMilli(-1L);

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;
    private static final Long SMALLER_GUILD_ID = 1L - 1L;

    @Autowired
    private PunishmentRepository punishmentRepository;

    @Autowired
    private PunishmentService punishmentService;

    @Autowired
    private PunishmentQueryService punishmentQueryService;

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

    private MockMvc restPunishmentMockMvc;

    private Punishment punishment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PunishmentResource punishmentResource = new PunishmentResource(punishmentService, punishmentQueryService);
        this.restPunishmentMockMvc = MockMvcBuilders.standaloneSetup(punishmentResource)
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
    public static Punishment createEntity(EntityManager em) {
        Punishment punishment = new Punishment()
            .maxStrikes(DEFAULT_MAX_STRIKES)
            .action(DEFAULT_ACTION)
            .punishmentDuration(DEFAULT_PUNISHMENT_DURATION)
            .guildId(DEFAULT_GUILD_ID);
        return punishment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Punishment createUpdatedEntity(EntityManager em) {
        Punishment punishment = new Punishment()
            .maxStrikes(UPDATED_MAX_STRIKES)
            .action(UPDATED_ACTION)
            .punishmentDuration(UPDATED_PUNISHMENT_DURATION)
            .guildId(UPDATED_GUILD_ID);
        return punishment;
    }

    @BeforeEach
    public void initTest() {
        punishment = createEntity(em);
    }

    @Test
    @Transactional
    public void createPunishment() throws Exception {
        int databaseSizeBeforeCreate = punishmentRepository.findAll().size();

        // Create the Punishment
        restPunishmentMockMvc.perform(post("/api/punishments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punishment)))
            .andExpect(status().isCreated());

        // Validate the Punishment in the database
        List<Punishment> punishmentList = punishmentRepository.findAll();
        assertThat(punishmentList).hasSize(databaseSizeBeforeCreate + 1);
        Punishment testPunishment = punishmentList.get(punishmentList.size() - 1);
        assertThat(testPunishment.getMaxStrikes()).isEqualTo(DEFAULT_MAX_STRIKES);
        assertThat(testPunishment.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testPunishment.getPunishmentDuration()).isEqualTo(DEFAULT_PUNISHMENT_DURATION);
        assertThat(testPunishment.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
    }

    @Test
    @Transactional
    public void createPunishmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = punishmentRepository.findAll().size();

        // Create the Punishment with an existing ID
        punishment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPunishmentMockMvc.perform(post("/api/punishments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punishment)))
            .andExpect(status().isBadRequest());

        // Validate the Punishment in the database
        List<Punishment> punishmentList = punishmentRepository.findAll();
        assertThat(punishmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkMaxStrikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = punishmentRepository.findAll().size();
        // set the field null
        punishment.setMaxStrikes(null);

        // Create the Punishment, which fails.

        restPunishmentMockMvc.perform(post("/api/punishments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punishment)))
            .andExpect(status().isBadRequest());

        List<Punishment> punishmentList = punishmentRepository.findAll();
        assertThat(punishmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActionIsRequired() throws Exception {
        int databaseSizeBeforeTest = punishmentRepository.findAll().size();
        // set the field null
        punishment.setAction(null);

        // Create the Punishment, which fails.

        restPunishmentMockMvc.perform(post("/api/punishments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punishment)))
            .andExpect(status().isBadRequest());

        List<Punishment> punishmentList = punishmentRepository.findAll();
        assertThat(punishmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPunishments() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList
        restPunishmentMockMvc.perform(get("/api/punishments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(punishment.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxStrikes").value(hasItem(DEFAULT_MAX_STRIKES)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].punishmentDuration").value(hasItem(DEFAULT_PUNISHMENT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getPunishment() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get the punishment
        restPunishmentMockMvc.perform(get("/api/punishments/{id}", punishment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(punishment.getId().intValue()))
            .andExpect(jsonPath("$.maxStrikes").value(DEFAULT_MAX_STRIKES))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.punishmentDuration").value(DEFAULT_PUNISHMENT_DURATION.toString()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllPunishmentsByMaxStrikesIsEqualToSomething() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where maxStrikes equals to DEFAULT_MAX_STRIKES
        defaultPunishmentShouldBeFound("maxStrikes.equals=" + DEFAULT_MAX_STRIKES);

        // Get all the punishmentList where maxStrikes equals to UPDATED_MAX_STRIKES
        defaultPunishmentShouldNotBeFound("maxStrikes.equals=" + UPDATED_MAX_STRIKES);
    }

    @Test
    @Transactional
    public void getAllPunishmentsByMaxStrikesIsInShouldWork() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where maxStrikes in DEFAULT_MAX_STRIKES or UPDATED_MAX_STRIKES
        defaultPunishmentShouldBeFound("maxStrikes.in=" + DEFAULT_MAX_STRIKES + "," + UPDATED_MAX_STRIKES);

        // Get all the punishmentList where maxStrikes equals to UPDATED_MAX_STRIKES
        defaultPunishmentShouldNotBeFound("maxStrikes.in=" + UPDATED_MAX_STRIKES);
    }

    @Test
    @Transactional
    public void getAllPunishmentsByMaxStrikesIsNullOrNotNull() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where maxStrikes is not null
        defaultPunishmentShouldBeFound("maxStrikes.specified=true");

        // Get all the punishmentList where maxStrikes is null
        defaultPunishmentShouldNotBeFound("maxStrikes.specified=false");
    }

    @Test
    @Transactional
    public void getAllPunishmentsByMaxStrikesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where maxStrikes is greater than or equal to DEFAULT_MAX_STRIKES
        defaultPunishmentShouldBeFound("maxStrikes.greaterThanOrEqual=" + DEFAULT_MAX_STRIKES);

        // Get all the punishmentList where maxStrikes is greater than or equal to UPDATED_MAX_STRIKES
        defaultPunishmentShouldNotBeFound("maxStrikes.greaterThanOrEqual=" + UPDATED_MAX_STRIKES);
    }

    @Test
    @Transactional
    public void getAllPunishmentsByMaxStrikesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where maxStrikes is less than or equal to DEFAULT_MAX_STRIKES
        defaultPunishmentShouldBeFound("maxStrikes.lessThanOrEqual=" + DEFAULT_MAX_STRIKES);

        // Get all the punishmentList where maxStrikes is less than or equal to SMALLER_MAX_STRIKES
        defaultPunishmentShouldNotBeFound("maxStrikes.lessThanOrEqual=" + SMALLER_MAX_STRIKES);
    }

    @Test
    @Transactional
    public void getAllPunishmentsByMaxStrikesIsLessThanSomething() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where maxStrikes is less than DEFAULT_MAX_STRIKES
        defaultPunishmentShouldNotBeFound("maxStrikes.lessThan=" + DEFAULT_MAX_STRIKES);

        // Get all the punishmentList where maxStrikes is less than UPDATED_MAX_STRIKES
        defaultPunishmentShouldBeFound("maxStrikes.lessThan=" + UPDATED_MAX_STRIKES);
    }

    @Test
    @Transactional
    public void getAllPunishmentsByMaxStrikesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where maxStrikes is greater than DEFAULT_MAX_STRIKES
        defaultPunishmentShouldNotBeFound("maxStrikes.greaterThan=" + DEFAULT_MAX_STRIKES);

        // Get all the punishmentList where maxStrikes is greater than SMALLER_MAX_STRIKES
        defaultPunishmentShouldBeFound("maxStrikes.greaterThan=" + SMALLER_MAX_STRIKES);
    }


    @Test
    @Transactional
    public void getAllPunishmentsByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where action equals to DEFAULT_ACTION
        defaultPunishmentShouldBeFound("action.equals=" + DEFAULT_ACTION);

        // Get all the punishmentList where action equals to UPDATED_ACTION
        defaultPunishmentShouldNotBeFound("action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    public void getAllPunishmentsByActionIsInShouldWork() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where action in DEFAULT_ACTION or UPDATED_ACTION
        defaultPunishmentShouldBeFound("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION);

        // Get all the punishmentList where action equals to UPDATED_ACTION
        defaultPunishmentShouldNotBeFound("action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    public void getAllPunishmentsByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where action is not null
        defaultPunishmentShouldBeFound("action.specified=true");

        // Get all the punishmentList where action is null
        defaultPunishmentShouldNotBeFound("action.specified=false");
    }

    @Test
    @Transactional
    public void getAllPunishmentsByPunishmentDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where punishmentDuration equals to DEFAULT_PUNISHMENT_DURATION
        defaultPunishmentShouldBeFound("punishmentDuration.equals=" + DEFAULT_PUNISHMENT_DURATION);

        // Get all the punishmentList where punishmentDuration equals to UPDATED_PUNISHMENT_DURATION
        defaultPunishmentShouldNotBeFound("punishmentDuration.equals=" + UPDATED_PUNISHMENT_DURATION);
    }

    @Test
    @Transactional
    public void getAllPunishmentsByPunishmentDurationIsInShouldWork() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where punishmentDuration in DEFAULT_PUNISHMENT_DURATION or UPDATED_PUNISHMENT_DURATION
        defaultPunishmentShouldBeFound("punishmentDuration.in=" + DEFAULT_PUNISHMENT_DURATION + "," + UPDATED_PUNISHMENT_DURATION);

        // Get all the punishmentList where punishmentDuration equals to UPDATED_PUNISHMENT_DURATION
        defaultPunishmentShouldNotBeFound("punishmentDuration.in=" + UPDATED_PUNISHMENT_DURATION);
    }

    @Test
    @Transactional
    public void getAllPunishmentsByPunishmentDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where punishmentDuration is not null
        defaultPunishmentShouldBeFound("punishmentDuration.specified=true");

        // Get all the punishmentList where punishmentDuration is null
        defaultPunishmentShouldNotBeFound("punishmentDuration.specified=false");
    }

    @Test
    @Transactional
    public void getAllPunishmentsByGuildIdIsEqualToSomething() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where guildId equals to DEFAULT_GUILD_ID
        defaultPunishmentShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);

        // Get all the punishmentList where guildId equals to UPDATED_GUILD_ID
        defaultPunishmentShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllPunishmentsByGuildIdIsInShouldWork() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
        defaultPunishmentShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);

        // Get all the punishmentList where guildId equals to UPDATED_GUILD_ID
        defaultPunishmentShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllPunishmentsByGuildIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where guildId is not null
        defaultPunishmentShouldBeFound("guildId.specified=true");

        // Get all the punishmentList where guildId is null
        defaultPunishmentShouldNotBeFound("guildId.specified=false");
    }

    @Test
    @Transactional
    public void getAllPunishmentsByGuildIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where guildId is greater than or equal to DEFAULT_GUILD_ID
        defaultPunishmentShouldBeFound("guildId.greaterThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the punishmentList where guildId is greater than or equal to UPDATED_GUILD_ID
        defaultPunishmentShouldNotBeFound("guildId.greaterThanOrEqual=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllPunishmentsByGuildIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where guildId is less than or equal to DEFAULT_GUILD_ID
        defaultPunishmentShouldBeFound("guildId.lessThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the punishmentList where guildId is less than or equal to SMALLER_GUILD_ID
        defaultPunishmentShouldNotBeFound("guildId.lessThanOrEqual=" + SMALLER_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllPunishmentsByGuildIdIsLessThanSomething() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where guildId is less than DEFAULT_GUILD_ID
        defaultPunishmentShouldNotBeFound("guildId.lessThan=" + DEFAULT_GUILD_ID);

        // Get all the punishmentList where guildId is less than UPDATED_GUILD_ID
        defaultPunishmentShouldBeFound("guildId.lessThan=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllPunishmentsByGuildIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        punishmentRepository.saveAndFlush(punishment);

        // Get all the punishmentList where guildId is greater than DEFAULT_GUILD_ID
        defaultPunishmentShouldNotBeFound("guildId.greaterThan=" + DEFAULT_GUILD_ID);

        // Get all the punishmentList where guildId is greater than SMALLER_GUILD_ID
        defaultPunishmentShouldBeFound("guildId.greaterThan=" + SMALLER_GUILD_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPunishmentShouldBeFound(String filter) throws Exception {
        restPunishmentMockMvc.perform(get("/api/punishments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(punishment.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxStrikes").value(hasItem(DEFAULT_MAX_STRIKES)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].punishmentDuration").value(hasItem(DEFAULT_PUNISHMENT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));

        // Check, that the count call also returns 1
        restPunishmentMockMvc.perform(get("/api/punishments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPunishmentShouldNotBeFound(String filter) throws Exception {
        restPunishmentMockMvc.perform(get("/api/punishments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPunishmentMockMvc.perform(get("/api/punishments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPunishment() throws Exception {
        // Get the punishment
        restPunishmentMockMvc.perform(get("/api/punishments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePunishment() throws Exception {
        // Initialize the database
        punishmentService.save(punishment);

        int databaseSizeBeforeUpdate = punishmentRepository.findAll().size();

        // Update the punishment
        Punishment updatedPunishment = punishmentRepository.findById(punishment.getId()).get();
        // Disconnect from session so that the updates on updatedPunishment are not directly saved in db
        em.detach(updatedPunishment);
        updatedPunishment
            .maxStrikes(UPDATED_MAX_STRIKES)
            .action(UPDATED_ACTION)
            .punishmentDuration(UPDATED_PUNISHMENT_DURATION)
            .guildId(UPDATED_GUILD_ID);

        restPunishmentMockMvc.perform(put("/api/punishments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPunishment)))
            .andExpect(status().isOk());

        // Validate the Punishment in the database
        List<Punishment> punishmentList = punishmentRepository.findAll();
        assertThat(punishmentList).hasSize(databaseSizeBeforeUpdate);
        Punishment testPunishment = punishmentList.get(punishmentList.size() - 1);
        assertThat(testPunishment.getMaxStrikes()).isEqualTo(UPDATED_MAX_STRIKES);
        assertThat(testPunishment.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testPunishment.getPunishmentDuration()).isEqualTo(UPDATED_PUNISHMENT_DURATION);
        assertThat(testPunishment.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingPunishment() throws Exception {
        int databaseSizeBeforeUpdate = punishmentRepository.findAll().size();

        // Create the Punishment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPunishmentMockMvc.perform(put("/api/punishments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(punishment)))
            .andExpect(status().isBadRequest());

        // Validate the Punishment in the database
        List<Punishment> punishmentList = punishmentRepository.findAll();
        assertThat(punishmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePunishment() throws Exception {
        // Initialize the database
        punishmentService.save(punishment);

        int databaseSizeBeforeDelete = punishmentRepository.findAll().size();

        // Delete the punishment
        restPunishmentMockMvc.perform(delete("/api/punishments/{id}", punishment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Punishment> punishmentList = punishmentRepository.findAll();
        assertThat(punishmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Punishment.class);
        Punishment punishment1 = new Punishment();
        punishment1.setId(1L);
        Punishment punishment2 = new Punishment();
        punishment2.setId(punishment1.getId());
        assertThat(punishment1).isEqualTo(punishment2);
        punishment2.setId(2L);
        assertThat(punishment1).isNotEqualTo(punishment2);
        punishment1.setId(null);
        assertThat(punishment1).isNotEqualTo(punishment2);
    }
}
