package com.example.backend.repository;

import com.example.backend.model.Beneficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface BeneficioRepository extends JpaRepository<Beneficio, Long> {
    
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT b FROM Beneficio b WHERE b.id = :id")
    Optional<Beneficio> findByIdWithLock(@Param("id") Long id);
}
