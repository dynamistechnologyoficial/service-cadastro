package com.banco.cadastro.br.service.impl;

import com.banco.cadastro.br.domain.ArquivoByte;
import com.banco.cadastro.br.repository.ArquivoByteRepository;
import com.banco.cadastro.br.service.ArquivoByteService;
import com.banco.cadastro.br.service.dto.ArquivoByteDTO;
import com.banco.cadastro.br.service.mapper.ArquivoByteMapper;
import com.banco.cadastro.br.web.rest.errors.BadRequestAlertException;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.banco.cadastro.br.domain.ArquivoByte}.
 */
@Service
@Transactional
public class ArquivoByteServiceImpl implements ArquivoByteService {

    private static final Logger LOG = LoggerFactory.getLogger(ArquivoByteServiceImpl.class);

    private final ArquivoByteRepository arquivoByteRepository;

    private final ArquivoByteMapper arquivoByteMapper;

    public ArquivoByteServiceImpl(ArquivoByteRepository arquivoByteRepository, ArquivoByteMapper arquivoByteMapper) {
        this.arquivoByteRepository = arquivoByteRepository;
        this.arquivoByteMapper = arquivoByteMapper;
    }

    @Override
    public ArquivoByteDTO save(ArquivoByteDTO arquivoByteDTO) {
        LOG.debug("Request to save ArquivoByte : {}", arquivoByteDTO);
        ArquivoByte arquivoByte = arquivoByteMapper.toEntity(arquivoByteDTO);
        if(arquivoByte.getId()!=null && arquivoByteRepository.existsById(arquivoByte.getId())) {
        	LOG.debug("Já existe um arquivo Byte com esse ID: {}", arquivoByte.getId());
        	throw new RuntimeException("Já existi um Arquivo cadastrado.");
        }
        arquivoByte = arquivoByteRepository.save(arquivoByte);
        return arquivoByteMapper.toDto(arquivoByte);
    }

    @Override
    public ArquivoByteDTO update(ArquivoByteDTO arquivoByteDTO) {
        LOG.debug("Request to update ArquivoByte : {}", arquivoByteDTO);
        ArquivoByte arquivoByte = arquivoByteMapper.toEntity(arquivoByteDTO);
        if(arquivoByte.getId()!=null && !arquivoByteRepository.existsById(arquivoByte.getId())) {
        	LOG.debug("Não existe um arquivo com esse ID: {}", arquivoByte.getId());
        	throw new RuntimeException("Não existi um Arquivo cadastrado para efetuar uma atualização.");
        }
        arquivoByte = arquivoByteRepository.save(arquivoByte);
        return arquivoByteMapper.toDto(arquivoByte);
    }

    @Override
    public Optional<ArquivoByteDTO> partialUpdate(ArquivoByteDTO arquivoByteDTO) {
        LOG.debug("Request to partially update ArquivoByte : {}", arquivoByteDTO);

        return arquivoByteRepository
            .findById(arquivoByteDTO.getId())
            .map(existingArquivoByte -> {
                arquivoByteMapper.partialUpdate(existingArquivoByte, arquivoByteDTO);

                return existingArquivoByte;
            })
            .map(arquivoByteRepository::save)
            .map(arquivoByteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArquivoByteDTO> findOne(Long id) {
        LOG.debug("Request to get ArquivoByte : {}", id);
        return arquivoByteRepository.findById(id).map(arquivoByteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ArquivoByte : {}", id);
        arquivoByteRepository.deleteById(id);
    }
}
