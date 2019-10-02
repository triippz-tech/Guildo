package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.AutoModIgnore;
import com.triippztech.guildo.repository.AutoModIgnoreRepository;
import com.triippztech.guildo.service.AutoModIgnoreService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.dto.AutoModIgnoreCriteria;
import com.triippztech.guildo.service.AutoModIgnoreQueryService;

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
 * Integration tests for the {@link AutoModIgnoreResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class AutoModIgnoreResourceIT {

    private static final Long DEFAULT_ROLE_ID = 1L;
    private static final Long UPDATED_ROLE_ID = 2L;
    private static final Long SMALLER_ROLE_ID = 1L - 1L;

    private static final Long DEFAULT_CHANNEL_ID = 1L;
    private static final Long UPDATED_CHANNEL_ID = 2L;
    private static final Long SMALLER_CHANNEL_ID = 1L - 1L;

    @Autowired
    private AutoModIgnoreRepository autoModIgnoreRepository;

    @Autowired
    private AutoModIgnoreService autoModIgnoreService;

    @Autowired
    private AutoModIgnoreQueryService autoModIgnoreQueryService;

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

    private MockMvc restAutoModIgnoreMockMvc;

    private AutoModIgnore autoModIgnore;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AutoModIgnoreResource autoModIgnoreResource = new AutoModIgnoreResource(autoModIgnoreService, autoModIgnoreQueryService);
        this.restAutoModIgnoreMockMvc = MockMvcBuilders.standaloneSetup(autoModIgnoreResource)
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
    public static AutoModIgnore createEntity(EntityManager em) {
        AutoModIgnore autoModIgnore = new AutoModIgnore()
            .roleId(DEFAULT_ROLE_ID)
            .channelId(DEFAULT_CHANNEL_ID);
        return autoModIgnore;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AutoModIgnore createUpdatedEntity(EntityManager em) {
        AutoModIgnore autoModIgnore = new AutoModIgnore()
            .roleId(UPDATED_ROLE_ID)
            .channelId(UPDATED_CHANNEL_ID);
        return autoModIgnore;
    }

    @BeforeEach
    public void initTest() {
        autoModIgnore = createEntity(em);
    }

    @Test
    @Transactional
    public void createAutoModIgnore() throws Exception {
        int databaseSizeBeforeCreate = autoModIgnoreRepository.findAll().size();

        // Create the AutoModIgnore
        restAutoModIgnoreMockMvc.perform(post("/api/auto-mod-ignores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModIgnore)))
            .andExpect(status().isCreated());

        // Validate the AutoModIgnore in the database
        List<AutoModIgnore> autoModIgnoreList = autoModIgnoreRepository.findAll();
        assertThat(autoModIgnoreList).hasSize(databaseSizeBeforeCreate + 1);
        AutoModIgnore testAutoModIgnore = autoModIgnoreList.get(autoModIgnoreList.size() - 1);
        assertThat(testAutoModIgnore.getRoleId()).isEqualTo(DEFAULT_ROLE_ID);
        assertThat(testAutoModIgnore.getChannelId()).isEqualTo(DEFAULT_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void createAutoModIgnoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = autoModIgnoreRepository.findAll().size();

        // Create the AutoModIgnore with an existing ID
        autoModIgnore.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutoModIgnoreMockMvc.perform(post("/api/auto-mod-ignores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModIgnore)))
            .andExpect(status().isBadRequest());

        // Validate the AutoModIgnore in the database
        List<AutoModIgnore> autoModIgnoreList = autoModIgnoreRepository.findAll();
        assertThat(autoModIgnoreList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkRoleIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModIgnoreRepository.findAll().size();
        // set the field null
        autoModIgnore.setRoleId(null);

        // Create the AutoModIgnore, which fails.

        restAutoModIgnoreMockMvc.perform(post("/api/auto-mod-ignores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModIgnore)))
            .andExpect(status().isBadRequest());

        List<AutoModIgnore> autoModIgnoreList = autoModIgnoreRepository.findAll();
        assertThat(autoModIgnoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChannelIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoModIgnoreRepository.findAll().size();
        // set the field null
        autoModIgnore.setChannelId(null);

        // Create the AutoModIgnore, which fails.

        restAutoModIgnoreMockMvc.perform(post("/api/auto-mod-ignores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModIgnore)))
            .andExpect(status().isBadRequest());

        List<AutoModIgnore> autoModIgnoreList = autoModIgnoreRepository.findAll();
        assertThat(autoModIgnoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAutoModIgnores() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get all the autoModIgnoreList
        restAutoModIgnoreMockMvc.perform(get("/api/auto-mod-ignores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autoModIgnore.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleId").value(hasItem(DEFAULT_ROLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].channelId").value(hasItem(DEFAULT_CHANNEL_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getAutoModIgnore() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get the autoModIgnore
        restAutoModIgnoreMockMvc.perform(get("/api/auto-mod-ignores/{id}", autoModIgnore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(autoModIgnore.getId().intValue()))
            .andExpect(jsonPath("$.roleId").value(DEFAULT_ROLE_ID.intValue()))
            .andExpect(jsonPath("$.channelId").value(DEFAULT_CHANNEL_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllAutoModIgnoresByRoleIdIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get all the autoModIgnoreList where roleId equals to DEFAULT_ROLE_ID
        defaultAutoModIgnoreShouldBeFound("roleId.equals=" + DEFAULT_ROLE_ID);

        // Get all the autoModIgnoreList where roleId equals to UPDATED_ROLE_ID
        defaultAutoModIgnoreShouldNotBeFound("roleId.equals=" + UPDATED_ROLE_ID);
    }

    @Test
    @Transactional
    public void getAllAutoModIgnoresByRoleIdIsInShouldWork() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get all the autoModIgnoreList where roleId in DEFAULT_ROLE_ID or UPDATED_ROLE_ID
        defaultAutoModIgnoreShouldBeFound("roleId.in=" + DEFAULT_ROLE_ID + "," + UPDATED_ROLE_ID);

        // Get all the autoModIgnoreList where roleId equals to UPDATED_ROLE_ID
        defaultAutoModIgnoreShouldNotBeFound("roleId.in=" + UPDATED_ROLE_ID);
    }

    @Test
    @Transactional
    public void getAllAutoModIgnoresByRoleIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get all the autoModIgnoreList where roleId is not null
        defaultAutoModIgnoreShouldBeFound("roleId.specified=true");

        // Get all the autoModIgnoreList where roleId is null
        defaultAutoModIgnoreShouldNotBeFound("roleId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModIgnoresByRoleIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get all the autoModIgnoreList where roleId is greater than or equal to DEFAULT_ROLE_ID
        defaultAutoModIgnoreShouldBeFound("roleId.greaterThanOrEqual=" + DEFAULT_ROLE_ID);

        // Get all the autoModIgnoreList where roleId is greater than or equal to UPDATED_ROLE_ID
        defaultAutoModIgnoreShouldNotBeFound("roleId.greaterThanOrEqual=" + UPDATED_ROLE_ID);
    }

    @Test
    @Transactional
    public void getAllAutoModIgnoresByRoleIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get all the autoModIgnoreList where roleId is less than or equal to DEFAULT_ROLE_ID
        defaultAutoModIgnoreShouldBeFound("roleId.lessThanOrEqual=" + DEFAULT_ROLE_ID);

        // Get all the autoModIgnoreList where roleId is less than or equal to SMALLER_ROLE_ID
        defaultAutoModIgnoreShouldNotBeFound("roleId.lessThanOrEqual=" + SMALLER_ROLE_ID);
    }

    @Test
    @Transactional
    public void getAllAutoModIgnoresByRoleIdIsLessThanSomething() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get all the autoModIgnoreList where roleId is less than DEFAULT_ROLE_ID
        defaultAutoModIgnoreShouldNotBeFound("roleId.lessThan=" + DEFAULT_ROLE_ID);

        // Get all the autoModIgnoreList where roleId is less than UPDATED_ROLE_ID
        defaultAutoModIgnoreShouldBeFound("roleId.lessThan=" + UPDATED_ROLE_ID);
    }

    @Test
    @Transactional
    public void getAllAutoModIgnoresByRoleIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get all the autoModIgnoreList where roleId is greater than DEFAULT_ROLE_ID
        defaultAutoModIgnoreShouldNotBeFound("roleId.greaterThan=" + DEFAULT_ROLE_ID);

        // Get all the autoModIgnoreList where roleId is greater than SMALLER_ROLE_ID
        defaultAutoModIgnoreShouldBeFound("roleId.greaterThan=" + SMALLER_ROLE_ID);
    }


    @Test
    @Transactional
    public void getAllAutoModIgnoresByChannelIdIsEqualToSomething() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get all the autoModIgnoreList where channelId equals to DEFAULT_CHANNEL_ID
        defaultAutoModIgnoreShouldBeFound("channelId.equals=" + DEFAULT_CHANNEL_ID);

        // Get all the autoModIgnoreList where channelId equals to UPDATED_CHANNEL_ID
        defaultAutoModIgnoreShouldNotBeFound("channelId.equals=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllAutoModIgnoresByChannelIdIsInShouldWork() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get all the autoModIgnoreList where channelId in DEFAULT_CHANNEL_ID or UPDATED_CHANNEL_ID
        defaultAutoModIgnoreShouldBeFound("channelId.in=" + DEFAULT_CHANNEL_ID + "," + UPDATED_CHANNEL_ID);

        // Get all the autoModIgnoreList where channelId equals to UPDATED_CHANNEL_ID
        defaultAutoModIgnoreShouldNotBeFound("channelId.in=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllAutoModIgnoresByChannelIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get all the autoModIgnoreList where channelId is not null
        defaultAutoModIgnoreShouldBeFound("channelId.specified=true");

        // Get all the autoModIgnoreList where channelId is null
        defaultAutoModIgnoreShouldNotBeFound("channelId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAutoModIgnoresByChannelIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get all the autoModIgnoreList where channelId is greater than or equal to DEFAULT_CHANNEL_ID
        defaultAutoModIgnoreShouldBeFound("channelId.greaterThanOrEqual=" + DEFAULT_CHANNEL_ID);

        // Get all the autoModIgnoreList where channelId is greater than or equal to UPDATED_CHANNEL_ID
        defaultAutoModIgnoreShouldNotBeFound("channelId.greaterThanOrEqual=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllAutoModIgnoresByChannelIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get all the autoModIgnoreList where channelId is less than or equal to DEFAULT_CHANNEL_ID
        defaultAutoModIgnoreShouldBeFound("channelId.lessThanOrEqual=" + DEFAULT_CHANNEL_ID);

        // Get all the autoModIgnoreList where channelId is less than or equal to SMALLER_CHANNEL_ID
        defaultAutoModIgnoreShouldNotBeFound("channelId.lessThanOrEqual=" + SMALLER_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllAutoModIgnoresByChannelIdIsLessThanSomething() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get all the autoModIgnoreList where channelId is less than DEFAULT_CHANNEL_ID
        defaultAutoModIgnoreShouldNotBeFound("channelId.lessThan=" + DEFAULT_CHANNEL_ID);

        // Get all the autoModIgnoreList where channelId is less than UPDATED_CHANNEL_ID
        defaultAutoModIgnoreShouldBeFound("channelId.lessThan=" + UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void getAllAutoModIgnoresByChannelIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        autoModIgnoreRepository.saveAndFlush(autoModIgnore);

        // Get all the autoModIgnoreList where channelId is greater than DEFAULT_CHANNEL_ID
        defaultAutoModIgnoreShouldNotBeFound("channelId.greaterThan=" + DEFAULT_CHANNEL_ID);

        // Get all the autoModIgnoreList where channelId is greater than SMALLER_CHANNEL_ID
        defaultAutoModIgnoreShouldBeFound("channelId.greaterThan=" + SMALLER_CHANNEL_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAutoModIgnoreShouldBeFound(String filter) throws Exception {
        restAutoModIgnoreMockMvc.perform(get("/api/auto-mod-ignores?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autoModIgnore.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleId").value(hasItem(DEFAULT_ROLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].channelId").value(hasItem(DEFAULT_CHANNEL_ID.intValue())));

        // Check, that the count call also returns 1
        restAutoModIgnoreMockMvc.perform(get("/api/auto-mod-ignores/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAutoModIgnoreShouldNotBeFound(String filter) throws Exception {
        restAutoModIgnoreMockMvc.perform(get("/api/auto-mod-ignores?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAutoModIgnoreMockMvc.perform(get("/api/auto-mod-ignores/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAutoModIgnore() throws Exception {
        // Get the autoModIgnore
        restAutoModIgnoreMockMvc.perform(get("/api/auto-mod-ignores/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAutoModIgnore() throws Exception {
        // Initialize the database
        autoModIgnoreService.save(autoModIgnore);

        int databaseSizeBeforeUpdate = autoModIgnoreRepository.findAll().size();

        // Update the autoModIgnore
        AutoModIgnore updatedAutoModIgnore = autoModIgnoreRepository.findById(autoModIgnore.getId()).get();
        // Disconnect from session so that the updates on updatedAutoModIgnore are not directly saved in db
        em.detach(updatedAutoModIgnore);
        updatedAutoModIgnore
            .roleId(UPDATED_ROLE_ID)
            .channelId(UPDATED_CHANNEL_ID);

        restAutoModIgnoreMockMvc.perform(put("/api/auto-mod-ignores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAutoModIgnore)))
            .andExpect(status().isOk());

        // Validate the AutoModIgnore in the database
        List<AutoModIgnore> autoModIgnoreList = autoModIgnoreRepository.findAll();
        assertThat(autoModIgnoreList).hasSize(databaseSizeBeforeUpdate);
        AutoModIgnore testAutoModIgnore = autoModIgnoreList.get(autoModIgnoreList.size() - 1);
        assertThat(testAutoModIgnore.getRoleId()).isEqualTo(UPDATED_ROLE_ID);
        assertThat(testAutoModIgnore.getChannelId()).isEqualTo(UPDATED_CHANNEL_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingAutoModIgnore() throws Exception {
        int databaseSizeBeforeUpdate = autoModIgnoreRepository.findAll().size();

        // Create the AutoModIgnore

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutoModIgnoreMockMvc.perform(put("/api/auto-mod-ignores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autoModIgnore)))
            .andExpect(status().isBadRequest());

        // Validate the AutoModIgnore in the database
        List<AutoModIgnore> autoModIgnoreList = autoModIgnoreRepository.findAll();
        assertThat(autoModIgnoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAutoModIgnore() throws Exception {
        // Initialize the database
        autoModIgnoreService.save(autoModIgnore);

        int databaseSizeBeforeDelete = autoModIgnoreRepository.findAll().size();

        // Delete the autoModIgnore
        restAutoModIgnoreMockMvc.perform(delete("/api/auto-mod-ignores/{id}", autoModIgnore.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AutoModIgnore> autoModIgnoreList = autoModIgnoreRepository.findAll();
        assertThat(autoModIgnoreList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutoModIgnore.class);
        AutoModIgnore autoModIgnore1 = new AutoModIgnore();
        autoModIgnore1.setId(1L);
        AutoModIgnore autoModIgnore2 = new AutoModIgnore();
        autoModIgnore2.setId(autoModIgnore1.getId());
        assertThat(autoModIgnore1).isEqualTo(autoModIgnore2);
        autoModIgnore2.setId(2L);
        assertThat(autoModIgnore1).isNotEqualTo(autoModIgnore2);
        autoModIgnore1.setId(null);
        assertThat(autoModIgnore1).isNotEqualTo(autoModIgnore2);
    }
}
