package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.GuildServerStrike;
import com.triippztech.guildo.domain.DiscordUser;
import com.triippztech.guildo.repository.GuildServerStrikeRepository;
import com.triippztech.guildo.service.guild.GuildServerStrikeService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.guild.GuildServerStrikeQueryService;

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
 * Integration tests for the {@link GuildServerStrikeResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class GuildServerStrikeResourceIT {

    private static final Integer DEFAULT_COUNT = 0;
    private static final Integer UPDATED_COUNT = 1;
    private static final Integer SMALLER_COUNT = 0 - 1;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final Long DEFAULT_GUILD_ID = 1L;
    private static final Long UPDATED_GUILD_ID = 2L;
    private static final Long SMALLER_GUILD_ID = 1L - 1L;

    @Autowired
    private GuildServerStrikeRepository guildServerStrikeRepository;

    @Autowired
    private GuildServerStrikeService guildServerStrikeService;

    @Autowired
    private GuildServerStrikeQueryService guildServerStrikeQueryService;

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

    private MockMvc restGuildServerStrikeMockMvc;

    private GuildServerStrike guildServerStrike;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GuildServerStrikeResource guildServerStrikeResource = new GuildServerStrikeResource(guildServerStrikeService, guildServerStrikeQueryService);
        this.restGuildServerStrikeMockMvc = MockMvcBuilders.standaloneSetup(guildServerStrikeResource)
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
    public static GuildServerStrike createEntity(EntityManager em) {
        GuildServerStrike guildServerStrike = new GuildServerStrike()
            .count(DEFAULT_COUNT)
            .userId(DEFAULT_USER_ID)
            .guildId(DEFAULT_GUILD_ID);
        // Add required entity
        DiscordUser discordUser;
        if (TestUtil.findAll(em, DiscordUser.class).isEmpty()) {
            discordUser = DiscordUserResourceIT.createEntity(em);
            em.persist(discordUser);
            em.flush();
        } else {
            discordUser = TestUtil.findAll(em, DiscordUser.class).get(0);
        }
        guildServerStrike.setDiscordUser(discordUser);
        return guildServerStrike;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GuildServerStrike createUpdatedEntity(EntityManager em) {
        GuildServerStrike guildServerStrike = new GuildServerStrike()
            .count(UPDATED_COUNT)
            .userId(UPDATED_USER_ID)
            .guildId(UPDATED_GUILD_ID);
        // Add required entity
        DiscordUser discordUser;
        if (TestUtil.findAll(em, DiscordUser.class).isEmpty()) {
            discordUser = DiscordUserResourceIT.createUpdatedEntity(em);
            em.persist(discordUser);
            em.flush();
        } else {
            discordUser = TestUtil.findAll(em, DiscordUser.class).get(0);
        }
        guildServerStrike.setDiscordUser(discordUser);
        return guildServerStrike;
    }

    @BeforeEach
    public void initTest() {
        guildServerStrike = createEntity(em);
    }

    @Test
    @Transactional
    public void createGuildServerStrike() throws Exception {
        int databaseSizeBeforeCreate = guildServerStrikeRepository.findAll().size();

        // Create the GuildServerStrike
        restGuildServerStrikeMockMvc.perform(post("/api/guild-server-strikes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerStrike)))
            .andExpect(status().isCreated());

        // Validate the GuildServerStrike in the database
        List<GuildServerStrike> guildServerStrikeList = guildServerStrikeRepository.findAll();
        assertThat(guildServerStrikeList).hasSize(databaseSizeBeforeCreate + 1);
        GuildServerStrike testGuildServerStrike = guildServerStrikeList.get(guildServerStrikeList.size() - 1);
        assertThat(testGuildServerStrike.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testGuildServerStrike.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testGuildServerStrike.getGuildId()).isEqualTo(DEFAULT_GUILD_ID);

        // Validate the id for MapsId, the ids must be same
        assertThat(testGuildServerStrike.getId()).isEqualTo(testGuildServerStrike.getDiscordUser().getId());
    }

    @Test
    @Transactional
    public void createGuildServerStrikeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = guildServerStrikeRepository.findAll().size();

        // Create the GuildServerStrike with an existing ID
        guildServerStrike.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuildServerStrikeMockMvc.perform(post("/api/guild-server-strikes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerStrike)))
            .andExpect(status().isBadRequest());

        // Validate the GuildServerStrike in the database
        List<GuildServerStrike> guildServerStrikeList = guildServerStrikeRepository.findAll();
        assertThat(guildServerStrikeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void updateGuildServerStrikeMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        guildServerStrikeService.save(guildServerStrike);
        int databaseSizeBeforeCreate = guildServerStrikeRepository.findAll().size();

        // Add a new parent entity
        DiscordUser discordUser = DiscordUserResourceIT.createUpdatedEntity(em);
        em.persist(discordUser);
        em.flush();

        // Load the guildServerStrike
        GuildServerStrike updatedGuildServerStrike = guildServerStrikeRepository.findById(guildServerStrike.getId()).get();
        // Disconnect from session so that the updates on updatedGuildServerStrike are not directly saved in db
        em.detach(updatedGuildServerStrike);

        // Update the DiscordUser with new association value
        updatedGuildServerStrike.setDiscordUser(discordUser);

        // Update the entity
        restGuildServerStrikeMockMvc.perform(put("/api/guild-server-strikes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGuildServerStrike)))
            .andExpect(status().isOk());

        // Validate the GuildServerStrike in the database
        List<GuildServerStrike> guildServerStrikeList = guildServerStrikeRepository.findAll();
        assertThat(guildServerStrikeList).hasSize(databaseSizeBeforeCreate);
        GuildServerStrike testGuildServerStrike = guildServerStrikeList.get(guildServerStrikeList.size() - 1);

        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testGuildServerStrike.getId()).isEqualTo(testGuildServerStrike.getDiscordUser().getId());
    }

    @Test
    @Transactional
    public void checkCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildServerStrikeRepository.findAll().size();
        // set the field null
        guildServerStrike.setCount(null);

        // Create the GuildServerStrike, which fails.

        restGuildServerStrikeMockMvc.perform(post("/api/guild-server-strikes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerStrike)))
            .andExpect(status().isBadRequest());

        List<GuildServerStrike> guildServerStrikeList = guildServerStrikeRepository.findAll();
        assertThat(guildServerStrikeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildServerStrikeRepository.findAll().size();
        // set the field null
        guildServerStrike.setUserId(null);

        // Create the GuildServerStrike, which fails.

        restGuildServerStrikeMockMvc.perform(post("/api/guild-server-strikes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerStrike)))
            .andExpect(status().isBadRequest());

        List<GuildServerStrike> guildServerStrikeList = guildServerStrikeRepository.findAll();
        assertThat(guildServerStrikeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGuildIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = guildServerStrikeRepository.findAll().size();
        // set the field null
        guildServerStrike.setGuildId(null);

        // Create the GuildServerStrike, which fails.

        restGuildServerStrikeMockMvc.perform(post("/api/guild-server-strikes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerStrike)))
            .andExpect(status().isBadRequest());

        List<GuildServerStrike> guildServerStrikeList = guildServerStrikeRepository.findAll();
        assertThat(guildServerStrikeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikes() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList
        restGuildServerStrikeMockMvc.perform(get("/api/guild-server-strikes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildServerStrike.getId().intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getGuildServerStrike() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get the guildServerStrike
        restGuildServerStrikeMockMvc.perform(get("/api/guild-server-strikes/{id}", guildServerStrike.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(guildServerStrike.getId().intValue()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.guildId").value(DEFAULT_GUILD_ID.intValue()));
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByCountIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where count equals to DEFAULT_COUNT
        defaultGuildServerStrikeShouldBeFound("count.equals=" + DEFAULT_COUNT);

        // Get all the guildServerStrikeList where count equals to UPDATED_COUNT
        defaultGuildServerStrikeShouldNotBeFound("count.equals=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByCountIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where count in DEFAULT_COUNT or UPDATED_COUNT
        defaultGuildServerStrikeShouldBeFound("count.in=" + DEFAULT_COUNT + "," + UPDATED_COUNT);

        // Get all the guildServerStrikeList where count equals to UPDATED_COUNT
        defaultGuildServerStrikeShouldNotBeFound("count.in=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where count is not null
        defaultGuildServerStrikeShouldBeFound("count.specified=true");

        // Get all the guildServerStrikeList where count is null
        defaultGuildServerStrikeShouldNotBeFound("count.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where count is greater than or equal to DEFAULT_COUNT
        defaultGuildServerStrikeShouldBeFound("count.greaterThanOrEqual=" + DEFAULT_COUNT);

        // Get all the guildServerStrikeList where count is greater than or equal to UPDATED_COUNT
        defaultGuildServerStrikeShouldNotBeFound("count.greaterThanOrEqual=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where count is less than or equal to DEFAULT_COUNT
        defaultGuildServerStrikeShouldBeFound("count.lessThanOrEqual=" + DEFAULT_COUNT);

        // Get all the guildServerStrikeList where count is less than or equal to SMALLER_COUNT
        defaultGuildServerStrikeShouldNotBeFound("count.lessThanOrEqual=" + SMALLER_COUNT);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByCountIsLessThanSomething() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where count is less than DEFAULT_COUNT
        defaultGuildServerStrikeShouldNotBeFound("count.lessThan=" + DEFAULT_COUNT);

        // Get all the guildServerStrikeList where count is less than UPDATED_COUNT
        defaultGuildServerStrikeShouldBeFound("count.lessThan=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where count is greater than DEFAULT_COUNT
        defaultGuildServerStrikeShouldNotBeFound("count.greaterThan=" + DEFAULT_COUNT);

        // Get all the guildServerStrikeList where count is greater than SMALLER_COUNT
        defaultGuildServerStrikeShouldBeFound("count.greaterThan=" + SMALLER_COUNT);
    }


    @Test
    @Transactional
    public void getAllGuildServerStrikesByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where userId equals to DEFAULT_USER_ID
        defaultGuildServerStrikeShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the guildServerStrikeList where userId equals to UPDATED_USER_ID
        defaultGuildServerStrikeShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultGuildServerStrikeShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the guildServerStrikeList where userId equals to UPDATED_USER_ID
        defaultGuildServerStrikeShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where userId is not null
        defaultGuildServerStrikeShouldBeFound("userId.specified=true");

        // Get all the guildServerStrikeList where userId is null
        defaultGuildServerStrikeShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where userId is greater than or equal to DEFAULT_USER_ID
        defaultGuildServerStrikeShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the guildServerStrikeList where userId is greater than or equal to UPDATED_USER_ID
        defaultGuildServerStrikeShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where userId is less than or equal to DEFAULT_USER_ID
        defaultGuildServerStrikeShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the guildServerStrikeList where userId is less than or equal to SMALLER_USER_ID
        defaultGuildServerStrikeShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where userId is less than DEFAULT_USER_ID
        defaultGuildServerStrikeShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the guildServerStrikeList where userId is less than UPDATED_USER_ID
        defaultGuildServerStrikeShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where userId is greater than DEFAULT_USER_ID
        defaultGuildServerStrikeShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the guildServerStrikeList where userId is greater than SMALLER_USER_ID
        defaultGuildServerStrikeShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }


    @Test
    @Transactional
    public void getAllGuildServerStrikesByGuildIdIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where guildId equals to DEFAULT_GUILD_ID
        defaultGuildServerStrikeShouldBeFound("guildId.equals=" + DEFAULT_GUILD_ID);

        // Get all the guildServerStrikeList where guildId equals to UPDATED_GUILD_ID
        defaultGuildServerStrikeShouldNotBeFound("guildId.equals=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByGuildIdIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where guildId in DEFAULT_GUILD_ID or UPDATED_GUILD_ID
        defaultGuildServerStrikeShouldBeFound("guildId.in=" + DEFAULT_GUILD_ID + "," + UPDATED_GUILD_ID);

        // Get all the guildServerStrikeList where guildId equals to UPDATED_GUILD_ID
        defaultGuildServerStrikeShouldNotBeFound("guildId.in=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByGuildIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where guildId is not null
        defaultGuildServerStrikeShouldBeFound("guildId.specified=true");

        // Get all the guildServerStrikeList where guildId is null
        defaultGuildServerStrikeShouldNotBeFound("guildId.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByGuildIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where guildId is greater than or equal to DEFAULT_GUILD_ID
        defaultGuildServerStrikeShouldBeFound("guildId.greaterThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the guildServerStrikeList where guildId is greater than or equal to UPDATED_GUILD_ID
        defaultGuildServerStrikeShouldNotBeFound("guildId.greaterThanOrEqual=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByGuildIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where guildId is less than or equal to DEFAULT_GUILD_ID
        defaultGuildServerStrikeShouldBeFound("guildId.lessThanOrEqual=" + DEFAULT_GUILD_ID);

        // Get all the guildServerStrikeList where guildId is less than or equal to SMALLER_GUILD_ID
        defaultGuildServerStrikeShouldNotBeFound("guildId.lessThanOrEqual=" + SMALLER_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByGuildIdIsLessThanSomething() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where guildId is less than DEFAULT_GUILD_ID
        defaultGuildServerStrikeShouldNotBeFound("guildId.lessThan=" + DEFAULT_GUILD_ID);

        // Get all the guildServerStrikeList where guildId is less than UPDATED_GUILD_ID
        defaultGuildServerStrikeShouldBeFound("guildId.lessThan=" + UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void getAllGuildServerStrikesByGuildIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);

        // Get all the guildServerStrikeList where guildId is greater than DEFAULT_GUILD_ID
        defaultGuildServerStrikeShouldNotBeFound("guildId.greaterThan=" + DEFAULT_GUILD_ID);

        // Get all the guildServerStrikeList where guildId is greater than SMALLER_GUILD_ID
        defaultGuildServerStrikeShouldBeFound("guildId.greaterThan=" + SMALLER_GUILD_ID);
    }


    @Test
    @Transactional
    public void getAllGuildServerStrikesByDiscordUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        DiscordUser discordUser = guildServerStrike.getDiscordUser();
        guildServerStrikeRepository.saveAndFlush(guildServerStrike);
        Long discordUserId = discordUser.getId();

        // Get all the guildServerStrikeList where discordUser equals to discordUserId
        defaultGuildServerStrikeShouldBeFound("discordUserId.equals=" + discordUserId);

        // Get all the guildServerStrikeList where discordUser equals to discordUserId + 1
        defaultGuildServerStrikeShouldNotBeFound("discordUserId.equals=" + (discordUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGuildServerStrikeShouldBeFound(String filter) throws Exception {
        restGuildServerStrikeMockMvc.perform(get("/api/guild-server-strikes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildServerStrike.getId().intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].guildId").value(hasItem(DEFAULT_GUILD_ID.intValue())));

        // Check, that the count call also returns 1
        restGuildServerStrikeMockMvc.perform(get("/api/guild-server-strikes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGuildServerStrikeShouldNotBeFound(String filter) throws Exception {
        restGuildServerStrikeMockMvc.perform(get("/api/guild-server-strikes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGuildServerStrikeMockMvc.perform(get("/api/guild-server-strikes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGuildServerStrike() throws Exception {
        // Get the guildServerStrike
        restGuildServerStrikeMockMvc.perform(get("/api/guild-server-strikes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGuildServerStrike() throws Exception {
        // Initialize the database
        guildServerStrikeService.save(guildServerStrike);

        int databaseSizeBeforeUpdate = guildServerStrikeRepository.findAll().size();

        // Update the guildServerStrike
        GuildServerStrike updatedGuildServerStrike = guildServerStrikeRepository.findById(guildServerStrike.getId()).get();
        // Disconnect from session so that the updates on updatedGuildServerStrike are not directly saved in db
        em.detach(updatedGuildServerStrike);
        updatedGuildServerStrike
            .count(UPDATED_COUNT)
            .userId(UPDATED_USER_ID)
            .guildId(UPDATED_GUILD_ID);

        restGuildServerStrikeMockMvc.perform(put("/api/guild-server-strikes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGuildServerStrike)))
            .andExpect(status().isOk());

        // Validate the GuildServerStrike in the database
        List<GuildServerStrike> guildServerStrikeList = guildServerStrikeRepository.findAll();
        assertThat(guildServerStrikeList).hasSize(databaseSizeBeforeUpdate);
        GuildServerStrike testGuildServerStrike = guildServerStrikeList.get(guildServerStrikeList.size() - 1);
        assertThat(testGuildServerStrike.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testGuildServerStrike.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testGuildServerStrike.getGuildId()).isEqualTo(UPDATED_GUILD_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingGuildServerStrike() throws Exception {
        int databaseSizeBeforeUpdate = guildServerStrikeRepository.findAll().size();

        // Create the GuildServerStrike

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuildServerStrikeMockMvc.perform(put("/api/guild-server-strikes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerStrike)))
            .andExpect(status().isBadRequest());

        // Validate the GuildServerStrike in the database
        List<GuildServerStrike> guildServerStrikeList = guildServerStrikeRepository.findAll();
        assertThat(guildServerStrikeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGuildServerStrike() throws Exception {
        // Initialize the database
        guildServerStrikeService.save(guildServerStrike);

        int databaseSizeBeforeDelete = guildServerStrikeRepository.findAll().size();

        // Delete the guildServerStrike
        restGuildServerStrikeMockMvc.perform(delete("/api/guild-server-strikes/{id}", guildServerStrike.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GuildServerStrike> guildServerStrikeList = guildServerStrikeRepository.findAll();
        assertThat(guildServerStrikeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuildServerStrike.class);
        GuildServerStrike guildServerStrike1 = new GuildServerStrike();
        guildServerStrike1.setId(1L);
        GuildServerStrike guildServerStrike2 = new GuildServerStrike();
        guildServerStrike2.setId(guildServerStrike1.getId());
        assertThat(guildServerStrike1).isEqualTo(guildServerStrike2);
        guildServerStrike2.setId(2L);
        assertThat(guildServerStrike1).isNotEqualTo(guildServerStrike2);
        guildServerStrike1.setId(null);
        assertThat(guildServerStrike1).isNotEqualTo(guildServerStrike2);
    }
}
