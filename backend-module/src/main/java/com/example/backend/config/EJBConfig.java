package com.example.backend.config;

import com.example.ejb.BeneficioEJBService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EJBConfig {
    
    @Bean
    public BeneficioEJBService beneficioEJBService() {
        return new BeneficioEJBService();
    }
}
