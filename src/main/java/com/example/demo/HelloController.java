package com.example.demo;


    import com.almasb.fxgl.app.GameApplication;
    import com.almasb.fxgl.app.GameSettings;
    import com.almasb.fxgl.app.scene.MenuType;
    import com.almasb.fxgl.dsl.FXGL;
    import com.almasb.fxgl.entity.Entity;
    import com.almasb.fxgl.entity.level.Level;
    import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader;
    import com.almasb.fxgl.input.UserAction;
    import com.almasb.fxgl.ui.FXGLButton;
    import com.example.demo.Factory.MySceneFactory;
    import com.example.demo.Factory.SimpleFactory;
    import com.example.demo.Scenes.CustomPauseMenu;
    import com.example.demo.Singletons.MapManager;
    import com.example.demo.Singletons.RoundStateManager;
    import com.example.demo.listeners.GameEntityType;
    import javafx.animation.*;
    import javafx.application.Platform;
    import javafx.geometry.Pos;
    import javafx.scene.control.ProgressBar;
    import javafx.scene.input.KeyCode;
    import javafx.scene.layout.HBox;
    import javafx.scene.media.Media;
    import javafx.scene.media.MediaPlayer;
    import javafx.scene.media.MediaView;
    import javafx.scene.paint.Color;
    import javafx.scene.shape.*;
    import javafx.scene.text.Font;
    import javafx.scene.text.FontWeight;
    import javafx.scene.text.Text;
    import javafx.scene.text.TextAlignment;
    import javafx.util.Duration;
    
    import java.io.File;
    import java.io.FileInputStream;
    import java.io.IOException;
    import java.io.ObjectInputStream;
    import java.util.ArrayList;
    import java.util.Random;
    
    import static com.almasb.fxgl.dsl.FXGL.*;
    
    public class HelloController extends GameApplication {
        private ProgressBar healthBar2;
        private ProgressBar healthBar;
        private int p1Score = 0;
        private int p2Score = 0;
        private HBox p1Star;
        private HBox p2Star;
    
        private Arc cooldownArc;
        private Text cooldownText;
        private double enhancedPunchCooldownTime = 5.0;
        private double remainingCooldownTime = 0.0;
        private boolean isCooldownActive = false;
    
        private Text ultAvailableText;
        private boolean isUltAvailableForP1 = false;
        private boolean P1Ult = false;
    
        private boolean isUltAvailableForP2 = false;
        private Text ultAvailableTextP2;
        private boolean P2Ult = false;
    
        private Arc cooldownArcP2;
        private Text cooldownTextP2;
        private double enhancedPunchCooldownTimeP2 = 5.0;
        private double remainingCooldownTimeP2 = 0.0;
        private boolean isP2CooldownActive = false;
    
        private String[] map = {"map","map2","map3","map4","map5","map6"};
        private String[] levels = {"Namek", "spirit_realm","chinatown","castle","bikini_bottom","cit-u"};
    
        private Text matchTimerText;
        private int matchTime = 103;
    
        private MatchTimer matchTimer = null;
    
        private CustomPauseMenu pauseMenu;
        private ArrayList<GameState> gameStateList = new ArrayList<>();
    
        private double x1 = 100,y1 = 100;
        private double x2 = 1000,y2 = 100;
    
        private boolean isFirstLaunch = true;
    
        @Override
        protected void onPreInit(){
            FXGL.getAssetLoader().loadTexture("/assets/textures/background-intro.mp4");
        }
    
        @Override
        protected void initSettings(GameSettings settings) {
            settings.setWidth(1920);//set this to 1984
            settings.setHeight(1080);// set this to 1088
            settings.setTitle("Stictactics");
    //        settings.setVersion("0.1");
            settings.setIntroEnabled(false);// i true later
            settings.setGameMenuEnabled(false);
            settings.setMainMenuEnabled(true);
            settings.setCloseConfirmation(true);
            //settings.setFullScreenAllowed(true);
            settings.setSceneFactory(new MySceneFactory());
            RoundStateManager.getInstance().setStartOfRound(true);
        }
    
    
        private Entity player;
        private Entity player2;
        @Override
        protected void initInput(){
            // added this
            FXGL.getInput().addAction(new UserAction("Pause Game") {
                @Override
                protected void onAction() {
                    if (!matchTimer.getIsPaused().get()) {
                        FXGL.getSceneService().pushSubScene(pauseMenu);
                        matchTimer.pause();
                    }
                }
            }, KeyCode.ESCAPE);
    
            getInput().addAction(new UserAction("P1Left"){
                @Override
                protected void onAction(){
                    player.getComponent(PlayerControl.class).left();
                }
            },KeyCode.A);
    
            getInput().addAction(new UserAction("P1Right"){
                @Override
                protected void onAction(){
                    player.getComponent(PlayerControl.class).right();
                }
            },KeyCode.D);
    
            getInput().addAction(new UserAction("P1Jump"){
                @Override
                protected void onAction(){
                    player.getComponent(PlayerControl.class).up();
                }
            },KeyCode.W);
    
            getInput().addAction(new UserAction("P1Punch"){
                @Override
                protected void onAction(){
                    player.getComponent(PlayerControl.class).punch();
                }
            },KeyCode.F);
    
            getInput().addAction(new UserAction("P1EnhancedPunch") {
                @Override
                protected void onAction() {
                    if (!isCooldownActive) { // Only trigger if cooldown is not active
                        player.getComponent(PlayerControl.class).enhancedPunch(); // Trigger the enhanced punch
                        startEnhancedPunchCooldown(); // Start the cooldown for the enhanced punch
                    }
                }
            }, KeyCode.E);
    
            getInput().addAction(new UserAction("P1Ult") {
                @Override
                protected void onAction() {
                    if (isUltAvailableForP1 && !P1Ult) {
                        player.getComponent(PlayerControl.class).ultAttack();
                        isUltAvailableForP1 = false;
                        ultAvailableText.setVisible(false);
                        P1Ult = true;
                    }
                }
            }, KeyCode.Q);
    
            //PLAYER 2
            getInput().addAction(new UserAction("P2Left"){
                @Override
                protected void onAction(){
                    player2.getComponent(PlayerControl2.class).P2left();
                }
            },KeyCode.LEFT);
    
            getInput().addAction(new UserAction("P2Right"){
                @Override
                protected void onAction(){
                    player2.getComponent(PlayerControl2.class).P2right();
                }
            },KeyCode.RIGHT);
    
            getInput().addAction(new UserAction("P2Jump"){
                @Override
                protected void onAction(){
                    player2.getComponent(PlayerControl2.class).P2up();
                }
            },KeyCode.UP);
    
            getInput().addAction(new UserAction("P2Punch"){
                @Override
                protected void onAction(){
                    player2.getComponent(PlayerControl2.class).P2punch();
                }
            },KeyCode.H);
    
            getInput().addAction(new UserAction("P2EnhancedPunch") {
                @Override
                protected void onAction() {
                    if (!isP2CooldownActive) {
                        player2.getComponent(PlayerControl2.class).P2enhancedPunch();
                        startP2EnhancedPunchCooldown();
                    }
                }
            }, KeyCode.U);
    
            getInput().addAction(new UserAction("P2Ult") {
                @Override
                protected void onAction() {
                    if (isUltAvailableForP2 && !P2Ult) {
                        player2.getComponent(PlayerControl2.class).P2ultAttack();
                        isUltAvailableForP2 = false;
                        ultAvailableTextP2.setVisible(false);
                        P2Ult = true;
                    }
                }
            }, KeyCode.O);
    
        }
    
        @Override
        protected void initPhysics() {
            getPhysicsWorld().setGravity(0, 195);
        }
    
        protected void initUI() {
            super.initUI();
    
        }
    
       // public int temp = 100 , temp2= 100;
        @Override
        protected void initGame() {
    
            getGameWorld().addEntityFactory(new SimpleFactory());
            // First, determine which map/level to use
            FXGL.set("player1Score", p1Score);
            FXGL.set("player2Score", p2Score);
            int mapIndex;
            String mapToLoad;
            String levelToLoad;
    
            matchTimerText = new Text(String.valueOf(matchTime));
            matchTimerText.setTranslateX(FXGL.getAppWidth() / 2);
            matchTimerText.setTranslateY(80);
            matchTimerText.setStyle("-fx-font-size: 80px; -fx-font-weight: bold;");
            matchTimerText.setFill(Color.BLACK);
            matchTimerText.setStroke(Color.WHITE);
            matchTimerText.setStrokeWidth(2);
            FXGL.getGameScene().addUINode(matchTimerText);
    
            Runnable onTimerEnd = () -> {
                int p1Health = player.getComponent(HealthComponent.class).getHealth();
                int p2Health = player2.getComponent(HealthComponent.class).getHealth();
                if (p1Health > p2Health) {
                    onPlayerDied(GameEntityType.PLAYER2);
                } else if (p2Health > p1Health) {
                    onPlayerDied(GameEntityType.PLAYER);
                } else {
                    showGameOverScreen("It's a Draw!");
                }
            };
            matchTimer = new MatchTimer(matchTime, matchTimerText, onTimerEnd);
    
            if (isFirstLaunch || RoundStateManager.getInstance().isStartOfRound()) {
                mapIndex = new Random().nextInt(map.length);
                mapToLoad = map[mapIndex];
                levelToLoad = levels[mapIndex];
                matchTimer.start();
                FXGL.set("currentMap", mapToLoad);
                MapManager.getInstance().setCurrentMap(mapToLoad);
                // Load background video
                try {
                    String videoPath = getClass().getResource("/assets/textures/" + levelToLoad + ".mp4").toExternalForm();
                    Media media = new Media(videoPath);
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    MediaView mediaView = new MediaView(mediaPlayer);
                    mediaView.setViewOrder(-1000);
    
                    mediaView.setPreserveRatio(false);
                    mediaView.fitWidthProperty().bind(FXGL.getGameScene().getViewport().widthProperty());
                    mediaView.fitHeightProperty().bind(FXGL.getGameScene().getViewport().heightProperty());
    
                    FXGL.getGameScene().addUINode(mediaView);
                    mediaPlayer.play();
    
                    mediaPlayer.setOnEndOfMedia(() -> FXGL.getGameScene().removeUINode(mediaView));
                    mediaPlayer.setOnError(() -> System.out.println("Media error: " + mediaPlayer.getError()));
                } catch (Exception e) {
                    System.out.println("Error loading background video: " + e);
                }
            } else {
                try {
                    mapToLoad = MapManager.getInstance().getCurrentMap();
                } catch (Exception e) {
                    System.out.println("Error loading saved map: " + e);
                    mapIndex = new Random().nextInt(map.length);
                    mapToLoad = map[mapIndex];
    
                }
            }
            pauseMenu = new CustomPauseMenu(MenuType.GAME_MENU,matchTimer,gameStateList);
    
        // Always load the level (for both new and saved games)
            try {
                Level level = getAssetLoader().loadLevel("tmx/" + mapToLoad + ".tmx", new TMXLevelLoader());
                getGameWorld().setLevel(level);
            } catch (Exception e) {
                System.out.println("Error loading level, using fallback: " + e);
                Level fallback = getAssetLoader().loadLevel("tmx/map.tmx", new TMXLevelLoader());
                getGameWorld().setLevel(fallback);
            }
    
    
            if(RoundStateManager.getInstance().isStartOfRound() || isFirstLaunch) {
                player = getGameWorld().spawn("player", 100, 100);
                player2 = getGameWorld().spawn("player2", 1000, 100);
    //            // Set full health for new round
                player.getComponent(HealthComponent.class).setHealth(100);
                player2.getComponent(HealthComponent.class).setHealth(100);
            } else {
                loadSavedGame(matchTimer);
                try {
                    player = getGameWorld().spawn("player", x1, y1);
                } catch (Exception e) {
                    player = getGameWorld().spawn("player", 100, 100);
                    System.out.println("Error spawning player: " + e);
                }
                try {
                    player2 = getGameWorld().spawn("player2", x2, y2);
                } catch (Exception e) {
                    player2 = getGameWorld().spawn("player2", 1000, 100);
                    System.out.println("Error spawning player2: " + e);
                }
            }
    
            healthBar = new ProgressBar(1.0);
            healthBar.setPrefWidth(500);
            healthBar.setPrefHeight(50);
            healthBar.setLayoutX(50);
            healthBar.setLayoutY(50);
            healthBar.setStyle("-fx-accent: green;");
            getGameScene().addUINode(healthBar);
    
            healthBar2 = new ProgressBar(1.0);
            healthBar2.setPrefWidth(500);
            healthBar2.setPrefHeight(50);
            healthBar2.setLayoutX(1400);
            healthBar2.setLayoutY(50);
            healthBar2.setStyle("-fx-accent: green;");
            getGameScene().addUINode(healthBar2);
    
            cooldownArc = new Arc(100, 70, 50, 50, 90, 0); // X, Y, radiusX, radiusY, startAngle, length
            cooldownArc.setType(ArcType.ROUND);
            cooldownArc.setFill(Color.BLACK);
            cooldownArc.setStroke(Color.BLACK);
            cooldownArc.setStrokeWidth(10);
    
            cooldownText = new Text(String.format("%.0f", remainingCooldownTime));
            cooldownText.setFont(Font.font(24));
            cooldownText.setFill(Color.RED);
            cooldownText.setX(100 - cooldownText.getLayoutBounds().getWidth() / 2);
            cooldownText.setY(100 + cooldownText.getLayoutBounds().getHeight() / 2);
    
            cooldownArc.setLayoutX(50);
            cooldownArc.setLayoutY(150);
            cooldownText.setLayoutX(50);
            cooldownText.setLayoutY(110);
    
            getGameScene().addUINode(cooldownArc);
            getGameScene().addUINode(cooldownText);
    
            p1Star = new HBox(10);
            p1Star.setLayoutX(600);
            p1Star.setLayoutY(50);
            p1Star.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-pref-width: 200px; -fx-pref-height: 50px;");
            p1Star.setMinWidth(200);
            p1Star.setMinHeight(100);
            getGameScene().addUINode(p1Star);
    
            p2Star = new HBox(10);
            p2Star.setAlignment(Pos.BASELINE_RIGHT);
            p2Star.setLayoutX(1150);
            p2Star.setLayoutY(50);
            p2Star.setMinWidth(200);
            p2Star.setMinHeight(100);
            p2Star.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5); -fx-pref-width: 200px; -fx-pref-height: 50px;");
            getGameScene().addUINode(p2Star);
    
            ultAvailableText = new Text("ULT Available \nPress Q!");
            ultAvailableText.setFont(Font.font("System", FontWeight.BOLD,45));
            ultAvailableText.setFill(Color.YELLOW);
            ultAvailableText.setLayoutX(70);
            ultAvailableText.setLayoutY(400);
            ultAvailableText.setVisible(false);
            getGameScene().addUINode(ultAvailableText);
    
            ultAvailableTextP2 = new Text("ULT Available \nPress O!");
            ultAvailableTextP2.setFont(Font.font("System",FontWeight.BOLD,45));
            ultAvailableTextP2.setFill(Color.YELLOW);
            ultAvailableTextP2.setLayoutX(1550);
            ultAvailableTextP2.setLayoutY(400);
            ultAvailableTextP2.setVisible(false);
            ultAvailableTextP2.setTextAlignment(TextAlignment.LEFT);
            getGameScene().addUINode(ultAvailableTextP2);
    
            cooldownArcP2 = new Arc(125, 70, 50, 50, 90, 0);
            cooldownArcP2.setType(ArcType.ROUND);
            cooldownArcP2.setFill(Color.RED);
            cooldownArcP2.setStroke(Color.RED);
            cooldownArcP2.setStrokeWidth(10);
    
            cooldownTextP2 = new Text(String.format("%.0f", remainingCooldownTimeP2));
            cooldownTextP2.setFont(Font.font(24));
            cooldownTextP2.setFill(Color.BLACK);
    
            cooldownTextP2.setX(100 - cooldownTextP2.getLayoutBounds().getWidth() / 2);
            cooldownTextP2.setY(100 + cooldownTextP2.getLayoutBounds().getHeight() / 2);
    
            cooldownArcP2.setLayoutX(1600);
            cooldownArcP2.setLayoutY(150);
            cooldownTextP2.setLayoutX(1625);
            cooldownTextP2.setLayoutY(110);
    
            getGameScene().addUINode(cooldownArcP2);
            getGameScene().addUINode(cooldownTextP2);
    
            // Verify components exist before setting listeners
            // Set up death listeners (these should always be set regardless of new round or loaded game)
            player.getComponent(HealthComponent.class).setDeathListener(this::onPlayerDied);
            player2.getComponent(HealthComponent.class).setDeathListener(this::onPlayerDied);
    
            player.getComponent(HealthComponent.class).setHealthChangeListener(this::updateHealthBar);
            player2.getComponent(HealthComponent.class).setHealthChangeListener(this::updateHealthBarPlayer2);
    
            if (RoundStateManager.getInstance().isStartOfRound()) {
                // New round - set full health
                player.getComponent(HealthComponent.class).setHealth(100);
                player2.getComponent(HealthComponent.class).setHealth(100);
                // Update health bars to reflect full health
                updateHealthBar(100);
                updateHealthBarPlayer2(100);
            } else {
                // Loading saved game - health will be set by loadSavedGame
                loadSavedGame(matchTimer);
                // Make sure the UI reflects the loaded health values
                if (player != null && player.hasComponent(HealthComponent.class)) {
                    updateHealthBar(player.getComponent(HealthComponent.class).getHealth());
                }
                if (player2 != null && player2.hasComponent(HealthComponent.class)) {
                    updateHealthBarPlayer2(player2.getComponent(HealthComponent.class).getHealth());
                }
            }
            p1Star.getChildren().clear();
            p2Star.getChildren().clear();
    
            for (int i = 0; i < p1Score; i++) {
                addStar(p1Star);
            }
            for (int i = 0; i < p2Score; i++) {
                addStar(p2Star);
            }
            p1Star.layout();
            p2Star.layout();
            p1Star.requestLayout();
            p2Star.requestLayout();
        }
    
        private boolean hasLoaded = false;
        @Override
        protected void onUpdate(double tpf) {
            if (!hasLoaded &&
                    FXGL.getGameWorld().getEntitiesByType(GameEntityType.PLAYER).size() > 0 &&
                    FXGL.getGameWorld().getEntitiesByType(GameEntityType.PLAYER2).size() > 0) {
                loadSavedGame(matchTimer);
                hasLoaded = true;
            }
        }
    
        private void updateHealthBar(int newHealth) {
            healthBar.setProgress(newHealth / 100.0);
            if (newHealth <= 50 && !P2Ult) {
                isUltAvailableForP2 = true;
                ultAvailableTextP2.setVisible(true);
                //System.out.println("ULT is now available for Player 2.");
            } else {
                isUltAvailableForP2 = false;
                ultAvailableTextP2.setVisible(false);
            }
        }
    
        private void updateHealthBarPlayer2(int newHealth) {
            //System.out.println("Player 2 health changed: " + newHealth);
    
            if (newHealth <= 50 && !P1Ult) {
                isUltAvailableForP1 = true;
                ultAvailableText.setVisible(true);
                //System.out.println("ULT is now available for Player 1.");
            } else {
                isUltAvailableForP1 = false;
                ultAvailableText.setVisible(false);
                //System.out.println("ULT is not available for Player 1.");
            }
            healthBar2.setProgress(newHealth/100.00);
        }
    
    
        private void onPlayerDied(GameEntityType deadPlayerType) {
            // First check if this death will result in a match win
            if ((deadPlayerType == GameEntityType.PLAYER && p2Score + 1 >= 3)) {
                p2Score++;
                showGameOverScreen("Player 2 Wins the Match!");
            } else if (deadPlayerType == GameEntityType.PLAYER2 && p1Score + 1 >= 3) {
                p1Score++;
                showGameOverScreen("Player 1 Wins the Match!");
            } else {
                // Only increment score for round wins, not match wins
                if (deadPlayerType == GameEntityType.PLAYER) {
                    p2Score++;
                } else {
                    p1Score++;
                }
                showRoundWinScreen(deadPlayerType);
            }
        }
    
        private void showRoundWinScreen(GameEntityType deadPlayerType) {
            RoundStateManager.getInstance().setStartOfRound(true);
            FXGL.getGameScene().clearUINodes();
    
            // Update stars display before showing the win screen
            p1Star.getChildren().clear();
            p2Star.getChildren().clear();
    
            for (int i = 0; i < p1Score; i++) {
                addStar(p1Star);
            }
            for (int i = 0; i < p2Score; i++) {
                addStar(p2Star);
            }
    
            String winner = (deadPlayerType == GameEntityType.PLAYER) ? "Player 2" : "Player 1";
            var message = FXGL.getUIFactoryService().newText(winner + " wins the round!", 36);
            message.setFill(Color.GREEN);
    
            // Rest of the method remains the same...
            FXGLButton nextRoundButton = new FXGLButton("Next Round");
            nextRoundButton.setOnAction(e -> startNextRound());
    
            var dialogBox = new javafx.scene.layout.VBox(20, message, nextRoundButton);
            dialogBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.90);"
                    + "-fx-padding: 30px;");
            dialogBox.setPrefWidth(300);
    
            double centerX = 1920 / 2.0;
            double centerY = 1080 / 2.0;
    
            dialogBox.setLayoutX(centerX - 150);
            dialogBox.setLayoutY(centerY - 100);
    
            FXGL.getGameScene().addUINode(dialogBox);
            FXGL.getGameController().pauseEngine();
        }
    
        private void showGameOverScreen(String winnerText) {
            RoundStateManager.getInstance().setStartOfRound(true);
            FXGL.getGameScene().clearUINodes();
    
            var message = FXGL.getUIFactoryService().newText(winnerText, 48);
            message.setFill(Color.YELLOW);
    
            FXGLButton restartButton = new FXGLButton("Restart Match");
            restartButton.setOnAction(e -> restartMatch());
    
            FXGLButton backToMenuButton = new FXGLButton("Back to Menu");
            backToMenuButton.setOnAction(e -> backToMainMenu());
    
            var dialogBox = new javafx.scene.layout.VBox(20, message, restartButton, backToMenuButton);
            dialogBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.90);"
                    + "-fx-padding: 30px;");
            dialogBox.setPrefWidth(400);
    
            double centerX = 1920 / 2.0;
            double centerY = 1080 / 2.0;
    
            dialogBox.setLayoutX(centerX - 200); // 400/2
            dialogBox.setLayoutY(centerY - 100);
    
            FXGL.getGameScene().addUINode(dialogBox);
            FXGL.getGameController().pauseEngine();
        }
    
        private void backToMainMenu() {
            // Reset all game state variables
            isFirstLaunch = true;
            p1Score = 0;
            p2Score = 0;
            P1Ult = false;
            P2Ult = false;
            isUltAvailableForP1 = false;
            isUltAvailableForP2 = false;
    
            // Clear any saved state
            gameStateList.clear();
            File file = new File("savedstates.txt");
            if (file.exists()) {
                file.delete();
            }
            // Reset UI elements
            if (p1Star != null) p1Star.getChildren().clear();
            if (p2Star != null) p2Star.getChildren().clear();
    
            FXGL.getGameController().resumeEngine();
            FXGL.getGameController().gotoMainMenu();
        }
    
        private void startNextRound() {
            // Clear existing stars
            p1Star.getChildren().clear();
            p2Star.getChildren().clear();
    
            // Add stars based on current scores
            for (int i = 0; i < p1Score; i++) {
                addStar(p1Star);
            }
            for (int i = 0; i < p2Score; i++) {
                addStar(p2Star);
            }
    
            // Reset cooldowns and ult flags
            isCooldownActive = false;
            isP2CooldownActive = false;
            P1Ult = false;
            P2Ult = false;
    
            // Request layout updates
            p1Star.layout();
            p2Star.layout();
            p1Star.requestLayout();
            p2Star.requestLayout();
    
            FXGL.getGameController().resumeEngine();
            FXGL.getGameController().startNewGame();
        }
    
        private void restartMatch() {
            // Reset all game state variables
            p1Score = 0;
            p2Score = 0;
            isFirstLaunch = true;
            P1Ult = false;
            P2Ult = false;
            isUltAvailableForP1 = false;
            isUltAvailableForP2 = false;
    
            // Clear any saved state
            gameStateList.clear();
            File file = new File("savedstates.txt");
            if (file.exists()) {
                file.delete();
            }
    
            // Reset UI elements
            if (p1Star != null) p1Star.getChildren().clear();
            if (p2Star != null) p2Star.getChildren().clear();
    
            FXGL.getGameController().resumeEngine();
            FXGL.getGameController().startNewGame();
        }
    
        private void addStar(HBox starBox) {
            //System.out.println("Adding star to " + starBox);  // Debugging line
    
            javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(25);
            circle.setFill(Color.YELLOW);
    
            Platform.runLater(() -> {
                starBox.getChildren().add(circle);
                //System.out.println("Children count: " + starBox.getChildren().size());
    
                starBox.requestLayout();
    
                //System.out.println("Star added: " + circle);
            });
        }
        private void startEnhancedPunchCooldown() {
            isCooldownActive = true;
            remainingCooldownTime = enhancedPunchCooldownTime;
    
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(enhancedPunchCooldownTime),
                            new KeyValue(cooldownArc.lengthProperty(), 360)
                    )
            );
            timeline.setCycleCount(1);
            timeline.play();
    
            getGameTimer().runAtInterval(() -> {
                if (remainingCooldownTime > 0) {
                    remainingCooldownTime -= 1.0;
                    double progress = remainingCooldownTime / enhancedPunchCooldownTime;
                    cooldownArc.setLength(360 * progress);
                    cooldownText.setText(String.format("%.0f", remainingCooldownTime));
    
                    cooldownText.setX(100 - cooldownText.getLayoutBounds().getWidth() / 2);
                    cooldownText.setY(100 + cooldownText.getLayoutBounds().getHeight() / 2);
                }
    
                if (remainingCooldownTime <= 0) {
                    isCooldownActive = false;
                }
            }, Duration.seconds(1));
        }
    
    
        private void startP2EnhancedPunchCooldown() {
            isP2CooldownActive = true;
            remainingCooldownTimeP2 = enhancedPunchCooldownTimeP2;
    
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(enhancedPunchCooldownTimeP2),
                            new KeyValue(cooldownArcP2.lengthProperty(), 360)
                    )
            );
            timeline.setCycleCount(1);
            timeline.play();
    
            getGameTimer().runAtInterval(() -> {
                if (remainingCooldownTimeP2 > 0) {
                    remainingCooldownTimeP2 -= 1.0;
                    double progress = remainingCooldownTimeP2 / enhancedPunchCooldownTimeP2;
                    cooldownArcP2.setLength(360 * progress);
    
                    cooldownTextP2.setText(String.format("%.0f", remainingCooldownTimeP2));
                } else {
                    isP2CooldownActive = false;
                    cooldownArcP2.setLength(0);
                    cooldownTextP2.setText("");
                }
            }, Duration.seconds(1));
        }
    
    
        private void onPlayer2HealthChanged(int newHealth) {
            //System.out.println("Player 2 health changed: " + newHealth);
            if (newHealth <= 50) {
                isUltAvailableForP1 = true;
                ultAvailableText.setVisible(true);
                //System.out.println("ULT is now available for Player 1.");
            } else {
                isUltAvailableForP1 = false;
                ultAvailableText.setVisible(false);
                //System.out.println("ULT is not available for Player 1.");
            }
        }
    
        private int findMapIndex(String mapName) {
            for (int i = 0; i < map.length; i++) {
                if (map[i].equals(mapName)) {
                    return i;
                }
            }
            return -1; // default to first map if not found
        }
    
        private void startMatchTimer() {
            FXGL.getGameTimer().runAtInterval(() -> {
                matchTime--;
                matchTimerText.setText(String.valueOf(matchTime));
    
                if (matchTime <= 0) {
                    //endMatch();
                }
            }, Duration.seconds(1));
        }
    
        //@SuppressWarnings("unchecked")
        // deserialization
        private void loadSavedGame(MatchTimer matchTimer) {
            if (isFirstLaunch) {
                isFirstLaunch = false;  // Set to false so future loads will work
                p1Score = 0;
                p2Score = 0;
                return;
            }
            File file = new File("savedstates.txt");
            if (!file.exists()) {
                System.out.println("No saved game states found.");
                return;
            }
            RoundStateManager.getInstance().setStartOfRound(false);
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                gameStateList = (ArrayList<GameState>) in.readObject();
    
                if (gameStateList.isEmpty()) {
                    System.out.println("Saved game state list is empty.");
                    return;
                }
    
                GameState gameState = gameStateList.get(gameStateList.size() - 1); // Load the most recent state
    
                // Restore game data into FXGL
                FXGL.set("player1Health", gameState.getPlayer1Health());
                FXGL.set("player2Health", gameState.getPlayer2Health());
                FXGL.set("player1Name", gameState.getPlayer1Name());
                FXGL.set("player2Name", gameState.getPlayer2Name());
                FXGL.set("player1Score", gameState.getPlayer1Score());
                FXGL.set("player2Score", gameState.getPlayer2Score());
                MapManager.getInstance().setCurrentMap(gameState.getMap());
                //FXGL.set("currentMap", gameState.getMap());
                FXGL.set("currentLevel",gameState.getCurrentLevel());
    
                //restore players/characters positions
                x1 = y1 = x2 = y2 = 0;
                x1 = gameState.getPlayer1X();
                y1 = gameState.getPlayer1Y();
                x2 = gameState.getPlayer2X();
                y2 = gameState.getPlayer2Y();
    //            temp = gameState.getPlayer1Health();
    //            temp2 = gameState.getPlayer2Health();
    
                System.out.println("Loading health values - P1: " + gameState.getPlayer1Health() +
                        ", P2: " + gameState.getPlayer2Health());
    
                if (player != null && player.hasComponent(HealthComponent.class)) {
                    System.out.println("Setting P1 health to: " + gameState.getPlayer1Health());
                    player.getComponent(HealthComponent.class).setHealth(gameState.getPlayer1Health());
                    updateHealthBar(gameState.getPlayer1Health());
                }
    
                if (player2 != null && player2.hasComponent(HealthComponent.class)) {
                    System.out.println("Setting P2 health to: " + gameState.getPlayer2Health());
                    player2.getComponent(HealthComponent.class).setHealth(gameState.getPlayer2Health());
                    updateHealthBarPlayer2(gameState.getPlayer2Health());
                }
    
    //            updateHealthBar(gameState.getPlayer1Health());
    //            updateHealthBarPlayer2(gameState.getPlayer2Health());
    
    
                p1Score = gameState.getPlayer1Score();
                p2Score = gameState.getPlayer2Score();
    
                if (matchTimer != null) {
                    matchTimer.pause(); // stop updates during restore
    
                    FXGL.getGameTimer().runOnceAfter(() -> {
                        matchTimer.setTimeLeft(gameState.getTimeLeft());
                        // Ensure it's running
                        if (matchTimer.getIsPaused().get()) {
                            matchTimer.resume();
                        }
                        matchTimer.start(); // <- KEY FIX: starts the thread if it wasn't running
                    }, Duration.seconds(1));
                }
    
                // Debug output
                System.out.println("Game State Loaded:");
                System.out.println("Time Left: " + gameState.getTimeLeft());
                System.out.println("Player 1 Health: " + gameState.getPlayer1Health());
                System.out.println("Player 2 Health: " + gameState.getPlayer2Health());
                System.out.println("Player 1 Name: " + gameState.getPlayer1Name());
                System.out.println("Player 2 Name: " + gameState.getPlayer2Name());
                System.out.println("Player 1 Score: " + gameState.getPlayer1Score());
                System.out.println("Player 2 Score: " + gameState.getPlayer2Score());
                System.out.println("Player 1 Position: (" + gameState.getPlayer1X() + ", " + gameState.getPlayer1Y() + ")");
                System.out.println("Player 2 Position: (" + gameState.getPlayer2X() + ", " + gameState.getPlayer2Y() + ")");
                System.out.println("Current Map: " + gameState.getMap());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    
    
    }