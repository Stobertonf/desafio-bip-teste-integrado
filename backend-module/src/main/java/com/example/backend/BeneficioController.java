package com.example.backend;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.TransferenciaRequest;
import com.example.backend.exception.SaldoInsuficienteException;
import com.example.backend.service.BeneficioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/beneficios")
@CrossOrigin(origins = "http://localhost:4200")
public class BeneficioController {
    
    private static final Logger LOGGER = Logger.getLogger(BeneficioController.class.getName());
    
    @Autowired
    private BeneficioService beneficioService;
    
    @GetMapping
    public ResponseEntity<List<BeneficioDTO>> listarTodos() {
        LOGGER.info("Listando todos os benef?cios");
        return ResponseEntity.ok(beneficioService.listarTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BeneficioDTO> buscarPorId(@PathVariable Long id) {
        LOGGER.info("Buscando benef?cio por ID: " + id);
        try {
            BeneficioDTO beneficio = beneficioService.buscarPorId(id);
            return ResponseEntity.ok(beneficio);
        } catch (RuntimeException e) {
            LOGGER.warning("Benef?cio n?o encontrado: " + id);
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<BeneficioDTO> criar(@RequestBody BeneficioDTO dto) {
        LOGGER.info("Criando novo benef?cio: " + dto);
        try {
            BeneficioDTO novoBeneficio = beneficioService.criar(dto);
            return new ResponseEntity<>(novoBeneficio, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            LOGGER.severe("Erro ao criar benef?cio: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BeneficioDTO> atualizar(@PathVariable Long id, @RequestBody BeneficioDTO dto) {
        LOGGER.info("Atualizando benef?cio ID: " + id);
        try {
            BeneficioDTO beneficioAtualizado = beneficioService.atualizar(id, dto);
            return ResponseEntity.ok(beneficioAtualizado);
        } catch (RuntimeException e) {
            LOGGER.warning("Benef?cio n?o encontrado para atualiza??o: " + id);
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        LOGGER.info("Deletando benef?cio ID: " + id);
        try {
            beneficioService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            LOGGER.warning("Benef?cio n?o encontrado para dele??o: " + id);
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/transferir")
    public ResponseEntity<Map<String, String>> transferir(@RequestBody TransferenciaRequest request) {
        LOGGER.info("Requisi??o de transfer?ncia recebida: " + request);
        try {
            beneficioService.transferir(request);
            
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Transfer?ncia realizada com sucesso!");
            response.put("status", "SUCESSO");
            
            LOGGER.info("Transfer?ncia conclu?da com sucesso");
            return ResponseEntity.ok(response);
            
        } catch (SaldoInsuficienteException e) {
            LOGGER.warning("Saldo insuficiente: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("erro", e.getMessage());
            error.put("status", "ERRO_SALDO_INSUFICIENTE");
            return ResponseEntity.badRequest().body(error);
            
        } catch (Exception e) {
            LOGGER.severe("Erro na transfer?ncia: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("erro", "Erro na transfer?ncia: " + e.getMessage());
            error.put("status", "ERRO");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
