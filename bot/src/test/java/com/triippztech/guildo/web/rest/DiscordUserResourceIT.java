package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.DiscordUserProfile;
import com.triippztech.guildo.domain.TempBan;
import com.triippztech.guildo.domain.Mute;
import com.triippztech.guildo.domain.GuildApplication;
import com.triippztech.guildo.repository.DiscordUserRepository;
import com.triippztech.guildo.service.DiscordUserService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.dto.DiscordUserCriteria;
import com.triippztech.guildo.service.DiscordUserQueryService;

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

import com.triippztech.guildo.domain.enumeration.DiscordUserLevel;
/**
 * Integration tests for the {@link DiscordUserResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class DiscordUserResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final Integer DEFAULT_COMMANDS_ISSUED = 1;
    private static final Integer UPDATED_COMMANDS_ISSUED = 2;
    private static final Integer SMALLER_COMMANDS_ISSUED = 1 - 1;

    private static final Boolean DEFAULT_BLACKLISTED = false;
    private static final Boolean UPDATED_BLACKLISTED = true;

    private static final DiscordUserLevel DEFAULT_USER_LEVEL = DiscordUserLevel.STANDARD;
    private static final DiscordUserLevel UPDATED_USER_LEVEL = DiscordUserLevel.SUPPORTER;

    @Autowired
    private DiscordUserRepository discordUserRepository;

    @Autowired
    private DiscordUserService discordUserService;

    @Autowired
    private DiscordUserQueryService discordUserQueryService;

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

    private MockMvc restDiscordUserMockMvc;

    private DiscordUser discordUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DiscordUserResource discordUserResource = new DiscordUserResource(discordUserService, discordUserQueryService);
        this.restDiscordUserMockMvc = MockMvcBuilders.standaloneSetup(discordUserResource)
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
    public static DiscordUser createEntity(EntityManager em) {
        DiscordUser discordUser = new DiscordUser()
            .userId(DEFAULT_USER_ID)
            .userName(DEFAULT_USER_NAME)
            .icon(DEFAULT_ICON)
            .commandsIssued(DEFAULT_COMMANDS_ISSUED)
            .blacklisted(DEFAULT_BLACKLISTED)
            .userLevel(DEFAULT_USER_LEVEL);
        return discordUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DiscordUser createUpdatedEntity(EntityManager em) {
        DiscordUser discordUser = new DiscordUser()
            .userId(UPDATED_USER_ID)
            .userName(UPDATED_USER_NAME)
            .icon(UPDATED_ICON)
            .commandsIssued(UPDATED_COMMANDS_ISSUED)
            .blacklisted(UPDATED_BLACKLISTED)
            .userLevel(UPDATED_USER_LEVEL);
        return discordUser;
    }

    @BeforeEach
    public void initTest() {
        discordUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiscordUser() throws Exception {
        int databaseSizeBeforeCreate = discordUserRepository.findAll().size();

        // Create the DiscordUser
        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isCreated());

        // Validate the DiscordUser in the database
        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeCreate + 1);
        DiscordUser testDiscordUser = discordUserList.get(discordUserList.size() - 1);
        assertThat(testDiscordUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testDiscordUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testDiscordUser.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testDiscordUser.getCommandsIssued()).isEqualTo(DEFAULT_COMMANDS_ISSUED);
        assertThat(testDiscordUser.isBlacklisted()).isEqualTo(DEFAULT_BLACKLISTED);
        assertThat(testDiscordUser.getUserLevel()).isEqualTo(DEFAULT_USER_LEVEL);
    }

    @Test
    @Transactional
    public void createDiscordUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = discordUserRepository.findAll().size();

        // Create the DiscordUser with an existing ID
        discordUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        // Validate the DiscordUser in the database
        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = discordUserRepository.findAll().size();
        // set the field null
        discordUser.setUserId(null);

        // Create the DiscordUser, which fails.

        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = discordUserRepository.findAll().size();
        // set the field null
        discordUser.setUserName(null);

        // Create the DiscordUser, which fails.

        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCommandsIssuedIsRequired() throws Exception {
        int databaseSizeBeforeTest = discordUserRepository.findAll().size();
        // set the field null
        discordUser.setCommandsIssued(null);

        // Create the DiscordUser, which fails.

        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBlacklistedIsRequired() throws Exception {
        int databaseSizeBeforeTest = discordUserRepository.findAll().size();
        // set the field null
        discordUser.setBlacklisted(null);

        // Create the DiscordUser, which fails.

        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = discordUserRepository.findAll().size();
        // set the field null
        discordUser.setUserLevel(null);

        // Create the DiscordUser, which fails.

        restDiscordUserMockMvc.perform(post("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiscordUsers() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList
        restDiscordUserMockMvc.perform(get("/api/discord-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discordUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON.toString())))
            .andExpect(jsonPath("$.[*].commandsIssued").value(hasItem(DEFAULT_COMMANDS_ISSUED)))
            .andExpect(jsonPath("$.[*].blacklisted").value(hasItem(DEFAULT_BLACKLISTED.booleanValue())))
            .andExpect(jsonPath("$.[*].userLevel").value(hasItem(DEFAULT_USER_LEVEL.toString())));
    }
    
    @Test
    @Transactional
    public void getDiscordUser() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get the discordUser
        restDiscordUserMockMvc.perform(get("/api/discord-users/{id}", discordUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(discordUser.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON.toString()))
            .andExpect(jsonPath("$.commandsIssued").value(DEFAULT_COMMANDS_ISSUED))
            .andExpect(jsonPath("$.blacklisted").value(DEFAULT_BLACKLISTED.booleanValue()))
            .andExpect(jsonPath("$.userLevel").value(DEFAULT_USER_LEVEL.toString()));
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userId equals to DEFAULT_USER_ID
        defaultDiscordUserShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the discordUserList where userId equals to UPDATED_USER_ID
        defaultDiscordUserShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultDiscordUserShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the discordUserList where userId equals to UPDATED_USER_ID
        defaultDiscordUserShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userId is not null
        defaultDiscordUserShouldBeFound("userId.specified=true");

        // Get all the discordUserList where userId is null
        defaultDiscordUserShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userId is greater than or equal to DEFAULT_USER_ID
        defaultDiscordUserShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the discordUserList where userId is greater than or equal to UPDATED_USER_ID
        defaultDiscordUserShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userId is less than or equal to DEFAULT_USER_ID
        defaultDiscordUserShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the discordUserList where userId is less than or equal to SMALLER_USER_ID
        defaultDiscordUserShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userId is less than DEFAULT_USER_ID
        defaultDiscordUserShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the discordUserList where userId is less than UPDATED_USER_ID
        defaultDiscordUserShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userId is greater than DEFAULT_USER_ID
        defaultDiscordUserShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the discordUserList where userId is greater than SMALLER_USER_ID
        defaultDiscordUserShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }


    @Test
    @Transactional
    public void getAllDiscordUsersByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userName equals to DEFAULT_USER_NAME
        defaultDiscordUserShouldBeFound("userName.equals=" + DEFAULT_USER_NAME);

        // Get all the discordUserList where userName equals to UPDATED_USER_NAME
        defaultDiscordUserShouldNotBeFound("userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userName in DEFAULT_USER_NAME or UPDATED_USER_NAME
        defaultDiscordUserShouldBeFound("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME);

        // Get all the discordUserList where userName equals to UPDATED_USER_NAME
        defaultDiscordUserShouldNotBeFound("userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userName is not null
        defaultDiscordUserShouldBeFound("userName.specified=true");

        // Get all the discordUserList where userName is null
        defaultDiscordUserShouldNotBeFound("userName.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByIconIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where icon equals to DEFAULT_ICON
        defaultDiscordUserShouldBeFound("icon.equals=" + DEFAULT_ICON);

        // Get all the discordUserList where icon equals to UPDATED_ICON
        defaultDiscordUserShouldNotBeFound("icon.equals=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByIconIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where icon in DEFAULT_ICON or UPDATED_ICON
        defaultDiscordUserShouldBeFound("icon.in=" + DEFAULT_ICON + "," + UPDATED_ICON);

        // Get all the discordUserList where icon equals to UPDATED_ICON
        defaultDiscordUserShouldNotBeFound("icon.in=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByIconIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where icon is not null
        defaultDiscordUserShouldBeFound("icon.specified=true");

        // Get all the discordUserList where icon is null
        defaultDiscordUserShouldNotBeFound("icon.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByCommandsIssuedIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where commandsIssued equals to DEFAULT_COMMANDS_ISSUED
        defaultDiscordUserShouldBeFound("commandsIssued.equals=" + DEFAULT_COMMANDS_ISSUED);

        // Get all the discordUserList where commandsIssued equals to UPDATED_COMMANDS_ISSUED
        defaultDiscordUserShouldNotBeFound("commandsIssued.equals=" + UPDATED_COMMANDS_ISSUED);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByCommandsIssuedIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where commandsIssued in DEFAULT_COMMANDS_ISSUED or UPDATED_COMMANDS_ISSUED
        defaultDiscordUserShouldBeFound("commandsIssued.in=" + DEFAULT_COMMANDS_ISSUED + "," + UPDATED_COMMANDS_ISSUED);

        // Get all the discordUserList where commandsIssued equals to UPDATED_COMMANDS_ISSUED
        defaultDiscordUserShouldNotBeFound("commandsIssued.in=" + UPDATED_COMMANDS_ISSUED);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByCommandsIssuedIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where commandsIssued is not null
        defaultDiscordUserShouldBeFound("commandsIssued.specified=true");

        // Get all the discordUserList where commandsIssued is null
        defaultDiscordUserShouldNotBeFound("commandsIssued.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByCommandsIssuedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where commandsIssued is greater than or equal to DEFAULT_COMMANDS_ISSUED
        defaultDiscordUserShouldBeFound("commandsIssued.greaterThanOrEqual=" + DEFAULT_COMMANDS_ISSUED);

        // Get all the discordUserList where commandsIssued is greater than or equal to UPDATED_COMMANDS_ISSUED
        defaultDiscordUserShouldNotBeFound("commandsIssued.greaterThanOrEqual=" + UPDATED_COMMANDS_ISSUED);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByCommandsIssuedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where commandsIssued is less than or equal to DEFAULT_COMMANDS_ISSUED
        defaultDiscordUserShouldBeFound("commandsIssued.lessThanOrEqual=" + DEFAULT_COMMANDS_ISSUED);

        // Get all the discordUserList where commandsIssued is less than or equal to SMALLER_COMMANDS_ISSUED
        defaultDiscordUserShouldNotBeFound("commandsIssued.lessThanOrEqual=" + SMALLER_COMMANDS_ISSUED);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByCommandsIssuedIsLessThanSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where commandsIssued is less than DEFAULT_COMMANDS_ISSUED
        defaultDiscordUserShouldNotBeFound("commandsIssued.lessThan=" + DEFAULT_COMMANDS_ISSUED);

        // Get all the discordUserList where commandsIssued is less than UPDATED_COMMANDS_ISSUED
        defaultDiscordUserShouldBeFound("commandsIssued.lessThan=" + UPDATED_COMMANDS_ISSUED);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByCommandsIssuedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where commandsIssued is greater than DEFAULT_COMMANDS_ISSUED
        defaultDiscordUserShouldNotBeFound("commandsIssued.greaterThan=" + DEFAULT_COMMANDS_ISSUED);

        // Get all the discordUserList where commandsIssued is greater than SMALLER_COMMANDS_ISSUED
        defaultDiscordUserShouldBeFound("commandsIssued.greaterThan=" + SMALLER_COMMANDS_ISSUED);
    }


    @Test
    @Transactional
    public void getAllDiscordUsersByBlacklistedIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where blacklisted equals to DEFAULT_BLACKLISTED
        defaultDiscordUserShouldBeFound("blacklisted.equals=" + DEFAULT_BLACKLISTED);

        // Get all the discordUserList where blacklisted equals to UPDATED_BLACKLISTED
        defaultDiscordUserShouldNotBeFound("blacklisted.equals=" + UPDATED_BLACKLISTED);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByBlacklistedIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where blacklisted in DEFAULT_BLACKLISTED or UPDATED_BLACKLISTED
        defaultDiscordUserShouldBeFound("blacklisted.in=" + DEFAULT_BLACKLISTED + "," + UPDATED_BLACKLISTED);

        // Get all the discordUserList where blacklisted equals to UPDATED_BLACKLISTED
        defaultDiscordUserShouldNotBeFound("blacklisted.in=" + UPDATED_BLACKLISTED);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByBlacklistedIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where blacklisted is not null
        defaultDiscordUserShouldBeFound("blacklisted.specified=true");

        // Get all the discordUserList where blacklisted is null
        defaultDiscordUserShouldNotBeFound("blacklisted.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userLevel equals to DEFAULT_USER_LEVEL
        defaultDiscordUserShouldBeFound("userLevel.equals=" + DEFAULT_USER_LEVEL);

        // Get all the discordUserList where userLevel equals to UPDATED_USER_LEVEL
        defaultDiscordUserShouldNotBeFound("userLevel.equals=" + UPDATED_USER_LEVEL);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserLevelIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userLevel in DEFAULT_USER_LEVEL or UPDATED_USER_LEVEL
        defaultDiscordUserShouldBeFound("userLevel.in=" + DEFAULT_USER_LEVEL + "," + UPDATED_USER_LEVEL);

        // Get all the discordUserList where userLevel equals to UPDATED_USER_LEVEL
        defaultDiscordUserShouldNotBeFound("userLevel.in=" + UPDATED_USER_LEVEL);
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);

        // Get all the discordUserList where userLevel is not null
        defaultDiscordUserShouldBeFound("userLevel.specified=true");

        // Get all the discordUserList where userLevel is null
        defaultDiscordUserShouldNotBeFound("userLevel.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUsersByUserProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);
        DiscordUserProfile userProfile = DiscordUserProfileResourceIT.createEntity(em);
        em.persist(userProfile);
        em.flush();
        discordUser.setUserProfile(userProfile);
        discordUserRepository.saveAndFlush(discordUser);
        Long userProfileId = userProfile.getId();

        // Get all the discordUserList where userProfile equals to userProfileId
        defaultDiscordUserShouldBeFound("userProfileId.equals=" + userProfileId);

        // Get all the discordUserList where userProfile equals to userProfileId + 1
        defaultDiscordUserShouldNotBeFound("userProfileId.equals=" + (userProfileId + 1));
    }


    @Test
    @Transactional
    public void getAllDiscordUsersByUserTempBansIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);
        TempBan userTempBans = TempBanResourceIT.createEntity(em);
        em.persist(userTempBans);
        em.flush();
        discordUser.addUserTempBans(userTempBans);
        discordUserRepository.saveAndFlush(discordUser);
        Long userTempBansId = userTempBans.getId();

        // Get all the discordUserList where userTempBans equals to userTempBansId
        defaultDiscordUserShouldBeFound("userTempBansId.equals=" + userTempBansId);

        // Get all the discordUserList where userTempBans equals to userTempBansId + 1
        defaultDiscordUserShouldNotBeFound("userTempBansId.equals=" + (userTempBansId + 1));
    }


    @Test
    @Transactional
    public void getAllDiscordUsersByUserMutesIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);
        Mute userMutes = MuteResourceIT.createEntity(em);
        em.persist(userMutes);
        em.flush();
        discordUser.addUserMutes(userMutes);
        discordUserRepository.saveAndFlush(discordUser);
        Long userMutesId = userMutes.getId();

        // Get all the discordUserList where userMutes equals to userMutesId
        defaultDiscordUserShouldBeFound("userMutesId.equals=" + userMutesId);

        // Get all the discordUserList where userMutes equals to userMutesId + 1
        defaultDiscordUserShouldNotBeFound("userMutesId.equals=" + (userMutesId + 1));
    }


    @Test
    @Transactional
    public void getAllDiscordUsersByUserApplicationsIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserRepository.saveAndFlush(discordUser);
        GuildApplication userApplications = GuildApplicationResourceIT.createEntity(em);
        em.persist(userApplications);
        em.flush();
        discordUser.addUserApplications(userApplications);
        discordUserRepository.saveAndFlush(discordUser);
        Long userApplicationsId = userApplications.getId();

        // Get all the discordUserList where userApplications equals to userApplicationsId
        defaultDiscordUserShouldBeFound("userApplicationsId.equals=" + userApplicationsId);

        // Get all the discordUserList where userApplications equals to userApplicationsId + 1
        defaultDiscordUserShouldNotBeFound("userApplicationsId.equals=" + (userApplicationsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDiscordUserShouldBeFound(String filter) throws Exception {
        restDiscordUserMockMvc.perform(get("/api/discord-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discordUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].commandsIssued").value(hasItem(DEFAULT_COMMANDS_ISSUED)))
            .andExpect(jsonPath("$.[*].blacklisted").value(hasItem(DEFAULT_BLACKLISTED.booleanValue())))
            .andExpect(jsonPath("$.[*].userLevel").value(hasItem(DEFAULT_USER_LEVEL.toString())));

        // Check, that the count call also returns 1
        restDiscordUserMockMvc.perform(get("/api/discord-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDiscordUserShouldNotBeFound(String filter) throws Exception {
        restDiscordUserMockMvc.perform(get("/api/discord-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDiscordUserMockMvc.perform(get("/api/discord-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDiscordUser() throws Exception {
        // Get the discordUser
        restDiscordUserMockMvc.perform(get("/api/discord-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscordUser() throws Exception {
        // Initialize the database
        discordUserService.save(discordUser);

        int databaseSizeBeforeUpdate = discordUserRepository.findAll().size();

        // Update the discordUser
        DiscordUser updatedDiscordUser = discordUserRepository.findById(discordUser.getId()).get();
        // Disconnect from session so that the updates on updatedDiscordUser are not directly saved in db
        em.detach(updatedDiscordUser);
        updatedDiscordUser
            .userId(UPDATED_USER_ID)
            .userName(UPDATED_USER_NAME)
            .icon(UPDATED_ICON)
            .commandsIssued(UPDATED_COMMANDS_ISSUED)
            .blacklisted(UPDATED_BLACKLISTED)
            .userLevel(UPDATED_USER_LEVEL);

        restDiscordUserMockMvc.perform(put("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDiscordUser)))
            .andExpect(status().isOk());

        // Validate the DiscordUser in the database
        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeUpdate);
        DiscordUser testDiscordUser = discordUserList.get(discordUserList.size() - 1);
        assertThat(testDiscordUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testDiscordUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testDiscordUser.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testDiscordUser.getCommandsIssued()).isEqualTo(UPDATED_COMMANDS_ISSUED);
        assertThat(testDiscordUser.isBlacklisted()).isEqualTo(UPDATED_BLACKLISTED);
        assertThat(testDiscordUser.getUserLevel()).isEqualTo(UPDATED_USER_LEVEL);
    }

    @Test
    @Transactional
    public void updateNonExistingDiscordUser() throws Exception {
        int databaseSizeBeforeUpdate = discordUserRepository.findAll().size();

        // Create the DiscordUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscordUserMockMvc.perform(put("/api/discord-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUser)))
            .andExpect(status().isBadRequest());

        // Validate the DiscordUser in the database
        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDiscordUser() throws Exception {
        // Initialize the database
        discordUserService.save(discordUser);

        int databaseSizeBeforeDelete = discordUserRepository.findAll().size();

        // Delete the discordUser
        restDiscordUserMockMvc.perform(delete("/api/discord-users/{id}", discordUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DiscordUser> discordUserList = discordUserRepository.findAll();
        assertThat(discordUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiscordUser.class);
        DiscordUser discordUser1 = new DiscordUser();
        discordUser1.setId(1L);
        DiscordUser discordUser2 = new DiscordUser();
        discordUser2.setId(discordUser1.getId());
        assertThat(discordUser1).isEqualTo(discordUser2);
        discordUser2.setId(2L);
        assertThat(discordUser1).isNotEqualTo(discordUser2);
        discordUser1.setId(null);
        assertThat(discordUser1).isNotEqualTo(discordUser2);
    }
}
