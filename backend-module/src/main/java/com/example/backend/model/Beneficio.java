package com.example.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "beneficio")
public class Beneficio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "beneficiario_id")
    private Beneficiario beneficiario;
    
    private String tipo;
    
    private BigDecimal valor;
    
    private BigDecimal saldo;
    
    @Column(name = "data_concessao")
    private LocalDate dataConcessao;
    
    @Version
    private Integer versao;
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Beneficiario getBeneficiario() { return beneficiario; }
    public void setBeneficiario(Beneficiario beneficiario) { this.beneficiario = beneficiario; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    
    public LocalDate getDataConcessao() { return dataConcessao; }
    public void setDataConcessao(LocalDate dataConcessao) { this.dataConcessao = dataConcessao; }
    
    public Integer getVersao() { return versao; }
    public void setVersao(Integer versao) { this.versao = versao; }
}
