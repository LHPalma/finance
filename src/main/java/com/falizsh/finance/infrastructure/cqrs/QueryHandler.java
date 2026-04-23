package com.falizsh.finance.infrastructure.cqrs;

/**
 * Contrato de todos os handlers de leitura.
 * Um handler por query — nunca reutilize o mesmo handler para queries distintas.
 *
 * @param <Q> tipo do query object
 * @param <R> tipo do retorno
 */
public interface QueryHandler<Q extends Query<R>, R> {

    R handle(Q query);
}
