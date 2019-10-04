package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.AutoModAutoRaid;
import com.triippztech.guildo.repository.AutoModAutoRaidRepository;
import com.triippztech.guildo.service.moderation.AutoModAutoRaidService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.moderation.AutoModAutoRaidQueryService;

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
 * Integration tests for the {@link AutoModAutoRaidResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class AutoModAutoRaidResourceIT {

    private static final Boolean DEFAULT_AUTO_RAID_ENABLED = false;
    private static final Boolean UPDATED_AUTO_RAID_ENABLED = true;

    private static final Integer DEFAULT_AUTO_RAID_TIME_THRESHOLD = 1;
    private static final Integer UPDATED_AUTO_RAID_TIME_THRESHOLD = 2;
    private static final Integer SMALLER_AUTO_RAID_TIME_THRESHOLD = 1 - 1;

    @Autowired
    private AutoModAutoRaidRepository autoModAutoRaidRepository;

    @Autowired
    private AutoModAutoRaidService autoModAutoRaidService;

    @Autowired
    private AutoModAutoRaidQueryService autoModAutoRaidQueryService;

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

    private MockMvc restAutoModAutoRaidMockMvc;

    private AutoModAutoRaid autoModAutoRaid;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AutoModAutoRaidResource autoModAutoRaidResource = new AutoModAutoRaidResource(autoModAutoRaidService, autoModAutoRaidQueryService);
        this.restAutoModAutoRaidMockMvc = MockMvcBuilders.standaloneSetup(autoModAutoRaidResource)
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
    public static AutoModAutoRaid createEntity(EntityManager em) {
        AutoModAutoRaid autoModAutoRaid = new AutoModAutoRaid()
            .autoRaidEnabled(DEFAULT_AUTO_RAID_ENABLED)
            .autoRaidTimeThreshold(DEFAULT_AUTO_RAID_TIME_THRESHOLD);
        return autoModAutoRaid;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AutoModAutoRaid createUpdatedEntity(EntityManager em) {
        AutoModAutoRaid autoModAutoRaid = new AutoModAutoRaid()
            .autoRaidEnabled(UPDATED_AUTO_RAID_ENABLED)
            .autoRaidTimeThreshold(UPDATED_AUTO_RAID_TIME_THRESHOLD);
        return autoModAutoRaid;
    }

    @BeforeEach
    public void initTest() {
        autoModAutoRaid = createEntity(em);
    }

    @Test
    @Transactional
    public void createAutoModAutoRaid() throws Exception {
        int databaseSizeBeforeCreate = autoModAutoRaidRepository.findAll().size();

        // Create the AutoModAutoRaid
        restAutoModAutoRaidMockMvc.perform(post("/api/auto-mod-auto-raids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModAutoRaid)))
            .andExpect(status().isCreated());

        // Validate the AutoModAutoRaid in the database
        List<AutoModAutoRaid> autoModAutoRaidList = autoModAutoRaidRepository.findAll();
        assertThat(autoModAutoRaidList).hasSize(databaseSizeBeforeCreate + 1);
        AutoModAutoRaid testAutoModAutoRaid = autoModAutoRaidList.get(autoModAutoRaidList.size() - 1);
        assertThat(testAutoModAutoRaid.isAutoRaidEnabled()).isEqualTo(DEFAULT_AUTO_RAID_ENABLED);
        assertThat(testAutoModAutoRaid.getAutoRaidTimeThreshold()).isEqualTo(DEFAULT_AUTO_RAID_TIME_THRESHOLD);
    }

    @Test
    @Transactional
    public void createAutoModAutoRaidWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = autoModAutoRaidRepository.findAll().size();

        // Create the AutoModAutoRaid with an existing ID
        autoModAutoRaid.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutoModAutoRaidMockMvc.perform(post("/api/auto-mod-auto-raids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModAutoRaid)))
            .andExpect(status().isBadRequest());

        // Validate the AutoModAutoRaid in the database
        List<AutoModAutoRaid> autoModAutoRaidList = autoModAutoRaidRepository.findAll();
        assertThat(autoModAutoRaidList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkAutoRaidEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModAutoRaidRepository.findAll().size();
        // set the field null
        autoModAutoRaid.setAutoRaidEnabled(null);

        // Create the AutoModAutoRaid, which fails.

        restAutoModAutoRaidMockMvc.perform(post("/api/auto-mod-auto-raids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModAutoRaid)))
            .andExpect(status().isBadRequest());

        List<AutoModAutoRaid> autoModAutoRaidList = autoModAutoRaidRepository.findAll();
        assertThat(autoModAutoRaidList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAutoRaidTimeThresholdIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModAutoRaidRepository.findAll().size();
        // set the field null
        autoModAutoRaid.setAutoRaidTimeThreshold(null);

        // Create the AutoModAutoRaid, which fails.

        restAutoModAutoRaidMockMvc.perform(post("/api/auto-mod-auto-raids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModAutoRaid)))
            .andExpect(status().isBadRequest());

        List<AutoModAutoRaid> autoModAutoRaidList = autoModAutoRaidRepository.findAll();
        assertThat(autoModAutoRaidList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAutoModAutoRaids() throws Exception {
        // Initialize the database
        autoModAutoRaidRepository.saveAndFlush(autoModAutoRaid);

        // Get all the autoModAutoRaidList
        restAutoModAutoRaidMockMvc.perform(get("/api/auto-mod-auto-raids?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autoModAutoRaid.getId().intValue())))
            .andExpect(jsonPath("$.[*].autoRaidEnabled").value(hasItem(DEFAULT_AUTO_RAID_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].autoRaidTimeThreshold").value(hasItem(DEFAULT_AUTO_RAID_TIME_THRESHOLD)));
    }
    
    @Test
    @Transactional
    public void getAutoModAutoRaid() throws Exception {
        // Initialize the database
        autoModAutoRaidRepository.saveAndFlush(autoModAutoRaid);

        // Get the autoModAutoRaid
        restAutoModAutoRaidMockMvc.perform(get("/api/auto-mod-auto-raids/{id}", autoModAutoRaid.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(autoModAutoRaid.getId().intValue()))
            .andExpect(jsonPath("$.autoRaidEnabled").value(DEFAULT_AUTO_RAID_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.autoRaidTimeThreshold").value(DEFAULT_AUTO_RAID_TIME_THRESHOLD));
    }

    @Test
    @Transactional
    public void getAllAutoModAutoRaidsByAutoRaidEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModAutoRaidRepository.saveAndFlush(autoModAutoRaid);

        // Get all the autoModAutoRaidList where autoRaidEnabled equals to DEFAULT_AUTO_RAID_ENABLED
        defaultAutoModAutoRaidShouldBeFound("autoRaidEnabled.equals=" + DEFAULT_AUTO_RAID_ENABLED);

        // Get all the autoModAutoRaidList where autoRaidEnabled equals to UPDATED_AUTO_RAID_ENABLED
        defaultAutoModAutoRaidShouldNotBeFound("autoRaidEnabled.equals=" + UPDATED_AUTO_RAID_ENABLED);
    }

    @Test
    @Transactional
    public void getAllAutoModAutoRaidsByAutoRaidEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        autoModAutoRaidRepository.saveAndFlush(autoModAutoRaid);

        // Get all the autoModAutoRaidList where autoRaidEnabled in DEFAULT_AUTO_RAID_ENABLED or UPDATED_AUTO_RAID_ENABLED
        defaultAutoModAutoRaidShouldBeFound("autoRaidEnabled.in=" + DEFAULT_AUTO_RAID_ENABLED + "," + UPDATED_AUTO_RAID_ENABLED);

        // Get all the autoModAutoRaidList where autoRaidEnabled equals to UPDATED_AUTO_RAID_ENABLED
        defaultAutoModAutoRaidShouldNotBeFound("autoRaidEnabled.in=" + UPDATED_AUTO_RAID_ENABLED);
    }

    @Test
    @Transactional
    public void getAllAutoModAutoRaidsByAutoRaidEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModAutoRaidRepository.saveAndFlush(autoModAutoRaid);

        // Get all the autoModAutoRaidList where autoRaidEnabled is not null
        defaultAutoModAutoRaidShouldBeFound("autoRaidEnabled.specified=true");

        // Get all the autoModAutoRaidList where autoRaidEnabled is null
        defaultAutoModAutoRaidShouldNotBeFound("autoRaidEnabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModAutoRaidsByAutoRaidTimeThresholdIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModAutoRaidRepository.saveAndFlush(autoModAutoRaid);

        // Get all the autoModAutoRaidList where autoRaidTimeThreshold equals to DEFAULT_AUTO_RAID_TIME_THRESHOLD
        defaultAutoModAutoRaidShouldBeFound("autoRaidTimeThreshold.equals=" + DEFAULT_AUTO_RAID_TIME_THRESHOLD);

        // Get all the autoModAutoRaidList where autoRaidTimeThreshold equals to UPDATED_AUTO_RAID_TIME_THRESHOLD
        defaultAutoModAutoRaidShouldNotBeFound("autoRaidTimeThreshold.equals=" + UPDATED_AUTO_RAID_TIME_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllAutoModAutoRaidsByAutoRaidTimeThresholdIsInShouldWork() throws Exception {
        // Initialize the database
        autoModAutoRaidRepository.saveAndFlush(autoModAutoRaid);

        // Get all the autoModAutoRaidList where autoRaidTimeThreshold in DEFAULT_AUTO_RAID_TIME_THRESHOLD or UPDATED_AUTO_RAID_TIME_THRESHOLD
        defaultAutoModAutoRaidShouldBeFound("autoRaidTimeThreshold.in=" + DEFAULT_AUTO_RAID_TIME_THRESHOLD + "," + UPDATED_AUTO_RAID_TIME_THRESHOLD);

        // Get all the autoModAutoRaidList where autoRaidTimeThreshold equals to UPDATED_AUTO_RAID_TIME_THRESHOLD
        defaultAutoModAutoRaidShouldNotBeFound("autoRaidTimeThreshold.in=" + UPDATED_AUTO_RAID_TIME_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllAutoModAutoRaidsByAutoRaidTimeThresholdIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModAutoRaidRepository.saveAndFlush(autoModAutoRaid);

        // Get all the autoModAutoRaidList where autoRaidTimeThreshold is not null
        defaultAutoModAutoRaidShouldBeFound("autoRaidTimeThreshold.specified=true");

        // Get all the autoModAutoRaidList where autoRaidTimeThreshold is null
        defaultAutoModAutoRaidShouldNotBeFound("autoRaidTimeThreshold.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModAutoRaidsByAutoRaidTimeThresholdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModAutoRaidRepository.saveAndFlush(autoModAutoRaid);

        // Get all the autoModAutoRaidList where autoRaidTimeThreshold is greater than or equal to DEFAULT_AUTO_RAID_TIME_THRESHOLD
        defaultAutoModAutoRaidShouldBeFound("autoRaidTimeThreshold.greaterThanOrEqual=" + DEFAULT_AUTO_RAID_TIME_THRESHOLD);

        // Get all the autoModAutoRaidList where autoRaidTimeThreshold is greater than or equal to UPDATED_AUTO_RAID_TIME_THRESHOLD
        defaultAutoModAutoRaidShouldNotBeFound("autoRaidTimeThreshold.greaterThanOrEqual=" + UPDATED_AUTO_RAID_TIME_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllAutoModAutoRaidsByAutoRaidTimeThresholdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModAutoRaidRepository.saveAndFlush(autoModAutoRaid);

        // Get all the autoModAutoRaidList where autoRaidTimeThreshold is less than or equal to DEFAULT_AUTO_RAID_TIME_THRESHOLD
        defaultAutoModAutoRaidShouldBeFound("autoRaidTimeThreshold.lessThanOrEqual=" + DEFAULT_AUTO_RAID_TIME_THRESHOLD);

        // Get all the autoModAutoRaidList where autoRaidTimeThreshold is less than or equal to SMALLER_AUTO_RAID_TIME_THRESHOLD
        defaultAutoModAutoRaidShouldNotBeFound("autoRaidTimeThreshold.lessThanOrEqual=" + SMALLER_AUTO_RAID_TIME_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllAutoModAutoRaidsByAutoRaidTimeThresholdIsLessThanSomething() throws Exception {
        // Initialize the database
        autoModAutoRaidRepository.saveAndFlush(autoModAutoRaid);

        // Get all the autoModAutoRaidList where autoRaidTimeThreshold is less than DEFAULT_AUTO_RAID_TIME_THRESHOLD
        defaultAutoModAutoRaidShouldNotBeFound("autoRaidTimeThreshold.lessThan=" + DEFAULT_AUTO_RAID_TIME_THRESHOLD);

        // Get all the autoModAutoRaidList where autoRaidTimeThreshold is less than UPDATED_AUTO_RAID_TIME_THRESHOLD
        defaultAutoModAutoRaidShouldBeFound("autoRaidTimeThreshold.lessThan=" + UPDATED_AUTO_RAID_TIME_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllAutoModAutoRaidsByAutoRaidTimeThresholdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        autoModAutoRaidRepository.saveAndFlush(autoModAutoRaid);

        // Get all the autoModAutoRaidList where autoRaidTimeThreshold is greater than DEFAULT_AUTO_RAID_TIME_THRESHOLD
        defaultAutoModAutoRaidShouldNotBeFound("autoRaidTimeThreshold.greaterThan=" + DEFAULT_AUTO_RAID_TIME_THRESHOLD);

        // Get all the autoModAutoRaidList where autoRaidTimeThreshold is greater than SMALLER_AUTO_RAID_TIME_THRESHOLD
        defaultAutoModAutoRaidShouldBeFound("autoRaidTimeThreshold.greaterThan=" + SMALLER_AUTO_RAID_TIME_THRESHOLD);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAutoModAutoRaidShouldBeFound(String filter) throws Exception {
        restAutoModAutoRaidMockMvc.perform(get("/api/auto-mod-auto-raids?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autoModAutoRaid.getId().intValue())))
            .andExpect(jsonPath("$.[*].autoRaidEnabled").value(hasItem(DEFAULT_AUTO_RAID_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].autoRaidTimeThreshold").value(hasItem(DEFAULT_AUTO_RAID_TIME_THRESHOLD)));

        // Check, that the count call also returns 1
        restAutoModAutoRaidMockMvc.perform(get("/api/auto-mod-auto-raids/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAutoModAutoRaidShouldNotBeFound(String filter) throws Exception {
        restAutoModAutoRaidMockMvc.perform(get("/api/auto-mod-auto-raids?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAutoModAutoRaidMockMvc.perform(get("/api/auto-mod-auto-raids/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAutoModAutoRaid() throws Exception {
        // Get the autoModAutoRaid
        restAutoModAutoRaidMockMvc.perform(get("/api/auto-mod-auto-raids/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAutoModAutoRaid() throws Exception {
        // Initialize the database
        autoModAutoRaidService.save(autoModAutoRaid);

        int databaseSizeBeforeUpdate = autoModAutoRaidRepository.findAll().size();

        // Update the autoModAutoRaid
        AutoModAutoRaid updatedAutoModAutoRaid = autoModAutoRaidRepository.findById(autoModAutoRaid.getId()).get();
        // Disconnect from session so that the updates on updatedAutoModAutoRaid are not directly saved in db
        em.detach(updatedAutoModAutoRaid);
        updatedAutoModAutoRaid
            .autoRaidEnabled(UPDATED_AUTO_RAID_ENABLED)
            .autoRaidTimeThreshold(UPDATED_AUTO_RAID_TIME_THRESHOLD);

        restAutoModAutoRaidMockMvc.perform(put("/api/auto-mod-auto-raids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAutoModAutoRaid)))
            .andExpect(status().isOk());

        // Validate the AutoModAutoRaid in the database
        List<AutoModAutoRaid> autoModAutoRaidList = autoModAutoRaidRepository.findAll();
        assertThat(autoModAutoRaidList).hasSize(databaseSizeBeforeUpdate);
        AutoModAutoRaid testAutoModAutoRaid = autoModAutoRaidList.get(autoModAutoRaidList.size() - 1);
        assertThat(testAutoModAutoRaid.isAutoRaidEnabled()).isEqualTo(UPDATED_AUTO_RAID_ENABLED);
        assertThat(testAutoModAutoRaid.getAutoRaidTimeThreshold()).isEqualTo(UPDATED_AUTO_RAID_TIME_THRESHOLD);
    }

    @Test
    @Transactional
    public void updateNonExistingAutoModAutoRaid() throws Exception {
        int databaseSizeBeforeUpdate = autoModAutoRaidRepository.findAll().size();

        // Create the AutoModAutoRaid

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutoModAutoRaidMockMvc.perform(put("/api/auto-mod-auto-raids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModAutoRaid)))
            .andExpect(status().isBadRequest());

        // Validate the AutoModAutoRaid in the database
        List<AutoModAutoRaid> autoModAutoRaidList = autoModAutoRaidRepository.findAll();
        assertThat(autoModAutoRaidList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAutoModAutoRaid() throws Exception {
        // Initialize the database
        autoModAutoRaidService.save(autoModAutoRaid);

        int databaseSizeBeforeDelete = autoModAutoRaidRepository.findAll().size();

        // Delete the autoModAutoRaid
        restAutoModAutoRaidMockMvc.perform(delete("/api/auto-mod-auto-raids/{id}", autoModAutoRaid.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AutoModAutoRaid> autoModAutoRaidList = autoModAutoRaidRepository.findAll();
        assertThat(autoModAutoRaidList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutoModAutoRaid.class);
        AutoModAutoRaid autoModAutoRaid1 = new AutoModAutoRaid();
        autoModAutoRaid1.setId(1L);
        AutoModAutoRaid autoModAutoRaid2 = new AutoModAutoRaid();
        autoModAutoRaid2.setId(autoModAutoRaid1.getId());
        assertThat(autoModAutoRaid1).isEqualTo(autoModAutoRaid2);
        autoModAutoRaid2.setId(2L);
        assertThat(autoModAutoRaid1).isNotEqualTo(autoModAutoRaid2);
        autoModAutoRaid1.setId(null);
        assertThat(autoModAutoRaid1).isNotEqualTo(autoModAutoRaid2);
    }
}
