package com.example.demo.Factory;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.IntroScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.MenuType;
import com.example.demo.Scenes.CustomIntro;
import com.example.demo.Scenes.CustomMainMenu;
import org.jetbrains.annotations.NotNull;

public class MySceneFactory extends SceneFactory {


    @NotNull
    @Override
    public FXGLMenu newMainMenu() {
        return new CustomMainMenu(MenuType.MAIN_MENU);
    }

//    @NotNull
//    @Override
//    public FXGLMenu newGameMenu() {
//        return new CustomPauseMenu(MenuType.GAME_MENU,timer,null);
//    }

    @NotNull
    @Override
    public IntroScene newIntro() {
        return new CustomIntro(); // custom intro
    }
}
