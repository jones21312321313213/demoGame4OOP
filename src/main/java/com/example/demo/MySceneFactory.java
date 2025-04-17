package com.example.demo;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.MenuType;
import org.jetbrains.annotations.NotNull;

public class MySceneFactory extends SceneFactory {

    @NotNull
    @Override
    public FXGLMenu newMainMenu() {
        return new CustomMainMenu(MenuType.MAIN_MENU);
    }

    @NotNull
    @Override
    public FXGLMenu newGameMenu() {
        return new CustomMainMenu(MenuType.GAME_MENU); // optional if you want a game menu too
    }
}
