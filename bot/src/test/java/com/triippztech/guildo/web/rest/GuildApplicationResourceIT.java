package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.GuildApplication;
import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.repository.GuildApplicationRepository;
import com.triippztech.guildo.service.guild.GuildApplicationService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.guild.GuildApplicationQueryService;

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

import com.triippztech.guildo.domain.enumeration.ApplicationStatus;
/**
 * Integration tests for the {@link GuildApplicationResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class GuildApplicationResourceIT {

    private static final String DEFAULT_CHARACTER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHARACTER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CHARACTER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CHARACTER_TYPE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_APPLICATION_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_APPLICATION_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_APPLICATION_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_APPLICATION_FILE_CONTENT_TYPE = "image/png";

    private static final ApplicationStatus DEFAULT_STATUS = ApplicationStatus.ACCEPTED;
    private static final ApplicationStatus UPDATED_STATUS = ApplicationStatus.PENDING;

    @Autowired
    private GuildApplicationRepository guildApplicationRepository;

    @Autowired
    private GuildApplicationService guildApplicationService;

    @Autowired
    private GuildApplicationQueryService guildApplicationQueryService;

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

    private MockMvc restGuildApplicationMockMvc;

    private GuildApplication guildApplication;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GuildApplicationResource guildApplicationResource = new GuildApplicationResource(guildApplicationService, guildApplicationQueryService);
        this.restGuildApplicationMockMvc = MockMvcBuilders.standaloneSetup(guildApplicationResource)
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
    public static GuildApplication createEntity(EntityManager em) {
        GuildApplication guildApplication = new GuildApplication()
            .characterName(DEFAULT_CHARACTER_NAME)
            .characterType(DEFAULT_CHARACTER_TYPE)
            .applicationFile(DEFAULT_APPLICATION_FILE)
            .applicationFileContentType(DEFAULT_APPLICATION_FILE_CONTENT_TYPE)
            .status(DEFAULT_STATUS);
        return guildApplication;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GuildApplication createUpdatedEntity(EntityManager em) {
        GuildApplication guildApplication = new GuildApplication()
            .characterName(UPDATED_CHARACTER_NAME)
            .characterType(UPDATED_CHARACTER_TYPE)
            .applicationFile(UPDATED_APPLICATION_FILE)
            .applicationFileContentType(UPDATED_APPLICATION_FILE_CONTENT_TYPE)
            .status(UPDATED_STATUS);
        return guildApplication;
    }

    @BeforeEach
    public void initTest() {
        guildApplication = createEntity(em);
    }

    @Test
    @Transactional
    public void createGuildApplication() throws Exception {
        int databaseSizeBeforeCreate = guildApplicationRepository.findAll().size();

        // Create the GuildApplication
        restGuildApplicationMockMvc.perform(post("/api/guild-applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildApplication)))
            .andExpect(status().isCreated());

        // Validate the GuildApplication in the database
        List<GuildApplication> guildApplicationList = guildApplicationRepository.findAll();
        assertThat(guildApplicationList).hasSize(databaseSizeBeforeCreate + 1);
        GuildApplication testGuildApplication = guildApplicationList.get(guildApplicationList.size() - 1);
        assertThat(testGuildApplication.getCharacterName()).isEqualTo(DEFAULT_CHARACTER_NAME);
        assertThat(testGuildApplication.getCharacterType()).isEqualTo(DEFAULT_CHARACTER_TYPE);
        assertThat(testGuildApplication.getApplicationFile()).isEqualTo(DEFAULT_APPLICATION_FILE);
        assertThat(testGuildApplication.getApplicationFileContentType()).isEqualTo(DEFAULT_APPLICATION_FILE_CONTENT_TYPE);
        assertThat(testGuildApplication.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createGuildApplicationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = guildApplicationRepository.findAll().size();

        // Create the GuildApplication with an existing ID
        guildApplication.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuildApplicationMockMvc.perform(post("/api/guild-applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildApplication)))
            .andExpect(status().isBadRequest());

        // Validate the GuildApplication in the database
        List<GuildApplication> guildApplicationList = guildApplicationRepository.findAll();
        assertThat(guildApplicationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCharacterNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildApplicationRepository.findAll().size();
        // set the field null
        guildApplication.setCharacterName(null);

        // Create the GuildApplication, which fails.

        restGuildApplicationMockMvc.perform(post("/api/guild-applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildApplication)))
            .andExpect(status().isBadRequest());

        List<GuildApplication> guildApplicationList = guildApplicationRepository.findAll();
        assertThat(guildApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCharacterTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildApplicationRepository.findAll().size();
        // set the field null
        guildApplication.setCharacterType(null);

        // Create the GuildApplication, which fails.

        restGuildApplicationMockMvc.perform(post("/api/guild-applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildApplication)))
            .andExpect(status().isBadRequest());

        List<GuildApplication> guildApplicationList = guildApplicationRepository.findAll();
        assertThat(guildApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildApplicationRepository.findAll().size();
        // set the field null
        guildApplication.setStatus(null);

        // Create the GuildApplication, which fails.

        restGuildApplicationMockMvc.perform(post("/api/guild-applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildApplication)))
            .andExpect(status().isBadRequest());

        List<GuildApplication> guildApplicationList = guildApplicationRepository.findAll();
        assertThat(guildApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGuildApplications() throws Exception {
        // Initialize the database
        guildApplicationRepository.saveAndFlush(guildApplication);

        // Get all the guildApplicationList
        restGuildApplicationMockMvc.perform(get("/api/guild-applications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].characterName").value(hasItem(DEFAULT_CHARACTER_NAME.toString())))
            .andExpect(jsonPath("$.[*].characterType").value(hasItem(DEFAULT_CHARACTER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].applicationFileContentType").value(hasItem(DEFAULT_APPLICATION_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].applicationFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_APPLICATION_FILE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getGuildApplication() throws Exception {
        // Initialize the database
        guildApplicationRepository.saveAndFlush(guildApplication);

        // Get the guildApplication
        restGuildApplicationMockMvc.perform(get("/api/guild-applications/{id}", guildApplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(guildApplication.getId().intValue()))
            .andExpect(jsonPath("$.characterName").value(DEFAULT_CHARACTER_NAME.toString()))
            .andExpect(jsonPath("$.characterType").value(DEFAULT_CHARACTER_TYPE.toString()))
            .andExpect(jsonPath("$.applicationFileContentType").value(DEFAULT_APPLICATION_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.applicationFile").value(Base64Utils.encodeToString(DEFAULT_APPLICATION_FILE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllGuildApplicationsByCharacterNameIsEqualToSomething() throws Exception {
        // Initialize the database
        guildApplicationRepository.saveAndFlush(guildApplication);

        // Get all the guildApplicationList where characterName equals to DEFAULT_CHARACTER_NAME
        defaultGuildApplicationShouldBeFound("characterName.equals=" + DEFAULT_CHARACTER_NAME);

        // Get all the guildApplicationList where characterName equals to UPDATED_CHARACTER_NAME
        defaultGuildApplicationShouldNotBeFound("characterName.equals=" + UPDATED_CHARACTER_NAME);
    }

    @Test
    @Transactional
    public void getAllGuildApplicationsByCharacterNameIsInShouldWork() throws Exception {
        // Initialize the database
        guildApplicationRepository.saveAndFlush(guildApplication);

        // Get all the guildApplicationList where characterName in DEFAULT_CHARACTER_NAME or UPDATED_CHARACTER_NAME
        defaultGuildApplicationShouldBeFound("characterName.in=" + DEFAULT_CHARACTER_NAME + "," + UPDATED_CHARACTER_NAME);

        // Get all the guildApplicationList where characterName equals to UPDATED_CHARACTER_NAME
        defaultGuildApplicationShouldNotBeFound("characterName.in=" + UPDATED_CHARACTER_NAME);
    }

    @Test
    @Transactional
    public void getAllGuildApplicationsByCharacterNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildApplicationRepository.saveAndFlush(guildApplication);

        // Get all the guildApplicationList where characterName is not null
        defaultGuildApplicationShouldBeFound("characterName.specified=true");

        // Get all the guildApplicationList where characterName is null
        defaultGuildApplicationShouldNotBeFound("characterName.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildApplicationsByCharacterTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        guildApplicationRepository.saveAndFlush(guildApplication);

        // Get all the guildApplicationList where characterType equals to DEFAULT_CHARACTER_TYPE
        defaultGuildApplicationShouldBeFound("characterType.equals=" + DEFAULT_CHARACTER_TYPE);

        // Get all the guildApplicationList where characterType equals to UPDATED_CHARACTER_TYPE
        defaultGuildApplicationShouldNotBeFound("characterType.equals=" + UPDATED_CHARACTER_TYPE);
    }

    @Test
    @Transactional
    public void getAllGuildApplicationsByCharacterTypeIsInShouldWork() throws Exception {
        // Initialize the database
        guildApplicationRepository.saveAndFlush(guildApplication);

        // Get all the guildApplicationList where characterType in DEFAULT_CHARACTER_TYPE or UPDATED_CHARACTER_TYPE
        defaultGuildApplicationShouldBeFound("characterType.in=" + DEFAULT_CHARACTER_TYPE + "," + UPDATED_CHARACTER_TYPE);

        // Get all the guildApplicationList where characterType equals to UPDATED_CHARACTER_TYPE
        defaultGuildApplicationShouldNotBeFound("characterType.in=" + UPDATED_CHARACTER_TYPE);
    }

    @Test
    @Transactional
    public void getAllGuildApplicationsByCharacterTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildApplicationRepository.saveAndFlush(guildApplication);

        // Get all the guildApplicationList where characterType is not null
        defaultGuildApplicationShouldBeFound("characterType.specified=true");

        // Get all the guildApplicationList where characterType is null
        defaultGuildApplicationShouldNotBeFound("characterType.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildApplicationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        guildApplicationRepository.saveAndFlush(guildApplication);

        // Get all the guildApplicationList where status equals to DEFAULT_STATUS
        defaultGuildApplicationShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the guildApplicationList where status equals to UPDATED_STATUS
        defaultGuildApplicationShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllGuildApplicationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        guildApplicationRepository.saveAndFlush(guildApplication);

        // Get all the guildApplicationList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultGuildApplicationShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the guildApplicationList where status equals to UPDATED_STATUS
        defaultGuildApplicationShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllGuildApplicationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildApplicationRepository.saveAndFlush(guildApplication);

        // Get all the guildApplicationList where status is not null
        defaultGuildApplicationShouldBeFound("status.specified=true");

        // Get all the guildApplicationList where status is null
        defaultGuildApplicationShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildApplicationsByAcceptedByIsEqualToSomething() throws Exception {
        // Initialize the database
        guildApplicationRepository.saveAndFlush(guildApplication);
        DiscordUser acceptedBy = DiscordUserResourceIT.createEntity(em);
        em.persist(acceptedBy);
        em.flush();
        guildApplication.setAcceptedBy(acceptedBy);
        guildApplicationRepository.saveAndFlush(guildApplication);
        Long acceptedById = acceptedBy.getId();

        // Get all the guildApplicationList where acceptedBy equals to acceptedById
        defaultGuildApplicationShouldBeFound("acceptedById.equals=" + acceptedById);

        // Get all the guildApplicationList where acceptedBy equals to acceptedById + 1
        defaultGuildApplicationShouldNotBeFound("acceptedById.equals=" + (acceptedById + 1));
    }


    @Test
    @Transactional
    public void getAllGuildApplicationsByAppliedUserIsEqualToSomething() throws Exception {
        // Initialize the database
        guildApplicationRepository.saveAndFlush(guildApplication);
        DiscordUser appliedUser = DiscordUserResourceIT.createEntity(em);
        em.persist(appliedUser);
        em.flush();
        guildApplication.setAppliedUser(appliedUser);
        guildApplicationRepository.saveAndFlush(guildApplication);
        Long appliedUserId = appliedUser.getId();

        // Get all the guildApplicationList where appliedUser equals to appliedUserId
        defaultGuildApplicationShouldBeFound("appliedUserId.equals=" + appliedUserId);

        // Get all the guildApplicationList where appliedUser equals to appliedUserId + 1
        defaultGuildApplicationShouldNotBeFound("appliedUserId.equals=" + (appliedUserId + 1));
    }


    @Test
    @Transactional
    public void getAllGuildApplicationsByGuildServerIsEqualToSomething() throws Exception {
        // Initialize the database
        guildApplicationRepository.saveAndFlush(guildApplication);
        GuildServer guildServer = GuildServerResourceIT.createEntity(em);
        em.persist(guildServer);
        em.flush();
        guildApplication.setGuildServer(guildServer);
        guildApplicationRepository.saveAndFlush(guildApplication);
        Long guildServerId = guildServer.getId();

        // Get all the guildApplicationList where guildServer equals to guildServerId
        defaultGuildApplicationShouldBeFound("guildServerId.equals=" + guildServerId);

        // Get all the guildApplicationList where guildServer equals to guildServerId + 1
        defaultGuildApplicationShouldNotBeFound("guildServerId.equals=" + (guildServerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGuildApplicationShouldBeFound(String filter) throws Exception {
        restGuildApplicationMockMvc.perform(get("/api/guild-applications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].characterName").value(hasItem(DEFAULT_CHARACTER_NAME)))
            .andExpect(jsonPath("$.[*].characterType").value(hasItem(DEFAULT_CHARACTER_TYPE)))
            .andExpect(jsonPath("$.[*].applicationFileContentType").value(hasItem(DEFAULT_APPLICATION_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].applicationFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_APPLICATION_FILE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restGuildApplicationMockMvc.perform(get("/api/guild-applications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGuildApplicationShouldNotBeFound(String filter) throws Exception {
        restGuildApplicationMockMvc.perform(get("/api/guild-applications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGuildApplicationMockMvc.perform(get("/api/guild-applications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGuildApplication() throws Exception {
        // Get the guildApplication
        restGuildApplicationMockMvc.perform(get("/api/guild-applications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGuildApplication() throws Exception {
        // Initialize the database
        guildApplicationService.save(guildApplication);

        int databaseSizeBeforeUpdate = guildApplicationRepository.findAll().size();

        // Update the guildApplication
        GuildApplication updatedGuildApplication = guildApplicationRepository.findById(guildApplication.getId()).get();
        // Disconnect from session so that the updates on updatedGuildApplication are not directly saved in db
        em.detach(updatedGuildApplication);
        updatedGuildApplication
            .characterName(UPDATED_CHARACTER_NAME)
            .characterType(UPDATED_CHARACTER_TYPE)
            .applicationFile(UPDATED_APPLICATION_FILE)
            .applicationFileContentType(UPDATED_APPLICATION_FILE_CONTENT_TYPE)
            .status(UPDATED_STATUS);

        restGuildApplicationMockMvc.perform(put("/api/guild-applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGuildApplication)))
            .andExpect(status().isOk());

        // Validate the GuildApplication in the database
        List<GuildApplication> guildApplicationList = guildApplicationRepository.findAll();
        assertThat(guildApplicationList).hasSize(databaseSizeBeforeUpdate);
        GuildApplication testGuildApplication = guildApplicationList.get(guildApplicationList.size() - 1);
        assertThat(testGuildApplication.getCharacterName()).isEqualTo(UPDATED_CHARACTER_NAME);
        assertThat(testGuildApplication.getCharacterType()).isEqualTo(UPDATED_CHARACTER_TYPE);
        assertThat(testGuildApplication.getApplicationFile()).isEqualTo(UPDATED_APPLICATION_FILE);
        assertThat(testGuildApplication.getApplicationFileContentType()).isEqualTo(UPDATED_APPLICATION_FILE_CONTENT_TYPE);
        assertThat(testGuildApplication.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingGuildApplication() throws Exception {
        int databaseSizeBeforeUpdate = guildApplicationRepository.findAll().size();

        // Create the GuildApplication

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuildApplicationMockMvc.perform(put("/api/guild-applications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildApplication)))
            .andExpect(status().isBadRequest());

        // Validate the GuildApplication in the database
        List<GuildApplication> guildApplicationList = guildApplicationRepository.findAll();
        assertThat(guildApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGuildApplication() throws Exception {
        // Initialize the database
        guildApplicationService.save(guildApplication);

        int databaseSizeBeforeDelete = guildApplicationRepository.findAll().size();

        // Delete the guildApplication
        restGuildApplicationMockMvc.perform(delete("/api/guild-applications/{id}", guildApplication.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GuildApplication> guildApplicationList = guildApplicationRepository.findAll();
        assertThat(guildApplicationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuildApplication.class);
        GuildApplication guildApplication1 = new GuildApplication();
        guildApplication1.setId(1L);
        GuildApplication guildApplication2 = new GuildApplication();
        guildApplication2.setId(guildApplication1.getId());
        assertThat(guildApplication1).isEqualTo(guildApplication2);
        guildApplication2.setId(2L);
        assertThat(guildApplication1).isNotEqualTo(guildApplication2);
        guildApplication1.setId(null);
        assertThat(guildApplication1).isNotEqualTo(guildApplication2);
    }
}
