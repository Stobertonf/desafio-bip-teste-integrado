package com.example.ejb;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "beneficio")
public class Beneficio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "beneficiario_id")
    private Long beneficiarioId;
    
    private BigDecimal saldo;
    
    @Version
    private Integer versao;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    
    public Long getBeneficiarioId() { return beneficiarioId; }
    public void setBeneficiarioId(Long beneficiarioId) { this.beneficiarioId = beneficiarioId; }
    
    public Integer getVersao() { return versao; }
    public void setVersao(Integer versao) { this.versao = versao; }
}
