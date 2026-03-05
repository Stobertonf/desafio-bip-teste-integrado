package com.example.backend.repository;

import com.example.backend.model.Beneficiario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiarioRepository extends JpaRepository<Beneficiario, Long> {
}
