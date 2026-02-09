
# Diagrama de Classes

````mermaid
classDiagram
    direction TB

%% --- CAMADA DE ADAPTADORES DE ENTRADA (WEB) ---
    namespace Web_Adapter {
        class EconomicsRateLookupController {
            +getDiFutures(LocalDate targetDate) ResponseEntity
        }
    }

%% --- CAMADA DE APLICAÇÃO (CORE / HEXÁGONO) ---
    namespace Application_Core {
        class GetDiFutureRatesUseCase {
            <<Interface>>
        %% Porta de Entrada
            +execute() List~DiFutureResult~
            +execute(LocalDate targetDate) List~DiFutureResult~
        }

        class GetDiFutureRates {
        %% Implementação do Caso de Uso
            +execute() List~DiFutureResult~
            +execute(LocalDate targetDate) List~DiFutureResult~
            -logicToFindNeighbors(TreeMap map, LocalDate date)
        }

        class DiFutureGateway {
            <<Interface>>
        %% Porta de Saída
            +getSettlementPrices() List~DiFutureResult~
        }

        class DiFutureResult {
            <<Record>>
        %% Objeto de Domínio (Limpo)
            +String contractCode
            +LocalDate maturityDate
            +BigDecimal rate
            +BigDecimal unitPrice
        }
    }

%% --- CAMADA DE ADAPTADORES DE SAÍDA (INFRAESTRUTURA) ---
    namespace Infra_Adapter {
        class B3DiProvider {
        %% O Adaptador Real
            +getSettlementPrices() List~DiFutureResult~
            -mapToDomain(B3Security security) DiFutureResult
        }

        class B3DiClient {
            <<Interface>>
        %% Cliente Feign
            +getDerivativeQuotation(String ticker) B3DiResponse
        }

    %% DTOs EXTERNOS (Espelho do JSON)
        class B3DiResponse {
            <<Record>>
            +List~B3Security~ securities
        }

        class B3Security {
            <<Record>>
            +String symbol
            +B3Market market
            +B3Asset asset
            +B3SecurityQuotation quotation
        }
    }

%% --- RELACIONAMENTOS ---

%% 1. Controller usa a Interface de Entrada
    EconomicsRateLookupController --> GetDiFutureRatesUseCase : Usa

%% 2. Implementação do Core
    GetDiFutureRates ..|> GetDiFutureRatesUseCase : Implementa
    GetDiFutureRates --> DiFutureGateway : Usa (Porta de Saída)
    GetDiFutureRates ..> DiFutureResult : Produz/Retorna

%% 3. Implementação da Infra
    B3DiProvider ..|> DiFutureGateway : Implementa (Adapter)
    B3DiProvider --> B3DiClient : Usa (Feign)

%% 4. Mapeamento de Dados (Anti-Corruption Layer)
    B3DiClient ..> B3DiResponse : Retorna
    B3DiResponse *-- B3Security : Contém
    B3DiProvider ..> B3DiResponse : Consome
    B3DiProvider ..> DiFutureResult : Cria (Mapeia e Limpa)

````

# Diagrama de Casos de Uso

```mermaid
flowchart LR
    User((Usuário / Sistema))

    subgraph "Adaptador de Entrada (Web)"
        Controller[EconomicsRateLookupController]
        Note[Agrupa CDI, Selic, Copom e B3]
    end

    subgraph "Core B3 (Application)"
        UC_Full[Consultar Curva Completa]
        UC_Filter[Consultar Data Específica]
    end

    User -->|GET /utils/di-future/di1| Controller

%% O Controller decide qual método do UseCase chamar
    Controller --> UC_Full
    Controller --> UC_Filter

%% Conexão visual com o arquivo real
    class Controller internalComponent
````

# Diagrama de Sequência

````mermaid
sequenceDiagram
    autonumber
    actor User
    
    box "Shared Adapter (Web)" #e1f5fe
        participant Controller as EconomicsRateLookupController
    end
    
    box "B3 Core (Application)" #fff9c4
        participant UseCase as GetDiFutureRatesUseCase
    end
    
    box "B3 Adapter (Infra)" #ffe0b2
        participant Provider as B3DiProvider
    end

    User->>Controller: GET /di-future/di1?maturityDate=2027-01-20

    %% Lógica do seu código atual
    Note right of Controller: Verifica targetDate (Early Return)

    alt targetDate == null
        Controller->>UseCase: execute()
        UseCase-->>Controller: Retorna Lista Completa
    else targetDate != null
        Controller->>UseCase: execute(2027-01-20)
        
        activate UseCase
        UseCase->>Provider: getSettlementPrices()
        Provider-->>UseCase: List<DiFutureResult>
        
        Note right of UseCase: Aplica Lógica de Vizinhos<br/>(Floor/Ceiling)
        
        UseCase-->>Controller: Lista Filtrada (Vizinhos)
        deactivate UseCase
    end

    Controller-->>User: HTTP 200 OK
````

