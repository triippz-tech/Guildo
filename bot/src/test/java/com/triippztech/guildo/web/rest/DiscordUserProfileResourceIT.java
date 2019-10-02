package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.DiscordUserProfile;
import com.triippztech.guildo.repository.DiscordUserProfileRepository;
import com.triippztech.guildo.service.DiscordUserProfileService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.dto.DiscordUserProfileCriteria;
import com.triippztech.guildo.service.DiscordUserProfileQueryService;

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
 * Integration tests for the {@link DiscordUserProfileResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class DiscordUserProfileResourceIT {

    private static final String DEFAULT_FAVORITE_GAME = "AAAAAAAAAA";
    private static final String UPDATED_FAVORITE_GAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_PHOTO = "BBBBBBBBBB";

    private static final String DEFAULT_TWITTER_URL = "AAAAAAAAAA";
    private static final String UPDATED_TWITTER_URL = "BBBBBBBBBB";

    private static final String DEFAULT_TWITCH_URL = "AAAAAAAAAA";
    private static final String UPDATED_TWITCH_URL = "BBBBBBBBBB";

    private static final String DEFAULT_YOUTUBE_URL = "AAAAAAAAAA";
    private static final String UPDATED_YOUTUBE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_FACEBOOK_URL = "AAAAAAAAAA";
    private static final String UPDATED_FACEBOOK_URL = "BBBBBBBBBB";

    private static final String DEFAULT_HITBOX_URL = "AAAAAAAAAA";
    private static final String UPDATED_HITBOX_URL = "BBBBBBBBBB";

    private static final String DEFAULT_BEAM_URL = "AAAAAAAAAA";
    private static final String UPDATED_BEAM_URL = "BBBBBBBBBB";

    @Autowired
    private DiscordUserProfileRepository discordUserProfileRepository;

    @Autowired
    private DiscordUserProfileService discordUserProfileService;

    @Autowired
    private DiscordUserProfileQueryService discordUserProfileQueryService;

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

    private MockMvc restDiscordUserProfileMockMvc;

    private DiscordUserProfile discordUserProfile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DiscordUserProfileResource discordUserProfileResource = new DiscordUserProfileResource(discordUserProfileService, discordUserProfileQueryService);
        this.restDiscordUserProfileMockMvc = MockMvcBuilders.standaloneSetup(discordUserProfileResource)
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
    public static DiscordUserProfile createEntity(EntityManager em) {
        DiscordUserProfile discordUserProfile = new DiscordUserProfile()
            .favoriteGame(DEFAULT_FAVORITE_GAME)
            .profilePhoto(DEFAULT_PROFILE_PHOTO)
            .twitterUrl(DEFAULT_TWITTER_URL)
            .twitchUrl(DEFAULT_TWITCH_URL)
            .youtubeUrl(DEFAULT_YOUTUBE_URL)
            .facebookUrl(DEFAULT_FACEBOOK_URL)
            .hitboxUrl(DEFAULT_HITBOX_URL)
            .beamUrl(DEFAULT_BEAM_URL);
        return discordUserProfile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DiscordUserProfile createUpdatedEntity(EntityManager em) {
        DiscordUserProfile discordUserProfile = new DiscordUserProfile()
            .favoriteGame(UPDATED_FAVORITE_GAME)
            .profilePhoto(UPDATED_PROFILE_PHOTO)
            .twitterUrl(UPDATED_TWITTER_URL)
            .twitchUrl(UPDATED_TWITCH_URL)
            .youtubeUrl(UPDATED_YOUTUBE_URL)
            .facebookUrl(UPDATED_FACEBOOK_URL)
            .hitboxUrl(UPDATED_HITBOX_URL)
            .beamUrl(UPDATED_BEAM_URL);
        return discordUserProfile;
    }

    @BeforeEach
    public void initTest() {
        discordUserProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiscordUserProfile() throws Exception {
        int databaseSizeBeforeCreate = discordUserProfileRepository.findAll().size();

        // Create the DiscordUserProfile
        restDiscordUserProfileMockMvc.perform(post("/api/discord-user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUserProfile)))
            .andExpect(status().isCreated());

        // Validate the DiscordUserProfile in the database
        List<DiscordUserProfile> discordUserProfileList = discordUserProfileRepository.findAll();
        assertThat(discordUserProfileList).hasSize(databaseSizeBeforeCreate + 1);
        DiscordUserProfile testDiscordUserProfile = discordUserProfileList.get(discordUserProfileList.size() - 1);
        assertThat(testDiscordUserProfile.getFavoriteGame()).isEqualTo(DEFAULT_FAVORITE_GAME);
        assertThat(testDiscordUserProfile.getProfilePhoto()).isEqualTo(DEFAULT_PROFILE_PHOTO);
        assertThat(testDiscordUserProfile.getTwitterUrl()).isEqualTo(DEFAULT_TWITTER_URL);
        assertThat(testDiscordUserProfile.getTwitchUrl()).isEqualTo(DEFAULT_TWITCH_URL);
        assertThat(testDiscordUserProfile.getYoutubeUrl()).isEqualTo(DEFAULT_YOUTUBE_URL);
        assertThat(testDiscordUserProfile.getFacebookUrl()).isEqualTo(DEFAULT_FACEBOOK_URL);
        assertThat(testDiscordUserProfile.getHitboxUrl()).isEqualTo(DEFAULT_HITBOX_URL);
        assertThat(testDiscordUserProfile.getBeamUrl()).isEqualTo(DEFAULT_BEAM_URL);
    }

    @Test
    @Transactional
    public void createDiscordUserProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = discordUserProfileRepository.findAll().size();

        // Create the DiscordUserProfile with an existing ID
        discordUserProfile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiscordUserProfileMockMvc.perform(post("/api/discord-user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUserProfile)))
            .andExpect(status().isBadRequest());

        // Validate the DiscordUserProfile in the database
        List<DiscordUserProfile> discordUserProfileList = discordUserProfileRepository.findAll();
        assertThat(discordUserProfileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDiscordUserProfiles() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList
        restDiscordUserProfileMockMvc.perform(get("/api/discord-user-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discordUserProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].favoriteGame").value(hasItem(DEFAULT_FAVORITE_GAME.toString())))
            .andExpect(jsonPath("$.[*].profilePhoto").value(hasItem(DEFAULT_PROFILE_PHOTO.toString())))
            .andExpect(jsonPath("$.[*].twitterUrl").value(hasItem(DEFAULT_TWITTER_URL.toString())))
            .andExpect(jsonPath("$.[*].twitchUrl").value(hasItem(DEFAULT_TWITCH_URL.toString())))
            .andExpect(jsonPath("$.[*].youtubeUrl").value(hasItem(DEFAULT_YOUTUBE_URL.toString())))
            .andExpect(jsonPath("$.[*].facebookUrl").value(hasItem(DEFAULT_FACEBOOK_URL.toString())))
            .andExpect(jsonPath("$.[*].hitboxUrl").value(hasItem(DEFAULT_HITBOX_URL.toString())))
            .andExpect(jsonPath("$.[*].beamUrl").value(hasItem(DEFAULT_BEAM_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getDiscordUserProfile() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get the discordUserProfile
        restDiscordUserProfileMockMvc.perform(get("/api/discord-user-profiles/{id}", discordUserProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(discordUserProfile.getId().intValue()))
            .andExpect(jsonPath("$.favoriteGame").value(DEFAULT_FAVORITE_GAME.toString()))
            .andExpect(jsonPath("$.profilePhoto").value(DEFAULT_PROFILE_PHOTO.toString()))
            .andExpect(jsonPath("$.twitterUrl").value(DEFAULT_TWITTER_URL.toString()))
            .andExpect(jsonPath("$.twitchUrl").value(DEFAULT_TWITCH_URL.toString()))
            .andExpect(jsonPath("$.youtubeUrl").value(DEFAULT_YOUTUBE_URL.toString()))
            .andExpect(jsonPath("$.facebookUrl").value(DEFAULT_FACEBOOK_URL.toString()))
            .andExpect(jsonPath("$.hitboxUrl").value(DEFAULT_HITBOX_URL.toString()))
            .andExpect(jsonPath("$.beamUrl").value(DEFAULT_BEAM_URL.toString()));
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByFavoriteGameIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where favoriteGame equals to DEFAULT_FAVORITE_GAME
        defaultDiscordUserProfileShouldBeFound("favoriteGame.equals=" + DEFAULT_FAVORITE_GAME);

        // Get all the discordUserProfileList where favoriteGame equals to UPDATED_FAVORITE_GAME
        defaultDiscordUserProfileShouldNotBeFound("favoriteGame.equals=" + UPDATED_FAVORITE_GAME);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByFavoriteGameIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where favoriteGame in DEFAULT_FAVORITE_GAME or UPDATED_FAVORITE_GAME
        defaultDiscordUserProfileShouldBeFound("favoriteGame.in=" + DEFAULT_FAVORITE_GAME + "," + UPDATED_FAVORITE_GAME);

        // Get all the discordUserProfileList where favoriteGame equals to UPDATED_FAVORITE_GAME
        defaultDiscordUserProfileShouldNotBeFound("favoriteGame.in=" + UPDATED_FAVORITE_GAME);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByFavoriteGameIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where favoriteGame is not null
        defaultDiscordUserProfileShouldBeFound("favoriteGame.specified=true");

        // Get all the discordUserProfileList where favoriteGame is null
        defaultDiscordUserProfileShouldNotBeFound("favoriteGame.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByProfilePhotoIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where profilePhoto equals to DEFAULT_PROFILE_PHOTO
        defaultDiscordUserProfileShouldBeFound("profilePhoto.equals=" + DEFAULT_PROFILE_PHOTO);

        // Get all the discordUserProfileList where profilePhoto equals to UPDATED_PROFILE_PHOTO
        defaultDiscordUserProfileShouldNotBeFound("profilePhoto.equals=" + UPDATED_PROFILE_PHOTO);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByProfilePhotoIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where profilePhoto in DEFAULT_PROFILE_PHOTO or UPDATED_PROFILE_PHOTO
        defaultDiscordUserProfileShouldBeFound("profilePhoto.in=" + DEFAULT_PROFILE_PHOTO + "," + UPDATED_PROFILE_PHOTO);

        // Get all the discordUserProfileList where profilePhoto equals to UPDATED_PROFILE_PHOTO
        defaultDiscordUserProfileShouldNotBeFound("profilePhoto.in=" + UPDATED_PROFILE_PHOTO);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByProfilePhotoIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where profilePhoto is not null
        defaultDiscordUserProfileShouldBeFound("profilePhoto.specified=true");

        // Get all the discordUserProfileList where profilePhoto is null
        defaultDiscordUserProfileShouldNotBeFound("profilePhoto.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByTwitterUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where twitterUrl equals to DEFAULT_TWITTER_URL
        defaultDiscordUserProfileShouldBeFound("twitterUrl.equals=" + DEFAULT_TWITTER_URL);

        // Get all the discordUserProfileList where twitterUrl equals to UPDATED_TWITTER_URL
        defaultDiscordUserProfileShouldNotBeFound("twitterUrl.equals=" + UPDATED_TWITTER_URL);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByTwitterUrlIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where twitterUrl in DEFAULT_TWITTER_URL or UPDATED_TWITTER_URL
        defaultDiscordUserProfileShouldBeFound("twitterUrl.in=" + DEFAULT_TWITTER_URL + "," + UPDATED_TWITTER_URL);

        // Get all the discordUserProfileList where twitterUrl equals to UPDATED_TWITTER_URL
        defaultDiscordUserProfileShouldNotBeFound("twitterUrl.in=" + UPDATED_TWITTER_URL);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByTwitterUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where twitterUrl is not null
        defaultDiscordUserProfileShouldBeFound("twitterUrl.specified=true");

        // Get all the discordUserProfileList where twitterUrl is null
        defaultDiscordUserProfileShouldNotBeFound("twitterUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByTwitchUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where twitchUrl equals to DEFAULT_TWITCH_URL
        defaultDiscordUserProfileShouldBeFound("twitchUrl.equals=" + DEFAULT_TWITCH_URL);

        // Get all the discordUserProfileList where twitchUrl equals to UPDATED_TWITCH_URL
        defaultDiscordUserProfileShouldNotBeFound("twitchUrl.equals=" + UPDATED_TWITCH_URL);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByTwitchUrlIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where twitchUrl in DEFAULT_TWITCH_URL or UPDATED_TWITCH_URL
        defaultDiscordUserProfileShouldBeFound("twitchUrl.in=" + DEFAULT_TWITCH_URL + "," + UPDATED_TWITCH_URL);

        // Get all the discordUserProfileList where twitchUrl equals to UPDATED_TWITCH_URL
        defaultDiscordUserProfileShouldNotBeFound("twitchUrl.in=" + UPDATED_TWITCH_URL);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByTwitchUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where twitchUrl is not null
        defaultDiscordUserProfileShouldBeFound("twitchUrl.specified=true");

        // Get all the discordUserProfileList where twitchUrl is null
        defaultDiscordUserProfileShouldNotBeFound("twitchUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByYoutubeUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where youtubeUrl equals to DEFAULT_YOUTUBE_URL
        defaultDiscordUserProfileShouldBeFound("youtubeUrl.equals=" + DEFAULT_YOUTUBE_URL);

        // Get all the discordUserProfileList where youtubeUrl equals to UPDATED_YOUTUBE_URL
        defaultDiscordUserProfileShouldNotBeFound("youtubeUrl.equals=" + UPDATED_YOUTUBE_URL);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByYoutubeUrlIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where youtubeUrl in DEFAULT_YOUTUBE_URL or UPDATED_YOUTUBE_URL
        defaultDiscordUserProfileShouldBeFound("youtubeUrl.in=" + DEFAULT_YOUTUBE_URL + "," + UPDATED_YOUTUBE_URL);

        // Get all the discordUserProfileList where youtubeUrl equals to UPDATED_YOUTUBE_URL
        defaultDiscordUserProfileShouldNotBeFound("youtubeUrl.in=" + UPDATED_YOUTUBE_URL);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByYoutubeUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where youtubeUrl is not null
        defaultDiscordUserProfileShouldBeFound("youtubeUrl.specified=true");

        // Get all the discordUserProfileList where youtubeUrl is null
        defaultDiscordUserProfileShouldNotBeFound("youtubeUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByFacebookUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where facebookUrl equals to DEFAULT_FACEBOOK_URL
        defaultDiscordUserProfileShouldBeFound("facebookUrl.equals=" + DEFAULT_FACEBOOK_URL);

        // Get all the discordUserProfileList where facebookUrl equals to UPDATED_FACEBOOK_URL
        defaultDiscordUserProfileShouldNotBeFound("facebookUrl.equals=" + UPDATED_FACEBOOK_URL);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByFacebookUrlIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where facebookUrl in DEFAULT_FACEBOOK_URL or UPDATED_FACEBOOK_URL
        defaultDiscordUserProfileShouldBeFound("facebookUrl.in=" + DEFAULT_FACEBOOK_URL + "," + UPDATED_FACEBOOK_URL);

        // Get all the discordUserProfileList where facebookUrl equals to UPDATED_FACEBOOK_URL
        defaultDiscordUserProfileShouldNotBeFound("facebookUrl.in=" + UPDATED_FACEBOOK_URL);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByFacebookUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where facebookUrl is not null
        defaultDiscordUserProfileShouldBeFound("facebookUrl.specified=true");

        // Get all the discordUserProfileList where facebookUrl is null
        defaultDiscordUserProfileShouldNotBeFound("facebookUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByHitboxUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where hitboxUrl equals to DEFAULT_HITBOX_URL
        defaultDiscordUserProfileShouldBeFound("hitboxUrl.equals=" + DEFAULT_HITBOX_URL);

        // Get all the discordUserProfileList where hitboxUrl equals to UPDATED_HITBOX_URL
        defaultDiscordUserProfileShouldNotBeFound("hitboxUrl.equals=" + UPDATED_HITBOX_URL);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByHitboxUrlIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where hitboxUrl in DEFAULT_HITBOX_URL or UPDATED_HITBOX_URL
        defaultDiscordUserProfileShouldBeFound("hitboxUrl.in=" + DEFAULT_HITBOX_URL + "," + UPDATED_HITBOX_URL);

        // Get all the discordUserProfileList where hitboxUrl equals to UPDATED_HITBOX_URL
        defaultDiscordUserProfileShouldNotBeFound("hitboxUrl.in=" + UPDATED_HITBOX_URL);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByHitboxUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where hitboxUrl is not null
        defaultDiscordUserProfileShouldBeFound("hitboxUrl.specified=true");

        // Get all the discordUserProfileList where hitboxUrl is null
        defaultDiscordUserProfileShouldNotBeFound("hitboxUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByBeamUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where beamUrl equals to DEFAULT_BEAM_URL
        defaultDiscordUserProfileShouldBeFound("beamUrl.equals=" + DEFAULT_BEAM_URL);

        // Get all the discordUserProfileList where beamUrl equals to UPDATED_BEAM_URL
        defaultDiscordUserProfileShouldNotBeFound("beamUrl.equals=" + UPDATED_BEAM_URL);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByBeamUrlIsInShouldWork() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where beamUrl in DEFAULT_BEAM_URL or UPDATED_BEAM_URL
        defaultDiscordUserProfileShouldBeFound("beamUrl.in=" + DEFAULT_BEAM_URL + "," + UPDATED_BEAM_URL);

        // Get all the discordUserProfileList where beamUrl equals to UPDATED_BEAM_URL
        defaultDiscordUserProfileShouldNotBeFound("beamUrl.in=" + UPDATED_BEAM_URL);
    }

    @Test
    @Transactional
    public void getAllDiscordUserProfilesByBeamUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        discordUserProfileRepository.saveAndFlush(discordUserProfile);

        // Get all the discordUserProfileList where beamUrl is not null
        defaultDiscordUserProfileShouldBeFound("beamUrl.specified=true");

        // Get all the discordUserProfileList where beamUrl is null
        defaultDiscordUserProfileShouldNotBeFound("beamUrl.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDiscordUserProfileShouldBeFound(String filter) throws Exception {
        restDiscordUserProfileMockMvc.perform(get("/api/discord-user-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discordUserProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].favoriteGame").value(hasItem(DEFAULT_FAVORITE_GAME)))
            .andExpect(jsonPath("$.[*].profilePhoto").value(hasItem(DEFAULT_PROFILE_PHOTO)))
            .andExpect(jsonPath("$.[*].twitterUrl").value(hasItem(DEFAULT_TWITTER_URL)))
            .andExpect(jsonPath("$.[*].twitchUrl").value(hasItem(DEFAULT_TWITCH_URL)))
            .andExpect(jsonPath("$.[*].youtubeUrl").value(hasItem(DEFAULT_YOUTUBE_URL)))
            .andExpect(jsonPath("$.[*].facebookUrl").value(hasItem(DEFAULT_FACEBOOK_URL)))
            .andExpect(jsonPath("$.[*].hitboxUrl").value(hasItem(DEFAULT_HITBOX_URL)))
            .andExpect(jsonPath("$.[*].beamUrl").value(hasItem(DEFAULT_BEAM_URL)));

        // Check, that the count call also returns 1
        restDiscordUserProfileMockMvc.perform(get("/api/discord-user-profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDiscordUserProfileShouldNotBeFound(String filter) throws Exception {
        restDiscordUserProfileMockMvc.perform(get("/api/discord-user-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDiscordUserProfileMockMvc.perform(get("/api/discord-user-profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDiscordUserProfile() throws Exception {
        // Get the discordUserProfile
        restDiscordUserProfileMockMvc.perform(get("/api/discord-user-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscordUserProfile() throws Exception {
        // Initialize the database
        discordUserProfileService.save(discordUserProfile);

        int databaseSizeBeforeUpdate = discordUserProfileRepository.findAll().size();

        // Update the discordUserProfile
        DiscordUserProfile updatedDiscordUserProfile = discordUserProfileRepository.findById(discordUserProfile.getId()).get();
        // Disconnect from session so that the updates on updatedDiscordUserProfile are not directly saved in db
        em.detach(updatedDiscordUserProfile);
        updatedDiscordUserProfile
            .favoriteGame(UPDATED_FAVORITE_GAME)
            .profilePhoto(UPDATED_PROFILE_PHOTO)
            .twitterUrl(UPDATED_TWITTER_URL)
            .twitchUrl(UPDATED_TWITCH_URL)
            .youtubeUrl(UPDATED_YOUTUBE_URL)
            .facebookUrl(UPDATED_FACEBOOK_URL)
            .hitboxUrl(UPDATED_HITBOX_URL)
            .beamUrl(UPDATED_BEAM_URL);

        restDiscordUserProfileMockMvc.perform(put("/api/discord-user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDiscordUserProfile)))
            .andExpect(status().isOk());

        // Validate the DiscordUserProfile in the database
        List<DiscordUserProfile> discordUserProfileList = discordUserProfileRepository.findAll();
        assertThat(discordUserProfileList).hasSize(databaseSizeBeforeUpdate);
        DiscordUserProfile testDiscordUserProfile = discordUserProfileList.get(discordUserProfileList.size() - 1);
        assertThat(testDiscordUserProfile.getFavoriteGame()).isEqualTo(UPDATED_FAVORITE_GAME);
        assertThat(testDiscordUserProfile.getProfilePhoto()).isEqualTo(UPDATED_PROFILE_PHOTO);
        assertThat(testDiscordUserProfile.getTwitterUrl()).isEqualTo(UPDATED_TWITTER_URL);
        assertThat(testDiscordUserProfile.getTwitchUrl()).isEqualTo(UPDATED_TWITCH_URL);
        assertThat(testDiscordUserProfile.getYoutubeUrl()).isEqualTo(UPDATED_YOUTUBE_URL);
        assertThat(testDiscordUserProfile.getFacebookUrl()).isEqualTo(UPDATED_FACEBOOK_URL);
        assertThat(testDiscordUserProfile.getHitboxUrl()).isEqualTo(UPDATED_HITBOX_URL);
        assertThat(testDiscordUserProfile.getBeamUrl()).isEqualTo(UPDATED_BEAM_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingDiscordUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = discordUserProfileRepository.findAll().size();

        // Create the DiscordUserProfile

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiscordUserProfileMockMvc.perform(put("/api/discord-user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(discordUserProfile)))
            .andExpect(status().isBadRequest());

        // Validate the DiscordUserProfile in the database
        List<DiscordUserProfile> discordUserProfileList = discordUserProfileRepository.findAll();
        assertThat(discordUserProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDiscordUserProfile() throws Exception {
        // Initialize the database
        discordUserProfileService.save(discordUserProfile);

        int databaseSizeBeforeDelete = discordUserProfileRepository.findAll().size();

        // Delete the discordUserProfile
        restDiscordUserProfileMockMvc.perform(delete("/api/discord-user-profiles/{id}", discordUserProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DiscordUserProfile> discordUserProfileList = discordUserProfileRepository.findAll();
        assertThat(discordUserProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DiscordUserProfile.class);
        DiscordUserProfile discordUserProfile1 = new DiscordUserProfile();
        discordUserProfile1.setId(1L);
        DiscordUserProfile discordUserProfile2 = new DiscordUserProfile();
        discordUserProfile2.setId(discordUserProfile1.getId());
        assertThat(discordUserProfile1).isEqualTo(discordUserProfile2);
        discordUserProfile2.setId(2L);
        assertThat(discordUserProfile1).isNotEqualTo(discordUserProfile2);
        discordUserProfile1.setId(null);
        assertThat(discordUserProfile1).isNotEqualTo(discordUserProfile2);
    }
}
