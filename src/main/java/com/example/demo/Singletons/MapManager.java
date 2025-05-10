package com.example.demo.Singletons;

public class MapManager {

    private static final MapManager instance = new MapManager();

    private String currentMap = "map1"; // default fallback

    private MapManager() {}

    public static MapManager getInstance() {
        return instance;
    }

    public String getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(String map) {
        this.currentMap = map;
    }
}

