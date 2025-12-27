# Checklist de Desenvolvimento - Projeto Finance

### ⚙️ Configuração & Infraestrutura
- [x] **Setup**: Spring Boot 3.5.9 + Java 25
- [x] **Banco de Dados**: MySQL + Flyway
- [x] **Migrations**:
    - [x] `V1__create_table_user.sql`
    - [x] `V2__create_table_user_email.sql`
    - [x] `V3__create_table_user_address.sql`
- [x] **Ferramentas**:
    - [x] Postman Environment & Globals

---

### 👤 Domínio: User (Usuário)
- [x] **Modelo**: `User`
- [x] **Repositório**: `UserRepository`
- [x] **Arquitetura**:
    - [x] `UserQuery` (Leitura)
    - [x] `UserCommand` (Escrita)
- [x] **DTOs**:
    - [x] `UserCreateDTO`
- [x] **Controller**: `UserController`
- [x] **Assembler**: `UserAssembler`
- [x] **Enums**:
    - [x] `UserStatus`

---

### 📧 Domínio: UserEmail (Email)
- [x] **Modelo**: `UserEmail`
- [x] **Repositório**: `UserEmailRepository`
- [x] **Service**:
    - [x] `UserEmailService`
- [x] **DTOs**:
    - [x] `UserEmailCreateDTO`
    - [x] `UserEmailResponseDTO`
- [x] **Controller**: `UserEmailController`
- [x] **Assembler**: `UserEmailAssembler`
- [x] **Enums**:
    - [x] `UserEmailType`
    - [x] `EmailStatus`

---

### 📍 Domínio: UserAddress (Endereço)
- [x] **Modelo**: `UserAddress`
- [x] **Repositório**: `UserAddressRepository`
- [x] **Service/Arquitetura**:
    - [x] `UserAddressQuery`
    - [ ] `UserAddressCommand` (Lógica de Create/Update/Delete)
- [x] **DTOs**:
    - [x] `UserAddressCreateDTO`
    - [ ] `UserAddressUpdateDTO`
- [x] **Controller**: `UserAddressController` (Falta POST/PUT/DELETE)
- [x] **Assembler**: `UserAddressAssemblerSuport`
- [x] **Value Objects**:
    - [x] `CEP`
- [x] **Enums**:
    - [x] `UserAddressType`
    - [x] `UserAddressStatus`
    - [x] `ZipCodeVerificationStatus`

---

### 📱 Domínio: UserTelephones (Telefone)
> Baseado na tabela `user_telephones`
- [x] **Migration**: `V4__create_table_user_telephones.sql`
- [x] **Modelo**: `UserTelephone`
- [ ] **Repositório**: `UserTelephoneRepository`
- [ ] **Service/Arquitetura**:
    - [ ] `UserTelephoneQuery`
    - [ ] `UserTelephoneCommand`
- [ ] **DTOs**:
    - [ ] `UserTelephoneCreateDTO`
    - [ ] `UserTelephoneUpdateDTO`
- [ ] **Controller**: `UserTelephoneController`
- [ ] **Assembler**: `UserTelephoneAssembler`
- [ ] **Enums**:
    - [ ] `TelephoneType` (`personal`, `professional`, `commercial`, `residential`)
    - [ ] `TelephoneStatus` (`active`, `inactive`, `unverified`, etc.)

---

### 🏦 Domínio: BankAccount (Conta Bancária)
> Baseado na tabela `bank_account`
- [ ] **Migration**: `V5__create_table_bank_account.sql`
- [ ] **Modelo**: `BankAccount` (Campos: `bank_name`, `balance`, `currency`)
- [ ] **Repositório**: `BankAccountRepository`
- [ ] **Service/Arquitetura**:
    - [ ] `BankAccountQuery`
    - [ ] `BankAccountCommand`
- [ ] **DTOs**:
    - [ ] `BankAccountCreateDTO`
    - [ ] `BankAccountResponseDTO`
- [ ] **Controller**: `BankAccountController`
- [ ] **Assembler**: `BankAccountAssembler`
- [ ] **Enums**:
    - [ ] `AccountType` (`checking`, `savings`, `investments`, `cash`)

---

### 🏷️ Domínio: Category (Categorias)
> Baseado nas tabelas `category` e `transaction_category`
- [ ] **Migration**: `V6__create_table_category.sql`
- [ ] **Modelo**: `Category`
- [ ] **Relacionamento**: Entidade Associativa `TransactionCategory` (se houver atributos extra) ou `@ManyToMany`
- [ ] **Repositório**: `CategoryRepository`
- [ ] **Service/Arquitetura**:
    - [ ] `CategoryQuery`
    - [ ] `CategoryCommand`
- [ ] **DTOs**:
    - [ ] `CategoryCreateDTO`
- [ ] **Controller**: `CategoryController`
- [ ] **Enums**:
    - [ ] `CategoryTypeEnum` (`income`, `expense`)

---

### 💳 Domínio: PaymentMethod (Formas de Pagamento)
> Baseado na tabela `payment_method`
- [ ] **Migration**: `V7__create_table_payment_method.sql`
- [ ] **Modelo**: `PaymentMethod`
- [ ] **Repositório**: `PaymentMethodRepository`
- [ ] **Service/Arquitetura**:
    - [ ] `PaymentMethodQuery`
    - [ ] `PaymentMethodCommand`
- [ ] **Controller**: `PaymentMethodController`
- [ ] **Enums**:
    - [ ] `PaymentTypeEnum` (`cash`, `debit_card`, `pix`, `bank_slip`, etc.)

---

### 💸 Domínio: Transaction (Transações)
> Baseado nas tabelas `transaction` e `transaction_reference_code`
- [ ] **Migration**: `V8__create_table_transaction.sql`
- [ ] **Modelos**:
    - [ ] `Transaction`
    - [ ] `TransactionReferenceCode` (Código de referência externo)
- [ ] **Repositórios**: `TransactionRepository`, `TransactionReferenceCodeRepository`
- [ ] **Service/Arquitetura**:
    - [ ] `TransactionQuery` (Filtros por data, conta, categoria)
    - [ ] `TransactionCommand` (Validar saldo, atualizar `bank_account`)
- [ ] **DTOs**:
    - [ ] `TransactionCreateDTO`
    - [ ] `TransactionDetailsDTO`
- [ ] **Controller**: `TransactionController`
- [ ] **Enums**:
    - [ ] `TransactionTypeEnum` (`income`, `outcome`, `transfer_in`, `transfer_out`)
    - [ ] `TransactionStatusEnum` (`completed`, `pending`, `cancelled`, `overdue`)

---

### 🔀 Domínio: Transfer (Transferências)
> Baseado na tabela `transfer` (link entre duas transactions)
- [ ] **Migration**: `V9__create_table_transfer.sql`
- [ ] **Modelo**: `Transfer` (Conecta `source_transaction` e `destination_transaction`)
- [ ] **Service**: Lógica de atomicidade (criar 2 transações + 1 registro de transferência)

---

### 📅 Domínio: Scheduled Operations (Agendamentos e Recorrência)
> Baseado no complexo de tabelas `scheduled_*`
- [ ] **Migration**: `V10__create_tables_scheduled_operations.sql`
- [ ] **Modelos (Herança/Composição)**:
    - [ ] `ScheduledOperation` (Base)
    - [ ] `ScheduledTransactionDetail` (Detalhes para Receitas/Despesas)
    - [ ] `ScheduledTransferDetail` (Detalhes para Transferências)
    - [ ] `ScheduledOperationRetryPolicy` (Política de retentativa)
    - [ ] `ScheduledOperationRecurrence` (Configuração de recorrência)
    - [ ] `ScheduledOperationExecution` (Histórico de tentativas)
- [ ] **Repositórios**: `ScheduledOperationRepository`
- [ ] **Service/Arquitetura**:
    - [ ] `ScheduledOperationCommand` (Criar agendamento)
    - [ ] `RecurringOperationProcessor` (Job para processar e gerar transações)
- [ ] **Enums**:
    - [ ] `ScheduledOperationTypeEnum` (`income`, `expense`, `transfer`)
    - [ ] `RecurrenceFrequencyEnum` (`daily`, `weekly`, `monthly`, `yearly`)
    - [ ] `ScheduledOperationStatusEnum` (`pending`, `executing`, `completed`, `failed`, `cancelled`)
    - [ ] `ScheduledOperationAttemptStatusEnum` (Motivos de falha: `insufficient_funds`, etc.)

---

### 🚧 Débitos Técnicos Atuais
- [ ] **Padronização**: Migrar `UserEmailService` para o padrão Query/Command.
- [ ] **Limpeza**: Remover código `@Deprecated` em `UserEmailService` e `UserEmailAssembler`.
- [ ] **Segurança**: Implementar criptografia real de senha (substituir "salt" hardcoded).
- [ ] **Correção**: Corrigir typo no nome da classe `UserAddressAssemblerSuport` (para `Support`).
- [ ] **Feature**: Implementar validação real de CPF em `UserCommand` (Requisito mencionado no TODO).