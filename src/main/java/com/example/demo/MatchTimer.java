package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicBoolean;

public class MatchTimer implements Runnable {

    private int timeLeft;
    private final Text timerDisplay;
    private final Runnable onTimerEnd;

    private Thread thread;

    public MatchTimer(int startTime, Text timerDisplay, Runnable onTimerEnd) {
        this.timeLeft = startTime;
        this.timerDisplay = timerDisplay;
        this.onTimerEnd = onTimerEnd;
    }

    public void start() {
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    @Override
    public void run() {
        AtomicBoolean transitionStarted = new AtomicBoolean(false);
        ScaleTransition pulseTransition = new ScaleTransition(Duration.seconds(1), timerDisplay);
        pulseTransition.setCycleCount(ScaleTransition.INDEFINITE);
        pulseTransition.setAutoReverse(true);
        pulseTransition.setFromX(1.0);
        pulseTransition.setToX(1.2);
        pulseTransition.setFromY(1.0);
        pulseTransition.setToY(1.2);
        pulseTransition.play();

        Timeline pulsingSpeedControl = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> {
                    if (timeLeft <= 20) {
                        pulseTransition.setRate(2.0);
                    } else {
                        pulseTransition.setRate(1.0);
                    }
                })
        );
        pulsingSpeedControl.setCycleCount(Timeline.INDEFINITE);
        pulsingSpeedControl.play();

        // Main countdown loop
        while (timeLeft > 0 && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }

            timeLeft--;
            Platform.runLater(() -> timerDisplay.setText(String.valueOf(timeLeft)));

            if (timeLeft <= 25 && !transitionStarted.get()) {
                transitionStarted.set(true);
                Platform.runLater(() -> timerDisplay.setFill(Color.RED));
                pulseTransition.setRate(3.0);
            }
        }

        if (timeLeft == 0) {
            Platform.runLater(onTimerEnd);
        }
    }
}
