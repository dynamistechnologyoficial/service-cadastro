package com.banco.cadastro.br.web.rest;

import static com.banco.cadastro.br.domain.ClassePadraoAsserts.*;
import static com.banco.cadastro.br.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.banco.cadastro.br.IntegrationTest;
import com.banco.cadastro.br.domain.ClassePadrao;
import com.banco.cadastro.br.repository.ClassePadraoRepository;
import com.banco.cadastro.br.service.dto.ClassePadraoDTO;
import com.banco.cadastro.br.service.mapper.ClassePadraoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ClassePadraoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClassePadraoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/classe-padraos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClassePadraoRepository classePadraoRepository;

    @Autowired
    private ClassePadraoMapper classePadraoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassePadraoMockMvc;

    private ClassePadrao classePadrao;

    private ClassePadrao insertedClassePadrao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassePadrao createEntity() {
        return new ClassePadrao().nome(DEFAULT_NOME).descricao(DEFAULT_DESCRICAO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassePadrao createUpdatedEntity() {
        return new ClassePadrao().nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);
    }

    @BeforeEach
    public void initTest() {
        classePadrao = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedClassePadrao != null) {
            classePadraoRepository.delete(insertedClassePadrao);
            insertedClassePadrao = null;
        }
    }

    @Test
    @Transactional
    void createClassePadrao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ClassePadrao
        ClassePadraoDTO classePadraoDTO = classePadraoMapper.toDto(classePadrao);
        var returnedClassePadraoDTO = om.readValue(
            restClassePadraoMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classePadraoDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClassePadraoDTO.class
        );

        // Validate the ClassePadrao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClassePadrao = classePadraoMapper.toEntity(returnedClassePadraoDTO);
        assertClassePadraoUpdatableFieldsEquals(returnedClassePadrao, getPersistedClassePadrao(returnedClassePadrao));

        insertedClassePadrao = returnedClassePadrao;
    }

    @Test
    @Transactional
    void createClassePadraoWithExistingId() throws Exception {
        // Create the ClassePadrao with an existing ID
        classePadrao.setId(1L);
        ClassePadraoDTO classePadraoDTO = classePadraoMapper.toDto(classePadrao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassePadraoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classePadraoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassePadrao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        classePadrao.setNome(null);

        // Create the ClassePadrao, which fails.
        ClassePadraoDTO classePadraoDTO = classePadraoMapper.toDto(classePadrao);

        restClassePadraoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classePadraoDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClassePadraos() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        // Get all the classePadraoList
        restClassePadraoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classePadrao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getClassePadrao() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        // Get the classePadrao
        restClassePadraoMockMvc
            .perform(get(ENTITY_API_URL_ID, classePadrao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classePadrao.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getClassePadraosByIdFiltering() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        Long id = classePadrao.getId();

        defaultClassePadraoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultClassePadraoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultClassePadraoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassePadraosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        // Get all the classePadraoList where nome equals to
        defaultClassePadraoFiltering("nome.equals=" + DEFAULT_NOME, "nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllClassePadraosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        // Get all the classePadraoList where nome in
        defaultClassePadraoFiltering("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME, "nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllClassePadraosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        // Get all the classePadraoList where nome is not null
        defaultClassePadraoFiltering("nome.specified=true", "nome.specified=false");
    }

    @Test
    @Transactional
    void getAllClassePadraosByNomeContainsSomething() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        // Get all the classePadraoList where nome contains
        defaultClassePadraoFiltering("nome.contains=" + DEFAULT_NOME, "nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllClassePadraosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        // Get all the classePadraoList where nome does not contain
        defaultClassePadraoFiltering("nome.doesNotContain=" + UPDATED_NOME, "nome.doesNotContain=" + DEFAULT_NOME);
    }

    @Test
    @Transactional
    void getAllClassePadraosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        // Get all the classePadraoList where descricao equals to
        defaultClassePadraoFiltering("descricao.equals=" + DEFAULT_DESCRICAO, "descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllClassePadraosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        // Get all the classePadraoList where descricao in
        defaultClassePadraoFiltering("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO, "descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllClassePadraosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        // Get all the classePadraoList where descricao is not null
        defaultClassePadraoFiltering("descricao.specified=true", "descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllClassePadraosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        // Get all the classePadraoList where descricao contains
        defaultClassePadraoFiltering("descricao.contains=" + DEFAULT_DESCRICAO, "descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllClassePadraosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        // Get all the classePadraoList where descricao does not contain
        defaultClassePadraoFiltering("descricao.doesNotContain=" + UPDATED_DESCRICAO, "descricao.doesNotContain=" + DEFAULT_DESCRICAO);
    }

    private void defaultClassePadraoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultClassePadraoShouldBeFound(shouldBeFound);
        defaultClassePadraoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassePadraoShouldBeFound(String filter) throws Exception {
        restClassePadraoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classePadrao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));

        // Check, that the count call also returns 1
        restClassePadraoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassePadraoShouldNotBeFound(String filter) throws Exception {
        restClassePadraoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassePadraoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClassePadrao() throws Exception {
        // Get the classePadrao
        restClassePadraoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClassePadrao() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classePadrao
        ClassePadrao updatedClassePadrao = classePadraoRepository.findById(classePadrao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClassePadrao are not directly saved in db
        em.detach(updatedClassePadrao);
        updatedClassePadrao.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);
        ClassePadraoDTO classePadraoDTO = classePadraoMapper.toDto(updatedClassePadrao);

        restClassePadraoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classePadraoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classePadraoDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClassePadrao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClassePadraoToMatchAllProperties(updatedClassePadrao);
    }

    @Test
    @Transactional
    void putNonExistingClassePadrao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classePadrao.setId(longCount.incrementAndGet());

        // Create the ClassePadrao
        ClassePadraoDTO classePadraoDTO = classePadraoMapper.toDto(classePadrao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassePadraoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classePadraoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classePadraoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassePadrao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassePadrao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classePadrao.setId(longCount.incrementAndGet());

        // Create the ClassePadrao
        ClassePadraoDTO classePadraoDTO = classePadraoMapper.toDto(classePadrao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassePadraoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classePadraoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassePadrao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassePadrao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classePadrao.setId(longCount.incrementAndGet());

        // Create the ClassePadrao
        ClassePadraoDTO classePadraoDTO = classePadraoMapper.toDto(classePadrao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassePadraoMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classePadraoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassePadrao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassePadraoWithPatch() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classePadrao using partial update
        ClassePadrao partialUpdatedClassePadrao = new ClassePadrao();
        partialUpdatedClassePadrao.setId(classePadrao.getId());

        partialUpdatedClassePadrao.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);

        restClassePadraoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassePadrao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClassePadrao))
            )
            .andExpect(status().isOk());

        // Validate the ClassePadrao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClassePadraoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClassePadrao, classePadrao),
            getPersistedClassePadrao(classePadrao)
        );
    }

    @Test
    @Transactional
    void fullUpdateClassePadraoWithPatch() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classePadrao using partial update
        ClassePadrao partialUpdatedClassePadrao = new ClassePadrao();
        partialUpdatedClassePadrao.setId(classePadrao.getId());

        partialUpdatedClassePadrao.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO);

        restClassePadraoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassePadrao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClassePadrao))
            )
            .andExpect(status().isOk());

        // Validate the ClassePadrao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClassePadraoUpdatableFieldsEquals(partialUpdatedClassePadrao, getPersistedClassePadrao(partialUpdatedClassePadrao));
    }

    @Test
    @Transactional
    void patchNonExistingClassePadrao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classePadrao.setId(longCount.incrementAndGet());

        // Create the ClassePadrao
        ClassePadraoDTO classePadraoDTO = classePadraoMapper.toDto(classePadrao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassePadraoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classePadraoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(classePadraoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassePadrao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassePadrao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classePadrao.setId(longCount.incrementAndGet());

        // Create the ClassePadrao
        ClassePadraoDTO classePadraoDTO = classePadraoMapper.toDto(classePadrao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassePadraoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(classePadraoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassePadrao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassePadrao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classePadrao.setId(longCount.incrementAndGet());

        // Create the ClassePadrao
        ClassePadraoDTO classePadraoDTO = classePadraoMapper.toDto(classePadrao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassePadraoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(classePadraoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassePadrao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassePadrao() throws Exception {
        // Initialize the database
        insertedClassePadrao = classePadraoRepository.saveAndFlush(classePadrao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the classePadrao
        restClassePadraoMockMvc
            .perform(delete(ENTITY_API_URL_ID, classePadrao.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return classePadraoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ClassePadrao getPersistedClassePadrao(ClassePadrao classePadrao) {
        return classePadraoRepository.findById(classePadrao.getId()).orElseThrow();
    }

    protected void assertPersistedClassePadraoToMatchAllProperties(ClassePadrao expectedClassePadrao) {
        assertClassePadraoAllPropertiesEquals(expectedClassePadrao, getPersistedClassePadrao(expectedClassePadrao));
    }

    protected void assertPersistedClassePadraoToMatchUpdatableProperties(ClassePadrao expectedClassePadrao) {
        assertClassePadraoAllUpdatablePropertiesEquals(expectedClassePadrao, getPersistedClassePadrao(expectedClassePadrao));
    }
}
