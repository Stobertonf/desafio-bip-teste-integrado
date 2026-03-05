# 🏗️ Desafio Fullstack Integrado

# 🏦 Sistema de Benefícios - Fullstack Challenge

![Java](https://img.shields.io/badge/Java-17-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen)
![Angular](https://img.shields.io/badge/Angular-17-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13-blue)
![Docker](https://img.shields.io/badge/Docker-24.0.7-blue)

## 📋 Sobre o Projeto

Sistema completo para gerenciamento de benefícios com funcionalidade de transferência entre contas. Desenvolvido como parte de um desafio fullstack, o projeto implementa uma arquitetura em camadas com backend Spring Boot, frontend Angular e banco de dados PostgreSQL.

### 🎯 Funcionalidades

- ✅ CRUD completo de benefícios
- ✅ Transferência entre benefícios com validação de saldo
- ✅ Listagem de benefícios com saldos atualizados
- ✅ Tratamento de erros e mensagens amigáveis
- ✅ Interface responsiva com Bootstrap

## 🛠️ Tecnologias Utilizadas

### Backend

- **Java 17** - Linguagem principal
- **Spring Boot 3.1.5** - Framework backend
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL 13** - Banco de dados relacional
- **Maven** - Gerenciamento de dependências
- **EJB 3.2** - Componente de negócio (com bug corrigido)

### Frontend

- **Angular 17** - Framework frontend
- **Bootstrap 5** - Estilização e componentes
- **TypeScript** - Linguagem
- **RxJS** - Programação reativa

### Infraestrutura

- **Docker** - Containerização do banco de dados
- **Docker Compose** - Orquestração de containers
- **Git** - Controle de versão

## 🚀 Como Executar o Projeto

### Pré-requisitos

- Java 17 JDK
- Node.js 18+ e npm
- Angular CLI 17
- Docker Desktop
- Maven 3.8+ (opcional, usar wrapper)

### 1. Clone o Repositório

```bash
git clone https://github.com/seu-usuario/desafio-bip-teste-integrado.git
cd desafio-bip-teste-integrado

# Iniciar containers PostgreSQL e pgAdmin
docker-compose up -d

# Verificar se os containers estão rodando
docker ps

# Você verá:
# - postgres-desafio (porta 5433)
# - pgadmin-desafio (porta 5050)

# Executar scripts SQL
docker cp db/schema.sql postgres-desafio:/schema.sql
docker cp db/seed.sql postgres-desafio:/seed.sql

docker exec -it postgres-desafio psql -U postgres -d beneficios_db -f /schema.sql
docker exec -it postgres-desafio psql -U postgres -d beneficios_db -f /seed.sql

# Verificar dados
docker exec -it postgres-desafio psql -U postgres -d beneficios_db -c "SELECT * FROM beneficiario;"
docker exec -it postgres-desafio psql -U postgres -d beneficios_db -c "SELECT * FROM beneficio;"

# Acessar pasta do backend
cd backend-module

# Compilar e rodar (usando Maven wrapper)
./mvnw clean compile
./mvnw spring-boot:run

# A API estará disponível em: http://localhost:8081/api/beneficios

# Em outro terminal, acessar pasta do frontend
cd frontend

# Instalar dependências
npm install

# Rodar aplicação
ng serve --open

# O frontend abrirá em: http://localhost:4200

📚 Endpoints da API
Método	URL	Descrição
GET	/api/beneficios	Lista todos os benefícios
GET	/api/beneficios/{id}	Busca benefício por ID
POST	/api/beneficios	Cria novo benefício
PUT	/api/beneficios/{id}	Atualiza benefício
DELETE	/api/beneficios/{id}	Remove benefício
POST	/api/beneficios/transferir	Realiza transferência
Exemplo de Transferência

POST /api/beneficios/transferir
{
  "origemId": 1,
  "destinoId": 2,
  "valor": 100.00
}

🐞 Bug do EJB Corrigido
O desafio incluía um bug no serviço EJB onde transferências eram realizadas sem validação de saldo e sem locking, podendo gerar inconsistências.

Correções Implementadas:
✅ Validação de saldo antes da transferência

✅ Optimistic locking para evitar concorrência

✅ Rollback automático em caso de erro

✅ Tratamento de exceções específicas

✅ Logs detalhados para auditoria

// Exemplo da correção no BeneficioEJBService
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public void transfer(Long fromId, Long toId, BigDecimal amount)
        throws SaldoInsuficienteException {

    // Busca com LOCK OTIMISTA
    Beneficio from = em.find(Beneficio.class, fromId, LockModeType.OPTIMISTIC);
    Beneficio to = em.find(Beneficio.class, toId, LockModeType.OPTIMISTIC);

    // Validação de saldo
    if (from.getSaldo().compareTo(amount) < 0) {
        throw new SaldoInsuficienteException(
            "Saldo insuficiente. Disponível: " + from.getSaldo()
        );
    }

    // Atualização com merge e flush
    from.setSaldo(from.getSaldo().subtract(amount));
    to.setSaldo(to.getSaldo().add(amount));

    em.merge(from);
    em.merge(to);
    em.flush();
}


🎨 Interface do Usuário
Lista de Benefícios
https://docs/images/lista-beneficios.png

Formulário de Transferência
https://docs/images/transferencia.png

Mensagem de Sucesso
https://docs/images/sucesso.png

Validação de Saldo
https://docs/images/erro-saldo.png

🧪 Testes
Testes Manuais com curl
bash
# Listar benefícios
curl http://localhost:8081/api/beneficios

# Transferência válida
curl -X POST http://localhost:8081/api/beneficios/transferir \
  -H "Content-Type: application/json" \
  -d '{"origemId":1,"destinoId":2,"valor":50.00}'

# Transferência com saldo insuficiente
curl -X POST http://localhost:8081/api/beneficios/transferir \
  -H "Content-Type: application/json" \
  -d '{"origemId":1,"destinoId":2,"valor":999999.00}'
📁 Estrutura do Projeto
text
desafio-bip-teste-integrado/
├── backend-module/          # Spring Boot Backend
│   ├── src/main/java/
│   │   └── com/example/backend/
│   │       ├── controller/  # REST Controllers
│   │       ├── service/     # Business Logic
│   │       ├── repository/  # Data Access
│   │       ├── model/       # JPA Entities
│   │       └── dto/         # Data Transfer Objects
│   └── src/main/resources/
│       └── application.properties
├── ejb-module/              # EJB Module (bug corrigido)
│   └── src/main/java/com/example/ejb/
│       └── BeneficioEJBService.java
├── frontend/                # Angular Frontend
│   ├── src/app/
│   │   ├── components/      # Angular Components
│   │   ├── services/        # API Services
│   │   └── models/          # TypeScript Interfaces
│   └── angular.json
├── db/                      # Database Scripts
│   ├── schema.sql
│   └── seed.sql
├── docs/                    # Documentação
│   └── images/              # Screenshots
└── docker-compose.yml       # Docker Configuration
🔧 Solução de Problemas
Porta 8081 já em uso
bash
# Encontrar processo na porta
netstat -ano | findstr :8081

# Matar processo (substitua PID)
taskkill /PID 12345 /F

# OU matar todos os Java
taskkill /F /IM java.exe
Erro de conexão com banco
bash
# Verificar se Docker está rodando
docker ps

# Reiniciar containers
docker-compose down
docker-compose up -d
Erros de compilação no frontend
bash
# Limpar cache e reinstalar
cd frontend
rm -rf node_modules package-lock.json
npm install
✅ Checklist do Desafio
Docker configurado com PostgreSQL

Scripts SQL (schema e seed)

Bug do EJB corrigido (validação + locking)

Backend Spring Boot com CRUD

Frontend Angular com componentes

Transferência funcionando

Tratamento de erros

Interface responsiva

Documentação completa

📄 Licença
Este projeto foi desenvolvido para fins de avaliação técnica.

👨‍💻 Autor
[Stoberton Francisco] - [stobertonf@gmail.com]
```
