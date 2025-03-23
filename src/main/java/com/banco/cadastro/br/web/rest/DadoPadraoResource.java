package com.banco.cadastro.br.web.rest;

import com.banco.cadastro.br.domain.DadoPadraoPK;
import com.banco.cadastro.br.repository.DadoPadraoRepository;
import com.banco.cadastro.br.service.DadoPadraoQueryService;
import com.banco.cadastro.br.service.DadoPadraoService;
import com.banco.cadastro.br.service.criteria.DadoPadraoCriteria;
import com.banco.cadastro.br.service.dto.DadoPadraoDTO;
import com.banco.cadastro.br.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.banco.cadastro.br.domain.DadoPadrao}.
 */
@RestController
@RequestMapping("/api/dado-padraos")
public class DadoPadraoResource {

    private static final Logger LOG = LoggerFactory.getLogger(DadoPadraoResource.class);

    private static final String ENTITY_NAME = "serviceCadastroDadoPadrao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DadoPadraoService dadoPadraoService;

    private final DadoPadraoRepository dadoPadraoRepository;

    private final DadoPadraoQueryService dadoPadraoQueryService;

    public DadoPadraoResource(
        DadoPadraoService dadoPadraoService,
        DadoPadraoRepository dadoPadraoRepository,
        DadoPadraoQueryService dadoPadraoQueryService
    ) {
        this.dadoPadraoService = dadoPadraoService;
        this.dadoPadraoRepository = dadoPadraoRepository;
        this.dadoPadraoQueryService = dadoPadraoQueryService;
    }

    /**
     * {@code POST  /dado-padraos} : Create a new dadoPadrao.
     *
     * @param dadoPadraoDTO the dadoPadraoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dadoPadraoDTO, or with status {@code 400 (Bad Request)} if the dadoPadrao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DadoPadraoDTO> createDadoPadrao(@Valid @RequestBody DadoPadraoDTO dadoPadraoDTO) throws URISyntaxException {
        LOG.debug("REST request to save DadoPadrao : {}", dadoPadraoDTO);
        if (dadoPadraoDTO.getIdDado() == null || dadoPadraoDTO.getClassePadraoId() == null ) {
            throw new BadRequestAlertException("A new dadoPadrao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (dadoPadraoRepository.existsById(new DadoPadraoPK(dadoPadraoDTO.getIdDado(), dadoPadraoDTO.getClassePadraoId()))) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        dadoPadraoDTO = dadoPadraoService.save(dadoPadraoDTO);
        return ResponseEntity.created(new URI("/api/dado-padraos/" + dadoPadraoDTO.getClassePadraoId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dadoPadraoDTO.getIdDado()+", "+dadoPadraoDTO.getClassePadraoId()))
            .body(dadoPadraoDTO);
    }

    /**
     * {@code PUT  /dado-padraos/:id} : Updates an existing dadoPadrao.
     *
     * @param id the id of the dadoPadraoDTO to save.
     * @param dadoPadraoDTO the dadoPadraoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dadoPadraoDTO,
     * or with status {@code 400 (Bad Request)} if the dadoPadraoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dadoPadraoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{idDado}/{classePadraoId}")
    public ResponseEntity<DadoPadraoDTO> updateDadoPadrao(
    	@PathVariable(value = "idDado", required = false) final Long idDado,
        @PathVariable(value = "classePadraoId", required = false) final Long classePadraoId,
        @Valid @RequestBody DadoPadraoDTO dadoPadraoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DadoPadrao : {}, {}, {}", idDado, classePadraoId, dadoPadraoDTO);
        if (dadoPadraoDTO.getIdDado() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(idDado, dadoPadraoDTO.getIdDado())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        
        if (dadoPadraoDTO.getClassePadraoId() == null) {
            throw new BadRequestAlertException("Invalid idDado", ENTITY_NAME, "classePadraoidDadonull");
        }
        if (!Objects.equals(classePadraoId, dadoPadraoDTO.getClassePadraoId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "classePadraoIdinvalid");
        }

        if (!dadoPadraoRepository.existsById(new DadoPadraoPK(dadoPadraoDTO.getIdDado(), dadoPadraoDTO.getClassePadraoId()))) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dadoPadraoDTO = dadoPadraoService.update(dadoPadraoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
            		dadoPadraoDTO.getIdDado()+", "+dadoPadraoDTO.getClassePadraoId()))
            .body(dadoPadraoDTO);
    }

    /**
     * {@code PATCH  /dado-padraos/:id} : Partial updates given fields of an existing dadoPadrao, field will ignore if it is null
     *
     * @param id the id of the dadoPadraoDTO to save.
     * @param dadoPadraoDTO the dadoPadraoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dadoPadraoDTO,
     * or with status {@code 400 (Bad Request)} if the dadoPadraoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dadoPadraoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dadoPadraoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{idDado}/{classePadraoId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DadoPadraoDTO> partialUpdateDadoPadrao(
    	@PathVariable(value = "idDado", required = false) final Long idDado,
    	@PathVariable(value = "classePadraoId", required = false) final Long classePadraoId,
        @NotNull @RequestBody DadoPadraoDTO dadoPadraoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DadoPadrao partially : {}, {}, {}", idDado, classePadraoId, dadoPadraoDTO);
        if (dadoPadraoDTO.getIdDado() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idDadonull");
        }
        if (!Objects.equals(idDado, dadoPadraoDTO.getIdDado())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idDadoinvalid");
        }
        
        if (dadoPadraoDTO.getClassePadraoId() == null) {
            throw new BadRequestAlertException("Invalid idDado", ENTITY_NAME, "classePadraoIdDadonull");
        }
        if (!Objects.equals(classePadraoId, dadoPadraoDTO.getClassePadraoId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "classePadraoIdDadoinvalid");
        }

        if (!dadoPadraoRepository.existsById(new DadoPadraoPK(dadoPadraoDTO.getIdDado(), dadoPadraoDTO.getClassePadraoId()))) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DadoPadraoDTO> result = dadoPadraoService.partialUpdate(dadoPadraoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
            		dadoPadraoDTO.getIdDado()+", "+dadoPadraoDTO.getClassePadraoId())
        );
    }

    /**
     * {@code GET  /dado-padraos} : get all the dadoPadraos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dadoPadraos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DadoPadraoDTO>> getAllDadoPadraos(
        DadoPadraoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DadoPadraos by criteria: {}", criteria);

        Page<DadoPadraoDTO> page = dadoPadraoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dado-padraos/count} : count all the dadoPadraos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDadoPadraos(DadoPadraoCriteria criteria) {
        LOG.debug("REST request to count DadoPadraos by criteria: {}", criteria);
        return ResponseEntity.ok().body(dadoPadraoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dado-padraos/:id} : get the "id" dadoPadrao.
     *
     * @param id the id of the dadoPadraoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dadoPadraoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{idDado}/{classePadraoId}")
    public ResponseEntity<DadoPadraoDTO> getDadoPadrao(
    		@PathVariable(value = "idDado") final Long idDado,
   	     	@PathVariable(value = "classePadraoId") final Long classePadraoId) {
        LOG.debug("REST request to get DadoPadrao : {}, {}", idDado,classePadraoId);
        Optional<DadoPadraoDTO> dadoPadraoDTO = dadoPadraoService.findOne(idDado,classePadraoId);
        return ResponseUtil.wrapOrNotFound(dadoPadraoDTO);
    }

    /**
     * {@code DELETE  /dado-padraos/:id} : delete the "id" dadoPadrao.
     *
     * @param id the id of the dadoPadraoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{idDado}/{classePadraoId}")
    public ResponseEntity<Void> deleteDadoPadrao(
    		@PathVariable("idDado") final Long idDado,
   	     	@PathVariable("classePadraoId") final Long classePadraoId) {
        LOG.debug("REST request to delete DadoPadrao : {}, {}", idDado,classePadraoId);
        dadoPadraoService.delete(idDado, classePadraoId);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, idDado+", "+classePadraoId))
            .build();
    }
}
