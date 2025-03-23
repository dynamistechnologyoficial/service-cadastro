package com.banco.cadastro.br.web.rest;

import static com.banco.cadastro.br.domain.DadoPadraoAsserts.*;
import static com.banco.cadastro.br.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.banco.cadastro.br.IntegrationTest;
import com.banco.cadastro.br.domain.ClassePadrao;
import com.banco.cadastro.br.domain.DadoPadrao;
import com.banco.cadastro.br.domain.DadoPadraoPK;
import com.banco.cadastro.br.repository.DadoPadraoRepository;
import com.banco.cadastro.br.service.DadoPadraoService;
import com.banco.cadastro.br.service.dto.DadoPadraoDTO;
import com.banco.cadastro.br.service.mapper.DadoPadraoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DadoPadraoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DadoPadraoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/dado-padraos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{idDado}/{classePadraoId}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DadoPadraoRepository dadoPadraoRepository;

    @Mock
    private DadoPadraoRepository dadoPadraoRepositoryMock;

    @Autowired
    private DadoPadraoMapper dadoPadraoMapper;

    @Mock
    private DadoPadraoService dadoPadraoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDadoPadraoMockMvc;

    private DadoPadrao dadoPadrao;

    private DadoPadrao insertedDadoPadrao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DadoPadrao createEntity(EntityManager em) {
        DadoPadrao dadoPadrao = new DadoPadrao().nome(DEFAULT_NOME).descricao(DEFAULT_DESCRICAO).ativo(DEFAULT_ATIVO);
        // Add required entity
        ClassePadrao classePadrao;
        if (TestUtil.findAll(em, ClassePadrao.class).isEmpty()) {
            classePadrao = ClassePadraoResourceIT.createEntity();
            em.persist(classePadrao);
            em.flush();
        } else {
            classePadrao = TestUtil.findAll(em, ClassePadrao.class).get(0);
        }
        dadoPadrao.setClassePadrao(classePadrao);
        dadoPadrao.setDadoPadraoDependencia(dadoPadrao);;
        dadoPadrao.id(new DadoPadraoPK(11L,dadoPadrao.getClassePadrao().getId()));
        return dadoPadrao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DadoPadrao createUpdatedEntity(EntityManager em) {
        DadoPadrao updatedDadoPadrao = new DadoPadrao().nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO);
        // Add required entity
        ClassePadrao classePadrao;
        if (TestUtil.findAll(em, ClassePadrao.class).isEmpty()) {
            classePadrao = ClassePadraoResourceIT.createUpdatedEntity();
            em.persist(classePadrao);
            em.flush();
        } else {
            classePadrao = TestUtil.findAll(em, ClassePadrao.class).get(0);
        }
        updatedDadoPadrao.setClassePadrao(classePadrao);
        return updatedDadoPadrao;
    }

    @BeforeEach
    public void initTest() {
        dadoPadrao = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDadoPadrao != null) {
            dadoPadraoRepository.delete(insertedDadoPadrao);
            insertedDadoPadrao = null;
        }
    }

    @Test
    @Transactional
    void createDadoPadrao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DadoPadrao
        DadoPadraoDTO dadoPadraoDTO = dadoPadraoMapper.toDto(dadoPadrao);
        var returnedDadoPadraoDTO = om.readValue(
            restDadoPadraoMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dadoPadraoDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DadoPadraoDTO.class
        );

        // Validate the DadoPadrao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDadoPadrao = dadoPadraoMapper.toEntity(returnedDadoPadraoDTO);
        assertDadoPadraoUpdatableFieldsEquals(returnedDadoPadrao, getPersistedDadoPadrao(returnedDadoPadrao));

        insertedDadoPadrao = returnedDadoPadrao;
    }

    @Test
    @Transactional
    void createDadoPadraoWithExistingId() throws Exception {
        // Create the DadoPadrao with an existing ID
//        dadoPadrao.setId(1L);
    	dadoPadraoRepository.saveAndFlush(dadoPadrao);
        DadoPadraoDTO dadoPadraoDTO = dadoPadraoMapper.toDto(dadoPadrao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDadoPadraoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dadoPadraoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DadoPadrao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dadoPadrao.setNome(null);

        // Create the DadoPadrao, which fails.
        DadoPadraoDTO dadoPadraoDTO = dadoPadraoMapper.toDto(dadoPadrao);

        restDadoPadraoMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dadoPadraoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDadoPadraos() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        // Get all the dadoPadraoList
        restDadoPadraoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].idDado").value(hasItem(dadoPadrao.getId().getIdDado().intValue())))
			.andExpect(jsonPath("$.[*].classePadraoId")
					.value(hasItem(dadoPadrao.getId().getClassePadraoId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDadoPadraosWithEagerRelationshipsIsEnabled() throws Exception {
        when(dadoPadraoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDadoPadraoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dadoPadraoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDadoPadraosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dadoPadraoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDadoPadraoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(dadoPadraoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDadoPadrao() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        // Get the dadoPadrao
        restDadoPadraoMockMvc
            .perform(get(ENTITY_API_URL_ID, dadoPadrao.getId().getIdDado(),dadoPadrao.getId().getClassePadraoId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.idDado").value(dadoPadrao.getId().getIdDado().intValue()))
			.andExpect(jsonPath("$.classePadraoId").value(dadoPadrao.getId().getClassePadraoId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO));
    }

    @Test
    @Transactional
    void getDadoPadraosByIdFiltering() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        Long idDado = dadoPadrao.getId().getIdDado();
		Long classePadraoId = dadoPadrao.getId().getClassePadraoId();

		defaultDadoPadraoShouldBeFound("idDado.equals=" + idDado);
		defaultDadoPadraoShouldNotBeFound("idDado.notEquals=" + idDado);

		defaultDadoPadraoShouldBeFound("classePadraoId.equals=" + classePadraoId);
		defaultDadoPadraoShouldNotBeFound("classePadraoId.notEquals=" + classePadraoId);

		defaultDadoPadraoShouldBeFound("idDado.greaterThanOrEqual=" + idDado);
		defaultDadoPadraoShouldNotBeFound("idDado.greaterThan=" + idDado);

		defaultDadoPadraoShouldBeFound("classePadraoId.greaterThanOrEqual=" + classePadraoId);
		defaultDadoPadraoShouldNotBeFound("classePadraoId.greaterThan=" + classePadraoId);

		defaultDadoPadraoShouldBeFound("idDado.lessThanOrEqual=" + idDado);
		defaultDadoPadraoShouldNotBeFound("idDado.lessThan=" + idDado);

		defaultDadoPadraoShouldBeFound("classePadraoId.lessThanOrEqual=" + classePadraoId);
		defaultDadoPadraoShouldNotBeFound("classePadraoId.lessThan=" + classePadraoId);
    }

    @Test
    @Transactional
    void getAllDadoPadraosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        // Get all the dadoPadraoList where nome equals to
        defaultDadoPadraoFiltering("nome.equals=" + DEFAULT_NOME, "nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllDadoPadraosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        // Get all the dadoPadraoList where nome in
        defaultDadoPadraoFiltering("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME, "nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllDadoPadraosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        // Get all the dadoPadraoList where nome is not null
        defaultDadoPadraoFiltering("nome.specified=true", "nome.specified=false");
    }

    @Test
    @Transactional
    void getAllDadoPadraosByNomeContainsSomething() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        // Get all the dadoPadraoList where nome contains
        defaultDadoPadraoFiltering("nome.contains=" + DEFAULT_NOME, "nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllDadoPadraosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        // Get all the dadoPadraoList where nome does not contain
        defaultDadoPadraoFiltering("nome.doesNotContain=" + UPDATED_NOME, "nome.doesNotContain=" + DEFAULT_NOME);
    }

    @Test
    @Transactional
    void getAllDadoPadraosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        // Get all the dadoPadraoList where descricao equals to
        defaultDadoPadraoFiltering("descricao.equals=" + DEFAULT_DESCRICAO, "descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDadoPadraosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        // Get all the dadoPadraoList where descricao in
        defaultDadoPadraoFiltering("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO, "descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDadoPadraosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        // Get all the dadoPadraoList where descricao is not null
        defaultDadoPadraoFiltering("descricao.specified=true", "descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllDadoPadraosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        // Get all the dadoPadraoList where descricao contains
        defaultDadoPadraoFiltering("descricao.contains=" + DEFAULT_DESCRICAO, "descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDadoPadraosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        // Get all the dadoPadraoList where descricao does not contain
        defaultDadoPadraoFiltering("descricao.doesNotContain=" + UPDATED_DESCRICAO, "descricao.doesNotContain=" + DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDadoPadraosByAtivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        // Get all the dadoPadraoList where ativo equals to
        defaultDadoPadraoFiltering("ativo.equals=" + DEFAULT_ATIVO, "ativo.equals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllDadoPadraosByAtivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        // Get all the dadoPadraoList where ativo in
        defaultDadoPadraoFiltering("ativo.in=" + DEFAULT_ATIVO + "," + UPDATED_ATIVO, "ativo.in=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllDadoPadraosByAtivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        // Get all the dadoPadraoList where ativo is not null
        defaultDadoPadraoFiltering("ativo.specified=true", "ativo.specified=false");
    }

    @Test
    @Transactional
    void getAllDadoPadraosByClassePadraoIsEqualToSomething() throws Exception {
        ClassePadrao classePadrao;
        if (TestUtil.findAll(em, ClassePadrao.class).isEmpty()) {
            dadoPadraoRepository.saveAndFlush(dadoPadrao);
            classePadrao = ClassePadraoResourceIT.createEntity();
        } else {
            classePadrao = TestUtil.findAll(em, ClassePadrao.class).get(0);
        }
        em.persist(classePadrao);
        em.flush();
        dadoPadrao.setClassePadrao(classePadrao);
        dadoPadraoRepository.saveAndFlush(dadoPadrao);
        Long classePadraoId = classePadrao.getId();
        // Get all the dadoPadraoList where classePadrao equals to classePadraoId
        defaultDadoPadraoShouldBeFound("classePadraoId.equals=" + classePadraoId);

        // Get all the dadoPadraoList where classePadrao equals to (classePadraoId + 1)
        defaultDadoPadraoShouldNotBeFound("classePadraoId.equals=" + (classePadraoId + 1));
    }

    private void defaultDadoPadraoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDadoPadraoShouldBeFound(shouldBeFound);
        defaultDadoPadraoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDadoPadraoShouldBeFound(String filter) throws Exception {
        restDadoPadraoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].idDado").value(hasItem(dadoPadrao.getId().getIdDado().intValue())))
			.andExpect(jsonPath("$.[*].classePadraoId")
					.value(hasItem(dadoPadrao.getId().getClassePadraoId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)));

        // Check, that the count call also returns 1
        restDadoPadraoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDadoPadraoShouldNotBeFound(String filter) throws Exception {
        restDadoPadraoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDadoPadraoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDadoPadrao() throws Exception {
        // Get the dadoPadrao
        restDadoPadraoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDadoPadrao() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        int databaseSizeBeforeUpdate = dadoPadraoRepository.findAll().size();;

        // Update the dadoPadrao
        DadoPadrao updatedDadoPadrao = dadoPadraoRepository.findById(dadoPadrao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDadoPadrao are not directly saved in db
        em.detach(updatedDadoPadrao);
        updatedDadoPadrao.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO);
        DadoPadraoDTO dadoPadraoDTO = dadoPadraoMapper.toDto(updatedDadoPadrao);

        restDadoPadraoMockMvc
            .perform(
            		put(ENTITY_API_URL_ID, dadoPadraoDTO.getIdDado(), dadoPadraoDTO.getClassePadraoId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dadoPadraoDTO))
            )
            .andExpect(status().isOk());

        // Validate the DadoPadrao in the database
        List<DadoPadrao> dadoPadraoList = dadoPadraoRepository.findAll();
        assertThat(dadoPadraoList).hasSize(databaseSizeBeforeUpdate);
        DadoPadrao testDadoPadrao = dadoPadraoList.get(dadoPadraoList.size() - 1);
        assertThat(testDadoPadrao.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDadoPadrao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testDadoPadrao.getAtivo()).isEqualTo(UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void putNonExistingDadoPadrao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoPadrao.setId(new DadoPadraoPK(longCount.incrementAndGet(), longCount.incrementAndGet()));

        // Create the DadoPadrao
        DadoPadraoDTO dadoPadraoDTO = dadoPadraoMapper.toDto(dadoPadrao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDadoPadraoMockMvc
            .perform(
            		put(ENTITY_API_URL_ID, dadoPadraoDTO.getIdDado(), dadoPadraoDTO.getClassePadraoId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dadoPadraoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DadoPadrao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDadoPadrao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoPadrao.setId(new DadoPadraoPK(longCount.incrementAndGet(), longCount.incrementAndGet()));

        // Create the DadoPadrao
        DadoPadraoDTO dadoPadraoDTO = dadoPadraoMapper.toDto(dadoPadrao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDadoPadraoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet(), longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dadoPadraoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DadoPadrao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDadoPadrao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoPadrao.setId(new DadoPadraoPK(longCount.incrementAndGet(), longCount.incrementAndGet()));

        // Create the DadoPadrao
        DadoPadraoDTO dadoPadraoDTO = dadoPadraoMapper.toDto(dadoPadrao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDadoPadraoMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dadoPadraoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DadoPadrao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDadoPadraoWithPatch() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dadoPadrao using partial update
        DadoPadrao partialUpdatedDadoPadrao = new DadoPadrao();
        partialUpdatedDadoPadrao.setId(dadoPadrao.getId());

        partialUpdatedDadoPadrao.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO);
        DadoPadraoDTO dto = dadoPadraoMapper.toDto(partialUpdatedDadoPadrao);

        restDadoPadraoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDadoPadrao.getId().getIdDado(),partialUpdatedDadoPadrao.getId().getClassePadraoId() )
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dto))
            )
            .andExpect(status().isOk());

        // Validate the DadoPadrao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDadoPadraoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDadoPadrao, dadoPadrao),
            getPersistedDadoPadrao(dadoPadrao)
        );
    }

    @Test
    @Transactional
    void fullUpdateDadoPadraoWithPatch() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dadoPadrao using partial update
        DadoPadrao partialUpdatedDadoPadrao = new DadoPadrao();
        partialUpdatedDadoPadrao.setId(dadoPadrao.getId());

        partialUpdatedDadoPadrao.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO);
        DadoPadraoDTO dto = dadoPadraoMapper.toDto(partialUpdatedDadoPadrao);

        restDadoPadraoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDadoPadrao.getId().getIdDado(), partialUpdatedDadoPadrao.getId().getClassePadraoId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dto))
            )
            .andExpect(status().isOk());

        // Validate the DadoPadrao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDadoPadraoUpdatableFieldsEquals(partialUpdatedDadoPadrao, getPersistedDadoPadrao(partialUpdatedDadoPadrao));
    }

    @Test
    @Transactional
    void patchNonExistingDadoPadrao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoPadrao.setId(new DadoPadraoPK(longCount.incrementAndGet(), longCount.incrementAndGet()));

        // Create the DadoPadrao
        DadoPadraoDTO dadoPadraoDTO = dadoPadraoMapper.toDto(dadoPadrao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDadoPadraoMockMvc
            .perform(
            		patch(ENTITY_API_URL_ID, dadoPadraoDTO.getIdDado(), dadoPadraoDTO.getClassePadraoId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dadoPadraoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DadoPadrao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDadoPadrao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoPadrao.setId(new DadoPadraoPK(longCount.incrementAndGet(), longCount.incrementAndGet()));

        // Create the DadoPadrao
        DadoPadraoDTO dadoPadraoDTO = dadoPadraoMapper.toDto(dadoPadrao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDadoPadraoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet(),longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dadoPadraoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DadoPadrao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDadoPadrao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoPadrao.setId(new DadoPadraoPK(longCount.incrementAndGet(), longCount.incrementAndGet()));

        // Create the DadoPadrao
        DadoPadraoDTO dadoPadraoDTO = dadoPadraoMapper.toDto(dadoPadrao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDadoPadraoMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dadoPadraoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DadoPadrao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDadoPadrao() throws Exception {
        // Initialize the database
        insertedDadoPadrao = dadoPadraoRepository.saveAndFlush(dadoPadrao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dadoPadrao
        restDadoPadraoMockMvc
            .perform(delete(ENTITY_API_URL_ID, dadoPadrao.getId().getIdDado(), dadoPadrao.getId().getClassePadraoId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dadoPadraoRepository.count();
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

    protected DadoPadrao getPersistedDadoPadrao(DadoPadrao dadoPadrao) {
        return dadoPadraoRepository.findById(dadoPadrao.getId()).orElseThrow();
    }

    protected void assertPersistedDadoPadraoToMatchAllProperties(DadoPadrao expectedDadoPadrao) {
        assertDadoPadraoAllPropertiesEquals(expectedDadoPadrao, getPersistedDadoPadrao(expectedDadoPadrao));
    }

    protected void assertPersistedDadoPadraoToMatchUpdatableProperties(DadoPadrao expectedDadoPadrao) {
        assertDadoPadraoAllUpdatablePropertiesEquals(expectedDadoPadrao, getPersistedDadoPadrao(expectedDadoPadrao));
    }
}
