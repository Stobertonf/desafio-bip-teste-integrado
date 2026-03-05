package com.example.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BeneficioDTO {
    private Long id;
    private Long beneficiarioId;
    private String beneficiarioNome;
    private String tipo;
    private BigDecimal valor;
    private BigDecimal saldo;
    private LocalDate dataConcessao;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBeneficiarioId() { return beneficiarioId; }
    public void setBeneficiarioId(Long beneficiarioId) { this.beneficiarioId = beneficiarioId; }

    public String getBeneficiarioNome() { return beneficiarioNome; }
    public void setBeneficiarioNome(String beneficiarioNome) { this.beneficiarioNome = beneficiarioNome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

    public LocalDate getDataConcessao() { return dataConcessao; }
    public void setDataConcessao(LocalDate dataConcessao) { this.dataConcessao = dataConcessao; }
}
