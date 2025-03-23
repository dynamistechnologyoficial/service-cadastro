package com.banco.cadastro.br.service.impl;

import com.banco.cadastro.br.domain.EntidadeTeste;
import com.banco.cadastro.br.repository.EntidadeTesteRepository;
import com.banco.cadastro.br.service.EntidadeTesteService;
import com.banco.cadastro.br.service.dto.EntidadeTesteDTO;
import com.banco.cadastro.br.service.mapper.EntidadeTesteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.banco.cadastro.br.domain.EntidadeTeste}.
 */
@Service
@Transactional
public class EntidadeTesteServiceImpl implements EntidadeTesteService {

    private static final Logger LOG = LoggerFactory.getLogger(EntidadeTesteServiceImpl.class);

    private final EntidadeTesteRepository entidadeTesteRepository;

    private final EntidadeTesteMapper entidadeTesteMapper;

    public EntidadeTesteServiceImpl(EntidadeTesteRepository entidadeTesteRepository, EntidadeTesteMapper entidadeTesteMapper) {
        this.entidadeTesteRepository = entidadeTesteRepository;
        this.entidadeTesteMapper = entidadeTesteMapper;
    }

    @Override
    public EntidadeTesteDTO save(EntidadeTesteDTO entidadeTesteDTO) {
        LOG.debug("Request to save EntidadeTeste : {}", entidadeTesteDTO);
        EntidadeTeste entidadeTeste = entidadeTesteMapper.toEntity(entidadeTesteDTO);
        entidadeTeste = entidadeTesteRepository.save(entidadeTeste);
        return entidadeTesteMapper.toDto(entidadeTeste);
    }

    @Override
    public EntidadeTesteDTO update(EntidadeTesteDTO entidadeTesteDTO) {
        LOG.debug("Request to update EntidadeTeste : {}", entidadeTesteDTO);
        EntidadeTeste entidadeTeste = entidadeTesteMapper.toEntity(entidadeTesteDTO);
        entidadeTeste.setIsPersisted();
        entidadeTeste = entidadeTesteRepository.save(entidadeTeste);
        return entidadeTesteMapper.toDto(entidadeTeste);
    }

    @Override
    public Optional<EntidadeTesteDTO> partialUpdate(EntidadeTesteDTO entidadeTesteDTO) {
        LOG.debug("Request to partially update EntidadeTeste : {}", entidadeTesteDTO);

        return entidadeTesteRepository
            .findById(entidadeTesteDTO.getId())
            .map(existingEntidadeTeste -> {
                entidadeTesteMapper.partialUpdate(existingEntidadeTeste, entidadeTesteDTO);

                return existingEntidadeTeste;
            })
            .map(entidadeTesteRepository::save)
            .map(entidadeTesteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EntidadeTesteDTO> findOne(Long id) {
        LOG.debug("Request to get EntidadeTeste : {}", id);
        return entidadeTesteRepository.findById(id).map(entidadeTesteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete EntidadeTeste : {}", id);
        entidadeTesteRepository.deleteById(id);
    }
}
