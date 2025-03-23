package com.banco.cadastro.br.service.impl;

import com.banco.cadastro.br.domain.ClassePadrao;
import com.banco.cadastro.br.repository.ClassePadraoRepository;
import com.banco.cadastro.br.service.ClassePadraoService;
import com.banco.cadastro.br.service.dto.ClassePadraoDTO;
import com.banco.cadastro.br.service.mapper.ClassePadraoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.banco.cadastro.br.domain.ClassePadrao}.
 */
@Service
@Transactional
public class ClassePadraoServiceImpl implements ClassePadraoService {

    private static final Logger LOG = LoggerFactory.getLogger(ClassePadraoServiceImpl.class);

    private final ClassePadraoRepository classePadraoRepository;

    private final ClassePadraoMapper classePadraoMapper;

    public ClassePadraoServiceImpl(ClassePadraoRepository classePadraoRepository, ClassePadraoMapper classePadraoMapper) {
        this.classePadraoRepository = classePadraoRepository;
        this.classePadraoMapper = classePadraoMapper;
    }

    @Override
    public ClassePadraoDTO save(ClassePadraoDTO classePadraoDTO) {
        LOG.debug("Request to save ClassePadrao : {}", classePadraoDTO);
        ClassePadrao classePadrao = classePadraoMapper.toEntity(classePadraoDTO);
        classePadrao = classePadraoRepository.save(classePadrao);
        return classePadraoMapper.toDto(classePadrao);
    }

    @Override
    public ClassePadraoDTO update(ClassePadraoDTO classePadraoDTO) {
        LOG.debug("Request to update ClassePadrao : {}", classePadraoDTO);
        ClassePadrao classePadrao = classePadraoMapper.toEntity(classePadraoDTO);
        classePadrao.setIsPersisted();
        classePadrao = classePadraoRepository.save(classePadrao);
        return classePadraoMapper.toDto(classePadrao);
    }

    @Override
    public Optional<ClassePadraoDTO> partialUpdate(ClassePadraoDTO classePadraoDTO) {
        LOG.debug("Request to partially update ClassePadrao : {}", classePadraoDTO);

        return classePadraoRepository
            .findById(classePadraoDTO.getId())
            .map(existingClassePadrao -> {
                classePadraoMapper.partialUpdate(existingClassePadrao, classePadraoDTO);

                return existingClassePadrao;
            })
            .map(classePadraoRepository::save)
            .map(classePadraoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClassePadraoDTO> findOne(Long id) {
        LOG.debug("Request to get ClassePadrao : {}", id);
        return classePadraoRepository.findById(id).map(classePadraoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ClassePadrao : {}", id);
        classePadraoRepository.deleteById(id);
    }
}
