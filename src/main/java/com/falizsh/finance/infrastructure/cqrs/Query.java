package com.falizsh.finance.infrastructure.cqrs;

/**
 * Marker interface para todos os query objects do sistema (leitura).
 * O parâmetro {@code R} representa o tipo de retorno do handler.
 */
public interface Query<R> {}
