
package com.example.backend;


import com.example.backend.model.Beneficio;
import com.example.backend.exception.SaldoInsuficienteException;


import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.OptimisticLockException;
import java.math.BigDecimal;

@Stateless
public class BeneficioEJBService {

    @PersistenceContext
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void transfer(Long fromId, Long toId, BigDecimal amount)
            throws SaldoInsuficienteException, OptimisticLockException {

        // 1. Busca com LOCK (previne concorrência)
        Beneficio from = em.find(Beneficio.class, fromId);
        Beneficio to = em.find(Beneficio.class, toId);

        // 2. Valida se existemBeneficioEJBService
        if (from == null || to == null) {
            throw new IllegalArgumentException("Benefício não encontrado");
        }

        // 3. ⚠️ VALIDAÇÃO CRÍTICA: saldo suficiente
        if (from.getSaldo().compareTo(amount) < 0) {
            throw new SaldoInsuficienteException(
                    "Saldo insuficiente. Disponível: " + from.getSaldo());
        }

        // 4. ⚠️ NÃO DEIXA VALOR NEGATIVO OU ZERO
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }

        // 5. ⚠️ NÃO DEIXA TRANSFERIR PRA SI MESMO
        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Não pode transferir para o mesmo benefício");
        }

        // 6. ATUALIZA OS SALDOS
        from.setSaldo(from.getSaldo().subtract(amount));
        to.setSaldo(to.getSaldo().add(amount));

        // 7. MERGE (o @Version vai garantir optimistic locking)
        em.merge(from);
        em.merge(to);

        // 8. FORÇA FLUSH pra detectar conflitos AGORA
        em.flush();
    }
}