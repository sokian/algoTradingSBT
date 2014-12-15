package ru.sbt.exchange.domain.message;

import ru.sbt.exchange.domain.PeriodInfo;
import ru.sbt.exchange.domain.Portfolio;

import java.io.Serializable;
import java.util.Map;

public class GameState implements Serializable {
    private final Map<String, Portfolio> portfolioByClientId;
    private final PeriodInfo periodInfo;

    public GameState(Map<String, Portfolio> portfolioByClientId, PeriodInfo periodInfo) {
        this.portfolioByClientId = portfolioByClientId;
        this.periodInfo = periodInfo;
    }

    public Map<String, Portfolio> getPortfolioByClientId() {
        return portfolioByClientId;
    }

    public PeriodInfo getPeriodInfo() {
        return periodInfo;
    }
}
