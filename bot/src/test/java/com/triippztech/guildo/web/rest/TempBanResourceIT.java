package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.TempBan;
import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.repository.TempBanRepository;
import com.triippztech.guildo.service.moderation.TempBanService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.moderation.TempBanQueryService;

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

/**
 * Integration tests for the {@link TempBanResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class TempBanResourceIT {

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_END_TIME = Instant.ofEpochMilli(-1L);

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;
    private static final Long SMALLER_GUILD_ID = 1L - 1L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    @Autowired
    private TempBanRepository tempBanRepository;

    @Autowired
    private TempBanService tempBanService;

    @Autowired
    private TempBanQueryService tempBanQueryService;

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

    private MockMvc restTempBanMockMvc;

    private TempBan tempBan;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TempBanResource tempBanResource = new TempBanResource(tempBanService, tempBanQueryService);
        this.restTempBanMockMvc = MockMvcBuilders.standaloneSetup(tempBanResource)
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
    public static TempBan createEntity(EntityManager em) {
        TempBan tempBan = new TempBan()
            .reason(DEFAULT_REASON)
            .endTime(DEFAULT_END_TIME)
            .guildId(DEFAULT_GUILD_ID)
            .userId(DEFAULT_USER_ID);
        return tempBan;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TempBan createUpdatedEntity(EntityManager em) {
        TempBan tempBan = new TempBan()
            .reason(UPDATED_REASON)
            .endTime(UPDATED_END_TIME)
            .guildId(UPDATED_GUILD_ID)
            .userId(UPDATED_USER_ID);
        return tempBan;
    }

    @BeforeEach
    public void initTest() {
        tempBan = createEntity(em);
    }

    @Test
    @Transactional
    public void createTempBan() throws Exception {
        int databaseSizeBeforeCreate = tempBanRepository.findAll().size();

        // Create the TempBan
        restTempBanMockMvc.perform(post("/api/temp-bans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempBan)))
            .andExpect(status().isCreated());

        // Validate the TempBan in the database
        List<TempBan> tempBanList = tempBanRepository.findAll();
        assertThat(tempBanList).hasSize(databaseSizeBeforeCreate + 1);
        TempBan testTempBan = tempBanList.get(tempBanList.size() - 1);
        assertThat(testTempBan.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testTempBan.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testTempBan.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
        assertThat(testTempBan.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    public void createTempBanWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tempBanRepository.findAll().size();

        // Create the TempBan with an existing ID
        tempBan.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTempBanMockMvc.perform(post("/api/temp-bans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempBan)))
            .andExpect(status().isBadRequest());

        // Validate the TempBan in the database
        List<TempBan> tempBanList = tempBanRepository.findAll();
        assertThat(tempBanList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkReasonIsRequired() throws Exception {
        int databaseSizeBeforeTest = tempBanRepository.findAll().size();
        // set the field null
        tempBan.setReason(null);

        // Create the TempBan, which fails.

        restTempBanMockMvc.perform(post("/api/temp-bans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempBan)))
            .andExpect(status().isBadRequest());

        List<TempBan> tempBanList = tempBanRepository.findAll();
        assertThat(tempBanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tempBanRepository.findAll().size();
        // set the field null
        tempBan.setEndTime(null);

        // Create the TempBan, which fails.

        restTempBanMockMvc.perform(post("/api/temp-bans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempBan)))
            .andExpect(status().isBadRequest());

        List<TempBan> tempBanList = tempBanRepository.findAll();
        assertThat(tempBanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGuildIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = tempBanRepository.findAll().size();
        // set the field null
        tempBan.setGuildId(null);

        // Create the TempBan, which fails.

        restTempBanMockMvc.perform(post("/api/temp-bans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempBan)))
            .andExpect(status().isBadRequest());

        List<TempBan> tempBanList = tempBanRepository.findAll();
        assertThat(tempBanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = tempBanRepository.findAll().size();
        // set the field null
        tempBan.setUserId(null);

        // Create the TempBan, which fails.

        restTempBanMockMvc.perform(post("/api/temp-bans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempBan)))
            .andExpect(status().isBadRequest());

        List<TempBan> tempBanList = tempBanRepository.findAll();
        assertThat(tempBanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTempBans() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList
        restTempBanMockMvc.perform(get("/api/temp-bans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tempBan.getId().intValue())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getTempBan() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get the tempBan
        restTempBanMockMvc.perform(get("/api/temp-bans/{id}", tempBan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tempBan.getId().intValue()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllTempBansByReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where reason equals to DEFAULT_REASON
        defaultTempBanShouldBeFound("reason.equals=" + DEFAULT_REASON);

        // Get all the tempBanList where reason equals to UPDATED_REASON
        defaultTempBanShouldNotBeFound("reason.equals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    public void getAllTempBansByReasonIsInShouldWork() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where reason in DEFAULT_REASON or UPDATED_REASON
        defaultTempBanShouldBeFound("reason.in=" + DEFAULT_REASON + "," + UPDATED_REASON);

        // Get all the tempBanList where reason equals to UPDATED_REASON
        defaultTempBanShouldNotBeFound("reason.in=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    public void getAllTempBansByReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where reason is not null
        defaultTempBanShouldBeFound("reason.specified=true");

        // Get all the tempBanList where reason is null
        defaultTempBanShouldNotBeFound("reason.specified=false");
    }

    @Test
    @Transactional
    public void getAllTempBansByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where endTime equals to DEFAULT_END_TIME
        defaultTempBanShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the tempBanList where endTime equals to UPDATED_END_TIME
        defaultTempBanShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTempBansByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultTempBanShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the tempBanList where endTime equals to UPDATED_END_TIME
        defaultTempBanShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllTempBansByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where endTime is not null
        defaultTempBanShouldBeFound("endTime.specified=true");

        // Get all the tempBanList where endTime is null
        defaultTempBanShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllTempBansByGuildIdIsEqualToSomething() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where guildId equals to DEFAULT_GUILD_ID
        defaultTempBanShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);

        // Get all the tempBanList where guildId equals to UPDATED_GUILD_ID
        defaultTempBanShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllTempBansByGuildIdIsInShouldWork() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
        defaultTempBanShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);

        // Get all the tempBanList where guildId equals to UPDATED_GUILD_ID
        defaultTempBanShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllTempBansByGuildIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where guildId is not null
        defaultTempBanShouldBeFound("guildId.specified=true");

        // Get all the tempBanList where guildId is null
        defaultTempBanShouldNotBeFound("guildId.specified=false");
    }

    @Test
    @Transactional
    public void getAllTempBansByGuildIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where guildId is greater than or equal to DEFAULT_GUILD_ID
        defaultTempBanShouldBeFound("guildId.greaterThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the tempBanList where guildId is greater than or equal to UPDATED_GUILD_ID
        defaultTempBanShouldNotBeFound("guildId.greaterThanOrEqual=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllTempBansByGuildIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where guildId is less than or equal to DEFAULT_GUILD_ID
        defaultTempBanShouldBeFound("guildId.lessThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the tempBanList where guildId is less than or equal to SMALLER_GUILD_ID
        defaultTempBanShouldNotBeFound("guildId.lessThanOrEqual=" + SMALLER_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllTempBansByGuildIdIsLessThanSomething() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where guildId is less than DEFAULT_GUILD_ID
        defaultTempBanShouldNotBeFound("guildId.lessThan=" + DEFAULT_GUILD_ID);

        // Get all the tempBanList where guildId is less than UPDATED_GUILD_ID
        defaultTempBanShouldBeFound("guildId.lessThan=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllTempBansByGuildIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where guildId is greater than DEFAULT_GUILD_ID
        defaultTempBanShouldNotBeFound("guildId.greaterThan=" + DEFAULT_GUILD_ID);

        // Get all the tempBanList where guildId is greater than SMALLER_GUILD_ID
        defaultTempBanShouldBeFound("guildId.greaterThan=" + SMALLER_GUILD_ID);
    }


    @Test
    @Transactional
    public void getAllTempBansByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where userId equals to DEFAULT_USER_ID
        defaultTempBanShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the tempBanList where userId equals to UPDATED_USER_ID
        defaultTempBanShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTempBansByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultTempBanShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the tempBanList where userId equals to UPDATED_USER_ID
        defaultTempBanShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTempBansByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where userId is not null
        defaultTempBanShouldBeFound("userId.specified=true");

        // Get all the tempBanList where userId is null
        defaultTempBanShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllTempBansByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where userId is greater than or equal to DEFAULT_USER_ID
        defaultTempBanShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the tempBanList where userId is greater than or equal to UPDATED_USER_ID
        defaultTempBanShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTempBansByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where userId is less than or equal to DEFAULT_USER_ID
        defaultTempBanShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the tempBanList where userId is less than or equal to SMALLER_USER_ID
        defaultTempBanShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTempBansByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where userId is less than DEFAULT_USER_ID
        defaultTempBanShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the tempBanList where userId is less than UPDATED_USER_ID
        defaultTempBanShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllTempBansByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);

        // Get all the tempBanList where userId is greater than DEFAULT_USER_ID
        defaultTempBanShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the tempBanList where userId is greater than SMALLER_USER_ID
        defaultTempBanShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }


    @Test
    @Transactional
    public void getAllTempBansByBannedUserIsEqualToSomething() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);
        DiscordUser bannedUser = DiscordUserResourceIT.createEntity(em);
        em.persist(bannedUser);
        em.flush();
        tempBan.setBannedUser(bannedUser);
        tempBanRepository.saveAndFlush(tempBan);
        Long bannedUserId = bannedUser.getId();

        // Get all the tempBanList where bannedUser equals to bannedUserId
        defaultTempBanShouldBeFound("bannedUserId.equals=" + bannedUserId);

        // Get all the tempBanList where bannedUser equals to bannedUserId + 1
        defaultTempBanShouldNotBeFound("bannedUserId.equals=" + (bannedUserId + 1));
    }


    @Test
    @Transactional
    public void getAllTempBansByTempBanGuildServerIsEqualToSomething() throws Exception {
        // Initialize the database
        tempBanRepository.saveAndFlush(tempBan);
        GuildServer tempBanGuildServer = GuildServerResourceIT.createEntity(em);
        em.persist(tempBanGuildServer);
        em.flush();
        tempBan.setTempBanGuildServer(tempBanGuildServer);
        tempBanRepository.saveAndFlush(tempBan);
        Long tempBanGuildServerId = tempBanGuildServer.getId();

        // Get all the tempBanList where tempBanGuildServer equals to tempBanGuildServerId
        defaultTempBanShouldBeFound("tempBanGuildServerId.equals=" + tempBanGuildServerId);

        // Get all the tempBanList where tempBanGuildServer equals to tempBanGuildServerId + 1
        defaultTempBanShouldNotBeFound("tempBanGuildServerId.equals=" + (tempBanGuildServerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTempBanShouldBeFound(String filter) throws Exception {
        restTempBanMockMvc.perform(get("/api/temp-bans?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tempBan.getId().intValue())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));

        // Check, that the count call also returns 1
        restTempBanMockMvc.perform(get("/api/temp-bans/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTempBanShouldNotBeFound(String filter) throws Exception {
        restTempBanMockMvc.perform(get("/api/temp-bans?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTempBanMockMvc.perform(get("/api/temp-bans/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTempBan() throws Exception {
        // Get the tempBan
        restTempBanMockMvc.perform(get("/api/temp-bans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTempBan() throws Exception {
        // Initialize the database
        tempBanService.save(tempBan);

        int databaseSizeBeforeUpdate = tempBanRepository.findAll().size();

        // Update the tempBan
        TempBan updatedTempBan = tempBanRepository.findById(tempBan.getId()).get();
        // Disconnect from session so that the updates on updatedTempBan are not directly saved in db
        em.detach(updatedTempBan);
        updatedTempBan
            .reason(UPDATED_REASON)
            .endTime(UPDATED_END_TIME)
            .guildId(UPDATED_GUILD_ID)
            .userId(UPDATED_USER_ID);

        restTempBanMockMvc.perform(put("/api/temp-bans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTempBan)))
            .andExpect(status().isOk());

        // Validate the TempBan in the database
        List<TempBan> tempBanList = tempBanRepository.findAll();
        assertThat(tempBanList).hasSize(databaseSizeBeforeUpdate);
        TempBan testTempBan = tempBanList.get(tempBanList.size() - 1);
        assertThat(testTempBan.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testTempBan.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testTempBan.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
        assertThat(testTempBan.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingTempBan() throws Exception {
        int databaseSizeBeforeUpdate = tempBanRepository.findAll().size();

        // Create the TempBan

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTempBanMockMvc.perform(put("/api/temp-bans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tempBan)))
            .andExpect(status().isBadRequest());

        // Validate the TempBan in the database
        List<TempBan> tempBanList = tempBanRepository.findAll();
        assertThat(tempBanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTempBan() throws Exception {
        // Initialize the database
        tempBanService.save(tempBan);

        int databaseSizeBeforeDelete = tempBanRepository.findAll().size();

        // Delete the tempBan
        restTempBanMockMvc.perform(delete("/api/temp-bans/{id}", tempBan.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TempBan> tempBanList = tempBanRepository.findAll();
        assertThat(tempBanList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TempBan.class);
        TempBan tempBan1 = new TempBan();
        tempBan1.setId(1L);
        TempBan tempBan2 = new TempBan();
        tempBan2.setId(tempBan1.getId());
        assertThat(tempBan1).isEqualTo(tempBan2);
        tempBan2.setId(2L);
        assertThat(tempBan1).isNotEqualTo(tempBan2);
        tempBan1.setId(null);
        assertThat(tempBan1).isNotEqualTo(tempBan2);
    }
}
