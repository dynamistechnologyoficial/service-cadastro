package com.banco.cadastro.br.web.rest;

import static com.banco.cadastro.br.domain.ArqMetadataAsserts.*;
import static com.banco.cadastro.br.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.banco.cadastro.br.IntegrationTest;
import com.banco.cadastro.br.domain.ArqMetadata;
import com.banco.cadastro.br.repository.ArqMetadataRepository;
import com.banco.cadastro.br.service.dto.ArqMetadataDTO;
import com.banco.cadastro.br.service.mapper.ArqMetadataMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Base64;
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
 * Integration tests for the {@link ArqMetadataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArqMetadataResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_OBS = "AAAAAAAAAA";
    private static final String UPDATED_OBS = "BBBBBBBBBB";

    private static final String DEFAULT_ARQUIVO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ARQUIVO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/arq-metadata";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArqMetadataRepository arqMetadataRepository;

    @Autowired
    private ArqMetadataMapper arqMetadataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArqMetadataMockMvc;

    private ArqMetadata arqMetadata;

    private ArqMetadata insertedArqMetadata;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArqMetadata createEntity() {
        return new ArqMetadata()
            .nome(DEFAULT_NOME)
            .obs(DEFAULT_OBS)
            .arquivoContentType(DEFAULT_ARQUIVO_CONTENT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArqMetadata createUpdatedEntity() {
        return new ArqMetadata()
            .nome(UPDATED_NOME)
            .obs(UPDATED_OBS)
            .arquivoContentType(UPDATED_ARQUIVO_CONTENT_TYPE);
    }

    @BeforeEach
    public void initTest() {
        arqMetadata = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedArqMetadata != null) {
            arqMetadataRepository.delete(insertedArqMetadata);
            insertedArqMetadata = null;
        }
    }

    @Test
    @Transactional
    void createArqMetadata() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ArqMetadata
        ArqMetadataDTO arqMetadataDTO = arqMetadataMapper.toDto(arqMetadata);
        var returnedArqMetadataDTO = om.readValue(
            restArqMetadataMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arqMetadataDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArqMetadataDTO.class
        );

        // Validate the ArqMetadata in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedArqMetadata = arqMetadataMapper.toEntity(returnedArqMetadataDTO);
        assertArqMetadataUpdatableFieldsEquals(returnedArqMetadata, getPersistedArqMetadata(returnedArqMetadata));

        insertedArqMetadata = returnedArqMetadata;
    }

    @Test
    @Transactional
    void createArqMetadataWithExistingId() throws Exception {
        // Create the ArqMetadata with an existing ID
        arqMetadata.setId(1L);
        ArqMetadataDTO arqMetadataDTO = arqMetadataMapper.toDto(arqMetadata);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArqMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arqMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArqMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        arqMetadata.setNome(null);

        // Create the ArqMetadata, which fails.
        ArqMetadataDTO arqMetadataDTO = arqMetadataMapper.toDto(arqMetadata);

        restArqMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arqMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArqMetadata() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        // Get all the arqMetadataList
        restArqMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arqMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].obs").value(hasItem(DEFAULT_OBS)))
            .andExpect(jsonPath("$.[*].arquivoContentType").value(hasItem(DEFAULT_ARQUIVO_CONTENT_TYPE)));
    }

    @Test
    @Transactional
    void getArqMetadata() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        // Get the arqMetadata
        restArqMetadataMockMvc
            .perform(get(ENTITY_API_URL_ID, arqMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(arqMetadata.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.obs").value(DEFAULT_OBS))
            .andExpect(jsonPath("$.arquivoContentType").value(DEFAULT_ARQUIVO_CONTENT_TYPE));
    }

    @Test
    @Transactional
    void getArqMetadataByIdFiltering() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        Long id = arqMetadata.getId();

        defaultArqMetadataFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultArqMetadataFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultArqMetadataFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllArqMetadataByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        // Get all the arqMetadataList where nome equals to
        defaultArqMetadataFiltering("nome.equals=" + DEFAULT_NOME, "nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllArqMetadataByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        // Get all the arqMetadataList where nome in
        defaultArqMetadataFiltering("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME, "nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllArqMetadataByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        // Get all the arqMetadataList where nome is not null
        defaultArqMetadataFiltering("nome.specified=true", "nome.specified=false");
    }

    @Test
    @Transactional
    void getAllArqMetadataByNomeContainsSomething() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        // Get all the arqMetadataList where nome contains
        defaultArqMetadataFiltering("nome.contains=" + DEFAULT_NOME, "nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllArqMetadataByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        // Get all the arqMetadataList where nome does not contain
        defaultArqMetadataFiltering("nome.doesNotContain=" + UPDATED_NOME, "nome.doesNotContain=" + DEFAULT_NOME);
    }

    @Test
    @Transactional
    void getAllArqMetadataByObsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        // Get all the arqMetadataList where obs equals to
        defaultArqMetadataFiltering("obs.equals=" + DEFAULT_OBS, "obs.equals=" + UPDATED_OBS);
    }

    @Test
    @Transactional
    void getAllArqMetadataByObsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        // Get all the arqMetadataList where obs in
        defaultArqMetadataFiltering("obs.in=" + DEFAULT_OBS + "," + UPDATED_OBS, "obs.in=" + UPDATED_OBS);
    }

    @Test
    @Transactional
    void getAllArqMetadataByObsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        // Get all the arqMetadataList where obs is not null
        defaultArqMetadataFiltering("obs.specified=true", "obs.specified=false");
    }

    @Test
    @Transactional
    void getAllArqMetadataByObsContainsSomething() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        // Get all the arqMetadataList where obs contains
        defaultArqMetadataFiltering("obs.contains=" + DEFAULT_OBS, "obs.contains=" + UPDATED_OBS);
    }

    @Test
    @Transactional
    void getAllArqMetadataByObsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        // Get all the arqMetadataList where obs does not contain
        defaultArqMetadataFiltering("obs.doesNotContain=" + UPDATED_OBS, "obs.doesNotContain=" + DEFAULT_OBS);
    }

    private void defaultArqMetadataFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultArqMetadataShouldBeFound(shouldBeFound);
        defaultArqMetadataShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArqMetadataShouldBeFound(String filter) throws Exception {
        restArqMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arqMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].obs").value(hasItem(DEFAULT_OBS)))
            .andExpect(jsonPath("$.[*].arquivoContentType").value(hasItem(DEFAULT_ARQUIVO_CONTENT_TYPE)));

        // Check, that the count call also returns 1
        restArqMetadataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArqMetadataShouldNotBeFound(String filter) throws Exception {
        restArqMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArqMetadataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingArqMetadata() throws Exception {
        // Get the arqMetadata
        restArqMetadataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArqMetadata() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the arqMetadata
        ArqMetadata updatedArqMetadata = arqMetadataRepository.findById(arqMetadata.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArqMetadata are not directly saved in db
        em.detach(updatedArqMetadata);
        updatedArqMetadata.nome(UPDATED_NOME).obs(UPDATED_OBS).arquivoContentType(UPDATED_ARQUIVO_CONTENT_TYPE);
        ArqMetadataDTO arqMetadataDTO = arqMetadataMapper.toDto(updatedArqMetadata);

        restArqMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arqMetadataDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(arqMetadataDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArqMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArqMetadataToMatchAllProperties(updatedArqMetadata);
    }

    @Test
    @Transactional
    void putNonExistingArqMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arqMetadata.setId(longCount.incrementAndGet());

        // Create the ArqMetadata
        ArqMetadataDTO arqMetadataDTO = arqMetadataMapper.toDto(arqMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArqMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arqMetadataDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(arqMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArqMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArqMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arqMetadata.setId(longCount.incrementAndGet());

        // Create the ArqMetadata
        ArqMetadataDTO arqMetadataDTO = arqMetadataMapper.toDto(arqMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArqMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(arqMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArqMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArqMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arqMetadata.setId(longCount.incrementAndGet());

        // Create the ArqMetadata
        ArqMetadataDTO arqMetadataDTO = arqMetadataMapper.toDto(arqMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArqMetadataMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arqMetadataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArqMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArqMetadataWithPatch() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the arqMetadata using partial update
        ArqMetadata partialUpdatedArqMetadata = new ArqMetadata();
        partialUpdatedArqMetadata.setId(arqMetadata.getId());

        partialUpdatedArqMetadata
            .nome(UPDATED_NOME)
            .obs(UPDATED_OBS)
            .arquivoContentType(UPDATED_ARQUIVO_CONTENT_TYPE);

        restArqMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArqMetadata.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArqMetadata))
            )
            .andExpect(status().isOk());

        // Validate the ArqMetadata in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArqMetadataUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedArqMetadata, arqMetadata),
            getPersistedArqMetadata(arqMetadata)
        );
    }

    @Test
    @Transactional
    void fullUpdateArqMetadataWithPatch() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the arqMetadata using partial update
        ArqMetadata partialUpdatedArqMetadata = new ArqMetadata();
        partialUpdatedArqMetadata.setId(arqMetadata.getId());

        partialUpdatedArqMetadata
            .nome(UPDATED_NOME)
            .obs(UPDATED_OBS)
            .arquivoContentType(UPDATED_ARQUIVO_CONTENT_TYPE);

        restArqMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArqMetadata.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArqMetadata))
            )
            .andExpect(status().isOk());

        // Validate the ArqMetadata in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArqMetadataUpdatableFieldsEquals(partialUpdatedArqMetadata, getPersistedArqMetadata(partialUpdatedArqMetadata));
    }

    @Test
    @Transactional
    void patchNonExistingArqMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arqMetadata.setId(longCount.incrementAndGet());

        // Create the ArqMetadata
        ArqMetadataDTO arqMetadataDTO = arqMetadataMapper.toDto(arqMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArqMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, arqMetadataDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(arqMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArqMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArqMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arqMetadata.setId(longCount.incrementAndGet());

        // Create the ArqMetadata
        ArqMetadataDTO arqMetadataDTO = arqMetadataMapper.toDto(arqMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArqMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(arqMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArqMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArqMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arqMetadata.setId(longCount.incrementAndGet());

        // Create the ArqMetadata
        ArqMetadataDTO arqMetadataDTO = arqMetadataMapper.toDto(arqMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArqMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(arqMetadataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArqMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArqMetadata() throws Exception {
        // Initialize the database
        insertedArqMetadata = arqMetadataRepository.saveAndFlush(arqMetadata);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the arqMetadata
        restArqMetadataMockMvc
            .perform(delete(ENTITY_API_URL_ID, arqMetadata.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return arqMetadataRepository.count();
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

    protected ArqMetadata getPersistedArqMetadata(ArqMetadata arqMetadata) {
        return arqMetadataRepository.findById(arqMetadata.getId()).orElseThrow();
    }

    protected void assertPersistedArqMetadataToMatchAllProperties(ArqMetadata expectedArqMetadata) {
        assertArqMetadataAllPropertiesEquals(expectedArqMetadata, getPersistedArqMetadata(expectedArqMetadata));
    }

    protected void assertPersistedArqMetadataToMatchUpdatableProperties(ArqMetadata expectedArqMetadata) {
        assertArqMetadataAllUpdatablePropertiesEquals(expectedArqMetadata, getPersistedArqMetadata(expectedArqMetadata));
    }
}
