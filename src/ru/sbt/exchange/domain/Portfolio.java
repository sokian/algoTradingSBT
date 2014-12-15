package ru.sbt.exchange.domain;

import ru.sbt.exchange.domain.instrument.Instrument;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Portfolio implements Serializable {
    private final Map<Instrument, Integer> countByInstrument;
    private double money;

    private final double acceptedOverdraft;
    private final double periodInterestRate;
    private final double brokerFeeInPercents;

    public Portfolio(Map<Instrument, Integer> countByInstrument, double money,
                     double acceptedOverdraft, double periodInterestRate, double brokerFeeInPercents) {
        this.acceptedOverdraft = acceptedOverdraft;
        this.periodInterestRate = periodInterestRate;
        this.brokerFeeInPercents = brokerFeeInPercents;
        this.countByInstrument = countByInstrument;
        this.money = money;
    }

    public double getAcceptedOverdraft() {
        return acceptedOverdraft;
    }

    public double getPeriodInterestRate() {
        return periodInterestRate;
    }

    public double getBrokerFeeInPercents() {
        return brokerFeeInPercents;
    }

    public Map<Instrument, Integer> getCountByInstrument() {
        return countByInstrument;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Portfolio prototype() {
        return new Portfolio(new LinkedHashMap<>(countByInstrument), money, acceptedOverdraft, periodInterestRate, brokerFeeInPercents);
    }

    @Override
    public String toString() {
        return "portfolio: " + countByInstrument + " | " + money;
    }

    public boolean isDefaulter() {
        return money < -acceptedOverdraft;
    }
}