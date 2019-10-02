package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.GuildApplicationForm;
import com.triippztech.guildo.repository.GuildApplicationFormRepository;
import com.triippztech.guildo.service.GuildApplicationFormService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.dto.GuildApplicationFormCriteria;
import com.triippztech.guildo.service.GuildApplicationFormQueryService;

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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.triippztech.guildo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link GuildApplicationFormResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class GuildApplicationFormResourceIT {

    private static final byte[] DEFAULT_APPLICATION_FORM = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_APPLICATION_FORM = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_APPLICATION_FORM_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_APPLICATION_FORM_CONTENT_TYPE = "image/png";

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;
    private static final Long SMALLER_GUILD_ID = 1L - 1L;

    @Autowired
    private GuildApplicationFormRepository guildApplicationFormRepository;

    @Autowired
    private GuildApplicationFormService guildApplicationFormService;

    @Autowired
    private GuildApplicationFormQueryService guildApplicationFormQueryService;

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

    private MockMvc restGuildApplicationFormMockMvc;

    private GuildApplicationForm guildApplicationForm;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GuildApplicationFormResource guildApplicationFormResource = new GuildApplicationFormResource(guildApplicationFormService, guildApplicationFormQueryService);
        this.restGuildApplicationFormMockMvc = MockMvcBuilders.standaloneSetup(guildApplicationFormResource)
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
    public static GuildApplicationForm createEntity(EntityManager em) {
        GuildApplicationForm guildApplicationForm = new GuildApplicationForm()
            .applicationForm(DEFAULT_APPLICATION_FORM)
            .applicationFormContentType(DEFAULT_APPLICATION_FORM_CONTENT_TYPE)
            .guildId(DEFAULT_GUILD_ID);
        return guildApplicationForm;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GuildApplicationForm createUpdatedEntity(EntityManager em) {
        GuildApplicationForm guildApplicationForm = new GuildApplicationForm()
            .applicationForm(UPDATED_APPLICATION_FORM)
            .applicationFormContentType(UPDATED_APPLICATION_FORM_CONTENT_TYPE)
            .guildId(UPDATED_GUILD_ID);
        return guildApplicationForm;
    }

    @BeforeEach
    public void initTest() {
        guildApplicationForm = createEntity(em);
    }

    @Test
    @Transactional
    public void createGuildApplicationForm() throws Exception {
        int databaseSizeBeforeCreate = guildApplicationFormRepository.findAll().size();

        // Create the GuildApplicationForm
        restGuildApplicationFormMockMvc.perform(post("/api/guild-application-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildApplicationForm)))
            .andExpect(status().isCreated());

        // Validate the GuildApplicationForm in the database
        List<GuildApplicationForm> guildApplicationFormList = guildApplicationFormRepository.findAll();
        assertThat(guildApplicationFormList).hasSize(databaseSizeBeforeCreate + 1);
        GuildApplicationForm testGuildApplicationForm = guildApplicationFormList.get(guildApplicationFormList.size() - 1);
        assertThat(testGuildApplicationForm.getApplicationForm()).isEqualTo(DEFAULT_APPLICATION_FORM);
        assertThat(testGuildApplicationForm.getApplicationFormContentType()).isEqualTo(DEFAULT_APPLICATION_FORM_CONTENT_TYPE);
        assertThat(testGuildApplicationForm.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
    }

    @Test
    @Transactional
    public void createGuildApplicationFormWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = guildApplicationFormRepository.findAll().size();

        // Create the GuildApplicationForm with an existing ID
        guildApplicationForm.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuildApplicationFormMockMvc.perform(post("/api/guild-application-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildApplicationForm)))
            .andExpect(status().isBadRequest());

        // Validate the GuildApplicationForm in the database
        List<GuildApplicationForm> guildApplicationFormList = guildApplicationFormRepository.findAll();
        assertThat(guildApplicationFormList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkGuildIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildApplicationFormRepository.findAll().size();
        // set the field null
        guildApplicationForm.setGuildId(null);

        // Create the GuildApplicationForm, which fails.

        restGuildApplicationFormMockMvc.perform(post("/api/guild-application-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildApplicationForm)))
            .andExpect(status().isBadRequest());

        List<GuildApplicationForm> guildApplicationFormList = guildApplicationFormRepository.findAll();
        assertThat(guildApplicationFormList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGuildApplicationForms() throws Exception {
        // Initialize the database
        guildApplicationFormRepository.saveAndFlush(guildApplicationForm);

        // Get all the guildApplicationFormList
        restGuildApplicationFormMockMvc.perform(get("/api/guild-application-forms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildApplicationForm.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationFormContentType").value(hasItem(DEFAULT_APPLICATION_FORM_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].applicationForm").value(hasItem(Base64Utils.encodeToString(DEFAULT_APPLICATION_FORM))))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getGuildApplicationForm() throws Exception {
        // Initialize the database
        guildApplicationFormRepository.saveAndFlush(guildApplicationForm);

        // Get the guildApplicationForm
        restGuildApplicationFormMockMvc.perform(get("/api/guild-application-forms/{id}", guildApplicationForm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(guildApplicationForm.getId().intValue()))
            .andExpect(jsonPath("$.applicationFormContentType").value(DEFAULT_APPLICATION_FORM_CONTENT_TYPE))
            .andExpect(jsonPath("$.applicationForm").value(Base64Utils.encodeToString(DEFAULT_APPLICATION_FORM)))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllGuildApplicationFormsByGuildIdIsEqualToSomething() throws Exception {
        // Initialize the database
        guildApplicationFormRepository.saveAndFlush(guildApplicationForm);

        // Get all the guildApplicationFormList where guildId equals to DEFAULT_GUILD_ID
        defaultGuildApplicationFormShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);

        // Get all the guildApplicationFormList where guildId equals to UPDATED_GUILD_ID
        defaultGuildApplicationFormShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildApplicationFormsByGuildIdIsInShouldWork() throws Exception {
        // Initialize the database
        guildApplicationFormRepository.saveAndFlush(guildApplicationForm);

        // Get all the guildApplicationFormList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
        defaultGuildApplicationFormShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);

        // Get all the guildApplicationFormList where guildId equals to UPDATED_GUILD_ID
        defaultGuildApplicationFormShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildApplicationFormsByGuildIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildApplicationFormRepository.saveAndFlush(guildApplicationForm);

        // Get all the guildApplicationFormList where guildId is not null
        defaultGuildApplicationFormShouldBeFound("guildId.specified=true");

        // Get all the guildApplicationFormList where guildId is null
        defaultGuildApplicationFormShouldNotBeFound("guildId.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildApplicationFormsByGuildIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildApplicationFormRepository.saveAndFlush(guildApplicationForm);

        // Get all the guildApplicationFormList where guildId is greater than or equal to DEFAULT_GUILD_ID
        defaultGuildApplicationFormShouldBeFound("guildId.greaterThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the guildApplicationFormList where guildId is greater than or equal to UPDATED_GUILD_ID
        defaultGuildApplicationFormShouldNotBeFound("guildId.greaterThanOrEqual=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildApplicationFormsByGuildIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildApplicationFormRepository.saveAndFlush(guildApplicationForm);

        // Get all the guildApplicationFormList where guildId is less than or equal to DEFAULT_GUILD_ID
        defaultGuildApplicationFormShouldBeFound("guildId.lessThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the guildApplicationFormList where guildId is less than or equal to SMALLER_GUILD_ID
        defaultGuildApplicationFormShouldNotBeFound("guildId.lessThanOrEqual=" + SMALLER_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildApplicationFormsByGuildIdIsLessThanSomething() throws Exception {
        // Initialize the database
        guildApplicationFormRepository.saveAndFlush(guildApplicationForm);

        // Get all the guildApplicationFormList where guildId is less than DEFAULT_GUILD_ID
        defaultGuildApplicationFormShouldNotBeFound("guildId.lessThan=" + DEFAULT_GUILD_ID);

        // Get all the guildApplicationFormList where guildId is less than UPDATED_GUILD_ID
        defaultGuildApplicationFormShouldBeFound("guildId.lessThan=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildApplicationFormsByGuildIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        guildApplicationFormRepository.saveAndFlush(guildApplicationForm);

        // Get all the guildApplicationFormList where guildId is greater than DEFAULT_GUILD_ID
        defaultGuildApplicationFormShouldNotBeFound("guildId.greaterThan=" + DEFAULT_GUILD_ID);

        // Get all the guildApplicationFormList where guildId is greater than SMALLER_GUILD_ID
        defaultGuildApplicationFormShouldBeFound("guildId.greaterThan=" + SMALLER_GUILD_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGuildApplicationFormShouldBeFound(String filter) throws Exception {
        restGuildApplicationFormMockMvc.perform(get("/api/guild-application-forms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildApplicationForm.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationFormContentType").value(hasItem(DEFAULT_APPLICATION_FORM_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].applicationForm").value(hasItem(Base64Utils.encodeToString(DEFAULT_APPLICATION_FORM))))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));

        // Check, that the count call also returns 1
        restGuildApplicationFormMockMvc.perform(get("/api/guild-application-forms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGuildApplicationFormShouldNotBeFound(String filter) throws Exception {
        restGuildApplicationFormMockMvc.perform(get("/api/guild-application-forms?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGuildApplicationFormMockMvc.perform(get("/api/guild-application-forms/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGuildApplicationForm() throws Exception {
        // Get the guildApplicationForm
        restGuildApplicationFormMockMvc.perform(get("/api/guild-application-forms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGuildApplicationForm() throws Exception {
        // Initialize the database
        guildApplicationFormService.save(guildApplicationForm);

        int databaseSizeBeforeUpdate = guildApplicationFormRepository.findAll().size();

        // Update the guildApplicationForm
        GuildApplicationForm updatedGuildApplicationForm = guildApplicationFormRepository.findById(guildApplicationForm.getId()).get();
        // Disconnect from session so that the updates on updatedGuildApplicationForm are not directly saved in db
        em.detach(updatedGuildApplicationForm);
        updatedGuildApplicationForm
            .applicationForm(UPDATED_APPLICATION_FORM)
            .applicationFormContentType(UPDATED_APPLICATION_FORM_CONTENT_TYPE)
            .guildId(UPDATED_GUILD_ID);

        restGuildApplicationFormMockMvc.perform(put("/api/guild-application-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGuildApplicationForm)))
            .andExpect(status().isOk());

        // Validate the GuildApplicationForm in the database
        List<GuildApplicationForm> guildApplicationFormList = guildApplicationFormRepository.findAll();
        assertThat(guildApplicationFormList).hasSize(databaseSizeBeforeUpdate);
        GuildApplicationForm testGuildApplicationForm = guildApplicationFormList.get(guildApplicationFormList.size() - 1);
        assertThat(testGuildApplicationForm.getApplicationForm()).isEqualTo(UPDATED_APPLICATION_FORM);
        assertThat(testGuildApplicationForm.getApplicationFormContentType()).isEqualTo(UPDATED_APPLICATION_FORM_CONTENT_TYPE);
        assertThat(testGuildApplicationForm.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingGuildApplicationForm() throws Exception {
        int databaseSizeBeforeUpdate = guildApplicationFormRepository.findAll().size();

        // Create the GuildApplicationForm

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuildApplicationFormMockMvc.perform(put("/api/guild-application-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildApplicationForm)))
            .andExpect(status().isBadRequest());

        // Validate the GuildApplicationForm in the database
        List<GuildApplicationForm> guildApplicationFormList = guildApplicationFormRepository.findAll();
        assertThat(guildApplicationFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGuildApplicationForm() throws Exception {
        // Initialize the database
        guildApplicationFormService.save(guildApplicationForm);

        int databaseSizeBeforeDelete = guildApplicationFormRepository.findAll().size();

        // Delete the guildApplicationForm
        restGuildApplicationFormMockMvc.perform(delete("/api/guild-application-forms/{id}", guildApplicationForm.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GuildApplicationForm> guildApplicationFormList = guildApplicationFormRepository.findAll();
        assertThat(guildApplicationFormList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuildApplicationForm.class);
        GuildApplicationForm guildApplicationForm1 = new GuildApplicationForm();
        guildApplicationForm1.setId(1L);
        GuildApplicationForm guildApplicationForm2 = new GuildApplicationForm();
        guildApplicationForm2.setId(guildApplicationForm1.getId());
        assertThat(guildApplicationForm1).isEqualTo(guildApplicationForm2);
        guildApplicationForm2.setId(2L);
        assertThat(guildApplicationForm1).isNotEqualTo(guildApplicationForm2);
        guildApplicationForm1.setId(null);
        assertThat(guildApplicationForm1).isNotEqualTo(guildApplicationForm2);
    }
}
