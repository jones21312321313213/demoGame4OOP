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
    private final AtomicBoolean isPaused;
    private final AtomicBoolean shouldStop;
    private final Object pauseLock = new Object();

    public MatchTimer(int startTime, Text timerDisplay, Runnable onTimerEnd) {
        this.timeLeft = startTime;
        this.timerDisplay = timerDisplay;
        this.onTimerEnd = onTimerEnd;
        this.isPaused = new AtomicBoolean(false);
        this.shouldStop = new AtomicBoolean(false);
    }

    public void start() {
        if (thread == null || !thread.isAlive()) {
            shouldStop.set(false);
            thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void stop() {
        shouldStop.set(true);
        resume(); // Wake up if paused
        if (thread != null) {
            thread.interrupt();
        }
    }

    public void setTimeLeft(int timeLeft){
        this.timeLeft = timeLeft;
    }

    public void pause() {
        isPaused.set(true);
    }

    public int getTimeLeft(){
        return timeLeft;
    }
    public void resume() {
        isPaused.set(false);
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }
    }

    public AtomicBoolean getIsPaused(){
        return isPaused;
    }
    @Override
    public void run() {
        // Setup your animations (same as before)
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
                }
                ));
        pulsingSpeedControl.setCycleCount(Timeline.INDEFINITE);
        pulsingSpeedControl.play();

        // Main countdown loop
        while (timeLeft > 0 && !shouldStop.get() && !Thread.currentThread().isInterrupted()) {
            try {
                // Handle pause state
                if (isPaused.get()) {
                    synchronized (pauseLock) {
                        while (isPaused.get()) {
                            pauseLock.wait();
                        }
                    }
                }

                Thread.sleep(1000);

                if (!isPaused.get()) {  // Only decrement if not paused
                    timeLeft--;
                    Platform.runLater(() -> timerDisplay.setText(String.valueOf(timeLeft)));
                }

                if (timeLeft <= 25 && !transitionStarted.get()) {
                    transitionStarted.set(true);
                    Platform.runLater(() -> timerDisplay.setFill(Color.RED));
                    pulseTransition.setRate(3.0);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        if (timeLeft == 0 && !shouldStop.get()) {
            Platform.runLater(onTimerEnd);
        }
    }
}
