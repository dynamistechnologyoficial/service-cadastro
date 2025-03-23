package com.banco.cadastro.br.web.rest;

import com.banco.cadastro.br.repository.ClassePadraoRepository;
import com.banco.cadastro.br.service.ClassePadraoQueryService;
import com.banco.cadastro.br.service.ClassePadraoService;
import com.banco.cadastro.br.service.criteria.ClassePadraoCriteria;
import com.banco.cadastro.br.service.dto.ClassePadraoDTO;
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
 * REST controller for managing {@link com.banco.cadastro.br.domain.ClassePadrao}.
 */
@RestController
@RequestMapping("/api/classe-padraos")
public class ClassePadraoResource {

    private static final Logger LOG = LoggerFactory.getLogger(ClassePadraoResource.class);

    private static final String ENTITY_NAME = "serviceCadastroClassePadrao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassePadraoService classePadraoService;

    private final ClassePadraoRepository classePadraoRepository;

    private final ClassePadraoQueryService classePadraoQueryService;

    public ClassePadraoResource(
        ClassePadraoService classePadraoService,
        ClassePadraoRepository classePadraoRepository,
        ClassePadraoQueryService classePadraoQueryService
    ) {
        this.classePadraoService = classePadraoService;
        this.classePadraoRepository = classePadraoRepository;
        this.classePadraoQueryService = classePadraoQueryService;
    }

    /**
     * {@code POST  /classe-padraos} : Create a new classePadrao.
     *
     * @param classePadraoDTO the classePadraoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classePadraoDTO, or with status {@code 400 (Bad Request)} if the classePadrao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClassePadraoDTO> createClassePadrao(@Valid @RequestBody ClassePadraoDTO classePadraoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ClassePadrao : {}", classePadraoDTO);
        if (classePadraoDTO.getId() != null) {
            throw new BadRequestAlertException("A new classePadrao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        classePadraoDTO = classePadraoService.save(classePadraoDTO);
        return ResponseEntity.created(new URI("/api/classe-padraos/" + classePadraoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, classePadraoDTO.getId().toString()))
            .body(classePadraoDTO);
    }

    /**
     * {@code PUT  /classe-padraos/:id} : Updates an existing classePadrao.
     *
     * @param id the id of the classePadraoDTO to save.
     * @param classePadraoDTO the classePadraoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classePadraoDTO,
     * or with status {@code 400 (Bad Request)} if the classePadraoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classePadraoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClassePadraoDTO> updateClassePadrao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClassePadraoDTO classePadraoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ClassePadrao : {}, {}", id, classePadraoDTO);
        if (classePadraoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classePadraoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classePadraoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        classePadraoDTO = classePadraoService.update(classePadraoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classePadraoDTO.getId().toString()))
            .body(classePadraoDTO);
    }

    /**
     * {@code PATCH  /classe-padraos/:id} : Partial updates given fields of an existing classePadrao, field will ignore if it is null
     *
     * @param id the id of the classePadraoDTO to save.
     * @param classePadraoDTO the classePadraoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classePadraoDTO,
     * or with status {@code 400 (Bad Request)} if the classePadraoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the classePadraoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the classePadraoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClassePadraoDTO> partialUpdateClassePadrao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClassePadraoDTO classePadraoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ClassePadrao partially : {}, {}", id, classePadraoDTO);
        if (classePadraoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classePadraoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classePadraoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClassePadraoDTO> result = classePadraoService.partialUpdate(classePadraoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classePadraoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /classe-padraos} : get all the classePadraos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classePadraos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ClassePadraoDTO>> getAllClassePadraos(
        ClassePadraoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ClassePadraos by criteria: {}", criteria);

        Page<ClassePadraoDTO> page = classePadraoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /classe-padraos/count} : count all the classePadraos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countClassePadraos(ClassePadraoCriteria criteria) {
        LOG.debug("REST request to count ClassePadraos by criteria: {}", criteria);
        return ResponseEntity.ok().body(classePadraoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /classe-padraos/:id} : get the "id" classePadrao.
     *
     * @param id the id of the classePadraoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classePadraoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClassePadraoDTO> getClassePadrao(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ClassePadrao : {}", id);
        Optional<ClassePadraoDTO> classePadraoDTO = classePadraoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classePadraoDTO);
    }

    /**
     * {@code DELETE  /classe-padraos/:id} : delete the "id" classePadrao.
     *
     * @param id the id of the classePadraoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassePadrao(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ClassePadrao : {}", id);
        classePadraoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
