# Padrões de Desenvolvimento - Projeto Finance

Este documento descreve as convenções e a arquitetura adotadas no projeto para garantir a qualidade e a consistência do código.

## 1. Arquitetura e Estrutura de Diretórios

### 1.1 Árevore de diretórios para módulos
```
nome-do-modulo/nome-do-submodulo/              
├── dto/                       
│   ├── request/               
│   └── response/              
├── model/                     
│   ├── [Entidade].java        
├── repository/                
│   ├── [Entidade]Command.java   
│   ├── [Entidade]Query.java     
│   └── [Entidade]Repository.java 
├── web/                       
│   ├── assembler/
│   │   └── [Entidade]Assembler.java
│   └── [Entidade]Controller.java
```


### 1.2 Árvore de diretórios para integrações externas (Ports and Adapters)
```
nome-do-modulo/nome-da-integracao/
├── application/
│   ├── port/
│   │   └── [Integracao]Gateway.java
│   ├── dto/
│   │   └── [Integracao]Result.java
│   ├── [Integracao]UseCase.java
│   └── [Integracao]UseCaseImpl.java
└── adapter/
    ├── dto/
    │   ├── [ServicoExterno]Request.java
    │   └── [ServicoExterno]Response.java
    ├── [Integracao]Provider.java
    └── [Integracao]Client.java
```





* **`model/`**: Classes de domínio.
    * Aqui moram os `Enums`
* **`web/`**: Camada de entrada (Controllers).
    * Não deve conter regras de negócio.
    * **`assembler/`**: Classes responsáveis por mapear/converter Entidades para DTOs e vice-versa.
        * Deve-se usar a estrutura HATEOS. 
* **`dto/`**: Objetos de Transporte.
    * **Sufixos*:
        * `DTO`: para objetos de transporte internos.
        * `Request`: para objetos de entrada (input) 
        * `Response` Para objetos de saída (output).
* **`repository/`**: Interfaces de acesso a dados.
    * `Command`: operações de escrita
    * `Query`: operações de leitura
* **`adapter/`**: Integrações externas (APIs de terceiros via Feign, Scrapers, etc.).


## Para as Integrações Externas (Ports and Adapters):
* **`application/`**: Núcleo da integração. Totalmente agnóstico e isolado de detalhes de infraestrutura (como HTTP, JSON, etc.).

    * **`port/`**: Portas de saída (Outbound Ports). Define as interfaces (ex: Gateway) com os contratos que a aplicação exige.
    * **`dto/`**: Objetos que representam o dado limpo e processado para o domínio.
        * **`Sufixo`**: utilizar Result para o objeto de retorno esperado pela aplicação.
        * **`UseCase`**: Interfaces e implementações contendo a regra de orquestração da integração.

* **`adapter/`**: Implementação técnica (Outbound Adapters). É quem efetivamente "fala" com o mundo externo.
    * **`dto/`**: Classes que espelham exatamente a estrutura (JSON/XML) da API de terceiros.
        *  Regra de Nomenclatura: Devem ter o Prefixo do Provedor Externo e o sufixo Payload. NUNCA utilize nomes genéricos ou Response nesta camada.
        * Correto: ViaCepAddressPayload, BrapiStockPayload, BcbSelicPayload.
    * **`Provider`**: Classe que implementa o Gateway da camada de application. Faz a chamada ao cliente e converte o Payload externo para o DTO interno (Result).
    * **`Client`**: O cliente HTTP em si (ex: interfaces do FeignClient).

## 2. Base de Dados (Migrations)
Utilizamos o [Flyway/Liquibase] para versionamento da base de dados.
* Os ficheiros encontram-se em `src/main/resources/db/migration/`.
* **Regra de Nomenclatura**: `V<numero>__<verbo>_<tabela>.sql`. NUNCA altere uma migration que já foi executada e fundida na *branch* principal (main/master).
