package com.example.demo.Singletons;

public class RoundStateManager {

    private static final RoundStateManager instance = new RoundStateManager();

    private boolean isStartOfRound = true;

    public static RoundStateManager getInstance() {
        return instance;
    }

    public boolean isStartOfRound() {
        return isStartOfRound;
    }

    public void setStartOfRound(boolean startOfRound) {
        this.isStartOfRound = startOfRound;
    }
}
