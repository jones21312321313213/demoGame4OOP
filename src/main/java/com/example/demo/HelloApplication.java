package com.example.demo;



public class HelloApplication {

    public static void main(String[] args) {
        launchGame(args);
    }

    private static void launchGame(String[] args) {
        // Launch the main game class
        HelloController gameApp = new HelloController();
        gameApp.launch(HelloController.class, args);
    }
}
