package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BotApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.GuildServerProfile;
import com.triippztech.guildo.repository.GuildServerProfileRepository;
import com.triippztech.guildo.service.GuildServerProfileService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;
import com.triippztech.guildo.service.dto.GuildServerProfileCriteria;
import com.triippztech.guildo.service.GuildServerProfileQueryService;

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

import com.triippztech.guildo.domain.enumeration.GuildType;
import com.triippztech.guildo.domain.enumeration.GuildPlayStyle;
/**
 * Integration tests for the {@link GuildServerProfileResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = {BotApp.class, TestSecurityConfiguration.class})
public class GuildServerProfileResourceIT {

    private static final GuildType DEFAULT_GUILD_TYPE = GuildType.CASUAL;
    private static final GuildType UPDATED_GUILD_TYPE = GuildType.SEMI_PRO;

    private static final GuildPlayStyle DEFAULT_PLAY_STYLE = GuildPlayStyle.RP;
    private static final GuildPlayStyle UPDATED_PLAY_STYLE = GuildPlayStyle.PVP;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_DISCORD_URL = "AAAAAAAAAA";
    private static final String UPDATED_DISCORD_URL = "BBBBBBBBBB";

    @Autowired
    private GuildServerProfileRepository guildServerProfileRepository;

    @Autowired
    private GuildServerProfileService guildServerProfileService;

    @Autowired
    private GuildServerProfileQueryService guildServerProfileQueryService;

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

    private MockMvc restGuildServerProfileMockMvc;

    private GuildServerProfile guildServerProfile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GuildServerProfileResource guildServerProfileResource = new GuildServerProfileResource(guildServerProfileService, guildServerProfileQueryService);
        this.restGuildServerProfileMockMvc = MockMvcBuilders.standaloneSetup(guildServerProfileResource)
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
    public static GuildServerProfile createEntity(EntityManager em) {
        GuildServerProfile guildServerProfile = new GuildServerProfile()
            .guildType(DEFAULT_GUILD_TYPE)
            .playStyle(DEFAULT_PLAY_STYLE)
            .description(DEFAULT_DESCRIPTION)
            .website(DEFAULT_WEBSITE)
            .discordUrl(DEFAULT_DISCORD_URL);
        return guildServerProfile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GuildServerProfile createUpdatedEntity(EntityManager em) {
        GuildServerProfile guildServerProfile = new GuildServerProfile()
            .guildType(UPDATED_GUILD_TYPE)
            .playStyle(UPDATED_PLAY_STYLE)
            .description(UPDATED_DESCRIPTION)
            .website(UPDATED_WEBSITE)
            .discordUrl(UPDATED_DISCORD_URL);
        return guildServerProfile;
    }

    @BeforeEach
    public void initTest() {
        guildServerProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createGuildServerProfile() throws Exception {
        int databaseSizeBeforeCreate = guildServerProfileRepository.findAll().size();

        // Create the GuildServerProfile
        restGuildServerProfileMockMvc.perform(post("/api/guild-server-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerProfile)))
            .andExpect(status().isCreated());

        // Validate the GuildServerProfile in the database
        List<GuildServerProfile> guildServerProfileList = guildServerProfileRepository.findAll();
        assertThat(guildServerProfileList).hasSize(databaseSizeBeforeCreate + 1);
        GuildServerProfile testGuildServerProfile = guildServerProfileList.get(guildServerProfileList.size() - 1);
        assertThat(testGuildServerProfile.getGuildType()).isEqualTo(DEFAULT_GUILD_TYPE);
        assertThat(testGuildServerProfile.getPlayStyle()).isEqualTo(DEFAULT_PLAY_STYLE);
        assertThat(testGuildServerProfile.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGuildServerProfile.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testGuildServerProfile.getDiscordUrl()).isEqualTo(DEFAULT_DISCORD_URL);
    }

    @Test
    @Transactional
    public void createGuildServerProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = guildServerProfileRepository.findAll().size();

        // Create the GuildServerProfile with an existing ID
        guildServerProfile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuildServerProfileMockMvc.perform(post("/api/guild-server-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerProfile)))
            .andExpect(status().isBadRequest());

        // Validate the GuildServerProfile in the database
        List<GuildServerProfile> guildServerProfileList = guildServerProfileRepository.findAll();
        assertThat(guildServerProfileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllGuildServerProfiles() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList
        restGuildServerProfileMockMvc.perform(get("/api/guild-server-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildServerProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].guildType").value(hasItem(DEFAULT_GUILD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].playStyle").value(hasItem(DEFAULT_PLAY_STYLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].discordUrl").value(hasItem(DEFAULT_DISCORD_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getGuildServerProfile() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get the guildServerProfile
        restGuildServerProfileMockMvc.perform(get("/api/guild-server-profiles/{id}", guildServerProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(guildServerProfile.getId().intValue()))
            .andExpect(jsonPath("$.guildType").value(DEFAULT_GUILD_TYPE.toString()))
            .andExpect(jsonPath("$.playStyle").value(DEFAULT_PLAY_STYLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.discordUrl").value(DEFAULT_DISCORD_URL.toString()));
    }

    @Test
    @Transactional
    public void getAllGuildServerProfilesByGuildTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList where guildType equals to DEFAULT_GUILD_TYPE
        defaultGuildServerProfileShouldBeFound("guildType.equals=" + DEFAULT_GUILD_TYPE);

        // Get all the guildServerProfileList where guildType equals to UPDATED_GUILD_TYPE
        defaultGuildServerProfileShouldNotBeFound("guildType.equals=" + UPDATED_GUILD_TYPE);
    }

    @Test
    @Transactional
    public void getAllGuildServerProfilesByGuildTypeIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList where guildType in DEFAULT_GUILD_TYPE or UPDATED_GUILD_TYPE
        defaultGuildServerProfileShouldBeFound("guildType.in=" + DEFAULT_GUILD_TYPE + "," + UPDATED_GUILD_TYPE);

        // Get all the guildServerProfileList where guildType equals to UPDATED_GUILD_TYPE
        defaultGuildServerProfileShouldNotBeFound("guildType.in=" + UPDATED_GUILD_TYPE);
    }

    @Test
    @Transactional
    public void getAllGuildServerProfilesByGuildTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList where guildType is not null
        defaultGuildServerProfileShouldBeFound("guildType.specified=true");

        // Get all the guildServerProfileList where guildType is null
        defaultGuildServerProfileShouldNotBeFound("guildType.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServerProfilesByPlayStyleIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList where playStyle equals to DEFAULT_PLAY_STYLE
        defaultGuildServerProfileShouldBeFound("playStyle.equals=" + DEFAULT_PLAY_STYLE);

        // Get all the guildServerProfileList where playStyle equals to UPDATED_PLAY_STYLE
        defaultGuildServerProfileShouldNotBeFound("playStyle.equals=" + UPDATED_PLAY_STYLE);
    }

    @Test
    @Transactional
    public void getAllGuildServerProfilesByPlayStyleIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList where playStyle in DEFAULT_PLAY_STYLE or UPDATED_PLAY_STYLE
        defaultGuildServerProfileShouldBeFound("playStyle.in=" + DEFAULT_PLAY_STYLE + "," + UPDATED_PLAY_STYLE);

        // Get all the guildServerProfileList where playStyle equals to UPDATED_PLAY_STYLE
        defaultGuildServerProfileShouldNotBeFound("playStyle.in=" + UPDATED_PLAY_STYLE);
    }

    @Test
    @Transactional
    public void getAllGuildServerProfilesByPlayStyleIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList where playStyle is not null
        defaultGuildServerProfileShouldBeFound("playStyle.specified=true");

        // Get all the guildServerProfileList where playStyle is null
        defaultGuildServerProfileShouldNotBeFound("playStyle.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServerProfilesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList where description equals to DEFAULT_DESCRIPTION
        defaultGuildServerProfileShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the guildServerProfileList where description equals to UPDATED_DESCRIPTION
        defaultGuildServerProfileShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllGuildServerProfilesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultGuildServerProfileShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the guildServerProfileList where description equals to UPDATED_DESCRIPTION
        defaultGuildServerProfileShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllGuildServerProfilesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList where description is not null
        defaultGuildServerProfileShouldBeFound("description.specified=true");

        // Get all the guildServerProfileList where description is null
        defaultGuildServerProfileShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServerProfilesByWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList where website equals to DEFAULT_WEBSITE
        defaultGuildServerProfileShouldBeFound("website.equals=" + DEFAULT_WEBSITE);

        // Get all the guildServerProfileList where website equals to UPDATED_WEBSITE
        defaultGuildServerProfileShouldNotBeFound("website.equals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    public void getAllGuildServerProfilesByWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList where website in DEFAULT_WEBSITE or UPDATED_WEBSITE
        defaultGuildServerProfileShouldBeFound("website.in=" + DEFAULT_WEBSITE + "," + UPDATED_WEBSITE);

        // Get all the guildServerProfileList where website equals to UPDATED_WEBSITE
        defaultGuildServerProfileShouldNotBeFound("website.in=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    public void getAllGuildServerProfilesByWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList where website is not null
        defaultGuildServerProfileShouldBeFound("website.specified=true");

        // Get all the guildServerProfileList where website is null
        defaultGuildServerProfileShouldNotBeFound("website.specified=false");
    }

    @Test
    @Transactional
    public void getAllGuildServerProfilesByDiscordUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList where discordUrl equals to DEFAULT_DISCORD_URL
        defaultGuildServerProfileShouldBeFound("discordUrl.equals=" + DEFAULT_DISCORD_URL);

        // Get all the guildServerProfileList where discordUrl equals to UPDATED_DISCORD_URL
        defaultGuildServerProfileShouldNotBeFound("discordUrl.equals=" + UPDATED_DISCORD_URL);
    }

    @Test
    @Transactional
    public void getAllGuildServerProfilesByDiscordUrlIsInShouldWork() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList where discordUrl in DEFAULT_DISCORD_URL or UPDATED_DISCORD_URL
        defaultGuildServerProfileShouldBeFound("discordUrl.in=" + DEFAULT_DISCORD_URL + "," + UPDATED_DISCORD_URL);

        // Get all the guildServerProfileList where discordUrl equals to UPDATED_DISCORD_URL
        defaultGuildServerProfileShouldNotBeFound("discordUrl.in=" + UPDATED_DISCORD_URL);
    }

    @Test
    @Transactional
    public void getAllGuildServerProfilesByDiscordUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        guildServerProfileRepository.saveAndFlush(guildServerProfile);

        // Get all the guildServerProfileList where discordUrl is not null
        defaultGuildServerProfileShouldBeFound("discordUrl.specified=true");

        // Get all the guildServerProfileList where discordUrl is null
        defaultGuildServerProfileShouldNotBeFound("discordUrl.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGuildServerProfileShouldBeFound(String filter) throws Exception {
        restGuildServerProfileMockMvc.perform(get("/api/guild-server-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guildServerProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].guildType").value(hasItem(DEFAULT_GUILD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].playStyle").value(hasItem(DEFAULT_PLAY_STYLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].discordUrl").value(hasItem(DEFAULT_DISCORD_URL)));

        // Check, that the count call also returns 1
        restGuildServerProfileMockMvc.perform(get("/api/guild-server-profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGuildServerProfileShouldNotBeFound(String filter) throws Exception {
        restGuildServerProfileMockMvc.perform(get("/api/guild-server-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGuildServerProfileMockMvc.perform(get("/api/guild-server-profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGuildServerProfile() throws Exception {
        // Get the guildServerProfile
        restGuildServerProfileMockMvc.perform(get("/api/guild-server-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGuildServerProfile() throws Exception {
        // Initialize the database
        guildServerProfileService.save(guildServerProfile);

        int databaseSizeBeforeUpdate = guildServerProfileRepository.findAll().size();

        // Update the guildServerProfile
        GuildServerProfile updatedGuildServerProfile = guildServerProfileRepository.findById(guildServerProfile.getId()).get();
        // Disconnect from session so that the updates on updatedGuildServerProfile are not directly saved in db
        em.detach(updatedGuildServerProfile);
        updatedGuildServerProfile
            .guildType(UPDATED_GUILD_TYPE)
            .playStyle(UPDATED_PLAY_STYLE)
            .description(UPDATED_DESCRIPTION)
            .website(UPDATED_WEBSITE)
            .discordUrl(UPDATED_DISCORD_URL);

        restGuildServerProfileMockMvc.perform(put("/api/guild-server-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGuildServerProfile)))
            .andExpect(status().isOk());

        // Validate the GuildServerProfile in the database
        List<GuildServerProfile> guildServerProfileList = guildServerProfileRepository.findAll();
        assertThat(guildServerProfileList).hasSize(databaseSizeBeforeUpdate);
        GuildServerProfile testGuildServerProfile = guildServerProfileList.get(guildServerProfileList.size() - 1);
        assertThat(testGuildServerProfile.getGuildType()).isEqualTo(UPDATED_GUILD_TYPE);
        assertThat(testGuildServerProfile.getPlayStyle()).isEqualTo(UPDATED_PLAY_STYLE);
        assertThat(testGuildServerProfile.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGuildServerProfile.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testGuildServerProfile.getDiscordUrl()).isEqualTo(UPDATED_DISCORD_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingGuildServerProfile() throws Exception {
        int databaseSizeBeforeUpdate = guildServerProfileRepository.findAll().size();

        // Create the GuildServerProfile

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuildServerProfileMockMvc.perform(put("/api/guild-server-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(guildServerProfile)))
            .andExpect(status().isBadRequest());

        // Validate the GuildServerProfile in the database
        List<GuildServerProfile> guildServerProfileList = guildServerProfileRepository.findAll();
        assertThat(guildServerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGuildServerProfile() throws Exception {
        // Initialize the database
        guildServerProfileService.save(guildServerProfile);

        int databaseSizeBeforeDelete = guildServerProfileRepository.findAll().size();

        // Delete the guildServerProfile
        restGuildServerProfileMockMvc.perform(delete("/api/guild-server-profiles/{id}", guildServerProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GuildServerProfile> guildServerProfileList = guildServerProfileRepository.findAll();
        assertThat(guildServerProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuildServerProfile.class);
        GuildServerProfile guildServerProfile1 = new GuildServerProfile();
        guildServerProfile1.setId(1L);
        GuildServerProfile guildServerProfile2 = new GuildServerProfile();
        guildServerProfile2.setId(guildServerProfile1.getId());
        assertThat(guildServerProfile1).isEqualTo(guildServerProfile2);
        guildServerProfile2.setId(2L);
        assertThat(guildServerProfile1).isNotEqualTo(guildServerProfile2);
        guildServerProfile1.setId(null);
        assertThat(guildServerProfile1).isNotEqualTo(guildServerProfile2);
    }
}
