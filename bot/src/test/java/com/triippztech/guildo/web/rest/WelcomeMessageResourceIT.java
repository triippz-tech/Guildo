package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.WelcomeMessage;
import com.triippztech.guildo.repository.WelcomeMessageRepository;
import com.triippztech.guildo.service.moderation.WelcomeMessageService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.moderation.WelcomeMessageQueryService;

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
 * Integration tests for the {@link WelcomeMessageResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class WelcomeMessageResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final String DEFAULT_FOOTER = "AAAAAAAAAA";
    private static final String UPDATED_FOOTER = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;
    private static final Long SMALLER_GUILD_ID = 1L - 1L;

    @Autowired
    private WelcomeMessageRepository welcomeMessageRepository;

    @Autowired
    private WelcomeMessageService welcomeMessageService;

    @Autowired
    private WelcomeMessageQueryService welcomeMessageQueryService;

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

    private MockMvc restWelcomeMessageMockMvc;

    private WelcomeMessage welcomeMessage;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WelcomeMessageResource welcomeMessageResource = new WelcomeMessageResource(welcomeMessageService, welcomeMessageQueryService);
        this.restWelcomeMessageMockMvc = MockMvcBuilders.standaloneSetup(welcomeMessageResource)
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
    public static WelcomeMessage createEntity(EntityManager em) {
        WelcomeMessage welcomeMessage = new WelcomeMessage()
            .name(DEFAULT_NAME)
            .messageTitle(DEFAULT_MESSAGE_TITLE)
            .body(DEFAULT_BODY)
            .footer(DEFAULT_FOOTER)
            .logoUrl(DEFAULT_LOGO_URL)
            .guildId(DEFAULT_GUILD_ID);
        return welcomeMessage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WelcomeMessage createUpdatedEntity(EntityManager em) {
        WelcomeMessage welcomeMessage = new WelcomeMessage()
            .name(UPDATED_NAME)
            .messageTitle(UPDATED_MESSAGE_TITLE)
            .body(UPDATED_BODY)
            .footer(UPDATED_FOOTER)
            .logoUrl(UPDATED_LOGO_URL)
            .guildId(UPDATED_GUILD_ID);
        return welcomeMessage;
    }

    @BeforeEach
    public void initTest() {
        welcomeMessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createWelcomeMessage() throws Exception {
        int databaseSizeBeforeCreate = welcomeMessageRepository.findAll().size();

        // Create the WelcomeMessage
        restWelcomeMessageMockMvc.perform(post("/api/welcome-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(welcomeMessage)))
            .andExpect(status().isCreated());

        // Validate the WelcomeMessage in the database
        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeCreate + 1);
        WelcomeMessage testWelcomeMessage = welcomeMessageList.get(welcomeMessageList.size() - 1);
        assertThat(testWelcomeMessage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWelcomeMessage.getMessageTitle()).isEqualTo(DEFAULT_MESSAGE_TITLE);
        assertThat(testWelcomeMessage.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testWelcomeMessage.getFooter()).isEqualTo(DEFAULT_FOOTER);
        assertThat(testWelcomeMessage.getLogoUrl()).isEqualTo(DEFAULT_LOGO_URL);
        assertThat(testWelcomeMessage.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
    }

    @Test
    @Transactional
    public void createWelcomeMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = welcomeMessageRepository.findAll().size();

        // Create the WelcomeMessage with an existing ID
        welcomeMessage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWelcomeMessageMockMvc.perform(post("/api/welcome-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(welcomeMessage)))
            .andExpect(status().isBadRequest());

        // Validate the WelcomeMessage in the database
        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = welcomeMessageRepository.findAll().size();
        // set the field null
        welcomeMessage.setName(null);

        // Create the WelcomeMessage, which fails.

        restWelcomeMessageMockMvc.perform(post("/api/welcome-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(welcomeMessage)))
            .andExpect(status().isBadRequest());

        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = welcomeMessageRepository.findAll().size();
        // set the field null
        welcomeMessage.setMessageTitle(null);

        // Create the WelcomeMessage, which fails.

        restWelcomeMessageMockMvc.perform(post("/api/welcome-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(welcomeMessage)))
            .andExpect(status().isBadRequest());

        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBodyIsRequired() throws Exception {
        int databaseSizeBeforeTest = welcomeMessageRepository.findAll().size();
        // set the field null
        welcomeMessage.setBody(null);

        // Create the WelcomeMessage, which fails.

        restWelcomeMessageMockMvc.perform(post("/api/welcome-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(welcomeMessage)))
            .andExpect(status().isBadRequest());

        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFooterIsRequired() throws Exception {
        int databaseSizeBeforeTest = welcomeMessageRepository.findAll().size();
        // set the field null
        welcomeMessage.setFooter(null);

        // Create the WelcomeMessage, which fails.

        restWelcomeMessageMockMvc.perform(post("/api/welcome-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(welcomeMessage)))
            .andExpect(status().isBadRequest());

        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGuildIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = welcomeMessageRepository.findAll().size();
        // set the field null
        welcomeMessage.setGuildId(null);

        // Create the WelcomeMessage, which fails.

        restWelcomeMessageMockMvc.perform(post("/api/welcome-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(welcomeMessage)))
            .andExpect(status().isBadRequest());

        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessages() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList
        restWelcomeMessageMockMvc.perform(get("/api/welcome-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(welcomeMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].messageTitle").value(hasItem(DEFAULT_MESSAGE_TITLE.toString())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
            .andExpect(jsonPath("$.[*].footer").value(hasItem(DEFAULT_FOOTER.toString())))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL.toString())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getWelcomeMessage() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get the welcomeMessage
        restWelcomeMessageMockMvc.perform(get("/api/welcome-messages/{id}", welcomeMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(welcomeMessage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.messageTitle").value(DEFAULT_MESSAGE_TITLE.toString()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()))
            .andExpect(jsonPath("$.footer").value(DEFAULT_FOOTER.toString()))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL.toString()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where name equals to DEFAULT_NAME
        defaultWelcomeMessageShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the welcomeMessageList where name equals to UPDATED_NAME
        defaultWelcomeMessageShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where name in DEFAULT_NAME or UPDATED_NAME
        defaultWelcomeMessageShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the welcomeMessageList where name equals to UPDATED_NAME
        defaultWelcomeMessageShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where name is not null
        defaultWelcomeMessageShouldBeFound("name.specified=true");

        // Get all the welcomeMessageList where name is null
        defaultWelcomeMessageShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByMessageTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where messageTitle equals to DEFAULT_MESSAGE_TITLE
        defaultWelcomeMessageShouldBeFound("messageTitle.equals=" + DEFAULT_MESSAGE_TITLE);

        // Get all the welcomeMessageList where messageTitle equals to UPDATED_MESSAGE_TITLE
        defaultWelcomeMessageShouldNotBeFound("messageTitle.equals=" + UPDATED_MESSAGE_TITLE);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByMessageTitleIsInShouldWork() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where messageTitle in DEFAULT_MESSAGE_TITLE or UPDATED_MESSAGE_TITLE
        defaultWelcomeMessageShouldBeFound("messageTitle.in=" + DEFAULT_MESSAGE_TITLE + "," + UPDATED_MESSAGE_TITLE);

        // Get all the welcomeMessageList where messageTitle equals to UPDATED_MESSAGE_TITLE
        defaultWelcomeMessageShouldNotBeFound("messageTitle.in=" + UPDATED_MESSAGE_TITLE);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByMessageTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where messageTitle is not null
        defaultWelcomeMessageShouldBeFound("messageTitle.specified=true");

        // Get all the welcomeMessageList where messageTitle is null
        defaultWelcomeMessageShouldNotBeFound("messageTitle.specified=false");
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByBodyIsEqualToSomething() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where body equals to DEFAULT_BODY
        defaultWelcomeMessageShouldBeFound("body.equals=" + DEFAULT_BODY);

        // Get all the welcomeMessageList where body equals to UPDATED_BODY
        defaultWelcomeMessageShouldNotBeFound("body.equals=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByBodyIsInShouldWork() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where body in DEFAULT_BODY or UPDATED_BODY
        defaultWelcomeMessageShouldBeFound("body.in=" + DEFAULT_BODY + "," + UPDATED_BODY);

        // Get all the welcomeMessageList where body equals to UPDATED_BODY
        defaultWelcomeMessageShouldNotBeFound("body.in=" + UPDATED_BODY);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByBodyIsNullOrNotNull() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where body is not null
        defaultWelcomeMessageShouldBeFound("body.specified=true");

        // Get all the welcomeMessageList where body is null
        defaultWelcomeMessageShouldNotBeFound("body.specified=false");
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByFooterIsEqualToSomething() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where footer equals to DEFAULT_FOOTER
        defaultWelcomeMessageShouldBeFound("footer.equals=" + DEFAULT_FOOTER);

        // Get all the welcomeMessageList where footer equals to UPDATED_FOOTER
        defaultWelcomeMessageShouldNotBeFound("footer.equals=" + UPDATED_FOOTER);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByFooterIsInShouldWork() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where footer in DEFAULT_FOOTER or UPDATED_FOOTER
        defaultWelcomeMessageShouldBeFound("footer.in=" + DEFAULT_FOOTER + "," + UPDATED_FOOTER);

        // Get all the welcomeMessageList where footer equals to UPDATED_FOOTER
        defaultWelcomeMessageShouldNotBeFound("footer.in=" + UPDATED_FOOTER);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByFooterIsNullOrNotNull() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where footer is not null
        defaultWelcomeMessageShouldBeFound("footer.specified=true");

        // Get all the welcomeMessageList where footer is null
        defaultWelcomeMessageShouldNotBeFound("footer.specified=false");
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByLogoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where logoUrl equals to DEFAULT_LOGO_URL
        defaultWelcomeMessageShouldBeFound("logoUrl.equals=" + DEFAULT_LOGO_URL);

        // Get all the welcomeMessageList where logoUrl equals to UPDATED_LOGO_URL
        defaultWelcomeMessageShouldNotBeFound("logoUrl.equals=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByLogoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where logoUrl in DEFAULT_LOGO_URL or UPDATED_LOGO_URL
        defaultWelcomeMessageShouldBeFound("logoUrl.in=" + DEFAULT_LOGO_URL + "," + UPDATED_LOGO_URL);

        // Get all the welcomeMessageList where logoUrl equals to UPDATED_LOGO_URL
        defaultWelcomeMessageShouldNotBeFound("logoUrl.in=" + UPDATED_LOGO_URL);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByLogoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where logoUrl is not null
        defaultWelcomeMessageShouldBeFound("logoUrl.specified=true");

        // Get all the welcomeMessageList where logoUrl is null
        defaultWelcomeMessageShouldNotBeFound("logoUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByGuildIdIsEqualToSomething() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where guildId equals to DEFAULT_GUILD_ID
        defaultWelcomeMessageShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);

        // Get all the welcomeMessageList where guildId equals to UPDATED_GUILD_ID
        defaultWelcomeMessageShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByGuildIdIsInShouldWork() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
        defaultWelcomeMessageShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);

        // Get all the welcomeMessageList where guildId equals to UPDATED_GUILD_ID
        defaultWelcomeMessageShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByGuildIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where guildId is not null
        defaultWelcomeMessageShouldBeFound("guildId.specified=true");

        // Get all the welcomeMessageList where guildId is null
        defaultWelcomeMessageShouldNotBeFound("guildId.specified=false");
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByGuildIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where guildId is greater than or equal to DEFAULT_GUILD_ID
        defaultWelcomeMessageShouldBeFound("guildId.greaterThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the welcomeMessageList where guildId is greater than or equal to UPDATED_GUILD_ID
        defaultWelcomeMessageShouldNotBeFound("guildId.greaterThanOrEqual=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByGuildIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where guildId is less than or equal to DEFAULT_GUILD_ID
        defaultWelcomeMessageShouldBeFound("guildId.lessThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the welcomeMessageList where guildId is less than or equal to SMALLER_GUILD_ID
        defaultWelcomeMessageShouldNotBeFound("guildId.lessThanOrEqual=" + SMALLER_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByGuildIdIsLessThanSomething() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where guildId is less than DEFAULT_GUILD_ID
        defaultWelcomeMessageShouldNotBeFound("guildId.lessThan=" + DEFAULT_GUILD_ID);

        // Get all the welcomeMessageList where guildId is less than UPDATED_GUILD_ID
        defaultWelcomeMessageShouldBeFound("guildId.lessThan=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllWelcomeMessagesByGuildIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        welcomeMessageRepository.saveAndFlush(welcomeMessage);

        // Get all the welcomeMessageList where guildId is greater than DEFAULT_GUILD_ID
        defaultWelcomeMessageShouldNotBeFound("guildId.greaterThan=" + DEFAULT_GUILD_ID);

        // Get all the welcomeMessageList where guildId is greater than SMALLER_GUILD_ID
        defaultWelcomeMessageShouldBeFound("guildId.greaterThan=" + SMALLER_GUILD_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWelcomeMessageShouldBeFound(String filter) throws Exception {
        restWelcomeMessageMockMvc.perform(get("/api/welcome-messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(welcomeMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].messageTitle").value(hasItem(DEFAULT_MESSAGE_TITLE)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].footer").value(hasItem(DEFAULT_FOOTER)))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));

        // Check, that the count call also returns 1
        restWelcomeMessageMockMvc.perform(get("/api/welcome-messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWelcomeMessageShouldNotBeFound(String filter) throws Exception {
        restWelcomeMessageMockMvc.perform(get("/api/welcome-messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWelcomeMessageMockMvc.perform(get("/api/welcome-messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingWelcomeMessage() throws Exception {
        // Get the welcomeMessage
        restWelcomeMessageMockMvc.perform(get("/api/welcome-messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWelcomeMessage() throws Exception {
        // Initialize the database
        welcomeMessageService.save(welcomeMessage);

        int databaseSizeBeforeUpdate = welcomeMessageRepository.findAll().size();

        // Update the welcomeMessage
        WelcomeMessage updatedWelcomeMessage = welcomeMessageRepository.findById(welcomeMessage.getId()).get();
        // Disconnect from session so that the updates on updatedWelcomeMessage are not directly saved in db
        em.detach(updatedWelcomeMessage);
        updatedWelcomeMessage
            .name(UPDATED_NAME)
            .messageTitle(UPDATED_MESSAGE_TITLE)
            .body(UPDATED_BODY)
            .footer(UPDATED_FOOTER)
            .logoUrl(UPDATED_LOGO_URL)
            .guildId(UPDATED_GUILD_ID);

        restWelcomeMessageMockMvc.perform(put("/api/welcome-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWelcomeMessage)))
            .andExpect(status().isOk());

        // Validate the WelcomeMessage in the database
        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeUpdate);
        WelcomeMessage testWelcomeMessage = welcomeMessageList.get(welcomeMessageList.size() - 1);
        assertThat(testWelcomeMessage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWelcomeMessage.getMessageTitle()).isEqualTo(UPDATED_MESSAGE_TITLE);
        assertThat(testWelcomeMessage.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testWelcomeMessage.getFooter()).isEqualTo(UPDATED_FOOTER);
        assertThat(testWelcomeMessage.getLogoUrl()).isEqualTo(UPDATED_LOGO_URL);
        assertThat(testWelcomeMessage.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingWelcomeMessage() throws Exception {
        int databaseSizeBeforeUpdate = welcomeMessageRepository.findAll().size();

        // Create the WelcomeMessage

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWelcomeMessageMockMvc.perform(put("/api/welcome-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(welcomeMessage)))
            .andExpect(status().isBadRequest());

        // Validate the WelcomeMessage in the database
        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWelcomeMessage() throws Exception {
        // Initialize the database
        welcomeMessageService.save(welcomeMessage);

        int databaseSizeBeforeDelete = welcomeMessageRepository.findAll().size();

        // Delete the welcomeMessage
        restWelcomeMessageMockMvc.perform(delete("/api/welcome-messages/{id}", welcomeMessage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WelcomeMessage> welcomeMessageList = welcomeMessageRepository.findAll();
        assertThat(welcomeMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WelcomeMessage.class);
        WelcomeMessage welcomeMessage1 = new WelcomeMessage();
        welcomeMessage1.setId(1L);
        WelcomeMessage welcomeMessage2 = new WelcomeMessage();
        welcomeMessage2.setId(welcomeMessage1.getId());
        assertThat(welcomeMessage1).isEqualTo(welcomeMessage2);
        welcomeMessage2.setId(2L);
        assertThat(welcomeMessage1).isNotEqualTo(welcomeMessage2);
        welcomeMessage1.setId(null);
        assertThat(welcomeMessage1).isNotEqualTo(welcomeMessage2);
    }
}
