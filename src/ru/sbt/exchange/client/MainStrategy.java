package ru.sbt.exchange.client;

import ru.sbt.exchange.domain.Order;
import ru.sbt.exchange.domain.PeriodInfo;
import ru.sbt.exchange.domain.Portfolio;
import ru.sbt.exchange.domain.TopOrders;
import ru.sbt.exchange.domain.instrument.Instrument;
import ru.sbt.exchange.domain.instrument.Instruments;

import javax.print.attribute.standard.OrientationRequested;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Sergey on 18.12.2014.
 */
public class MainStrategy implements Strategy {
    Portfolio myPortfolio = null;
    HashMap<Instrument, TopOrders> currentTopOrders = null;
    PeriodInfo currentPeriodInfo = null;
    HashMap<Instrument, Double> lastExecutionPrice = new HashMap<>();
    TreeMap<String, Order> myAliveOrders = new TreeMap<>();

    @Override
    public void onStrategyStart(Broker broker) {
        if (broker != null) {
            myPortfolio = broker.getMyPortfolio();
        }
    }

    @Override
    public void updateTopOrders(Broker broker) {
        currentTopOrders = new HashMap<>();
        List<Instrument> list = Instruments.supportedInstruments();
        for (Instrument instrument : list) {
            currentTopOrders.put(instrument, broker.getTopOrders(instrument));
        }
    }

    @Override
    public void onPeriodStart(Broker broker) {
        if (myPortfolio == null) {
            myPortfolio = broker.getMyPortfolio();
        }
        currentPeriodInfo = broker.getPeriodInfo();
    }

    @Override
    public void onAddOrder(Order order, Broker broker) {
        if (myAliveOrders.containsKey(order.getOrderId())) {
            myAliveOrders.replace(order.getOrderId(), order);
        }
    }

    @Override
    public void onCancelOrder(Order order, Broker broker) {
        if (myAliveOrders.containsKey(order.getOrderId())) {
            myAliveOrders.remove(order.getOrderId());
        }
    }

    @Override
    public void onExecution(Order order, Broker broker) {
        if (myAliveOrders.containsKey(order.getOrderId())) {
            Order myOrder = myAliveOrders.get(order.getOrderId()).minus(order);
            if (myOrder.getQuantity() > 0) {
                myAliveOrders.replace(order.getOrderId(), myOrder);
            } else {
                myAliveOrders.remove(order.getOrderId());
            }
        }
    }

    @Override
    public void onEvictOrder(Order order, Broker broker) {
        myAliveOrders.remove(order);
    }

    @Override
    public void makeMove(Broker broker) {

    }
}
