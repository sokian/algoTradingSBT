package ru.sbt.exchange.client;

import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.ExchangeEventType;
import ru.sbt.exchange.domain.Order;

public class MyAlgoStrategy implements AlgoStrategy {
    MainStrategy mainStrategy;
    @Override
    public void onEvent(ExchangeEvent event, Broker broker) {
        if (event.getExchangeEventType() == ExchangeEventType.STRATEGY_START) {
            mainStrategy = new MainStrategy();
            mainStrategy.onStrategyStart(broker);
            return;
        }

        if (broker == null) {
            return;
        }

        Order order = event.getOrder();
        switch (event.getExchangeEventType()) {
            case NEW_PERIOD_START:
                mainStrategy.onPeriodStart(broker);
                break;
            case ORDER_NEW:
                mainStrategy.onAddOrder(order, broker);
                break;
            case ORDER_CANCEL:
                mainStrategy.onCancelOrder(order, broker);
                break;
            case ORDER_EVICT:
                mainStrategy.onEvictOrder(order, broker);
                break;
            case ORDER_EXECUTION:
                mainStrategy.onExecution(order, broker);
                break;
            default:
                break;
        }
        mainStrategy.updateTopOrders(broker);
        mainStrategy.makeMove(broker);
    }
}