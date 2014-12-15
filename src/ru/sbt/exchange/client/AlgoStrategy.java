package ru.sbt.exchange.client;


import ru.sbt.exchange.domain.ExchangeEvent;

/**
 * Base interface for algorithmic trading. To manage you portfolio use Broker
 *
 * @see ru.sbt.exchange.client.Broker
 */
public interface AlgoStrategy {
    void onEvent(ExchangeEvent event, Broker broker);
}
