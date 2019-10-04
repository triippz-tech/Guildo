package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.AutoModAntiDup;
import com.triippztech.guildo.repository.AutoModAntiDupRepository;
import com.triippztech.guildo.service.moderation.AutoModAntiDupService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.moderation.AutoModAntiDupQueryService;

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
 * Integration tests for the {@link AutoModAntiDupResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class AutoModAntiDupResourceIT {

    private static final Integer DEFAULT_DELETE_THRESHOLD = 1;
    private static final Integer UPDATED_DELETE_THRESHOLD = 2;
    private static final Integer SMALLER_DELETE_THRESHOLD = 1 - 1;

    private static final Integer DEFAULT_DUPS_TO_PUNISH = 1;
    private static final Integer UPDATED_DUPS_TO_PUNISH = 2;
    private static final Integer SMALLER_DUPS_TO_PUNISH = 1 - 1;

    @Autowired
    private AutoModAntiDupRepository autoModAntiDupRepository;

    @Autowired
    private AutoModAntiDupService autoModAntiDupService;

    @Autowired
    private AutoModAntiDupQueryService autoModAntiDupQueryService;

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

    private MockMvc restAutoModAntiDupMockMvc;

    private AutoModAntiDup autoModAntiDup;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AutoModAntiDupResource autoModAntiDupResource = new AutoModAntiDupResource(autoModAntiDupService, autoModAntiDupQueryService);
        this.restAutoModAntiDupMockMvc = MockMvcBuilders.standaloneSetup(autoModAntiDupResource)
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
    public static AutoModAntiDup createEntity(EntityManager em) {
        AutoModAntiDup autoModAntiDup = new AutoModAntiDup()
            .deleteThreshold(DEFAULT_DELETE_THRESHOLD)
            .dupsToPunish(DEFAULT_DUPS_TO_PUNISH);
        return autoModAntiDup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AutoModAntiDup createUpdatedEntity(EntityManager em) {
        AutoModAntiDup autoModAntiDup = new AutoModAntiDup()
            .deleteThreshold(UPDATED_DELETE_THRESHOLD)
            .dupsToPunish(UPDATED_DUPS_TO_PUNISH);
        return autoModAntiDup;
    }

    @BeforeEach
    public void initTest() {
        autoModAntiDup = createEntity(em);
    }

    @Test
    @Transactional
    public void createAutoModAntiDup() throws Exception {
        int databaseSizeBeforeCreate = autoModAntiDupRepository.findAll().size();

        // Create the AutoModAntiDup
        restAutoModAntiDupMockMvc.perform(post("/api/auto-mod-anti-dups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModAntiDup)))
            .andExpect(status().isCreated());

        // Validate the AutoModAntiDup in the database
        List<AutoModAntiDup> autoModAntiDupList = autoModAntiDupRepository.findAll();
        assertThat(autoModAntiDupList).hasSize(databaseSizeBeforeCreate + 1);
        AutoModAntiDup testAutoModAntiDup = autoModAntiDupList.get(autoModAntiDupList.size() - 1);
        assertThat(testAutoModAntiDup.getDeleteThreshold()).isEqualTo(DEFAULT_DELETE_THRESHOLD);
        assertThat(testAutoModAntiDup.getDupsToPunish()).isEqualTo(DEFAULT_DUPS_TO_PUNISH);
    }

    @Test
    @Transactional
    public void createAutoModAntiDupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = autoModAntiDupRepository.findAll().size();

        // Create the AutoModAntiDup with an existing ID
        autoModAntiDup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutoModAntiDupMockMvc.perform(post("/api/auto-mod-anti-dups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModAntiDup)))
            .andExpect(status().isBadRequest());

        // Validate the AutoModAntiDup in the database
        List<AutoModAntiDup> autoModAntiDupList = autoModAntiDupRepository.findAll();
        assertThat(autoModAntiDupList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDeleteThresholdIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModAntiDupRepository.findAll().size();
        // set the field null
        autoModAntiDup.setDeleteThreshold(null);

        // Create the AutoModAntiDup, which fails.

        restAutoModAntiDupMockMvc.perform(post("/api/auto-mod-anti-dups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModAntiDup)))
            .andExpect(status().isBadRequest());

        List<AutoModAntiDup> autoModAntiDupList = autoModAntiDupRepository.findAll();
        assertThat(autoModAntiDupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDupsToPunishIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModAntiDupRepository.findAll().size();
        // set the field null
        autoModAntiDup.setDupsToPunish(null);

        // Create the AutoModAntiDup, which fails.

        restAutoModAntiDupMockMvc.perform(post("/api/auto-mod-anti-dups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModAntiDup)))
            .andExpect(status().isBadRequest());

        List<AutoModAntiDup> autoModAntiDupList = autoModAntiDupRepository.findAll();
        assertThat(autoModAntiDupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAutoModAntiDups() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get all the autoModAntiDupList
        restAutoModAntiDupMockMvc.perform(get("/api/auto-mod-anti-dups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autoModAntiDup.getId().intValue())))
            .andExpect(jsonPath("$.[*].deleteThreshold").value(hasItem(DEFAULT_DELETE_THRESHOLD)))
            .andExpect(jsonPath("$.[*].dupsToPunish").value(hasItem(DEFAULT_DUPS_TO_PUNISH)));
    }
    
    @Test
    @Transactional
    public void getAutoModAntiDup() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get the autoModAntiDup
        restAutoModAntiDupMockMvc.perform(get("/api/auto-mod-anti-dups/{id}", autoModAntiDup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(autoModAntiDup.getId().intValue()))
            .andExpect(jsonPath("$.deleteThreshold").value(DEFAULT_DELETE_THRESHOLD))
            .andExpect(jsonPath("$.dupsToPunish").value(DEFAULT_DUPS_TO_PUNISH));
    }

    @Test
    @Transactional
    public void getAllAutoModAntiDupsByDeleteThresholdIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get all the autoModAntiDupList where deleteThreshold equals to DEFAULT_DELETE_THRESHOLD
        defaultAutoModAntiDupShouldBeFound("deleteThreshold.equals=" + DEFAULT_DELETE_THRESHOLD);

        // Get all the autoModAntiDupList where deleteThreshold equals to UPDATED_DELETE_THRESHOLD
        defaultAutoModAntiDupShouldNotBeFound("deleteThreshold.equals=" + UPDATED_DELETE_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllAutoModAntiDupsByDeleteThresholdIsInShouldWork() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get all the autoModAntiDupList where deleteThreshold in DEFAULT_DELETE_THRESHOLD or UPDATED_DELETE_THRESHOLD
        defaultAutoModAntiDupShouldBeFound("deleteThreshold.in=" + DEFAULT_DELETE_THRESHOLD + "," + UPDATED_DELETE_THRESHOLD);

        // Get all the autoModAntiDupList where deleteThreshold equals to UPDATED_DELETE_THRESHOLD
        defaultAutoModAntiDupShouldNotBeFound("deleteThreshold.in=" + UPDATED_DELETE_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllAutoModAntiDupsByDeleteThresholdIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get all the autoModAntiDupList where deleteThreshold is not null
        defaultAutoModAntiDupShouldBeFound("deleteThreshold.specified=true");

        // Get all the autoModAntiDupList where deleteThreshold is null
        defaultAutoModAntiDupShouldNotBeFound("deleteThreshold.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModAntiDupsByDeleteThresholdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get all the autoModAntiDupList where deleteThreshold is greater than or equal to DEFAULT_DELETE_THRESHOLD
        defaultAutoModAntiDupShouldBeFound("deleteThreshold.greaterThanOrEqual=" + DEFAULT_DELETE_THRESHOLD);

        // Get all the autoModAntiDupList where deleteThreshold is greater than or equal to UPDATED_DELETE_THRESHOLD
        defaultAutoModAntiDupShouldNotBeFound("deleteThreshold.greaterThanOrEqual=" + UPDATED_DELETE_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllAutoModAntiDupsByDeleteThresholdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get all the autoModAntiDupList where deleteThreshold is less than or equal to DEFAULT_DELETE_THRESHOLD
        defaultAutoModAntiDupShouldBeFound("deleteThreshold.lessThanOrEqual=" + DEFAULT_DELETE_THRESHOLD);

        // Get all the autoModAntiDupList where deleteThreshold is less than or equal to SMALLER_DELETE_THRESHOLD
        defaultAutoModAntiDupShouldNotBeFound("deleteThreshold.lessThanOrEqual=" + SMALLER_DELETE_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllAutoModAntiDupsByDeleteThresholdIsLessThanSomething() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get all the autoModAntiDupList where deleteThreshold is less than DEFAULT_DELETE_THRESHOLD
        defaultAutoModAntiDupShouldNotBeFound("deleteThreshold.lessThan=" + DEFAULT_DELETE_THRESHOLD);

        // Get all the autoModAntiDupList where deleteThreshold is less than UPDATED_DELETE_THRESHOLD
        defaultAutoModAntiDupShouldBeFound("deleteThreshold.lessThan=" + UPDATED_DELETE_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllAutoModAntiDupsByDeleteThresholdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get all the autoModAntiDupList where deleteThreshold is greater than DEFAULT_DELETE_THRESHOLD
        defaultAutoModAntiDupShouldNotBeFound("deleteThreshold.greaterThan=" + DEFAULT_DELETE_THRESHOLD);

        // Get all the autoModAntiDupList where deleteThreshold is greater than SMALLER_DELETE_THRESHOLD
        defaultAutoModAntiDupShouldBeFound("deleteThreshold.greaterThan=" + SMALLER_DELETE_THRESHOLD);
    }


    @Test
    @Transactional
    public void getAllAutoModAntiDupsByDupsToPunishIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get all the autoModAntiDupList where dupsToPunish equals to DEFAULT_DUPS_TO_PUNISH
        defaultAutoModAntiDupShouldBeFound("dupsToPunish.equals=" + DEFAULT_DUPS_TO_PUNISH);

        // Get all the autoModAntiDupList where dupsToPunish equals to UPDATED_DUPS_TO_PUNISH
        defaultAutoModAntiDupShouldNotBeFound("dupsToPunish.equals=" + UPDATED_DUPS_TO_PUNISH);
    }

    @Test
    @Transactional
    public void getAllAutoModAntiDupsByDupsToPunishIsInShouldWork() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get all the autoModAntiDupList where dupsToPunish in DEFAULT_DUPS_TO_PUNISH or UPDATED_DUPS_TO_PUNISH
        defaultAutoModAntiDupShouldBeFound("dupsToPunish.in=" + DEFAULT_DUPS_TO_PUNISH + "," + UPDATED_DUPS_TO_PUNISH);

        // Get all the autoModAntiDupList where dupsToPunish equals to UPDATED_DUPS_TO_PUNISH
        defaultAutoModAntiDupShouldNotBeFound("dupsToPunish.in=" + UPDATED_DUPS_TO_PUNISH);
    }

    @Test
    @Transactional
    public void getAllAutoModAntiDupsByDupsToPunishIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get all the autoModAntiDupList where dupsToPunish is not null
        defaultAutoModAntiDupShouldBeFound("dupsToPunish.specified=true");

        // Get all the autoModAntiDupList where dupsToPunish is null
        defaultAutoModAntiDupShouldNotBeFound("dupsToPunish.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModAntiDupsByDupsToPunishIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get all the autoModAntiDupList where dupsToPunish is greater than or equal to DEFAULT_DUPS_TO_PUNISH
        defaultAutoModAntiDupShouldBeFound("dupsToPunish.greaterThanOrEqual=" + DEFAULT_DUPS_TO_PUNISH);

        // Get all the autoModAntiDupList where dupsToPunish is greater than or equal to UPDATED_DUPS_TO_PUNISH
        defaultAutoModAntiDupShouldNotBeFound("dupsToPunish.greaterThanOrEqual=" + UPDATED_DUPS_TO_PUNISH);
    }

    @Test
    @Transactional
    public void getAllAutoModAntiDupsByDupsToPunishIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get all the autoModAntiDupList where dupsToPunish is less than or equal to DEFAULT_DUPS_TO_PUNISH
        defaultAutoModAntiDupShouldBeFound("dupsToPunish.lessThanOrEqual=" + DEFAULT_DUPS_TO_PUNISH);

        // Get all the autoModAntiDupList where dupsToPunish is less than or equal to SMALLER_DUPS_TO_PUNISH
        defaultAutoModAntiDupShouldNotBeFound("dupsToPunish.lessThanOrEqual=" + SMALLER_DUPS_TO_PUNISH);
    }

    @Test
    @Transactional
    public void getAllAutoModAntiDupsByDupsToPunishIsLessThanSomething() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get all the autoModAntiDupList where dupsToPunish is less than DEFAULT_DUPS_TO_PUNISH
        defaultAutoModAntiDupShouldNotBeFound("dupsToPunish.lessThan=" + DEFAULT_DUPS_TO_PUNISH);

        // Get all the autoModAntiDupList where dupsToPunish is less than UPDATED_DUPS_TO_PUNISH
        defaultAutoModAntiDupShouldBeFound("dupsToPunish.lessThan=" + UPDATED_DUPS_TO_PUNISH);
    }

    @Test
    @Transactional
    public void getAllAutoModAntiDupsByDupsToPunishIsGreaterThanSomething() throws Exception {
        // Initialize the database
        autoModAntiDupRepository.saveAndFlush(autoModAntiDup);

        // Get all the autoModAntiDupList where dupsToPunish is greater than DEFAULT_DUPS_TO_PUNISH
        defaultAutoModAntiDupShouldNotBeFound("dupsToPunish.greaterThan=" + DEFAULT_DUPS_TO_PUNISH);

        // Get all the autoModAntiDupList where dupsToPunish is greater than SMALLER_DUPS_TO_PUNISH
        defaultAutoModAntiDupShouldBeFound("dupsToPunish.greaterThan=" + SMALLER_DUPS_TO_PUNISH);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAutoModAntiDupShouldBeFound(String filter) throws Exception {
        restAutoModAntiDupMockMvc.perform(get("/api/auto-mod-anti-dups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autoModAntiDup.getId().intValue())))
            .andExpect(jsonPath("$.[*].deleteThreshold").value(hasItem(DEFAULT_DELETE_THRESHOLD)))
            .andExpect(jsonPath("$.[*].dupsToPunish").value(hasItem(DEFAULT_DUPS_TO_PUNISH)));

        // Check, that the count call also returns 1
        restAutoModAntiDupMockMvc.perform(get("/api/auto-mod-anti-dups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAutoModAntiDupShouldNotBeFound(String filter) throws Exception {
        restAutoModAntiDupMockMvc.perform(get("/api/auto-mod-anti-dups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAutoModAntiDupMockMvc.perform(get("/api/auto-mod-anti-dups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAutoModAntiDup() throws Exception {
        // Get the autoModAntiDup
        restAutoModAntiDupMockMvc.perform(get("/api/auto-mod-anti-dups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAutoModAntiDup() throws Exception {
        // Initialize the database
        autoModAntiDupService.save(autoModAntiDup);

        int databaseSizeBeforeUpdate = autoModAntiDupRepository.findAll().size();

        // Update the autoModAntiDup
        AutoModAntiDup updatedAutoModAntiDup = autoModAntiDupRepository.findById(autoModAntiDup.getId()).get();
        // Disconnect from session so that the updates on updatedAutoModAntiDup are not directly saved in db
        em.detach(updatedAutoModAntiDup);
        updatedAutoModAntiDup
            .deleteThreshold(UPDATED_DELETE_THRESHOLD)
            .dupsToPunish(UPDATED_DUPS_TO_PUNISH);

        restAutoModAntiDupMockMvc.perform(put("/api/auto-mod-anti-dups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAutoModAntiDup)))
            .andExpect(status().isOk());

        // Validate the AutoModAntiDup in the database
        List<AutoModAntiDup> autoModAntiDupList = autoModAntiDupRepository.findAll();
        assertThat(autoModAntiDupList).hasSize(databaseSizeBeforeUpdate);
        AutoModAntiDup testAutoModAntiDup = autoModAntiDupList.get(autoModAntiDupList.size() - 1);
        assertThat(testAutoModAntiDup.getDeleteThreshold()).isEqualTo(UPDATED_DELETE_THRESHOLD);
        assertThat(testAutoModAntiDup.getDupsToPunish()).isEqualTo(UPDATED_DUPS_TO_PUNISH);
    }

    @Test
    @Transactional
    public void updateNonExistingAutoModAntiDup() throws Exception {
        int databaseSizeBeforeUpdate = autoModAntiDupRepository.findAll().size();

        // Create the AutoModAntiDup

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutoModAntiDupMockMvc.perform(put("/api/auto-mod-anti-dups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModAntiDup)))
            .andExpect(status().isBadRequest());

        // Validate the AutoModAntiDup in the database
        List<AutoModAntiDup> autoModAntiDupList = autoModAntiDupRepository.findAll();
        assertThat(autoModAntiDupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAutoModAntiDup() throws Exception {
        // Initialize the database
        autoModAntiDupService.save(autoModAntiDup);

        int databaseSizeBeforeDelete = autoModAntiDupRepository.findAll().size();

        // Delete the autoModAntiDup
        restAutoModAntiDupMockMvc.perform(delete("/api/auto-mod-anti-dups/{id}", autoModAntiDup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AutoModAntiDup> autoModAntiDupList = autoModAntiDupRepository.findAll();
        assertThat(autoModAntiDupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutoModAntiDup.class);
        AutoModAntiDup autoModAntiDup1 = new AutoModAntiDup();
        autoModAntiDup1.setId(1L);
        AutoModAntiDup autoModAntiDup2 = new AutoModAntiDup();
        autoModAntiDup2.setId(autoModAntiDup1.getId());
        assertThat(autoModAntiDup1).isEqualTo(autoModAntiDup2);
        autoModAntiDup2.setId(2L);
        assertThat(autoModAntiDup1).isNotEqualTo(autoModAntiDup2);
        autoModAntiDup1.setId(null);
        assertThat(autoModAntiDup1).isNotEqualTo(autoModAntiDup2);
    }
}
