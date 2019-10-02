package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.Mute;
import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.domain.GuildServer;
import com.triippztech.guildo.repository.MuteRepository;
import com.triippztech.guildo.service.MuteService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.dto.MuteCriteria;
import com.triippztech.guildo.service.MuteQueryService;

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
 * Integration tests for the {@link MuteResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class MuteResourceIT {

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_END_TIME = "AAAAAAAAAA";
    private static final String UPDATED_END_TIME = "BBBBBBBBBB";

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;
    private static final Long SMALLER_GUILD_ID = 1L - 1L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    @Autowired
    private MuteRepository muteRepository;

    @Autowired
    private MuteService muteService;

    @Autowired
    private MuteQueryService muteQueryService;

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

    private MockMvc restMuteMockMvc;

    private Mute mute;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MuteResource muteResource = new MuteResource(muteService, muteQueryService);
        this.restMuteMockMvc = MockMvcBuilders.standaloneSetup(muteResource)
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
    public static Mute createEntity(EntityManager em) {
        Mute mute = new Mute()
            .reason(DEFAULT_REASON)
            .endTime(DEFAULT_END_TIME)
            .guildId(DEFAULT_GUILD_ID)
            .userId(DEFAULT_USER_ID);
        return mute;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mute createUpdatedEntity(EntityManager em) {
        Mute mute = new Mute()
            .reason(UPDATED_REASON)
            .endTime(UPDATED_END_TIME)
            .guildId(UPDATED_GUILD_ID)
            .userId(UPDATED_USER_ID);
        return mute;
    }

    @BeforeEach
    public void initTest() {
        mute = createEntity(em);
    }

    @Test
    @Transactional
    public void createMute() throws Exception {
        int databaseSizeBeforeCreate = muteRepository.findAll().size();

        // Create the Mute
        restMuteMockMvc.perform(post("/api/mutes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mute)))
            .andExpect(status().isCreated());

        // Validate the Mute in the database
        List<Mute> muteList = muteRepository.findAll();
        assertThat(muteList).hasSize(databaseSizeBeforeCreate + 1);
        Mute testMute = muteList.get(muteList.size() - 1);
        assertThat(testMute.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testMute.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testMute.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);
        assertThat(testMute.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    public void createMuteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = muteRepository.findAll().size();

        // Create the Mute with an existing ID
        mute.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMuteMockMvc.perform(post("/api/mutes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mute)))
            .andExpect(status().isBadRequest());

        // Validate the Mute in the database
        List<Mute> muteList = muteRepository.findAll();
        assertThat(muteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkReasonIsRequired() throws Exception {
        int databaseSizeBeforeTest = muteRepository.findAll().size();
        // set the field null
        mute.setReason(null);

        // Create the Mute, which fails.

        restMuteMockMvc.perform(post("/api/mutes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mute)))
            .andExpect(status().isBadRequest());

        List<Mute> muteList = muteRepository.findAll();
        assertThat(muteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = muteRepository.findAll().size();
        // set the field null
        mute.setEndTime(null);

        // Create the Mute, which fails.

        restMuteMockMvc.perform(post("/api/mutes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mute)))
            .andExpect(status().isBadRequest());

        List<Mute> muteList = muteRepository.findAll();
        assertThat(muteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMutes() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList
        restMuteMockMvc.perform(get("/api/mutes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mute.getId().intValue())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getMute() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get the mute
        restMuteMockMvc.perform(get("/api/mutes/{id}", mute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mute.getId().intValue()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllMutesByReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where reason equals to DEFAULT_REASON
        defaultMuteShouldBeFound("reason.equals=" + DEFAULT_REASON);

        // Get all the muteList where reason equals to UPDATED_REASON
        defaultMuteShouldNotBeFound("reason.equals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    public void getAllMutesByReasonIsInShouldWork() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where reason in DEFAULT_REASON or UPDATED_REASON
        defaultMuteShouldBeFound("reason.in=" + DEFAULT_REASON + "," + UPDATED_REASON);

        // Get all the muteList where reason equals to UPDATED_REASON
        defaultMuteShouldNotBeFound("reason.in=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    public void getAllMutesByReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where reason is not null
        defaultMuteShouldBeFound("reason.specified=true");

        // Get all the muteList where reason is null
        defaultMuteShouldNotBeFound("reason.specified=false");
    }

    @Test
    @Transactional
    public void getAllMutesByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where endTime equals to DEFAULT_END_TIME
        defaultMuteShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the muteList where endTime equals to UPDATED_END_TIME
        defaultMuteShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllMutesByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultMuteShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the muteList where endTime equals to UPDATED_END_TIME
        defaultMuteShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllMutesByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where endTime is not null
        defaultMuteShouldBeFound("endTime.specified=true");

        // Get all the muteList where endTime is null
        defaultMuteShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllMutesByGuildIdIsEqualToSomething() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where guildId equals to DEFAULT_GUILD_ID
        defaultMuteShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);

        // Get all the muteList where guildId equals to UPDATED_GUILD_ID
        defaultMuteShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllMutesByGuildIdIsInShouldWork() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
        defaultMuteShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);

        // Get all the muteList where guildId equals to UPDATED_GUILD_ID
        defaultMuteShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllMutesByGuildIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where guildId is not null
        defaultMuteShouldBeFound("guildId.specified=true");

        // Get all the muteList where guildId is null
        defaultMuteShouldNotBeFound("guildId.specified=false");
    }

    @Test
    @Transactional
    public void getAllMutesByGuildIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where guildId is greater than or equal to DEFAULT_GUILD_ID
        defaultMuteShouldBeFound("guildId.greaterThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the muteList where guildId is greater than or equal to UPDATED_GUILD_ID
        defaultMuteShouldNotBeFound("guildId.greaterThanOrEqual=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllMutesByGuildIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where guildId is less than or equal to DEFAULT_GUILD_ID
        defaultMuteShouldBeFound("guildId.lessThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the muteList where guildId is less than or equal to SMALLER_GUILD_ID
        defaultMuteShouldNotBeFound("guildId.lessThanOrEqual=" + SMALLER_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllMutesByGuildIdIsLessThanSomething() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where guildId is less than DEFAULT_GUILD_ID
        defaultMuteShouldNotBeFound("guildId.lessThan=" + DEFAULT_GUILD_ID);

        // Get all the muteList where guildId is less than UPDATED_GUILD_ID
        defaultMuteShouldBeFound("guildId.lessThan=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllMutesByGuildIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where guildId is greater than DEFAULT_GUILD_ID
        defaultMuteShouldNotBeFound("guildId.greaterThan=" + DEFAULT_GUILD_ID);

        // Get all the muteList where guildId is greater than SMALLER_GUILD_ID
        defaultMuteShouldBeFound("guildId.greaterThan=" + SMALLER_GUILD_ID);
    }


    @Test
    @Transactional
    public void getAllMutesByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where userId equals to DEFAULT_USER_ID
        defaultMuteShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the muteList where userId equals to UPDATED_USER_ID
        defaultMuteShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMutesByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultMuteShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the muteList where userId equals to UPDATED_USER_ID
        defaultMuteShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMutesByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where userId is not null
        defaultMuteShouldBeFound("userId.specified=true");

        // Get all the muteList where userId is null
        defaultMuteShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllMutesByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where userId is greater than or equal to DEFAULT_USER_ID
        defaultMuteShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the muteList where userId is greater than or equal to UPDATED_USER_ID
        defaultMuteShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMutesByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where userId is less than or equal to DEFAULT_USER_ID
        defaultMuteShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the muteList where userId is less than or equal to SMALLER_USER_ID
        defaultMuteShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMutesByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where userId is less than DEFAULT_USER_ID
        defaultMuteShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the muteList where userId is less than UPDATED_USER_ID
        defaultMuteShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMutesByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);

        // Get all the muteList where userId is greater than DEFAULT_USER_ID
        defaultMuteShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the muteList where userId is greater than SMALLER_USER_ID
        defaultMuteShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }


    @Test
    @Transactional
    public void getAllMutesByMutedUserIsEqualToSomething() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);
        DiscordUser mutedUser = DiscordUserResourceIT.createEntity(em);
        em.persist(mutedUser);
        em.flush();
        mute.setMutedUser(mutedUser);
        muteRepository.saveAndFlush(mute);
        Long mutedUserId = mutedUser.getId();

        // Get all the muteList where mutedUser equals to mutedUserId
        defaultMuteShouldBeFound("mutedUserId.equals=" + mutedUserId);

        // Get all the muteList where mutedUser equals to mutedUserId + 1
        defaultMuteShouldNotBeFound("mutedUserId.equals=" + (mutedUserId + 1));
    }


    @Test
    @Transactional
    public void getAllMutesByMutedGuildServerIsEqualToSomething() throws Exception {
        // Initialize the database
        muteRepository.saveAndFlush(mute);
        GuildServer mutedGuildServer = GuildServerResourceIT.createEntity(em);
        em.persist(mutedGuildServer);
        em.flush();
        mute.setMutedGuildServer(mutedGuildServer);
        muteRepository.saveAndFlush(mute);
        Long mutedGuildServerId = mutedGuildServer.getId();

        // Get all the muteList where mutedGuildServer equals to mutedGuildServerId
        defaultMuteShouldBeFound("mutedGuildServerId.equals=" + mutedGuildServerId);

        // Get all the muteList where mutedGuildServer equals to mutedGuildServerId + 1
        defaultMuteShouldNotBeFound("mutedGuildServerId.equals=" + (mutedGuildServerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMuteShouldBeFound(String filter) throws Exception {
        restMuteMockMvc.perform(get("/api/mutes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mute.getId().intValue())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));

        // Check, that the count call also returns 1
        restMuteMockMvc.perform(get("/api/mutes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMuteShouldNotBeFound(String filter) throws Exception {
        restMuteMockMvc.perform(get("/api/mutes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMuteMockMvc.perform(get("/api/mutes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMute() throws Exception {
        // Get the mute
        restMuteMockMvc.perform(get("/api/mutes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMute() throws Exception {
        // Initialize the database
        muteService.save(mute);

        int databaseSizeBeforeUpdate = muteRepository.findAll().size();

        // Update the mute
        Mute updatedMute = muteRepository.findById(mute.getId()).get();
        // Disconnect from session so that the updates on updatedMute are not directly saved in db
        em.detach(updatedMute);
        updatedMute
            .reason(UPDATED_REASON)
            .endTime(UPDATED_END_TIME)
            .guildId(UPDATED_GUILD_ID)
            .userId(UPDATED_USER_ID);

        restMuteMockMvc.perform(put("/api/mutes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMute)))
            .andExpect(status().isOk());

        // Validate the Mute in the database
        List<Mute> muteList = muteRepository.findAll();
        assertThat(muteList).hasSize(databaseSizeBeforeUpdate);
        Mute testMute = muteList.get(muteList.size() - 1);
        assertThat(testMute.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testMute.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testMute.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
        assertThat(testMute.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingMute() throws Exception {
        int databaseSizeBeforeUpdate = muteRepository.findAll().size();

        // Create the Mute

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMuteMockMvc.perform(put("/api/mutes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mute)))
            .andExpect(status().isBadRequest());

        // Validate the Mute in the database
        List<Mute> muteList = muteRepository.findAll();
        assertThat(muteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMute() throws Exception {
        // Initialize the database
        muteService.save(mute);

        int databaseSizeBeforeDelete = muteRepository.findAll().size();

        // Delete the mute
        restMuteMockMvc.perform(delete("/api/mutes/{id}", mute.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mute> muteList = muteRepository.findAll();
        assertThat(muteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mute.class);
        Mute mute1 = new Mute();
        mute1.setId(1L);
        Mute mute2 = new Mute();
        mute2.setId(mute1.getId());
        assertThat(mute1).isEqualTo(mute2);
        mute2.setId(2L);
        assertThat(mute1).isNotEqualTo(mute2);
        mute1.setId(null);
        assertThat(mute1).isNotEqualTo(mute2);
    }
}
