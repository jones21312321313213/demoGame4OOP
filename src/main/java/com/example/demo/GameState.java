package com.example.demo;

import java.io.Serializable;

public class GameState implements Serializable {
    private int timeLeft;
    private int player1Health;
    private int player2Health;
    private String player1Name;
    private String player2Name;
    private int player1Score;
    private int player2Score;
    private double player1X, player1Y;
    private double player2X, player2Y;
    private String map;

    public GameState(int timeLeft, int player1Health, int player2Health,
                     String player1Name, String player2Name,
                     int player1Score, int player2Score,
                     double player1X, double player1Y,
                     double player2X, double player2Y,
                     String map) {

        this.timeLeft = timeLeft;
        this.player1Health = player1Health;
        this.player2Health = player2Health;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.player1X = player1X;
        this.player1Y = player1Y;
        this.player2X = player2X;
        this.player2Y = player2Y;
        this.map = map;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public int getPlayer1Health() {
        return player1Health;
    }

    public int getPlayer2Health() {
        return player2Health;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public double getPlayer1X() {
        return player1X;
    }

    public double getPlayer1Y() {
        return player1Y;
    }

    public double getPlayer2X() {
        return player2X;
    }

    public double getPlayer2Y() {
        return player2Y;
    }

    public String getMap() {
        return map;
    }
    @Override
    public String toString() {
        return "GameState{" +
                "timeLeft=" + timeLeft +
                ", player1Health=" + player1Health +
                ", player2Health=" + player2Health +
                ", player1Name='" + player1Name + '\'' +
                ", player2Name='" + player2Name + '\'' +
                ", player1Score=" + player1Score +
                ", player2Score=" + player2Score +
                ", player1X=" + player1X +
                ", player1Y=" + player1Y +
                ", player2X=" + player2X +
                ", player2Y=" + player2Y +
                ", map='" + map + '\'' +
                '}';
    }

}
