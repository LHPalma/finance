package com.falizsh.finance.infrastructure.cqrs;

/**
 * Marker interface para todos os comandos do sistema (escrita).
 * O parâmetro {@code R} representa o tipo de retorno do handler.
 */
public interface Command<R> {}
