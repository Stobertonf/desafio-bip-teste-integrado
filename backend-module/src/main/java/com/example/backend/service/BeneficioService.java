package com.example.backend.service;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.TransferenciaRequest;
import com.example.backend.exception.SaldoInsuficienteException;
import com.example.backend.model.Beneficio;
import com.example.backend.model.Beneficiario;
import com.example.backend.repository.BeneficioRepository;
import com.example.backend.repository.BeneficiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class BeneficioService {
    
    private static final Logger LOGGER = Logger.getLogger(BeneficioService.class.getName());
    
    @Autowired
    private BeneficioRepository beneficioRepository;
    
    @Autowired
    private BeneficiarioRepository beneficiarioRepository;
    
    public List<BeneficioDTO> listarTodos() {
        LOGGER.info("Listando todos os benef?cios");
        return beneficioRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public BeneficioDTO buscarPorId(Long id) {
        LOGGER.info("Buscando benef?cio ID: " + id);
        Beneficio beneficio = beneficioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Benef?cio n?o encontrado: " + id));
        return toDTO(beneficio);
    }
    
    @Transactional
    public BeneficioDTO criar(BeneficioDTO dto) {
        LOGGER.info("Criando benef?cio para benefici?rio: " + dto.getBeneficiarioId());
        Beneficiario beneficiario = beneficiarioRepository.findById(dto.getBeneficiarioId())
            .orElseThrow(() -> new RuntimeException("Benefici?rio n?o encontrado"));
        
        Beneficio beneficio = new Beneficio();
        beneficio.setBeneficiario(beneficiario);
        beneficio.setTipo(dto.getTipo());
        beneficio.setValor(dto.getValor());
        beneficio.setSaldo(dto.getValor());
        beneficio.setDataConcessao(dto.getDataConcessao());
        
        return toDTO(beneficioRepository.save(beneficio));
    }
    
    @Transactional
    public BeneficioDTO atualizar(Long id, BeneficioDTO dto) {
        LOGGER.info("Atualizando benef?cio ID: " + id);
        Beneficio beneficio = beneficioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Benef?cio n?o encontrado"));
        
        beneficio.setTipo(dto.getTipo());
        beneficio.setValor(dto.getValor());
        beneficio.setSaldo(dto.getSaldo());
        
        return toDTO(beneficioRepository.save(beneficio));
    }
    
    @Transactional
    public void deletar(Long id) {
        LOGGER.info("Deletando benef?cio ID: " + id);
        beneficioRepository.deleteById(id);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void transferir(TransferenciaRequest request) throws SaldoInsuficienteException {
        LOGGER.info("Processando transfer?ncia: " + request);
        
        Beneficio origem = beneficioRepository.findById(request.getOrigemId())
            .orElseThrow(() -> new RuntimeException("Benef?cio origem n?o encontrado: " + request.getOrigemId()));
        
        Beneficio destino = beneficioRepository.findById(request.getDestinoId())
            .orElseThrow(() -> new RuntimeException("Benef?cio destino n?o encontrado: " + request.getDestinoId()));
        
        LOGGER.info("Saldo origem: " + origem.getSaldo() + ", Saldo destino: " + destino.getSaldo());
        
        if (origem.getSaldo().compareTo(request.getValor()) < 0) {
            throw new SaldoInsuficienteException(
                "Saldo insuficiente. Dispon?vel: " + origem.getSaldo() + ", Requerido: " + request.getValor()
            );
        }
        
        origem.setSaldo(origem.getSaldo().subtract(request.getValor()));
        destino.setSaldo(destino.getSaldo().add(request.getValor()));
        
        beneficioRepository.save(origem);
        beneficioRepository.save(destino);
        
        LOGGER.info("Transfer?ncia conclu?da com sucesso!");
    }
    
    private BeneficioDTO toDTO(Beneficio beneficio) {
        BeneficioDTO dto = new BeneficioDTO();
        dto.setId(beneficio.getId());
        dto.setBeneficiarioId(beneficio.getBeneficiario().getId());
        dto.setBeneficiarioNome(beneficio.getBeneficiario().getNome());
        dto.setTipo(beneficio.getTipo());
        dto.setValor(beneficio.getValor());
        dto.setSaldo(beneficio.getSaldo());
        dto.setDataConcessao(beneficio.getDataConcessao());
        return dto;
    }
}
