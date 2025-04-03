package com.banco.cadastro.br.web.rest;

import static com.banco.cadastro.br.domain.ArquivoByteAsserts.*;
import static com.banco.cadastro.br.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.banco.cadastro.br.IntegrationTest;
import com.banco.cadastro.br.domain.ArqMetadata;
import com.banco.cadastro.br.domain.ArquivoByte;
import com.banco.cadastro.br.repository.ArquivoByteRepository;
import com.banco.cadastro.br.service.dto.ArquivoByteDTO;
import com.banco.cadastro.br.service.mapper.ArquivoByteMapper;
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
 * Integration tests for the {@link ArquivoByteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArquivoByteResourceIT {

    private static final byte[] DEFAULT_ARQUIVO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ARQUIVO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ARQUIVO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ARQUIVO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/arquivo-bytes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArquivoByteRepository arquivoByteRepository;

    @Autowired
    private ArquivoByteMapper arquivoByteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArquivoByteMockMvc;

    private ArquivoByte arquivoByte;

    private ArquivoByte insertedArquivoByte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArquivoByte createEntity(EntityManager em) {
        ArquivoByte arquivoByte = new ArquivoByte().arquivo(DEFAULT_ARQUIVO);
        // Add required entity
        ArqMetadata arqMetadata;
        if (TestUtil.findAll(em, ArqMetadata.class).isEmpty()) {
            arqMetadata = ArqMetadataResourceIT.createEntity();
            em.persist(arqMetadata);
            em.flush();
        } else {
            arqMetadata = TestUtil.findAll(em, ArqMetadata.class).get(0);
        }
        arquivoByte.setArqMetadata(arqMetadata);
        return arquivoByte;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArquivoByte createUpdatedEntity(EntityManager em) {
        ArquivoByte updatedArquivoByte = new ArquivoByte().arquivo(UPDATED_ARQUIVO);
        // Add required entity
        ArqMetadata arqMetadata;
        if (TestUtil.findAll(em, ArqMetadata.class).isEmpty()) {
            arqMetadata = ArqMetadataResourceIT.createUpdatedEntity();
            em.persist(arqMetadata);
            em.flush();
        } else {
            arqMetadata = TestUtil.findAll(em, ArqMetadata.class).get(0);
        }
        updatedArquivoByte.setArqMetadata(arqMetadata);
        return updatedArquivoByte;
    }

    @BeforeEach
    public void initTest() {
        arquivoByte = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedArquivoByte != null) {
            arquivoByteRepository.delete(insertedArquivoByte);
            insertedArquivoByte = null;
        }
    }

    @Test
    @Transactional
    void createArquivoByte() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ArquivoByte
        ArquivoByteDTO arquivoByteDTO = arquivoByteMapper.toDto(arquivoByte);
        var returnedArquivoByteDTO = om.readValue(
            restArquivoByteMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arquivoByteDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArquivoByteDTO.class
        );

        // Validate the ArquivoByte in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedArquivoByte = arquivoByteMapper.toEntity(returnedArquivoByteDTO);
        assertArquivoByteUpdatableFieldsEquals(returnedArquivoByte, getPersistedArquivoByte(returnedArquivoByte));

        insertedArquivoByte = returnedArquivoByte;
    }

    @Test
    @Transactional
    void createArquivoByteWithExistingId() throws Exception {
        // Create the ArquivoByte with an existing ID
        arquivoByte.setId(1L);
        ArquivoByteDTO arquivoByteDTO = arquivoByteMapper.toDto(arquivoByte);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArquivoByteMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arquivoByteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArquivoByte in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllArquivoBytes() throws Exception {
        // Initialize the database
        insertedArquivoByte = arquivoByteRepository.saveAndFlush(arquivoByte);

        // Get all the arquivoByteList
        restArquivoByteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arquivoByte.getId().intValue())))
            .andExpect(jsonPath("$.[*].arquivoContentType").value(hasItem(DEFAULT_ARQUIVO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].arquivo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_ARQUIVO))));
    }

    @Test
    @Transactional
    void getArquivoByte() throws Exception {
        // Initialize the database
        insertedArquivoByte = arquivoByteRepository.saveAndFlush(arquivoByte);

        // Get the arquivoByte
        restArquivoByteMockMvc
            .perform(get(ENTITY_API_URL_ID, arquivoByte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(arquivoByte.getId().intValue()))
            .andExpect(jsonPath("$.arquivoContentType").value(DEFAULT_ARQUIVO_CONTENT_TYPE))
            .andExpect(jsonPath("$.arquivo").value(Base64.getEncoder().encodeToString(DEFAULT_ARQUIVO)));
    }

    @Test
    @Transactional
    void getArquivoBytesByIdFiltering() throws Exception {
        // Initialize the database
        insertedArquivoByte = arquivoByteRepository.saveAndFlush(arquivoByte);

        Long id = arquivoByte.getId();

        defaultArquivoByteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultArquivoByteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultArquivoByteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllArquivoBytesByArqMetadataIsEqualToSomething() throws Exception {
        // Get already existing entity
        ArqMetadata arqMetadata = arquivoByte.getArqMetadata();
        arquivoByteRepository.saveAndFlush(arquivoByte);
        Long arqMetadataId = arqMetadata.getId();
        // Get all the arquivoByteList where arqMetadata equals to arqMetadataId
        defaultArquivoByteShouldBeFound("arqMetadataId.equals=" + arqMetadataId);

        // Get all the arquivoByteList where arqMetadata equals to (arqMetadataId + 1)
        defaultArquivoByteShouldNotBeFound("arqMetadataId.equals=" + (arqMetadataId + 1));
    }

    private void defaultArquivoByteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultArquivoByteShouldBeFound(shouldBeFound);
        defaultArquivoByteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArquivoByteShouldBeFound(String filter) throws Exception {
        restArquivoByteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arquivoByte.getId().intValue())))
            .andExpect(jsonPath("$.[*].arquivoContentType").value(hasItem(DEFAULT_ARQUIVO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].arquivo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_ARQUIVO))));

        // Check, that the count call also returns 1
        restArquivoByteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArquivoByteShouldNotBeFound(String filter) throws Exception {
        restArquivoByteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArquivoByteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingArquivoByte() throws Exception {
        // Get the arquivoByte
        restArquivoByteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArquivoByte() throws Exception {
        // Initialize the database
        insertedArquivoByte = arquivoByteRepository.saveAndFlush(arquivoByte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the arquivoByte
        ArquivoByte updatedArquivoByte = arquivoByteRepository.findById(arquivoByte.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArquivoByte are not directly saved in db
        em.detach(updatedArquivoByte);
        updatedArquivoByte.arquivo(UPDATED_ARQUIVO);
        ArquivoByteDTO arquivoByteDTO = arquivoByteMapper.toDto(updatedArquivoByte);

        restArquivoByteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arquivoByteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(arquivoByteDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArquivoByte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArquivoByteToMatchAllProperties(updatedArquivoByte);
    }

    @Test
    @Transactional
    void putNonExistingArquivoByte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arquivoByte.setId(longCount.incrementAndGet());

        // Create the ArquivoByte
        ArquivoByteDTO arquivoByteDTO = arquivoByteMapper.toDto(arquivoByte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArquivoByteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arquivoByteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(arquivoByteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArquivoByte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArquivoByte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arquivoByte.setId(longCount.incrementAndGet());

        // Create the ArquivoByte
        ArquivoByteDTO arquivoByteDTO = arquivoByteMapper.toDto(arquivoByte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoByteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(arquivoByteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArquivoByte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArquivoByte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arquivoByte.setId(longCount.incrementAndGet());

        // Create the ArquivoByte
        ArquivoByteDTO arquivoByteDTO = arquivoByteMapper.toDto(arquivoByte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoByteMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arquivoByteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArquivoByte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArquivoByteWithPatch() throws Exception {
        // Initialize the database
        insertedArquivoByte = arquivoByteRepository.saveAndFlush(arquivoByte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the arquivoByte using partial update
        ArquivoByte partialUpdatedArquivoByte = new ArquivoByte();
        partialUpdatedArquivoByte.setId(arquivoByte.getId());

        partialUpdatedArquivoByte.arquivo(UPDATED_ARQUIVO);

        restArquivoByteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArquivoByte.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArquivoByte))
            )
            .andExpect(status().isOk());

        // Validate the ArquivoByte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArquivoByteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedArquivoByte, arquivoByte),
            getPersistedArquivoByte(arquivoByte)
        );
    }

    @Test
    @Transactional
    void fullUpdateArquivoByteWithPatch() throws Exception {
        // Initialize the database
        insertedArquivoByte = arquivoByteRepository.saveAndFlush(arquivoByte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the arquivoByte using partial update
        ArquivoByte partialUpdatedArquivoByte = new ArquivoByte();
        partialUpdatedArquivoByte.setId(arquivoByte.getId());

        partialUpdatedArquivoByte.arquivo(UPDATED_ARQUIVO);

        restArquivoByteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArquivoByte.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArquivoByte))
            )
            .andExpect(status().isOk());

        // Validate the ArquivoByte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArquivoByteUpdatableFieldsEquals(partialUpdatedArquivoByte, getPersistedArquivoByte(partialUpdatedArquivoByte));
    }

    @Test
    @Transactional
    void patchNonExistingArquivoByte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arquivoByte.setId(longCount.incrementAndGet());

        // Create the ArquivoByte
        ArquivoByteDTO arquivoByteDTO = arquivoByteMapper.toDto(arquivoByte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArquivoByteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, arquivoByteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(arquivoByteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArquivoByte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArquivoByte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arquivoByte.setId(longCount.incrementAndGet());

        // Create the ArquivoByte
        ArquivoByteDTO arquivoByteDTO = arquivoByteMapper.toDto(arquivoByte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoByteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(arquivoByteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArquivoByte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArquivoByte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arquivoByte.setId(longCount.incrementAndGet());

        // Create the ArquivoByte
        ArquivoByteDTO arquivoByteDTO = arquivoByteMapper.toDto(arquivoByte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArquivoByteMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(arquivoByteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArquivoByte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArquivoByte() throws Exception {
        // Initialize the database
        insertedArquivoByte = arquivoByteRepository.saveAndFlush(arquivoByte);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the arquivoByte
        restArquivoByteMockMvc
            .perform(delete(ENTITY_API_URL_ID, arquivoByte.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return arquivoByteRepository.count();
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

    protected ArquivoByte getPersistedArquivoByte(ArquivoByte arquivoByte) {
        return arquivoByteRepository.findById(arquivoByte.getId()).orElseThrow();
    }

    protected void assertPersistedArquivoByteToMatchAllProperties(ArquivoByte expectedArquivoByte) {
        assertArquivoByteAllPropertiesEquals(expectedArquivoByte, getPersistedArquivoByte(expectedArquivoByte));
    }

    protected void assertPersistedArquivoByteToMatchUpdatableProperties(ArquivoByte expectedArquivoByte) {
        assertArquivoByteAllUpdatablePropertiesEquals(expectedArquivoByte, getPersistedArquivoByte(expectedArquivoByte));
    }
}
