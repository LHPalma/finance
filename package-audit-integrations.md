# Auditoria de pacotes: serviços externos em `integrations`

## Regra avaliada
Todos os consumos de serviços externos devem estar no namespace/pasta `com.falizsh.finance.integrations`.

## Achados

### 1) Consumos externos fora de `integrations`

#### 1.1 ViaCEP está em `identity`
- Arquivo: `src/main/java/com/falizsh/finance/identity/users/userAddress/adapter/ViaCepClient.java`
- Evidência: classe anotada com `@FeignClient` consumindo `https://viacep.com.br/ws`.
- Impacto: viola a separação proposta (integração externa alocada em domínio de identidade).
- Recomendação: mover para `integrations/viacep` (ou `integrations/address`) e expor porta/interface para o módulo de usuário.

#### 1.2 CloudConvert está em `marketdata`
- Arquivo: `src/main/java/com/falizsh/finance/marketdata/holiday/adapter/CloudConvertClient.java`
- Evidência: classe anotada com `@FeignClient` para `${cloudconvert.url}`.
- Impacto: cliente externo acoplado ao bounded context de `marketdata`.
- Recomendação: mover para `integrations/cloudconvert` e manter em `marketdata` apenas portas de aplicação/domínio.

### 2) Inconsistências de nomenclatura de pacote/arquivos em integrações

#### 2.1 Typo em pacote `adpater`
- Arquivos:
  - `src/main/java/com/falizsh/finance/integrations/anbima/adpater/AnbimaClient.java`
  - `src/main/java/com/falizsh/finance/integrations/anbima/adpater/impl/AnbimaProvider.java`
- Evidência: pacote usa `adpater` ao invés de `adapter`.
- Impacto: dificulta busca e padronização arquitetural.
- Recomendação: renomear pasta/pacote para `adapter`.

#### 2.2 Typos de `scraper`
- Arquivos:
  - `src/main/java/com/falizsh/finance/integrations/bcbifdata/adapter/BacenSrapperService.java`
  - `src/main/java/com/falizsh/finance/integrations/bcbifdata/web/BacenScrapperController.java`
- Evidência: variações `Srapper` e `Scrapper`.
- Impacto: ruído de nomenclatura e risco de divergência em refatorações maiores.
- Recomendação: padronizar para `BacenScraperService` e `BacenScraperController`.

### 3) Pontos em conformidade com a regra
- `awesomeApi`, `stock`, `b3`, `bcbifdata` já estão sob `src/main/java/com/falizsh/finance/integrations/...`.

## Priorização sugerida para refatoração
1. Mover `ViaCepClient` e `CloudConvertClient` para `integrations`.
2. Corrigir typos de pacote/nomes (`adpater`, `Srapper`, `Scrapper`).
3. Ajustar imports e validar build/testes.
