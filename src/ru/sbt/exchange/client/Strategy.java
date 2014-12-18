package ru.sbt.exchange.client;

import ru.sbt.exchange.domain.Order;

/**
 * Created by Sergey on 18.12.2014.
 */
public interface Strategy {
    void onStrategyStart(Broker broker);
    void onPeriodStart(Broker broker);
    void onAddOrder(Order order, Broker broker);
    void onCancelOrder(Order order, Broker broker);
    void onExecution(Order order, Broker broker);
    void onEvictOrder(Order order, Broker broker);
    void updateTopOrders(Broker broker);
    void makeMove(Broker broker);
}
