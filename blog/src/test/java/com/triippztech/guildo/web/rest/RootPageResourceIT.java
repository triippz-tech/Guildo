package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.BlogApp;
import com.triippztech.guildo.config.TestSecurityConfiguration;
import com.triippztech.guildo.domain.RootPage;
import com.triippztech.guildo.repository.RootPageRepository;
import com.triippztech.guildo.service.RootPageService;
import com.triippztech.guildo.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
 * Integration tests for the {@link RootPageResource} REST controller.
 */
@SpringBootTest(classes = {BlogApp.class, TestSecurityConfiguration.class})
public class RootPageResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    @Autowired
    private RootPageRepository rootPageRepository;

    @Autowired
    private RootPageService rootPageService;

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

    private MockMvc restRootPageMockMvc;

    private RootPage rootPage;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RootPageResource rootPageResource = new RootPageResource(rootPageService);
        this.restRootPageMockMvc = MockMvcBuilders.standaloneSetup(rootPageResource)
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
    public static RootPage createEntity(EntityManager em) {
        RootPage rootPage = new RootPage()
            .title(DEFAULT_TITLE)
            .slug(DEFAULT_SLUG);
        return rootPage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RootPage createUpdatedEntity(EntityManager em) {
        RootPage rootPage = new RootPage()
            .title(UPDATED_TITLE)
            .slug(UPDATED_SLUG);
        return rootPage;
    }

    @BeforeEach
    public void initTest() {
        rootPage = createEntity(em);
    }

    @Test
    @Transactional
    public void createRootPage() throws Exception {
        int databaseSizeBeforeCreate = rootPageRepository.findAll().size();

        // Create the RootPage
        restRootPageMockMvc.perform(post("/api/root-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rootPage)))
            .andExpect(status().isCreated());

        // Validate the RootPage in the database
        List<RootPage> rootPageList = rootPageRepository.findAll();
        assertThat(rootPageList).hasSize(databaseSizeBeforeCreate + 1);
        RootPage testRootPage = rootPageList.get(rootPageList.size() - 1);
        assertThat(testRootPage.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRootPage.getSlug()).isEqualTo(DEFAULT_SLUG);
    }

    @Test
    @Transactional
    public void createRootPageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rootPageRepository.findAll().size();

        // Create the RootPage with an existing ID
        rootPage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRootPageMockMvc.perform(post("/api/root-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rootPage)))
            .andExpect(status().isBadRequest());

        // Validate the RootPage in the database
        List<RootPage> rootPageList = rootPageRepository.findAll();
        assertThat(rootPageList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = rootPageRepository.findAll().size();
        // set the field null
        rootPage.setTitle(null);

        // Create the RootPage, which fails.

        restRootPageMockMvc.perform(post("/api/root-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rootPage)))
            .andExpect(status().isBadRequest());

        List<RootPage> rootPageList = rootPageRepository.findAll();
        assertThat(rootPageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = rootPageRepository.findAll().size();
        // set the field null
        rootPage.setSlug(null);

        // Create the RootPage, which fails.

        restRootPageMockMvc.perform(post("/api/root-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rootPage)))
            .andExpect(status().isBadRequest());

        List<RootPage> rootPageList = rootPageRepository.findAll();
        assertThat(rootPageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRootPages() throws Exception {
        // Initialize the database
        rootPageRepository.saveAndFlush(rootPage);

        // Get all the rootPageList
        restRootPageMockMvc.perform(get("/api/root-pages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rootPage.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())));
    }
    
    @Test
    @Transactional
    public void getRootPage() throws Exception {
        // Initialize the database
        rootPageRepository.saveAndFlush(rootPage);

        // Get the rootPage
        restRootPageMockMvc.perform(get("/api/root-pages/{id}", rootPage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rootPage.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRootPage() throws Exception {
        // Get the rootPage
        restRootPageMockMvc.perform(get("/api/root-pages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRootPage() throws Exception {
        // Initialize the database
        rootPageService.save(rootPage);

        int databaseSizeBeforeUpdate = rootPageRepository.findAll().size();

        // Update the rootPage
        RootPage updatedRootPage = rootPageRepository.findById(rootPage.getId()).get();
        // Disconnect from session so that the updates on updatedRootPage are not directly saved in db
        em.detach(updatedRootPage);
        updatedRootPage
            .title(UPDATED_TITLE)
            .slug(UPDATED_SLUG);

        restRootPageMockMvc.perform(put("/api/root-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRootPage)))
            .andExpect(status().isOk());

        // Validate the RootPage in the database
        List<RootPage> rootPageList = rootPageRepository.findAll();
        assertThat(rootPageList).hasSize(databaseSizeBeforeUpdate);
        RootPage testRootPage = rootPageList.get(rootPageList.size() - 1);
        assertThat(testRootPage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRootPage.getSlug()).isEqualTo(UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void updateNonExistingRootPage() throws Exception {
        int databaseSizeBeforeUpdate = rootPageRepository.findAll().size();

        // Create the RootPage

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRootPageMockMvc.perform(put("/api/root-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rootPage)))
            .andExpect(status().isBadRequest());

        // Validate the RootPage in the database
        List<RootPage> rootPageList = rootPageRepository.findAll();
        assertThat(rootPageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRootPage() throws Exception {
        // Initialize the database
        rootPageService.save(rootPage);

        int databaseSizeBeforeDelete = rootPageRepository.findAll().size();

        // Delete the rootPage
        restRootPageMockMvc.perform(delete("/api/root-pages/{id}", rootPage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RootPage> rootPageList = rootPageRepository.findAll();
        assertThat(rootPageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RootPage.class);
        RootPage rootPage1 = new RootPage();
        rootPage1.setId(1L);
        RootPage rootPage2 = new RootPage();
        rootPage2.setId(rootPage1.getId());
        assertThat(rootPage1).isEqualTo(rootPage2);
        rootPage2.setId(2L);
        assertThat(rootPage1).isNotEqualTo(rootPage2);
        rootPage1.setId(null);
        assertThat(rootPage1).isNotEqualTo(rootPage2);
    }
}
