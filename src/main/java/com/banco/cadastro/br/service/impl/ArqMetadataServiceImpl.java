package com.banco.cadastro.br.service.impl;

import com.banco.cadastro.br.domain.ArqMetadata;
import com.banco.cadastro.br.repository.ArqMetadataRepository;
import com.banco.cadastro.br.service.ArqMetadataService;
import com.banco.cadastro.br.service.dto.ArqMetadataDTO;
import com.banco.cadastro.br.service.mapper.ArqMetadataMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.banco.cadastro.br.domain.ArqMetadata}.
 */
@Service
@Transactional
public class ArqMetadataServiceImpl implements ArqMetadataService {

    private static final Logger LOG = LoggerFactory.getLogger(ArqMetadataServiceImpl.class);

    private final ArqMetadataRepository arqMetadataRepository;

    private final ArqMetadataMapper arqMetadataMapper;

    public ArqMetadataServiceImpl(ArqMetadataRepository arqMetadataRepository, ArqMetadataMapper arqMetadataMapper) {
        this.arqMetadataRepository = arqMetadataRepository;
        this.arqMetadataMapper = arqMetadataMapper;
    }

    @Override
    public ArqMetadataDTO save(ArqMetadataDTO arqMetadataDTO) {
        LOG.debug("Request to save ArqMetadata : {}", arqMetadataDTO);
        ArqMetadata arqMetadata = arqMetadataMapper.toEntity(arqMetadataDTO);
        arqMetadata = arqMetadataRepository.save(arqMetadata);
        return arqMetadataMapper.toDto(arqMetadata);
    }

    @Override
    public ArqMetadataDTO update(ArqMetadataDTO arqMetadataDTO) {
        LOG.debug("Request to update ArqMetadata : {}", arqMetadataDTO);
        ArqMetadata arqMetadata = arqMetadataMapper.toEntity(arqMetadataDTO);
        arqMetadata.setIsPersisted();
        arqMetadata = arqMetadataRepository.save(arqMetadata);
        return arqMetadataMapper.toDto(arqMetadata);
    }

    @Override
    public Optional<ArqMetadataDTO> partialUpdate(ArqMetadataDTO arqMetadataDTO) {
        LOG.debug("Request to partially update ArqMetadata : {}", arqMetadataDTO);

        return arqMetadataRepository
            .findById(arqMetadataDTO.getId())
            .map(existingArqMetadata -> {
                arqMetadataMapper.partialUpdate(existingArqMetadata, arqMetadataDTO);

                return existingArqMetadata;
            })
            .map(arqMetadataRepository::save)
            .map(arqMetadataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArqMetadataDTO> findOne(Long id) {
        LOG.debug("Request to get ArqMetadata : {}", id);
        return arqMetadataRepository.findById(id).map(arqMetadataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ArqMetadata : {}", id);
        arqMetadataRepository.deleteById(id);
    }
}
