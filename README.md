# Padrões de Desenvolvimento - Projeto Finance

Este documento descreve as convenções e a arquitetura adotadas no projeto para garantir a qualidade e a consistência do código.

## 1. Arquitetura e Estrutura de Diretórios

### 1.1 Árvore de diretórios para módulos

```
nome-do-modulo/
├── application/                          ← regras de aplicação (orquestração)
│   ├── command/
│   │   └── [contexto]/
│   │       ├── CreateFooCommand.java     ← record, implementa Command<R>
│   │       └── CreateFooHandler.java     ← implementa CommandHandler<C, R>
│   ├── query/
│   │   └── [contexto]/
│   │       ├── FetchFooByIdQuery.java    ← record, implementa Query<R>
│   │       └── FetchFooByIdHandler.java  ← implementa QueryHandler<Q, R>
│   ├── dto/
│   │   └── [contexto]/
│   │       ├── request/
│   │       │   └── CreateFooRequest.java
│   │       └── response/
│   │           └── FooResponse.java      ← sempre com of()
│   └── usecase/
│       ├── CreateFooUseCase.java         ← interface
│       └── CreateFoo.java               ← impl: mapeia request → command → handler
├── domain/                              ← núcleo do negócio, zero dependência externa
│   ├── model/
│   │   └── [contexto]/
│   │       ├── Foo.java                 ← entidade / aggregate root
│   │       └── FooStatus.java           ← enum de domínio
│   └── repository/
│       └── [contexto]/
│           └── FooRepository.java       ← interface (Spring Data ou pura)
└── infrastructure/                      ← detalhes técnicos (web, persistência, etc.)
    └── web/
        └── [contexto]/
            ├── FooController.java        ← só chama use case e passa para o assembler
            └── assembler/
                └── FooAssembler.java     ← único lugar de mapeamento domínio → DTO + HATEOAS
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

## 2. Padrões de Código

### 2.1 DTOs — Factory Method `of()`

Todo DTO de **resposta** deve expor um método estático `of()` como único ponto de entrada para construção.
**Nunca usar `new DTO(...)` ou `.builder()` diretamente nos call sites.**

**≤ 3 campos** — `of()` recebe todos os parâmetros e usa o builder internamente:

```java
@Builder
public record FooResponse(Long id, String name, String status) {

    public static FooResponse of(Long id, String name, String status) {
        return FooResponse.builder()
                .id(id)
                .name(name)
                .status(status)
                .build();
    }
}

// uso
FooResponse.of(foo.getId(), foo.getName(), foo.getStatus());
```

**> 3 campos** — `of()` retorna o builder para que o chamador encadeie os campos:

```java
@Builder
public record FooResponse(
        Long id,
        String name,
        String status,
        BigDecimal value,
        LocalDate date
) {
    public static FooResponseBuilder of() {
        return builder();
    }
}

// uso
FooResponse.of()
        .id(foo.getId())
        .name(foo.getName())
        .status(foo.getStatus())
        .value(foo.getValue())
        .date(foo.getDate())
        .build();
```

**DTOs de request** (`*Request.java`) são excluídos desta regra — são instanciados pelo Jackson via desserialização HTTP, nunca pelo código da aplicação.

### 2.2 CQRS — Um handler por operação

O lado de **escrita** e o de **leitura** seguem o mesmo princípio: uma classe, uma responsabilidade.

```
application/
├── command/
│   └── foo/
│       ├── CreateFooCommand.java    ← dados do comando (record)
│       └── CreateFooHandler.java    ← executa o comando
└── query/
    └── foo/
        └── FetchFooByIdHandler.java ← executa a query
```

- **Handlers de escrita** (`command/`): anotados com `@Transactional`. Podem retornar o domínio ou um DTO de resultado por pragmatismo — o cliente não precisa fazer uma query extra para obter o estado gerado.
- **Handlers de leitura** (`query/`): anotados com `@Transactional(readOnly = true)`. Nunca alteram estado.
- **Um `*QueryImpl` com múltiplos métodos é proibido** — cada query tem seu próprio handler.

### 2.3 Assemblers e HATEOAS

Todo endpoint de controller **obrigatoriamente** passa por um assembler. Nenhum controller retorna um DTO diretamente.

```
// ❌ proibido
return ResponseEntity.ok(new FooResponse(...));

// ✅ correto
return ResponseEntity.ok(assembler.toModel(foo));
```

O assembler é o único lugar onde domínio (ou resultado de operação) é convertido em DTO de resposta. Mappers separados são proibidos — a lógica de mapeamento vive dentro do assembler.

Todo assembler deve incluir ao menos um **self link**. Use `linkTo(methodOn(...))` quando o endpoint existir:

```java
@Component
public class FooAssembler
        implements RepresentationModelAssembler<Foo, EntityModel<FooResponse>> {

    @Override
    public EntityModel<FooResponse> toModel(Foo foo) {
        FooResponse response = FooResponse.of()
                .id(foo.getId())
                .name(foo.getName())
                .build();

        return EntityModel.of(
                response,
                linkTo(methodOn(FooController.class).getById(foo.getId())).withSelfRel()
        );
    }
}
```

Quando o endpoint de self ainda não existir, use `Link.of(...)` manual e registre o débito técnico explicitamente no código:

```java
// TODO: substituir por linkTo(methodOn(...)) quando GET /foos/{id} for implementado.
//       Débito técnico rastreado: GET /foos/{id}
Link self = Link.of("/foos/" + foo.getId()).withSelfRel();
```

**Ausência de assembler ou de self link em qualquer PR é débito técnico e deve ser rastreado.**

## 3. Base de Dados (Migrations)
Utilizamos o [Flyway/Liquibase] para versionamento da base de dados.
* Os ficheiros encontram-se em `src/main/resources/db/migration/` organizados por banco:
    * `mysql/`: histórico legado MySQL
    * `postgresql/`: migrations ativas para PostgreSQL
* **Regra de Nomenclatura**: `V<numero>__<verbo>_<tabela>.sql`. NUNCA altere uma migration que já foi executada e fundida na *branch* principal (main/master).
