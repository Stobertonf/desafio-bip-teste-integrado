package com.example.ejb;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.logging.Logger;

@Stateless
public class BeneficioEJBService {
    
    private static final Logger LOGGER = Logger.getLogger(BeneficioEJBService.class.getName());
    
    @PersistenceContext(unitName = "beneficiosPU")
    private EntityManager em;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void transfer(Long fromId, Long toId, BigDecimal amount) 
            throws SaldoInsuficienteException, OptimisticLockException, IllegalArgumentException {
        
        LOGGER.info("=== INICIANDO TRANSFER?NCIA NO EJB ===");
        LOGGER.info("Origem: " + fromId + ", Destino: " + toId + ", Valor: " + amount);
        
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
        
        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("N?o pode transferir para o mesmo benef?cio");
        }
        
        try {
            // Busca sem LockModeType primeiro para testar
            LOGGER.info("Buscando benef?cio de origem ID: " + fromId);
            Beneficio from = em.find(Beneficio.class, fromId);
            LOGGER.info("Origem encontrada: " + (from != null));
            
            LOGGER.info("Buscando benef?cio de destino ID: " + toId);
            Beneficio to = em.find(Beneficio.class, toId);
            LOGGER.info("Destino encontrado: " + (to != null));
            
            if (from == null) {
                throw new IllegalArgumentException("Benef?cio de origem n?o encontrado: " + fromId);
            }
            if (to == null) {
                throw new IllegalArgumentException("Benef?cio de destino n?o encontrado: " + toId);
            }
            
            LOGGER.info("Saldo origem: " + from.getSaldo());
            LOGGER.info("Saldo destino: " + to.getSaldo());
            
            if (from.getSaldo().compareTo(amount) < 0) {
                throw new SaldoInsuficienteException(
                    "Saldo insuficiente. Dispon?vel: " + from.getSaldo() + ", Requerido: " + amount
                );
            }
            
            from.setSaldo(from.getSaldo().subtract(amount));
            to.setSaldo(to.getSaldo().add(amount));
            
            em.merge(from);
            em.merge(to);
            em.flush();
            
            LOGGER.info("=== TRANSFER?NCIA CONCLU?DA COM SUCESSO! ===");
            
        } catch (Exception e) {
            LOGGER.severe("ERRO: " + e.getClass().getName() + " - " + e.getMessage());
            throw e;
        }
    }
}
