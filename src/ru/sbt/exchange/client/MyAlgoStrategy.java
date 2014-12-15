package ru.sbt.exchange.client;

import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.ExchangeEventType;

public class MyAlgoStrategy implements AlgoStrategy {
    @Override
    public void onEvent(ExchangeEvent event, Broker broker) {
        if (event.getExchangeEventType() == ExchangeEventType.NEW_PERIOD_START) {
            System.out.println(broker.getPeriodInfo().getCurrentPeriodNumber() + " " +  broker.getMyPortfolio());
        }
    }
}