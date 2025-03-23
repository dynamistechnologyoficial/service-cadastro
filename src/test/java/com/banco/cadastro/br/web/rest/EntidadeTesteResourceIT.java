package com.banco.cadastro.br.web.rest;

import static com.banco.cadastro.br.domain.EntidadeTesteAsserts.*;
import static com.banco.cadastro.br.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.banco.cadastro.br.IntegrationTest;
import com.banco.cadastro.br.domain.EntidadeTeste;
import com.banco.cadastro.br.repository.EntidadeTesteRepository;
import com.banco.cadastro.br.service.dto.EntidadeTesteDTO;
import com.banco.cadastro.br.service.mapper.EntidadeTesteMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link EntidadeTesteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EntidadeTesteResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DTNASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DTNASCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DTNASCIMENTO = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ATIVOOO = false;
    private static final Boolean UPDATED_ATIVOOO = true;

    private static final String ENTITY_API_URL = "/api/entidade-testes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntidadeTesteRepository entidadeTesteRepository;

    @Autowired
    private EntidadeTesteMapper entidadeTesteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEntidadeTesteMockMvc;

    private EntidadeTeste entidadeTeste;

    private EntidadeTeste insertedEntidadeTeste;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntidadeTeste createEntity() {
        return new EntidadeTeste().nome(DEFAULT_NOME).dtnascimento(DEFAULT_DTNASCIMENTO).ativooo(DEFAULT_ATIVOOO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntidadeTeste createUpdatedEntity() {
        return new EntidadeTeste().nome(UPDATED_NOME).dtnascimento(UPDATED_DTNASCIMENTO).ativooo(UPDATED_ATIVOOO);
    }

    @BeforeEach
    public void initTest() {
        entidadeTeste = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEntidadeTeste != null) {
            entidadeTesteRepository.delete(insertedEntidadeTeste);
            insertedEntidadeTeste = null;
        }
    }

    @Test
    @Transactional
    void createEntidadeTeste() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EntidadeTeste
        EntidadeTesteDTO entidadeTesteDTO = entidadeTesteMapper.toDto(entidadeTeste);
        var returnedEntidadeTesteDTO = om.readValue(
            restEntidadeTesteMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(entidadeTesteDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EntidadeTesteDTO.class
        );

        // Validate the EntidadeTeste in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEntidadeTeste = entidadeTesteMapper.toEntity(returnedEntidadeTesteDTO);
        assertEntidadeTesteUpdatableFieldsEquals(returnedEntidadeTeste, getPersistedEntidadeTeste(returnedEntidadeTeste));

        insertedEntidadeTeste = returnedEntidadeTeste;
    }

    @Test
    @Transactional
    void createEntidadeTesteWithExistingId() throws Exception {
        // Create the EntidadeTeste with an existing ID
        entidadeTeste.setId(1L);
        EntidadeTesteDTO entidadeTesteDTO = entidadeTesteMapper.toDto(entidadeTeste);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntidadeTesteMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entidadeTesteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntidadeTeste in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEntidadeTestes() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList
        restEntidadeTesteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entidadeTeste.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].dtnascimento").value(hasItem(DEFAULT_DTNASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].ativooo").value(hasItem(DEFAULT_ATIVOOO)));
    }

    @Test
    @Transactional
    void getEntidadeTeste() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get the entidadeTeste
        restEntidadeTesteMockMvc
            .perform(get(ENTITY_API_URL_ID, entidadeTeste.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entidadeTeste.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.dtnascimento").value(DEFAULT_DTNASCIMENTO.toString()))
            .andExpect(jsonPath("$.ativooo").value(DEFAULT_ATIVOOO));
    }

    @Test
    @Transactional
    void getEntidadeTestesByIdFiltering() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        Long id = entidadeTeste.getId();

        defaultEntidadeTesteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEntidadeTesteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEntidadeTesteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEntidadeTestesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList where nome equals to
        defaultEntidadeTesteFiltering("nome.equals=" + DEFAULT_NOME, "nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEntidadeTestesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList where nome in
        defaultEntidadeTesteFiltering("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME, "nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEntidadeTestesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList where nome is not null
        defaultEntidadeTesteFiltering("nome.specified=true", "nome.specified=false");
    }

    @Test
    @Transactional
    void getAllEntidadeTestesByNomeContainsSomething() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList where nome contains
        defaultEntidadeTesteFiltering("nome.contains=" + DEFAULT_NOME, "nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEntidadeTestesByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList where nome does not contain
        defaultEntidadeTesteFiltering("nome.doesNotContain=" + UPDATED_NOME, "nome.doesNotContain=" + DEFAULT_NOME);
    }

    @Test
    @Transactional
    void getAllEntidadeTestesByDtnascimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList where dtnascimento equals to
        defaultEntidadeTesteFiltering("dtnascimento.equals=" + DEFAULT_DTNASCIMENTO, "dtnascimento.equals=" + UPDATED_DTNASCIMENTO);
    }

    @Test
    @Transactional
    void getAllEntidadeTestesByDtnascimentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList where dtnascimento in
        defaultEntidadeTesteFiltering(
            "dtnascimento.in=" + DEFAULT_DTNASCIMENTO + "," + UPDATED_DTNASCIMENTO,
            "dtnascimento.in=" + UPDATED_DTNASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllEntidadeTestesByDtnascimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList where dtnascimento is not null
        defaultEntidadeTesteFiltering("dtnascimento.specified=true", "dtnascimento.specified=false");
    }

    @Test
    @Transactional
    void getAllEntidadeTestesByDtnascimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList where dtnascimento is greater than or equal to
        defaultEntidadeTesteFiltering(
            "dtnascimento.greaterThanOrEqual=" + DEFAULT_DTNASCIMENTO,
            "dtnascimento.greaterThanOrEqual=" + UPDATED_DTNASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllEntidadeTestesByDtnascimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList where dtnascimento is less than or equal to
        defaultEntidadeTesteFiltering(
            "dtnascimento.lessThanOrEqual=" + DEFAULT_DTNASCIMENTO,
            "dtnascimento.lessThanOrEqual=" + SMALLER_DTNASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllEntidadeTestesByDtnascimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList where dtnascimento is less than
        defaultEntidadeTesteFiltering("dtnascimento.lessThan=" + UPDATED_DTNASCIMENTO, "dtnascimento.lessThan=" + DEFAULT_DTNASCIMENTO);
    }

    @Test
    @Transactional
    void getAllEntidadeTestesByDtnascimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList where dtnascimento is greater than
        defaultEntidadeTesteFiltering(
            "dtnascimento.greaterThan=" + SMALLER_DTNASCIMENTO,
            "dtnascimento.greaterThan=" + DEFAULT_DTNASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllEntidadeTestesByAtivoooIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList where ativooo equals to
        defaultEntidadeTesteFiltering("ativooo.equals=" + DEFAULT_ATIVOOO, "ativooo.equals=" + UPDATED_ATIVOOO);
    }

    @Test
    @Transactional
    void getAllEntidadeTestesByAtivoooIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList where ativooo in
        defaultEntidadeTesteFiltering("ativooo.in=" + DEFAULT_ATIVOOO + "," + UPDATED_ATIVOOO, "ativooo.in=" + UPDATED_ATIVOOO);
    }

    @Test
    @Transactional
    void getAllEntidadeTestesByAtivoooIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        // Get all the entidadeTesteList where ativooo is not null
        defaultEntidadeTesteFiltering("ativooo.specified=true", "ativooo.specified=false");
    }

    private void defaultEntidadeTesteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEntidadeTesteShouldBeFound(shouldBeFound);
        defaultEntidadeTesteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEntidadeTesteShouldBeFound(String filter) throws Exception {
        restEntidadeTesteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entidadeTeste.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].dtnascimento").value(hasItem(DEFAULT_DTNASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].ativooo").value(hasItem(DEFAULT_ATIVOOO)));

        // Check, that the count call also returns 1
        restEntidadeTesteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEntidadeTesteShouldNotBeFound(String filter) throws Exception {
        restEntidadeTesteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEntidadeTesteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEntidadeTeste() throws Exception {
        // Get the entidadeTeste
        restEntidadeTesteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEntidadeTeste() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entidadeTeste
        EntidadeTeste updatedEntidadeTeste = entidadeTesteRepository.findById(entidadeTeste.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEntidadeTeste are not directly saved in db
        em.detach(updatedEntidadeTeste);
        updatedEntidadeTeste.nome(UPDATED_NOME).dtnascimento(UPDATED_DTNASCIMENTO).ativooo(UPDATED_ATIVOOO);
        EntidadeTesteDTO entidadeTesteDTO = entidadeTesteMapper.toDto(updatedEntidadeTeste);

        restEntidadeTesteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entidadeTesteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(entidadeTesteDTO))
            )
            .andExpect(status().isOk());

        // Validate the EntidadeTeste in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEntidadeTesteToMatchAllProperties(updatedEntidadeTeste);
    }

    @Test
    @Transactional
    void putNonExistingEntidadeTeste() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entidadeTeste.setId(longCount.incrementAndGet());

        // Create the EntidadeTeste
        EntidadeTesteDTO entidadeTesteDTO = entidadeTesteMapper.toDto(entidadeTeste);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntidadeTesteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entidadeTesteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(entidadeTesteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntidadeTeste in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEntidadeTeste() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entidadeTeste.setId(longCount.incrementAndGet());

        // Create the EntidadeTeste
        EntidadeTesteDTO entidadeTesteDTO = entidadeTesteMapper.toDto(entidadeTeste);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntidadeTesteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(entidadeTesteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntidadeTeste in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEntidadeTeste() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entidadeTeste.setId(longCount.incrementAndGet());

        // Create the EntidadeTeste
        EntidadeTesteDTO entidadeTesteDTO = entidadeTesteMapper.toDto(entidadeTeste);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntidadeTesteMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entidadeTesteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EntidadeTeste in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEntidadeTesteWithPatch() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entidadeTeste using partial update
        EntidadeTeste partialUpdatedEntidadeTeste = new EntidadeTeste();
        partialUpdatedEntidadeTeste.setId(entidadeTeste.getId());

        partialUpdatedEntidadeTeste.dtnascimento(UPDATED_DTNASCIMENTO).ativooo(UPDATED_ATIVOOO);

        restEntidadeTesteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntidadeTeste.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEntidadeTeste))
            )
            .andExpect(status().isOk());

        // Validate the EntidadeTeste in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntidadeTesteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEntidadeTeste, entidadeTeste),
            getPersistedEntidadeTeste(entidadeTeste)
        );
    }

    @Test
    @Transactional
    void fullUpdateEntidadeTesteWithPatch() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entidadeTeste using partial update
        EntidadeTeste partialUpdatedEntidadeTeste = new EntidadeTeste();
        partialUpdatedEntidadeTeste.setId(entidadeTeste.getId());

        partialUpdatedEntidadeTeste.nome(UPDATED_NOME).dtnascimento(UPDATED_DTNASCIMENTO).ativooo(UPDATED_ATIVOOO);

        restEntidadeTesteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntidadeTeste.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEntidadeTeste))
            )
            .andExpect(status().isOk());

        // Validate the EntidadeTeste in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntidadeTesteUpdatableFieldsEquals(partialUpdatedEntidadeTeste, getPersistedEntidadeTeste(partialUpdatedEntidadeTeste));
    }

    @Test
    @Transactional
    void patchNonExistingEntidadeTeste() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entidadeTeste.setId(longCount.incrementAndGet());

        // Create the EntidadeTeste
        EntidadeTesteDTO entidadeTesteDTO = entidadeTesteMapper.toDto(entidadeTeste);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntidadeTesteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, entidadeTesteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(entidadeTesteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntidadeTeste in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEntidadeTeste() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entidadeTeste.setId(longCount.incrementAndGet());

        // Create the EntidadeTeste
        EntidadeTesteDTO entidadeTesteDTO = entidadeTesteMapper.toDto(entidadeTeste);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntidadeTesteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(entidadeTesteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntidadeTeste in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEntidadeTeste() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entidadeTeste.setId(longCount.incrementAndGet());

        // Create the EntidadeTeste
        EntidadeTesteDTO entidadeTesteDTO = entidadeTesteMapper.toDto(entidadeTeste);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntidadeTesteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(entidadeTesteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EntidadeTeste in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEntidadeTeste() throws Exception {
        // Initialize the database
        insertedEntidadeTeste = entidadeTesteRepository.saveAndFlush(entidadeTeste);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the entidadeTeste
        restEntidadeTesteMockMvc
            .perform(delete(ENTITY_API_URL_ID, entidadeTeste.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return entidadeTesteRepository.count();
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

    protected EntidadeTeste getPersistedEntidadeTeste(EntidadeTeste entidadeTeste) {
        return entidadeTesteRepository.findById(entidadeTeste.getId()).orElseThrow();
    }

    protected void assertPersistedEntidadeTesteToMatchAllProperties(EntidadeTeste expectedEntidadeTeste) {
        assertEntidadeTesteAllPropertiesEquals(expectedEntidadeTeste, getPersistedEntidadeTeste(expectedEntidadeTeste));
    }

    protected void assertPersistedEntidadeTesteToMatchUpdatableProperties(EntidadeTeste expectedEntidadeTeste) {
        assertEntidadeTesteAllUpdatablePropertiesEquals(expectedEntidadeTeste, getPersistedEntidadeTeste(expectedEntidadeTeste));
    }
}
