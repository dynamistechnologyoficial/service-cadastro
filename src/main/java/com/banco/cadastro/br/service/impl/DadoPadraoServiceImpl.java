package com.banco.cadastro.br.service.impl;

import com.banco.cadastro.br.domain.DadoPadrao;
import com.banco.cadastro.br.domain.DadoPadraoPK;
import com.banco.cadastro.br.repository.DadoPadraoRepository;
import com.banco.cadastro.br.service.DadoPadraoService;
import com.banco.cadastro.br.service.dto.DadoPadraoDTO;
import com.banco.cadastro.br.service.mapper.DadoPadraoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.banco.cadastro.br.domain.DadoPadrao}.
 */
@Service
@Transactional
public class DadoPadraoServiceImpl implements DadoPadraoService {

    private static final Logger LOG = LoggerFactory.getLogger(DadoPadraoServiceImpl.class);

    private final DadoPadraoRepository dadoPadraoRepository;

    private final DadoPadraoMapper dadoPadraoMapper;

    public DadoPadraoServiceImpl(DadoPadraoRepository dadoPadraoRepository, DadoPadraoMapper dadoPadraoMapper) {
        this.dadoPadraoRepository = dadoPadraoRepository;
        this.dadoPadraoMapper = dadoPadraoMapper;
    }

    @Override
    public DadoPadraoDTO save(DadoPadraoDTO dadoPadraoDTO) {
        LOG.debug("Request to save DadoPadrao : {}", dadoPadraoDTO);
        DadoPadrao dadoPadrao = dadoPadraoMapper.toEntity(dadoPadraoDTO);
        dadoPadrao = dadoPadraoRepository.save(dadoPadrao);
        return dadoPadraoMapper.toDto(dadoPadrao);
    }

    @Override
    public DadoPadraoDTO update(DadoPadraoDTO dadoPadraoDTO) {
        LOG.debug("Request to update DadoPadrao : {}", dadoPadraoDTO);
        DadoPadrao dadoPadrao = dadoPadraoMapper.toEntity(dadoPadraoDTO);
        dadoPadrao.setIsPersisted();
        dadoPadrao = dadoPadraoRepository.save(dadoPadrao);
        return dadoPadraoMapper.toDto(dadoPadrao);
    }

    @Override
    public Optional<DadoPadraoDTO> partialUpdate(DadoPadraoDTO dadoPadraoDTO) {
        LOG.debug("Request to partially update DadoPadrao : {}", dadoPadraoDTO);

        return dadoPadraoRepository
            .findById(newDadoPadraoPK(dadoPadraoDTO.getIdDado(), dadoPadraoDTO.getClassePadraoId()))
            .map(existingDadoPadrao -> {
                dadoPadraoMapper.partialUpdate(existingDadoPadrao, dadoPadraoDTO);

                return existingDadoPadrao;
            })
            .map(dadoPadraoRepository::save)
            .map(dadoPadraoMapper::toDto);
    }

    public Page<DadoPadraoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return dadoPadraoRepository.findAllWithEagerRelationships(pageable).map(dadoPadraoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DadoPadraoDTO> findOne(Long idDados, Long classePadraoId) {
    	LOG.debug("Request to get DadoPadrao : {}, {}", idDados, classePadraoId);
        return dadoPadraoRepository.findOneWithEagerRelationships(newDadoPadraoPK(idDados, classePadraoId)).map(dadoPadraoMapper::toDto);
    }

    @Override
    public void delete(Long idDados, Long classePadraoId) {
    	LOG.debug("Request to delete DadoPadrao : {}, {}", idDados, classePadraoId);
        dadoPadraoRepository.deleteById(newDadoPadraoPK(idDados, classePadraoId));
    }
    
    private DadoPadraoPK newDadoPadraoPK(Long idDados, Long classePadraoId) {
		DadoPadraoPK dadoPadraoPK = new DadoPadraoPK();
    	dadoPadraoPK.idDado(idDados);
    	dadoPadraoPK.classePadraoId(classePadraoId);
    	return dadoPadraoPK;
	}
}
