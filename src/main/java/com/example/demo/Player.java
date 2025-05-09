package com.example.demo;

public class Player {
    private String name;
    private int health;
    public void setName(String name){
        this.name = name;
    }

    public Player(){
        health = 100;
    }

    public String getName(){
        return name;
    }

    public int getHealth(){
        return health;
    }
}
