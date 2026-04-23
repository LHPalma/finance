package com.falizsh.finance.infrastructure.cqrs;

/**
 * Contrato de todos os handlers de escrita.
 * Um handler por comando — nunca reutilize o mesmo handler para comandos distintos.
 *
 * @param <C> tipo do comando
 * @param <R> tipo do retorno
 */
public interface CommandHandler<C extends Command<R>, R> {

    R handle(C command);
}
