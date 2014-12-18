package ru.sbt.exchange.client;

import ru.sbt.exchange.domain.*;
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
        cancelMyOrders(broker);
        double eps = 1e-9;
        for (Instrument instrument : Instruments.supportedInstruments()) {
            TopOrders tp = broker.getTopOrders(instrument);
            if (!tp.getBuyOrders().isEmpty()) {
                double bestPrice = tp.getBuyOrders().get(0).getPrice();
                if (bestPrice > limitPrice(instrument, Direction.BUY)) {
                    broker.addOrder(tp.getBuyOrders().get(0).opposite());
                }
            }
            if (!tp.getSellOrders().isEmpty()) {
                double bestPrice = tp.getSellOrders().get(0).getPrice();
                if (bestPrice < limitPrice(instrument, Direction.SELL)) {
                    broker.addOrder(tp.getSellOrders().get(0).opposite());
                }
            }
        }
    }

    private double limitPrice(Instrument instrument, Direction dir) {
        if (dir == Direction.BUY) {
            return 95;
        } else {
            return 90;
        }
    }

    private void cancelMyOrders(Broker broker) {
        List<Order> list = broker.getMyLiveOrders();
        for (Order order : list) {
            TopOrders tp = currentTopOrders.get(order.getInstrument());
            if (order.getDirection() == Direction.BUY) {
                double price = tp.getBuyOrders().isEmpty() ? 0 : tp.getBuyOrders().get(0).getPrice();
                if (order.getPrice() < price - 10) {
                    broker.cancelOrderById(order.getOrderId());
                }
            } else {
                double price = tp.getSellOrders().isEmpty() ? 100 : tp.getSellOrders().get(0).getPrice();
                if (order.getPrice() > price + 10) {
                    broker.cancelOrderById(order.getOrderId());
                }
            }
        }
    }
}
